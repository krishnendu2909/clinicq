package com.infy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.entity.Admin;
import com.infy.entity.Doctor;
import com.infy.entity.DoctorSchedule;
import com.infy.entity.TimeSlot;
import com.infy.entity.User;
import com.infy.exception.InfyHospitalException;
import com.infy.models.Department;
import com.infy.models.Gender;
import com.infy.models.Role;
import com.infy.repository.AdminRepository;
import com.infy.repository.DoctorRepository;
import com.infy.repository.DoctorScheduleRepository;
import com.infy.repository.TimeSlotRepository;
import com.infy.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Doctor testDoctor;
    private User testUser;
    private DoctorSchedule testSchedule;
    private TimeSlot testTimeSlot;
    private Admin testAdmin;

    @BeforeEach
    void setUp() {
        // Setup test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("doctor@test.com");
        testUser.setPassword("password");
        testUser.setRole(Role.DOCTOR);

        testDoctor = new Doctor();
        testDoctor.setId(1L);
        testDoctor.setName("Dr. Smith");
        testDoctor.setPhone("1234567890");
        testDoctor.setGender(Gender.MALE);
        testDoctor.setDepartment(Department.CARDIOLOGY);
        testDoctor.setLocation("New York");
        testDoctor.setDescription("Cardiologist");
        testDoctor.setUser(testUser);

        testSchedule = new DoctorSchedule();
        testSchedule.setId(1L);
        testSchedule.setDoctor(testDoctor);
        testSchedule.setDayOfWeek(DayOfWeek.MONDAY);
        testSchedule.setStartTime(LocalTime.of(9, 0));
        testSchedule.setEndTime(LocalTime.of(17, 0));
        testSchedule.setSlotDuration(30);

        testTimeSlot = new TimeSlot();
        testTimeSlot.setId(1L);
        testTimeSlot.setDoctor(testDoctor);
        testTimeSlot.setSlotDate(LocalDate.now().plusDays(1));
        testTimeSlot.setStartTime(LocalTime.of(9, 0));
        testTimeSlot.setEndTime(LocalTime.of(9, 30));
        testTimeSlot.setBooked(false);

        testAdmin = new Admin();
        testAdmin.setId(1L);
        testAdmin.setMaxDaysInAdvance(30);
        testAdmin.setMaxPatientsPerSlot(1);
        testAdmin.setCancellationCutoffHours(24);
    }

    // Test addDoctor - Success Case
    @Test
    void testAddDoctor_Success() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        // When
        Long result = adminService.addDoctor("doctor@test.com", "password", "Dr. Smith", 
                Department.CARDIOLOGY, Gender.MALE, "1234567890", "New York", "Cardiologist");

        // Then
        assertNotNull(result);
        assertEquals(1L, result);
        verify(userRepository).save(any(User.class));
        verify(doctorRepository).save(any(Doctor.class));
    }

    // Test deleteDoctor - Success Case
    @Test
    void testDeleteDoctor_Success() throws InfyHospitalException {
        // Given
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        doNothing().when(timeSlotRepository).deleteByDoctor(testDoctor);
        doNothing().when(doctorRepository).delete(testDoctor);
        doNothing().when(userRepository).delete(testUser);

        // When
        adminService.deleteDoctor(1L);

        // Then
        verify(doctorRepository).findById(1L);
        verify(timeSlotRepository).deleteByDoctor(testDoctor);
        verify(doctorRepository).delete(testDoctor);
        verify(userRepository).delete(testUser);
    }

    // Test deleteDoctor - Doctor Not Found Exception
    @Test
    void testDeleteDoctor_DoctorNotFound_ThrowsException() {
        // Given
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            adminService.deleteDoctor(999L);
        });
        assertEquals("Service.NO_DOCTOR_FOUND", exception.getMessage());
        verify(doctorRepository).findById(999L);
    }

    // Test configureDoctorSchedule - Success Case
    @Test
    void testConfigureDoctorSchedule_Success() throws InfyHospitalException {
        // Given
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(testSchedule);
        when(adminRepository.findTopByOrderByIdAsc()).thenReturn(Optional.of(testAdmin));
        
        // Create schedules that will be found by generateTimeSlots after deletion and recreation
        List<DoctorSchedule> schedules = new ArrayList<>();
        
        // Add MONDAY schedule
        DoctorSchedule mondaySchedule = new DoctorSchedule();
        mondaySchedule.setId(1L);
        mondaySchedule.setDoctor(testDoctor);
        mondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        mondaySchedule.setStartTime(LocalTime.of(9, 0));
        mondaySchedule.setEndTime(LocalTime.of(17, 0));
        mondaySchedule.setSlotDuration(30);
        schedules.add(mondaySchedule);
        
        // Add WEDNESDAY schedule
        DoctorSchedule wednesdaySchedule = new DoctorSchedule();
        wednesdaySchedule.setId(2L);
        wednesdaySchedule.setDoctor(testDoctor);
        wednesdaySchedule.setDayOfWeek(DayOfWeek.WEDNESDAY);
        wednesdaySchedule.setStartTime(LocalTime.of(9, 0));
        wednesdaySchedule.setEndTime(LocalTime.of(17, 0));
        wednesdaySchedule.setSlotDuration(30);
        schedules.add(wednesdaySchedule);
        
        when(doctorScheduleRepository.findByDoctor(eq(testDoctor))).thenReturn(schedules);
        when(timeSlotRepository.existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class))).thenReturn(false);
        when(timeSlotRepository.saveAll(any(List.class))).thenReturn(new ArrayList<>());

        // When
        adminService.configureDoctorSchedule(1L, days, LocalTime.of(9, 0), LocalTime.of(17, 0), 30);

        // Then - verify the new implementation behavior
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).deleteByDoctorId(1L); // New: delete existing schedules
        verify(timeSlotRepository).deleteFutureUnbookedSlots(1L, LocalDate.now()); // New: delete unbooked slots
        verify(doctorScheduleRepository, times(2)).save(any(DoctorSchedule.class)); // Save new schedules
        verify(adminRepository).findTopByOrderByIdAsc();
        verify(doctorScheduleRepository).findByDoctor(eq(testDoctor));
        verify(timeSlotRepository, atLeastOnce()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository).saveAll(any(List.class)); // New: use saveAll instead of individual saves
    }

    // Test configureDoctorSchedule - Doctor Not Found Exception
    @Test
    void testConfigureDoctorSchedule_DoctorNotFound_ThrowsException() {
        // Given
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.MONDAY);
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            adminService.configureDoctorSchedule(999L, days, LocalTime.of(9, 0), LocalTime.of(17, 0), 30);
        });
        assertEquals("Service.NO_DOCTOR_FOUND", exception.getMessage());
        verify(doctorRepository).findById(999L);
    }

    // Test configureDoctorSchedule - Admin Not Found Exception
    @Test
    void testConfigureDoctorSchedule_AdminNotFound_ThrowsException() {
        // Given
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.MONDAY);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.existsByDoctorAndDayOfWeek(testDoctor, DayOfWeek.MONDAY)).thenReturn(false);
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(testSchedule);
        when(adminRepository.findTopByOrderByIdAsc()).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            adminService.configureDoctorSchedule(1L, days, LocalTime.of(9, 0), LocalTime.of(17, 0), 30);
        });
        assertEquals("Service.NO_ADMIN_FOUND", exception.getMessage());
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).existsByDoctorAndDayOfWeek(testDoctor, DayOfWeek.MONDAY);
        verify(doctorScheduleRepository).save(any(DoctorSchedule.class));
        verify(adminRepository).findTopByOrderByIdAsc();
    }

    // Test configureDoctorSchedule - Skip Existing Schedule
    @Test
    void testConfigureDoctorSchedule_SkipExistingSchedule() throws InfyHospitalException {
        // Given - Updated for new implementation that always deletes existing schedules
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.MONDAY);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(testSchedule);
        when(adminRepository.findTopByOrderByIdAsc()).thenReturn(Optional.of(testAdmin));
        
        // Create MONDAY schedule that will be found by generateTimeSlots after deletion and recreation
        List<DoctorSchedule> schedules = new ArrayList<>();
        DoctorSchedule mondaySchedule = new DoctorSchedule();
        mondaySchedule.setId(1L);
        mondaySchedule.setDoctor(testDoctor);
        mondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        mondaySchedule.setStartTime(LocalTime.of(9, 0));
        mondaySchedule.setEndTime(LocalTime.of(17, 0));
        mondaySchedule.setSlotDuration(30);
        schedules.add(mondaySchedule);
        
        when(doctorScheduleRepository.findByDoctor(eq(testDoctor))).thenReturn(schedules);
        when(timeSlotRepository.existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class))).thenReturn(false);
        when(timeSlotRepository.saveAll(any(List.class))).thenReturn(new ArrayList<>());

        // When
        adminService.configureDoctorSchedule(1L, days, LocalTime.of(9, 0), LocalTime.of(17, 0), 30);

        // Then - verify the new implementation behavior (always deletes and recreates)
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).deleteByDoctorId(1L); // Always deletes existing schedules
        verify(timeSlotRepository).deleteFutureUnbookedSlots(1L, LocalDate.now()); // Always deletes unbooked slots
        verify(doctorScheduleRepository, times(1)).save(any(DoctorSchedule.class)); // Save new schedule
        verify(adminRepository).findTopByOrderByIdAsc();
        verify(doctorScheduleRepository).findByDoctor(eq(testDoctor));
        verify(timeSlotRepository, atLeastOnce()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository).saveAll(any(List.class)); // Use saveAll
    }

    // Test generateTimeSlots - Success Case
    @Test
    void testGenerateTimeSlots_Success() throws InfyHospitalException {
        // Given
        // Create schedules for all days of the week to ensure time slots are generated
        List<DoctorSchedule> schedules = new ArrayList<>();
        DayOfWeek[] days = DayOfWeek.values();
        
        for (int i = 0; i < days.length; i++) {
            DoctorSchedule schedule = new DoctorSchedule();
            schedule.setId((long) (i + 1));
            schedule.setDoctor(testDoctor);
            schedule.setDayOfWeek(days[i]);
            schedule.setStartTime(LocalTime.of(9, 0));
            schedule.setEndTime(LocalTime.of(17, 0));
            schedule.setSlotDuration(30);
            schedules.add(schedule);
        }
        
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.findByDoctor(testDoctor)).thenReturn(schedules);
        when(timeSlotRepository.existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class))).thenReturn(false);
        when(timeSlotRepository.save(any(TimeSlot.class))).thenReturn(testTimeSlot);

        // When
        adminService.generateTimeSlots(1L, 5);

        // Then
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).findByDoctor(eq(testDoctor));
        verify(timeSlotRepository, atLeastOnce()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository, atLeastOnce()).save(any(TimeSlot.class));
    }

    // Test generateTimeSlots - Doctor Not Found Exception
    @Test
    void testGenerateTimeSlots_DoctorNotFound_ThrowsException() {
        // Given
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            adminService.generateTimeSlots(999L, 5);
        });
        assertEquals("Service.NO_DOCTOR_FOUND", exception.getMessage());
        verify(doctorRepository).findById(999L);
    }

    // Test generateTimeSlots - Skip Existing TimeSlots
    @Test
    void testGenerateTimeSlots_SkipExistingTimeSlots() throws InfyHospitalException {
        // Given - Updated for new implementation that uses saveAll
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        
        // Create a schedule for today's day of the week to ensure time slots are generated
        DoctorSchedule todaySchedule = new DoctorSchedule();
        todaySchedule.setId(1L);
        todaySchedule.setDoctor(testDoctor);
        todaySchedule.setDayOfWeek(LocalDate.now().getDayOfWeek());
        todaySchedule.setStartTime(LocalTime.of(9, 0));
        todaySchedule.setEndTime(LocalTime.of(17, 0));
        todaySchedule.setSlotDuration(30);
        
        when(doctorScheduleRepository.findByDoctor(eq(testDoctor))).thenReturn(Arrays.asList(todaySchedule));
        when(timeSlotRepository.existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class))).thenReturn(true); // Existing slot
        when(timeSlotRepository.saveAll(any(List.class))).thenReturn(new ArrayList<>());

        // When
        adminService.generateTimeSlots(1L, 1);

        // Then - verify the new implementation behavior
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).findByDoctor(eq(testDoctor));
        verify(timeSlotRepository, atLeastOnce()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository).saveAll(any(List.class)); // saveAll is called even if list is empty
    }

    // Test generateTimeSlots - No Schedules Available
    @Test
    void testGenerateTimeSlots_NoSchedulesAvailable() throws InfyHospitalException {
        // Given - Updated for new implementation that uses saveAll
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.findByDoctor(eq(testDoctor))).thenReturn(Collections.emptyList());
        when(timeSlotRepository.saveAll(any(List.class))).thenReturn(new ArrayList<>());

        // When
        adminService.generateTimeSlots(1L, 5);

        // Then - verify the new implementation behavior
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).findByDoctor(eq(testDoctor));
        verify(timeSlotRepository, never()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository).saveAll(any(List.class)); // saveAll is called even with empty list
    }

    // Test generateTimeSlots - Different DayOfWeek Handling
    @Test
    void testGenerateTimeSlots_DifferentDayOfWeekHandling() throws InfyHospitalException {
        // Given - Updated for new implementation that uses saveAll
        DoctorSchedule tuesdaySchedule = new DoctorSchedule();
        tuesdaySchedule.setId(2L);
        tuesdaySchedule.setDoctor(testDoctor);
        tuesdaySchedule.setDayOfWeek(DayOfWeek.TUESDAY);
        tuesdaySchedule.setStartTime(LocalTime.of(10, 0));
        tuesdaySchedule.setEndTime(LocalTime.of(16, 0));
        tuesdaySchedule.setSlotDuration(60);

        List<DoctorSchedule> schedules = Arrays.asList(testSchedule, tuesdaySchedule);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.findByDoctor(eq(testDoctor))).thenReturn(schedules);
        when(timeSlotRepository.existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class))).thenReturn(false);
        when(timeSlotRepository.saveAll(any(List.class))).thenReturn(new ArrayList<>());

        // When
        adminService.generateTimeSlots(1L, 2);

        // Then - verify the new implementation behavior
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).findByDoctor(eq(testDoctor));
        verify(timeSlotRepository, atLeastOnce()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository).saveAll(any(List.class)); // Use saveAll instead of individual saves
    }

    // Test updateBookingRules - Success Case with Existing Admin
    @Test
    void testUpdateBookingRules_SuccessWithExistingAdmin() {
        // Given
        when(adminRepository.findAll()).thenReturn(Arrays.asList(testAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        // When
        adminService.updateBookingRules(5, 60, 48);

        // Then
        verify(adminRepository).findAll();
        verify(adminRepository).save(testAdmin);
        assertEquals(5, testAdmin.getMaxPatientsPerSlot());
        assertEquals(60, testAdmin.getMaxDaysInAdvance());
        assertEquals(48, testAdmin.getCancellationCutoffHours());
    }

    // Test updateBookingRules - Success Case with New Admin
    @Test
    void testUpdateBookingRules_SuccessWithNewAdmin() {
        // Given
        when(adminRepository.findAll()).thenReturn(Collections.emptyList());
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        // When
        adminService.updateBookingRules(3, 30, 12);

        // Then
        verify(adminRepository).findAll();
        verify(adminRepository).save(any(Admin.class));
    }

    // Test updateBookingRules - Multiple Admins Handling
    @Test
    void testUpdateBookingRules_MultipleAdminsHandling() {
        // Given
        Admin admin2 = new Admin();
        admin2.setId(2L);
        admin2.setMaxDaysInAdvance(15);
        admin2.setMaxPatientsPerSlot(2);
        admin2.setCancellationCutoffHours(6);

        when(adminRepository.findAll()).thenReturn(Arrays.asList(testAdmin, admin2));
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        // When
        adminService.updateBookingRules(4, 45, 24);

        // Then
        verify(adminRepository).findAll();
        verify(adminRepository).save(any(Admin.class));
        // Should update the first admin found
    }

    // Test generateTimeSlots - Edge Case with Zero Days Ahead
    @Test
    void testGenerateTimeSlots_ZeroDaysAhead() throws InfyHospitalException {
        // Given
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.findByDoctor(testDoctor)).thenReturn(Arrays.asList(testSchedule));

        // When
        adminService.generateTimeSlots(1L, 0);

        // Then
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).findByDoctor(testDoctor);
        verify(timeSlotRepository, never()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository, never()).save(any(TimeSlot.class));
    }

    // Test generateTimeSlots - Edge Case with Large Days Ahead
    @Test
    void testGenerateTimeSlots_LargeDaysAhead() throws InfyHospitalException {
        // Given
        // Create schedules for all days of the week to ensure time slots are generated over the year
        List<DoctorSchedule> schedules = new ArrayList<>();
        DayOfWeek[] days = DayOfWeek.values();
        
        for (int i = 0; i < days.length; i++) {
            DoctorSchedule schedule = new DoctorSchedule();
            schedule.setId((long) (i + 1));
            schedule.setDoctor(testDoctor);
            schedule.setDayOfWeek(days[i]);
            schedule.setStartTime(LocalTime.of(9, 0));
            schedule.setEndTime(LocalTime.of(17, 0));
            schedule.setSlotDuration(30);
            schedules.add(schedule);
        }
        
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.findByDoctor(testDoctor)).thenReturn(schedules);
        when(timeSlotRepository.existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class))).thenReturn(false);
        when(timeSlotRepository.save(any(TimeSlot.class))).thenReturn(testTimeSlot);

        // When
        adminService.generateTimeSlots(1L, 365); // One year ahead

        // Then
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).findByDoctor(testDoctor);
        verify(timeSlotRepository, atLeastOnce()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository, atLeastOnce()).save(any(TimeSlot.class));
    }

    // Test configureDoctorSchedule - Edge Case with Empty Days List
    @Test
    void testConfigureDoctorSchedule_EmptyDaysList() throws InfyHospitalException {
        // Given
        List<DayOfWeek> emptyDays = Collections.emptyList();
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(adminRepository.findTopByOrderByIdAsc()).thenReturn(Optional.of(testAdmin));
        
        // Return empty schedules list since no schedules should exist for this test
        when(doctorScheduleRepository.findByDoctor(testDoctor)).thenReturn(Collections.emptyList());
        
        // When
        adminService.configureDoctorSchedule(1L, emptyDays, LocalTime.of(9, 0), LocalTime.of(17, 0), 30);

        // Then
        verify(doctorRepository).findById(1L);
        verify(adminRepository).findTopByOrderByIdAsc();
        verify(doctorScheduleRepository, never()).existsByDoctorAndDayOfWeek(eq(testDoctor), any(DayOfWeek.class));
        verify(doctorScheduleRepository, never()).save(any(DoctorSchedule.class));
        verify(doctorScheduleRepository).findByDoctor(eq(testDoctor)); // This is called by generateTimeSlots
        // No time slots should be generated since there are no schedules
        verify(timeSlotRepository, never()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository, never()).save(any(TimeSlot.class));
    }

    // Test configureDoctorSchedule - Edge Case with Minimum Slot Duration
    @Test
    void testConfigureDoctorSchedule_MinimumSlotDuration() throws InfyHospitalException {
        // Given - Updated for new implementation that always deletes existing schedules
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.MONDAY);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(testSchedule);
        when(adminRepository.findTopByOrderByIdAsc()).thenReturn(Optional.of(testAdmin));
        
        // Create MONDAY schedule that will be found by generateTimeSlots with 15-minute slots
        List<DoctorSchedule> schedules = new ArrayList<>();
        DoctorSchedule mondaySchedule = new DoctorSchedule();
        mondaySchedule.setId(1L);
        mondaySchedule.setDoctor(testDoctor);
        mondaySchedule.setDayOfWeek(DayOfWeek.MONDAY);
        mondaySchedule.setStartTime(LocalTime.of(9, 0));
        mondaySchedule.setEndTime(LocalTime.of(10, 0));
        mondaySchedule.setSlotDuration(15);
        schedules.add(mondaySchedule);
        
        when(doctorScheduleRepository.findByDoctor(eq(testDoctor))).thenReturn(schedules);
        when(timeSlotRepository.existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class))).thenReturn(false);
        when(timeSlotRepository.saveAll(any(List.class))).thenReturn(new ArrayList<>());

        // When
        adminService.configureDoctorSchedule(1L, days, LocalTime.of(9, 0), LocalTime.of(10, 0), 15); // 15-minute slots

        // Then - verify the new implementation behavior
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).existsByDoctorAndDayOfWeek(eq(testDoctor), eq(DayOfWeek.MONDAY));
        verify(doctorScheduleRepository).save(any(DoctorSchedule.class));
        verify(adminRepository).findTopByOrderByIdAsc();
        verify(doctorScheduleRepository).findByDoctor(eq(testDoctor));
        verify(timeSlotRepository, atLeastOnce()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository, atLeastOnce()).save(any(TimeSlot.class));
    }

    // Test generateTimeSlots - Edge Case with Very Short Schedule
    @Test
    void testGenerateTimeSlots_VeryShortSchedule() throws InfyHospitalException {
        // Given
        DoctorSchedule shortSchedule = new DoctorSchedule();
        shortSchedule.setId(1L);
        shortSchedule.setDoctor(testDoctor);
        shortSchedule.setDayOfWeek(DayOfWeek.MONDAY);
        shortSchedule.setStartTime(LocalTime.of(9, 0));
        shortSchedule.setEndTime(LocalTime.of(9, 15)); // Only 15 minutes total
        shortSchedule.setSlotDuration(30); // Slot duration longer than schedule

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorScheduleRepository.findByDoctor(testDoctor)).thenReturn(Arrays.asList(shortSchedule));

        // When
        adminService.generateTimeSlots(1L, 1);

        // Then
        verify(doctorRepository).findById(1L);
        verify(doctorScheduleRepository).findByDoctor(testDoctor);
        verify(timeSlotRepository, never()).existsByDoctorAndSlotDateAndStartTime(eq(testDoctor), any(LocalDate.class), any(LocalTime.class));
        verify(timeSlotRepository, never()).save(any(TimeSlot.class));
    }

    // Test updateBookingRules - Edge Case with Zero Values
    @Test
    void testUpdateBookingRules_ZeroValues() {
        // Given
        when(adminRepository.findAll()).thenReturn(Arrays.asList(testAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        // When
        adminService.updateBookingRules(0, 0, 0);

        // Then
        verify(adminRepository).findAll();
        verify(adminRepository).save(testAdmin);
        assertEquals(0, testAdmin.getMaxPatientsPerSlot());
        assertEquals(0, testAdmin.getMaxDaysInAdvance());
        assertEquals(0, testAdmin.getCancellationCutoffHours());
    }

    // Test updateBookingRules - Edge Case with Large Values
    @Test
    void testUpdateBookingRules_LargeValues() {
        // Given
        when(adminRepository.findAll()).thenReturn(Arrays.asList(testAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        // When
        adminService.updateBookingRules(1000, 3650, 8760); // Very large values

        // Then
        verify(adminRepository).findAll();
        verify(adminRepository).save(testAdmin);
        assertEquals(1000, testAdmin.getMaxPatientsPerSlot());
        assertEquals(3650, testAdmin.getMaxDaysInAdvance());
        assertEquals(8760, testAdmin.getCancellationCutoffHours());
    }
}
