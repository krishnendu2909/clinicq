package com.infy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.modelmapper.ModelMapper;

import com.infy.dto.AppointmentDTO;
import com.infy.dto.DoctorDTO;
import com.infy.dto.TimeSlotDTO;
import com.infy.dto.UserDTO;
import com.infy.entity.Admin;
import com.infy.entity.Appointment;
import com.infy.entity.Doctor;
import com.infy.entity.Patient;
import com.infy.entity.TimeSlot;
import com.infy.entity.User;
import com.infy.exception.InfyHospitalException;
import com.infy.models.AppointmentStatus;
import com.infy.models.AppointmentType;
import com.infy.models.Department;
import com.infy.models.Gender;
import com.infy.models.Role;
import com.infy.repository.AdminRepository;
import com.infy.repository.AppointmentRepository;
import com.infy.repository.DoctorRepository;
import com.infy.repository.PatientRepository;
import com.infy.repository.TimeSlotRepository;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient testPatient;
    private Doctor testDoctor;
    private TimeSlot testTimeSlot;
    private Appointment testAppointment;
    private Admin testAdmin;

    @BeforeEach
    void setUp() {
        // Setup test data
        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setName("John Doe");
        testPatient.setPhone("1234567890");
        testPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPatient.setGender(Gender.MALE);

        User patientUser = new User();
        patientUser.setId(1L);
        patientUser.setEmail("patient@test.com");
        patientUser.setPassword("password");
        patientUser.setRole(Role.PATIENT);
        testPatient.setUser(patientUser);

        testDoctor = new Doctor();
        testDoctor.setId(1L);
        testDoctor.setName("Dr. Smith");
        testDoctor.setPhone("0987654321");
        testDoctor.setGender(Gender.MALE);
        testDoctor.setDepartment(Department.CARDIOLOGY);
        testDoctor.setLocation("New York");
        testDoctor.setDescription("Cardiologist");

        User doctorUser = new User();
        doctorUser.setId(2L);
        doctorUser.setEmail("doctor@test.com");
        doctorUser.setPassword("password");
        doctorUser.setRole(Role.DOCTOR);
        testDoctor.setUser(doctorUser);

        testTimeSlot = new TimeSlot();
        testTimeSlot.setId(1L);
        testTimeSlot.setDoctor(testDoctor);
        testTimeSlot.setSlotDate(LocalDate.now().plusDays(1));
        testTimeSlot.setStartTime(LocalTime.of(10, 0));
        testTimeSlot.setEndTime(LocalTime.of(10, 30));
        testTimeSlot.setBooked(false);

        testAppointment = new Appointment();
        testAppointment.setId(1L);
        testAppointment.setPatient(testPatient);
        testAppointment.setDoctor(testDoctor);
        testAppointment.setTimeSlot(testTimeSlot);
        testAppointment.setReason("Regular checkup");
        testAppointment.setStatus(AppointmentStatus.BOOKED);
        testAppointment.setType(AppointmentType.PRE_BOOKED);

        testAdmin = new Admin();
        testAdmin.setId(1L);
        testAdmin.setMaxDaysInAdvance(30);
        testAdmin.setMaxPatientsPerSlot(1);
        testAdmin.setCancellationCutoffHours(24);
    }

    // Test getDoctorsByDepartment - Success Case
    @Test
    void testGetDoctorsByDepartment_Success() throws InfyHospitalException {
        // Given
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorRepository.findByDepartment(Department.CARDIOLOGY)).thenReturn(doctors);

        // When
        List<DoctorDTO> result = patientService.getDoctorsByDepartment(Department.CARDIOLOGY);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDoctor.getName(), result.get(0).getName());
        assertEquals(Department.CARDIOLOGY, result.get(0).getDepartment());
        verify(doctorRepository).findByDepartment(Department.CARDIOLOGY);
    }

    // Test getDoctorsByDepartment - No Doctors Found Exception
    @Test
    void testGetDoctorsByDepartment_NoDoctorsFound_ThrowsException() {
        // Given
        when(doctorRepository.findByDepartment(Department.ORTHOPEDICS)).thenReturn(Collections.emptyList());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getDoctorsByDepartment(Department.ORTHOPEDICS);
        });
        assertEquals("Service.NO_DOCTORS_FOUND", exception.getMessage());
        verify(doctorRepository).findByDepartment(Department.ORTHOPEDICS);
    }

    // Test getDoctorById - Success Case
    @Test
    void testGetDoctorById_Success() throws InfyHospitalException {
        // Given
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));

        // When
        DoctorDTO result = patientService.getDoctorById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testDoctor.getName(), result.getName());
        assertEquals(testDoctor.getDepartment(), result.getDepartment());
        verify(doctorRepository).findById(1L);
    }

    // Test getDoctorById - Doctor Not Found Exception
    @Test
    void testGetDoctorById_DoctorNotFound_ThrowsException() {
        // Given
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getDoctorById(999L);
        });
        assertEquals("Service.NO_DOCTOR_FOUND", exception.getMessage());
        verify(doctorRepository).findById(999L);
    }

    // Test getAllSlots - Success Case
    @Test
    void testGetAllSlots_Success() throws InfyHospitalException {
        // Given
        List<TimeSlot> timeSlots = Arrays.asList(testTimeSlot);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(timeSlotRepository.findByDoctorIdAndSlotDate(1L, LocalDate.now().plusDays(1))).thenReturn(timeSlots);

        // When
        List<TimeSlotDTO> result = patientService.getAllSlots(1L, LocalDate.now().plusDays(1));

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTimeSlot.getStartTime(), result.get(0).getStartTime());
        assertEquals(testTimeSlot.getEndTime(), result.get(0).getEndTime());
        verify(doctorRepository).findById(1L);
        verify(timeSlotRepository).findByDoctorIdAndSlotDate(1L, LocalDate.now().plusDays(1));
    }

    // Test getAllSlots - Doctor Not Found Exception
    @Test
    void testGetAllSlots_DoctorNotFound_ThrowsException() {
        // Given
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getAllSlots(999L, LocalDate.now().plusDays(1));
        });
        assertEquals("Service.NO_DOCTOR_FOUND", exception.getMessage());
        verify(doctorRepository).findById(999L);
    }

    // Test getAllSlots - No TimeSlots Found Exception
    @Test
    void testGetAllSlots_NoTimeSlotsFound_ThrowsException() {
        // Given
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(timeSlotRepository.findByDoctorIdAndSlotDate(1L, LocalDate.now().plusDays(1))).thenReturn(Collections.emptyList());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getAllSlots(1L, LocalDate.now().plusDays(1));
        });
        assertEquals("Service.NO_TIMESLOTS_FOUND", exception.getMessage());
        verify(doctorRepository).findById(1L);
        verify(timeSlotRepository).findByDoctorIdAndSlotDate(1L, LocalDate.now().plusDays(1));
    }

    // Test bookAppointment - Success Case
    @Test
    void testBookAppointment_Success() throws InfyHospitalException {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(testTimeSlot));
        when(timeSlotRepository.save(any(TimeSlot.class))).thenReturn(testTimeSlot);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        // When
        Long result = patientService.bookAppointment(1L, 1L, 1L, "Regular checkup");

        // Then
        assertNotNull(result);
        assertEquals(1L, result);
        assertTrue(testTimeSlot.getBooked());
        assertEquals(AppointmentStatus.BOOKED, testAppointment.getStatus());
        verify(patientRepository).findById(1L);
        verify(doctorRepository).findById(1L);
        verify(timeSlotRepository).findById(1L);
        verify(timeSlotRepository).save(testTimeSlot);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    // Test bookAppointment - Patient Not Found Exception
    @Test
    void testBookAppointment_PatientNotFound_ThrowsException() {
        // Given
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.bookAppointment(999L, 1L, 1L, "Regular checkup");
        });
        assertEquals("Service.NO_PATIENT_FOUND", exception.getMessage());
        verify(patientRepository).findById(999L);
    }

    // Test bookAppointment - Doctor Not Found Exception
    @Test
    void testBookAppointment_DoctorNotFound_ThrowsException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.bookAppointment(1L, 999L, 1L, "Regular checkup");
        });
        assertEquals("Service.NO_DOCTOR_FOUND", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(doctorRepository).findById(999L);
    }

    // Test bookAppointment - TimeSlot Not Found Exception
    @Test
    void testBookAppointment_TimeSlotNotFound_ThrowsException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(timeSlotRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.bookAppointment(1L, 1L, 999L, "Regular checkup");
        });
        assertEquals("Service.NO_TIMESLOT_FOUND", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(doctorRepository).findById(1L);
        verify(timeSlotRepository).findById(999L);
    }

    // Test bookAppointment - Slot Does Not Belong To Doctor Exception
    @Test
    void testBookAppointment_SlotDoesNotBelongToDoctor_ThrowsException() {
        // Given
        Doctor differentDoctor = new Doctor();
        differentDoctor.setId(2L);
        testTimeSlot.setDoctor(differentDoctor);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(testTimeSlot));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.bookAppointment(1L, 1L, 1L, "Regular checkup");
        });
        assertEquals("Service.SLOT_DOES_NOT_BELONG_TO_DOCTOR", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(doctorRepository).findById(1L);
        verify(timeSlotRepository).findById(1L);
    }

    // Test bookAppointment - Already Booked Exception
    @Test
    void testBookAppointment_AlreadyBooked_ThrowsException() {
        // Given
        testTimeSlot.setBooked(true);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(testTimeSlot));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.bookAppointment(1L, 1L, 1L, "Regular checkup");
        });
        assertEquals("Service.ALREADY_BOOKED", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(doctorRepository).findById(1L);
        verify(timeSlotRepository).findById(1L);
    }

    // Test getUpcomingAppointments - Success Case
    @Test
    void testGetUpcomingAppointments_Success() throws InfyHospitalException {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(appointmentRepository.findByPatientId(1L)).thenReturn(appointments);

        // When
        List<AppointmentDTO> result = patientService.getUpcomingAppointments(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(AppointmentStatus.BOOKED, result.get(0).getStatus());
        verify(patientRepository).findById(1L);
        verify(appointmentRepository).findByPatientId(1L);
    }

    // Test getUpcomingAppointments - Patient Not Found Exception
    @Test
    void testGetUpcomingAppointments_PatientNotFound_ThrowsException() {
        // Given
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getUpcomingAppointments(999L);
        });
        assertEquals("Service.NO_PATIENT_FOUND", exception.getMessage());
        verify(patientRepository).findById(999L);
    }

    // Test getUpcomingAppointments - No Appointments Found Exception
    @Test
    void testGetUpcomingAppointments_NoAppointmentsFound_ThrowsException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(appointmentRepository.findByPatientId(1L)).thenReturn(Collections.emptyList());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getUpcomingAppointments(1L);
        });
        assertEquals("Service.APPOINTMENT_NOT_FOUND", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(appointmentRepository).findByPatientId(1L);
    }

    // Test getUpcomingAppointments - No Upcoming Appointments Found Exception
    @Test
    void testGetUpcomingAppointments_NoUpcomingAppointmentsFound_ThrowsException() {
        // Given
        testAppointment.setStatus(AppointmentStatus.COMPLETED);
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(appointmentRepository.findByPatientId(1L)).thenReturn(appointments);

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getUpcomingAppointments(1L);
        });
        assertEquals("Service.NO_UPCOMING_APPOINTMENT_FOUND", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(appointmentRepository).findByPatientId(1L);
    }

    // Test getAllAppointments - Success Case
    @Test
    void testGetAllAppointments_Success() throws InfyHospitalException {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(appointmentRepository.findByPatientId(1L)).thenReturn(appointments);

        // When
        List<AppointmentDTO> result = patientService.getAllAppointments(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(patientRepository).findById(1L);
        verify(appointmentRepository).findByPatientId(1L);
    }

    // Test getAllAppointments - No Appointments Found Exception
    @Test
    void testGetAllAppointments_NoAppointmentsFound_ThrowsException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(appointmentRepository.findByPatientId(1L)).thenReturn(Collections.emptyList());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getAllAppointments(1L);
        });
        assertEquals("Service.APPOINTMENT_NOT_FOUND", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(appointmentRepository).findByPatientId(1L);
    }

    // Test cancelAppointment - Success Case
    @Test
    void testCancelAppointment_Success() throws InfyHospitalException {
        // Given
        LocalDateTime appointmentDateTime = LocalDateTime.now().plusDays(2);
        testTimeSlot.setSlotDate(appointmentDateTime.toLocalDate());
        testTimeSlot.setStartTime(appointmentDateTime.toLocalTime());

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(adminRepository.findById(2L)).thenReturn(Optional.of(testAdmin));
        when(timeSlotRepository.save(any(TimeSlot.class))).thenReturn(testTimeSlot);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        // When
        Long result = patientService.cancelAppointment(1L);

        // Then
        assertEquals(1L, result);
        assertEquals(AppointmentStatus.CANCELLED, testAppointment.getStatus());
        assertFalse(testTimeSlot.getBooked());
        verify(appointmentRepository).findById(1L);
        verify(adminRepository).findById(2L);
        verify(timeSlotRepository).save(testTimeSlot);
        verify(appointmentRepository).save(testAppointment);
    }

    // Test cancelAppointment - Appointment Not Found Exception
    @Test
    void testCancelAppointment_AppointmentNotFound_ThrowsException() {
        // Given
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.cancelAppointment(999L);
        });
        assertEquals("Service.APPOINTMENT_NOT_FOUND", exception.getMessage());
        verify(appointmentRepository).findById(999L);
    }

    // Test cancelAppointment - Already Cancelled Exception
    @Test
    void testCancelAppointment_AlreadyCancelled_ThrowsException() {
        // Given
        testAppointment.setStatus(AppointmentStatus.CANCELLED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.cancelAppointment(1L);
        });
        assertEquals("Service.ALREADY_CANCELLED", exception.getMessage());
        verify(appointmentRepository).findById(1L);
    }

    // Test cancelAppointment - Admin Not Found Exception
    @Test
    void testCancelAppointment_AdminNotFound_ThrowsException() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(adminRepository.findById(2L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.cancelAppointment(1L);
        });
        assertEquals("Service.NO_ADMIN_FOUND", exception.getMessage());
        verify(appointmentRepository).findById(1L);
        verify(adminRepository).findById(2L);
    }

    // Test cancelAppointment - Cancellation Not Allowed Exception
    @Test
    void testCancelAppointment_CancellationNotAllowed_ThrowsException() {
        // Given
        LocalDateTime appointmentDateTime = LocalDateTime.now().plusHours(12); // Within cutoff hours
        testTimeSlot.setSlotDate(appointmentDateTime.toLocalDate());
        testTimeSlot.setStartTime(appointmentDateTime.toLocalTime());

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(adminRepository.findById(2L)).thenReturn(Optional.of(testAdmin));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.cancelAppointment(1L);
        });
        assertEquals("Service.CANCELLATION_NOT_ALLOWED", exception.getMessage());
        verify(appointmentRepository).findById(1L);
        verify(adminRepository).findById(2L);
    }

    // Test reschedule - Success Case
    @Test
    void testReschedule_Success() throws InfyHospitalException {
        // Given
        TimeSlot newSlot = new TimeSlot();
        newSlot.setId(2L);
        newSlot.setDoctor(testDoctor);
        newSlot.setSlotDate(LocalDate.now().plusDays(2));
        newSlot.setStartTime(LocalTime.of(14, 0));
        newSlot.setEndTime(LocalTime.of(14, 30));
        newSlot.setBooked(false);

        LocalDateTime oldAppointmentDateTime = LocalDateTime.now().plusDays(1);
        testTimeSlot.setSlotDate(oldAppointmentDateTime.toLocalDate());
        testTimeSlot.setStartTime(oldAppointmentDateTime.toLocalTime());

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(timeSlotRepository.findById(2L)).thenReturn(Optional.of(newSlot));
        when(timeSlotRepository.save(any(TimeSlot.class))).thenReturn(testTimeSlot);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        // When
        AppointmentDTO result = patientService.reschedule(1L, 2L);

        // Then
        assertNotNull(result);
        assertFalse(testTimeSlot.getBooked());
        assertTrue(newSlot.getBooked());
        verify(appointmentRepository).findById(1L);
        verify(timeSlotRepository).findById(2L);
        verify(timeSlotRepository).save(testTimeSlot);
        verify(timeSlotRepository).save(newSlot);
        verify(appointmentRepository).save(testAppointment);
    }

    // Test reschedule - Appointment Not Found Exception
    @Test
    void testReschedule_AppointmentNotFound_ThrowsException() {
        // Given
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.reschedule(999L, 2L);
        });
        assertEquals("Service.APPOINTMENT_NOT_FOUND", exception.getMessage());
        verify(appointmentRepository).findById(999L);
    }

    // Test reschedule - Reschedule Not Allowed Exception
    @Test
    void testReschedule_RescheduleNotAllowed_ThrowsException() {
        // Given
        LocalDateTime oldAppointmentDateTime = LocalDateTime.now().minusHours(1); // Past appointment
        testTimeSlot.setSlotDate(oldAppointmentDateTime.toLocalDate());
        testTimeSlot.setStartTime(oldAppointmentDateTime.toLocalTime());

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.reschedule(1L, 2L);
        });
        assertEquals("Service.RESCHEDULE_NOT_ALLOWED", exception.getMessage());
        verify(appointmentRepository).findById(1L);
    }

    // Test reschedule - New TimeSlot Not Found Exception
    @Test
    void testReschedule_NewTimeSlotNotFound_ThrowsException() {
        // Given
        LocalDateTime oldAppointmentDateTime = LocalDateTime.now().plusDays(1);
        testTimeSlot.setSlotDate(oldAppointmentDateTime.toLocalDate());
        testTimeSlot.setStartTime(oldAppointmentDateTime.toLocalTime());

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(timeSlotRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.reschedule(1L, 999L);
        });
        assertEquals("Service.NO_TIMESLOTS_FOUND", exception.getMessage());
        verify(appointmentRepository).findById(1L);
        verify(timeSlotRepository).findById(999L);
    }

    // Test reschedule - New Slot Already Booked Exception
    @Test
    void testReschedule_NewSlotAlreadyBooked_ThrowsException() {
        // Given
        TimeSlot newSlot = new TimeSlot();
        newSlot.setId(2L);
        newSlot.setDoctor(testDoctor);
        newSlot.setSlotDate(LocalDate.now().plusDays(2));
        newSlot.setStartTime(LocalTime.of(14, 0));
        newSlot.setEndTime(LocalTime.of(14, 30));
        newSlot.setBooked(true);

        LocalDateTime oldAppointmentDateTime = LocalDateTime.now().plusDays(1);
        testTimeSlot.setSlotDate(oldAppointmentDateTime.toLocalDate());
        testTimeSlot.setStartTime(oldAppointmentDateTime.toLocalTime());

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(timeSlotRepository.findById(2L)).thenReturn(Optional.of(newSlot));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.reschedule(1L, 2L);
        });
        assertEquals("Service.ALREADY_BOOKED", exception.getMessage());
        verify(appointmentRepository).findById(1L);
        verify(timeSlotRepository).findById(2L);
    }

    // Test getVisitHistoryFiltered - Success Case
    @Test
    void testGetVisitHistoryFiltered_Success() throws InfyHospitalException {
        // Given
        testAppointment.setStatus(AppointmentStatus.COMPLETED);
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(appointmentRepository.findFilteredHistory(1L, null, null, null)).thenReturn(appointments);

        // When
        List<AppointmentDTO> result = patientService.getVisitHistoryFiltered(1L, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(patientRepository).findById(1L);
        verify(appointmentRepository).findFilteredHistory(1L, null, null, null);
    }

    // Test getVisitHistoryFiltered - Patient Not Found Exception
    @Test
    void testGetVisitHistoryFiltered_PatientNotFound_ThrowsException() {
        // Given
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getVisitHistoryFiltered(999L, null, null, null);
        });
        assertEquals("Service.NO_PATIENT_FOUND", exception.getMessage());
        verify(patientRepository).findById(999L);
    }

    // Test getVisitHistoryFiltered - Invalid Time Range Exception
    @Test
    void testGetVisitHistoryFiltered_InvalidTimeRange_ThrowsException() {
        // Given
        LocalDate startDate = LocalDate.now().plusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(1); // End date before start date
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getVisitHistoryFiltered(1L, startDate, endDate, null);
        });
        assertEquals("Service.INVALID_TIME_RANGE", exception.getMessage());
        verify(patientRepository).findById(1L);
    }

    // Test getVisitHistoryFiltered - No History Found Exception
    @Test
    void testGetVisitHistoryFiltered_NoHistoryFound_ThrowsException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(appointmentRepository.findFilteredHistory(1L, null, null, null)).thenReturn(Collections.emptyList());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            patientService.getVisitHistoryFiltered(1L, null, null, null);
        });
        assertEquals("Service.NO_HISTORY_FOUND", exception.getMessage());
        verify(patientRepository).findById(1L);
        verify(appointmentRepository).findFilteredHistory(1L, null, null, null);
    }
}
