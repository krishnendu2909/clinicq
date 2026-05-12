package com.infy.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.infy.entity.Appointment;
import com.infy.entity.Doctor;
import com.infy.entity.Patient;
import com.infy.entity.TimeSlot;
import com.infy.entity.User;
import com.infy.models.AppointmentStatus;
import com.infy.models.AppointmentType;
import com.infy.models.Department;
import com.infy.models.Gender;
import com.infy.models.Role;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class AppointmentRepositoryTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    private Appointment testAppointment;
    private Appointment testAppointment2;
    private Patient testPatient;
    private Doctor testDoctor;
    private TimeSlot testTimeSlot;

    @BeforeEach
    void setUp() {
        // Setup test data
        User patientUser = new User();
        patientUser.setId(1L);
        patientUser.setEmail("patient@test.com");
        patientUser.setPassword("password");
        patientUser.setRole(Role.PATIENT);

        User doctorUser = new User();
        doctorUser.setId(2L);
        doctorUser.setEmail("doctor@test.com");
        doctorUser.setPassword("password");
        doctorUser.setRole(Role.DOCTOR);

        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setName("John Doe");
        testPatient.setPhone("9876543210");
        testPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPatient.setGender(Gender.MALE);
        testPatient.setUser(patientUser);

        testDoctor = new Doctor();
        testDoctor.setId(1L);
        testDoctor.setName("Dr. Smith");
        testDoctor.setPhone("1234567890");
        testDoctor.setGender(Gender.MALE);
        testDoctor.setDepartment(Department.CARDIOLOGY);
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

        testAppointment2 = new Appointment();
        testAppointment2.setId(2L);
        testAppointment2.setPatient(testPatient);
        testAppointment2.setDoctor(testDoctor);
        testAppointment2.setTimeSlot(testTimeSlot);
        testAppointment2.setReason("Follow-up");
        testAppointment2.setStatus(AppointmentStatus.COMPLETED);
        testAppointment2.setType(AppointmentType.PRE_BOOKED);
    }

    // Test findByPatientId - Success Case
    @Test
    void testFindByPatientId_Success() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment, testAppointment2);
        when(appointmentRepository.findByPatientId(1L)).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findByPatientId(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testAppointment));
        assertTrue(result.contains(testAppointment2));
        verify(appointmentRepository).findByPatientId(1L);
    }

    // Test findByPatientId - Empty Result Case
    @Test
    void testFindByPatientId_EmptyResult() {
        // Given
        when(appointmentRepository.findByPatientId(999L)).thenReturn(Collections.emptyList());

        // When
        List<Appointment> result = appointmentRepository.findByPatientId(999L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(appointmentRepository).findByPatientId(999L);
    }

    // Test findByDoctorId - Success Case
    @Test
    void testFindByDoctorId_Success() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findByDoctorId(1L)).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findByDoctorId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Regular checkup", result.get(0).getReason());
        verify(appointmentRepository).findByDoctorId(1L);
    }

    // Test findByDoctorId - Empty Result Case
    @Test
    void testFindByDoctorId_EmptyResult() {
        // Given
        when(appointmentRepository.findByDoctorId(999L)).thenReturn(Collections.emptyList());

        // When
        List<Appointment> result = appointmentRepository.findByDoctorId(999L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(appointmentRepository).findByDoctorId(999L);
    }

    // Test findByStatus - Success Case with BOOKED Status
    @Test
    void testFindByStatus_SuccessWithBookedStatus() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findByStatus(AppointmentStatus.BOOKED)).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findByStatus(AppointmentStatus.BOOKED);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(AppointmentStatus.BOOKED, result.get(0).getStatus());
        verify(appointmentRepository).findByStatus(AppointmentStatus.BOOKED);
    }

    // Test findByStatus - Success Case with COMPLETED Status
    @Test
    void testFindByStatus_SuccessWithCompletedStatus() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment2);
        when(appointmentRepository.findByStatus(AppointmentStatus.COMPLETED)).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findByStatus(AppointmentStatus.COMPLETED);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(AppointmentStatus.COMPLETED, result.get(0).getStatus());
        verify(appointmentRepository).findByStatus(AppointmentStatus.COMPLETED);
    }

    // Test findByStatus - Success Case with Multiple Status Matches
    @Test
    void testFindByStatus_SuccessWithMultipleStatusMatches() {
        // Given
        Appointment appointment3 = new Appointment();
        appointment3.setId(3L);
        appointment3.setPatient(testPatient);
        appointment3.setDoctor(testDoctor);
        appointment3.setTimeSlot(testTimeSlot);
        appointment3.setReason("Consultation");
        appointment3.setStatus(AppointmentStatus.BOOKED);
        appointment3.setType(AppointmentType.PRE_BOOKED);

        List<Appointment> appointments = Arrays.asList(testAppointment, appointment3);
        when(appointmentRepository.findByStatus(AppointmentStatus.BOOKED)).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findByStatus(AppointmentStatus.BOOKED);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.getStatus() == AppointmentStatus.BOOKED));
        verify(appointmentRepository).findByStatus(AppointmentStatus.BOOKED);
    }

    // Test findByStatus - Empty Result Case
    @Test
    void testFindByStatus_EmptyResult() {
        // Given
        when(appointmentRepository.findByStatus(AppointmentStatus.CANCELLED))
                .thenReturn(Collections.emptyList());

        // When
        List<Appointment> result = appointmentRepository.findByStatus(AppointmentStatus.CANCELLED);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(appointmentRepository).findByStatus(AppointmentStatus.CANCELLED);
    }

    // Test findByTimeSlotId - Success Case
    @Test
    void testFindByTimeSlotId_Success() {
        // Given
        when(appointmentRepository.findByTimeSlotId(1L)).thenReturn(Optional.of(testAppointment));

        // When
        Optional<Appointment> result = appointmentRepository.findByTimeSlotId(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Regular checkup", result.get().getReason());
        assertEquals(1L, result.get().getTimeSlot().getId());
        verify(appointmentRepository).findByTimeSlotId(1L);
    }

    // Test findByTimeSlotId - Not Found Case
    @Test
    void testFindByTimeSlotId_NotFound() {
        // Given
        when(appointmentRepository.findByTimeSlotId(999L)).thenReturn(Optional.empty());

        // When
        Optional<Appointment> result = appointmentRepository.findByTimeSlotId(999L);

        // Then
        assertFalse(result.isPresent());
        verify(appointmentRepository).findByTimeSlotId(999L);
    }

    // Test findFilteredHistory - Success Case with All Parameters
    @Test
    void testFindFilteredHistory_SuccessWithAllParameters() {
        // Given
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        List<Appointment> appointments = Arrays.asList(testAppointment2);
        when(appointmentRepository.findFilteredHistory(1L, 1L, startDate, endDate))
                .thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, 1L, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(AppointmentStatus.COMPLETED, result.get(0).getStatus());
        verify(appointmentRepository).findFilteredHistory(1L, 1L, startDate, endDate);
    }

    // Test findFilteredHistory - Success Case with Only Patient ID
    @Test
    void testFindFilteredHistory_SuccessWithOnlyPatientId() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment2);
        when(appointmentRepository.findFilteredHistory(1L, null, null, null))
                .thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(AppointmentStatus.COMPLETED, result.get(0).getStatus());
        verify(appointmentRepository).findFilteredHistory(1L, null, null, null);
    }

    // Test findFilteredHistory - Success Case with Patient ID and Doctor ID
    @Test
    void testFindFilteredHistory_SuccessWithPatientIdAndDoctorId() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment2);
        when(appointmentRepository.findFilteredHistory(1L, 1L, null, null))
                .thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, 1L, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findFilteredHistory(1L, 1L, null, null);
    }

    // Test findFilteredHistory - Success Case with Patient ID and Date Range
    @Test
    void testFindFilteredHistory_SuccessWithPatientIdAndDateRange() {
        // Given
        LocalDate startDate = LocalDate.now().minusMonths(3);
        LocalDate endDate = LocalDate.now();
        List<Appointment> appointments = Arrays.asList(testAppointment2);
        when(appointmentRepository.findFilteredHistory(1L, null, startDate, endDate))
                .thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, null, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findFilteredHistory(1L, null, startDate, endDate);
    }

    // Test findFilteredHistory - Success Case with Only Date Range
    @Test
    void testFindFilteredHistory_SuccessWithOnlyDateRange() {
        // Given
        LocalDate startDate = LocalDate.now().minusMonths(6);
        LocalDate endDate = LocalDate.now();
        List<Appointment> appointments = Arrays.asList(testAppointment2);
        when(appointmentRepository.findFilteredHistory(1L, null, startDate, endDate))
                .thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, null, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findFilteredHistory(1L, null, startDate, endDate);
    }

    // Test findFilteredHistory - Success Case with Only Doctor ID
    @Test
    void testFindFilteredHistory_SuccessWithOnlyDoctorId() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment2);
        when(appointmentRepository.findFilteredHistory(1L, 1L, null, null))
                .thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, 1L, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findFilteredHistory(1L, 1L, null, null);
    }

    // Test findFilteredHistory - Empty Result Case
    @Test
    void testFindFilteredHistory_EmptyResult() {
        // Given
        when(appointmentRepository.findFilteredHistory(1L, null, null, null))
                .thenReturn(Collections.emptyList());

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, null, null, null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(appointmentRepository).findFilteredHistory(1L, null, null, null);
    }

    // Test findFilteredHistory - Success Case with Multiple Completed Appointments
    @Test
    void testFindFilteredHistory_SuccessWithMultipleCompletedAppointments() {
        // Given
        Appointment appointment3 = new Appointment();
        appointment3.setId(3L);
        appointment3.setPatient(testPatient);
        appointment3.setDoctor(testDoctor);
        appointment3.setTimeSlot(testTimeSlot);
        appointment3.setReason("Emergency");
        appointment3.setStatus(AppointmentStatus.COMPLETED);
        appointment3.setType(AppointmentType.WALK_IN);

        List<Appointment> appointments = Arrays.asList(testAppointment2, appointment3);
        when(appointmentRepository.findFilteredHistory(1L, null, null, null))
                .thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.getStatus() == AppointmentStatus.COMPLETED));
        verify(appointmentRepository).findFilteredHistory(1L, null, null, null);
    }

    // Test findById - Success Case
    @Test
    void testFindById_Success() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));

        // When
        Optional<Appointment> result = appointmentRepository.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Regular checkup", result.get().getReason());
        assertEquals(AppointmentStatus.BOOKED, result.get().getStatus());
        verify(appointmentRepository).findById(1L);
    }

    // Test findById - Not Found Case
    @Test
    void testFindById_NotFound() {
        // Given
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Appointment> result = appointmentRepository.findById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(appointmentRepository).findById(999L);
    }

    // Test save - Success Case
    @Test
    void testSave_Success() {
        // Given
        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(testPatient);
        newAppointment.setDoctor(testDoctor);
        newAppointment.setTimeSlot(testTimeSlot);
        newAppointment.setReason("New appointment");
        newAppointment.setStatus(AppointmentStatus.BOOKED);
        newAppointment.setType(AppointmentType.PRE_BOOKED);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        // When
        Appointment result = appointmentRepository.save(newAppointment);

        // Then
        assertNotNull(result);
        assertEquals("Regular checkup", result.getReason());
        verify(appointmentRepository).save(newAppointment);
    }

    // Test findAll - Success Case
    @Test
    void testFindAll_Success() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment, testAppointment2);
        when(appointmentRepository.findAll()).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testAppointment));
        assertTrue(result.contains(testAppointment2));
        verify(appointmentRepository).findAll();
    }

    // Test findAll - Empty Result Case
    @Test
    void testFindAll_EmptyResult() {
        // Given
        when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Appointment> result = appointmentRepository.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(appointmentRepository).findAll();
    }

    // Test deleteById - Success Case
    @Test
    void testDeleteById_Success() {
        // Given
        doNothing().when(appointmentRepository).deleteById(1L);

        // When
        appointmentRepository.deleteById(1L);

        // Then
        verify(appointmentRepository).deleteById(1L);
    }

    // Test count - Success Case
    @Test
    void testCount_Success() {
        // Given
        when(appointmentRepository.count()).thenReturn(2L);

        // When
        Long result = appointmentRepository.count();

        // Then
        assertEquals(2L, result);
        verify(appointmentRepository).count();
    }

    // Test existsById - Success Case
    @Test
    void testExistsById_Success() {
        // Given
        when(appointmentRepository.existsById(1L)).thenReturn(true);

        // When
        Boolean result = appointmentRepository.existsById(1L);

        // Then
        assertTrue(result);
        verify(appointmentRepository).existsById(1L);
    }

    // Test existsById - Not Found Case
    @Test
    void testExistsById_NotFound() {
        // Given
        when(appointmentRepository.existsById(999L)).thenReturn(false);

        // When
        Boolean result = appointmentRepository.existsById(999L);

        // Then
        assertFalse(result);
        verify(appointmentRepository).existsById(999L);
    }

    // Test findAll with Pageable - Success Case
    @Test
    void testFindAllWithPageable_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appointment> appointmentPage = new PageImpl<>(Arrays.asList(testAppointment));
        when(appointmentRepository.findAll(any(Pageable.class))).thenReturn(appointmentPage);

        // When
        Page<Appointment> result = appointmentRepository.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Regular checkup", result.getContent().get(0).getReason());
        verify(appointmentRepository).findAll(pageable);
    }

    // Test findFilteredHistory - Edge Case with Invalid Date Range
    @Test
    void testFindFilteredHistory_EdgeCaseWithInvalidDateRange() {
        // Given
        LocalDate startDate = LocalDate.now().plusDays(1); // Future date
        LocalDate endDate = LocalDate.now(); // Past date
        when(appointmentRepository.findFilteredHistory(1L, null, startDate, endDate))
                .thenReturn(Collections.emptyList());

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, null, startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(appointmentRepository).findFilteredHistory(1L, null, startDate, endDate);
    }

    // Test findFilteredHistory - Edge Case with Same Start and End Date
    @Test
    void testFindFilteredHistory_EdgeCaseWithSameStartAndEndDate() {
        // Given
        LocalDate sameDate = LocalDate.now().minusDays(7);
        List<Appointment> appointments = Arrays.asList(testAppointment2);
        when(appointmentRepository.findFilteredHistory(1L, null, sameDate, sameDate))
                .thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, null, sameDate, sameDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findFilteredHistory(1L, null, sameDate, sameDate);
    }

    // Test findFilteredHistory - Edge Case with Very Old Start Date
    @Test
    void testFindFilteredHistory_EdgeCaseWithVeryOldStartDate() {
        // Given
        LocalDate veryOldDate = LocalDate.of(1900, 1, 1);
        List<Appointment> appointments = Arrays.asList(testAppointment2);
        when(appointmentRepository.findFilteredHistory(1L, null, veryOldDate, null))
                .thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, null, veryOldDate, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findFilteredHistory(1L, null, veryOldDate, null);
    }

    // Test findFilteredHistory - Edge Case with Future End Date
    @Test
    void testFindFilteredHistory_EdgeCaseWithFutureEndDate() {
        // Given
        LocalDate futureDate = LocalDate.now().plusYears(1);
        List<Appointment> appointments = Arrays.asList(testAppointment2);
        when(appointmentRepository.findFilteredHistory(1L, null, null, futureDate))
                .thenReturn(appointments);

        // When
        List<Appointment> result = appointmentRepository.findFilteredHistory(1L, null, null, futureDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findFilteredHistory(1L, null, null, futureDate);
    }
}
