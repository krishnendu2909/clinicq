package com.infy.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.infy.dto.AppointmentDTO;
import com.infy.dto.AppointmentRequestDTO;
import com.infy.dto.DoctorDTO;
import com.infy.dto.TimeSlotDTO;
import com.infy.dto.UserDTO;
import com.infy.exception.InfyHospitalException;
import com.infy.models.Department;
import com.infy.service.PatientService;

@ExtendWith(MockitoExtension.class)
class PatientApiTest {

    @Mock
    private PatientService patientService;

    @Mock
    private Environment environment;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PatientApi patientApi;

    private DoctorDTO testDoctorDTO;
    private TimeSlotDTO testTimeSlotDTO;
    private AppointmentDTO testAppointmentDTO;
    private AppointmentRequestDTO testAppointmentRequestDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        UserDTO doctorUser = new UserDTO();
        doctorUser.setId(1L);
        doctorUser.setEmail("doctor@test.com");
        doctorUser.setRole(com.infy.models.Role.DOCTOR);

        testDoctorDTO = new DoctorDTO();
        testDoctorDTO.setId(1L);
        testDoctorDTO.setName("Dr. Smith");
        testDoctorDTO.setDepartment(Department.CARDIOLOGY);
        testDoctorDTO.setPhone("1234567890");
        testDoctorDTO.setUser(doctorUser);

        testTimeSlotDTO = new TimeSlotDTO();
        testTimeSlotDTO.setId(1L);
        testTimeSlotDTO.setDoctor(testDoctorDTO);
        testTimeSlotDTO.setSlotDate(LocalDate.now().plusDays(1));
        testTimeSlotDTO.setStartTime(java.time.LocalTime.of(10, 0));
        testTimeSlotDTO.setEndTime(java.time.LocalTime.of(10, 30));
        testTimeSlotDTO.setBooked(false);

        testAppointmentDTO = new AppointmentDTO();
        testAppointmentDTO.setId(1L);
        testAppointmentDTO.setReason("Regular checkup");
        testAppointmentDTO.setStatus(com.infy.models.AppointmentStatus.BOOKED);
        testAppointmentDTO.setType(com.infy.models.AppointmentType.PRE_BOOKED);

        testAppointmentRequestDTO = new AppointmentRequestDTO();
        testAppointmentRequestDTO.setPatientId(1L);
        testAppointmentRequestDTO.setDoctorId(1L);
        testAppointmentRequestDTO.setSlotId(1L);
        testAppointmentRequestDTO.setReason("Regular checkup");
    }

    // Test getDoctorsByDepartment - Success Case
    @Test
    void testGetDoctorsByDepartment_Success() throws InfyHospitalException {
        // Given
        List<DoctorDTO> doctors = Arrays.asList(testDoctorDTO);
        when(patientService.getDoctorsByDepartment(Department.CARDIOLOGY)).thenReturn(doctors);

        // When
        ResponseEntity<List<DoctorDTO>> result = patientApi.getDoctorsByDepartment("CARDIOLOGY");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("Dr. Smith", result.getBody().get(0).getName());
        verify(patientService).getDoctorsByDepartment(Department.CARDIOLOGY);
    }

    // Test getDoctorsByDepartment - Success Case with Different Department
    @Test
    void testGetDoctorsByDepartment_SuccessWithDifferentDepartment() throws InfyHospitalException {
        // Given
        testDoctorDTO.setDepartment(Department.ORTHOPEDICS);
        List<DoctorDTO> doctors = Arrays.asList(testDoctorDTO);
        when(patientService.getDoctorsByDepartment(Department.ORTHOPEDICS)).thenReturn(doctors);

        // When
        ResponseEntity<List<DoctorDTO>> result = patientApi.getDoctorsByDepartment("ORTHOPEDICS");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals(Department.ORTHOPEDICS, result.getBody().get(0).getDepartment());
        verify(patientService).getDoctorsByDepartment(Department.ORTHOPEDICS);
    }

    // Test getDoctorsByDepartment - Success Case with Empty Result
    @Test
    void testGetDoctorsByDepartment_SuccessWithEmptyResult() throws InfyHospitalException {
        // Given
        when(patientService.getDoctorsByDepartment(Department.PEDIATRICS)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<DoctorDTO>> result = patientApi.getDoctorsByDepartment("PEDIATRICS");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
        verify(patientService).getDoctorsByDepartment(Department.PEDIATRICS);
    }

    // Test getDoctorsByDepartment - Service Exception
    @Test
    void testGetDoctorsByDepartment_ServiceException() throws InfyHospitalException {
        // Given
        when(patientService.getDoctorsByDepartment(Department.GENERAL))
                .thenThrow(new InfyHospitalException("Service.NO_DOCTORS_FOUND"));

        // When
        ResponseEntity<List<DoctorDTO>> result = patientApi.getDoctorsByDepartment("GENERAL");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(patientService).getDoctorsByDepartment(Department.GENERAL);
    }

    // Test getDoctorsById - Success Case
    @Test
    void testGetDoctorsById_Success() throws InfyHospitalException {
        // Given
        when(patientService.getDoctorById(1L)).thenReturn(testDoctorDTO);

        // When
        ResponseEntity<DoctorDTO> result = patientApi.getDoctorsById(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Dr. Smith", result.getBody().getName());
        assertEquals(1L, result.getBody().getId());
        verify(patientService).getDoctorById(1L);
    }

    // Test getDoctorsById - Service Exception
    @Test
    void testGetDoctorsById_ServiceException() throws InfyHospitalException {
        // Given
        when(patientService.getDoctorById(999L))
                .thenThrow(new InfyHospitalException("Service.NO_DOCTOR_FOUND"));

        // When
        ResponseEntity<DoctorDTO> result = patientApi.getDoctorsById(999L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(patientService).getDoctorById(999L);
    }

    // Test getSlots - Success Case
    @Test
    void testGetSlots_Success() throws InfyHospitalException {
        // Given
        List<TimeSlotDTO> slots = Arrays.asList(testTimeSlotDTO);
        when(patientService.getAllSlots(1L, LocalDate.now().plusDays(1))).thenReturn(slots);

        // When
        ResponseEntity<List<TimeSlotDTO>> result = patientApi.getSlots(1L, LocalDate.now().plusDays(1));

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(1L, result.getBody().get(0).getId());
        verify(patientService).getAllSlots(1L, LocalDate.now().plusDays(1));
    }

    // Test getSlots - Success Case with Multiple Slots
    @Test
    void testGetSlots_SuccessWithMultipleSlots() throws InfyHospitalException {
        // Given
        TimeSlotDTO slot2 = new TimeSlotDTO();
        slot2.setId(2L);
        slot2.setDoctor(testDoctorDTO);
        slot2.setSlotDate(LocalDate.now().plusDays(1));
        slot2.setStartTime(java.time.LocalTime.of(11, 0));
        slot2.setEndTime(java.time.LocalTime.of(11, 30));
        slot2.setBooked(false);

        List<TimeSlotDTO> slots = Arrays.asList(testTimeSlotDTO, slot2);
        when(patientService.getAllSlots(1L, LocalDate.now().plusDays(1))).thenReturn(slots);

        // When
        ResponseEntity<List<TimeSlotDTO>> result = patientApi.getSlots(1L, LocalDate.now().plusDays(1));

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(patientService).getAllSlots(1L, LocalDate.now().plusDays(1));
    }

    // Test getSlots - Success Case with Empty Result
    @Test
    void testGetSlots_SuccessWithEmptyResult() throws InfyHospitalException {
        // Given
        when(patientService.getAllSlots(1L, LocalDate.now().plusDays(1)))
                .thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<TimeSlotDTO>> result = patientApi.getSlots(1L, LocalDate.now().plusDays(1));

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
        verify(patientService).getAllSlots(1L, LocalDate.now().plusDays(1));
    }

    // Test getSlots - Service Exception
    @Test
    void testGetSlots_ServiceException() throws InfyHospitalException {
        // Given
        when(patientService.getAllSlots(1L, LocalDate.now().plusDays(1)))
                .thenThrow(new InfyHospitalException("Service.NO_TIMESLOTS_FOUND"));

        // When
        ResponseEntity<List<TimeSlotDTO>> result = patientApi.getSlots(1L, LocalDate.now().plusDays(1));

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(patientService).getAllSlots(1L, LocalDate.now().plusDays(1));
    }

    // Test bookAppointment - Success Case
    @Test
    void testBookAppointment_Success() throws InfyHospitalException {
        // Given
        when(patientService.bookAppointment(1L, 1L, 1L, "Regular checkup")).thenReturn(1L);
        when(environment.getProperty("Api.BOOKING_SUCCESS")).thenReturn("Appointment booked successfully with ID: ");

        // When
        ResponseEntity<String> result = patientApi.bookAppointment(testAppointmentRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("Appointment booked successfully with ID: 1"));
        verify(patientService).bookAppointment(1L, 1L, 1L, "Regular checkup");
        verify(environment).getProperty("Api.BOOKING_SUCCESS");
    }

    // Test bookAppointment - Service Exception
    @Test
    void testBookAppointment_ServiceException() throws InfyHospitalException {
        // Given
        when(patientService.bookAppointment(1L, 1L, 1L, "Regular checkup"))
                .thenThrow(new InfyHospitalException("Service.ALREADY_BOOKED"));

        // When
        ResponseEntity<String> result = patientApi.bookAppointment(testAppointmentRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(patientService).bookAppointment(1L, 1L, 1L, "Regular checkup");
    }

    // Test getUpcomingAppointments - Success Case
    @Test
    void testGetUpcomingAppointments_Success() throws InfyHospitalException {
        // Given
        List<AppointmentDTO> appointments = Arrays.asList(testAppointmentDTO);
        when(patientService.getUpcomingAppointments(1L)).thenReturn(appointments);

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getUpcomingAppointments(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("Regular checkup", result.getBody().get(0).getReason());
        verify(patientService).getUpcomingAppointments(1L);
    }

    // Test getUpcomingAppointments - Success Case with Empty Result
    @Test
    void testGetUpcomingAppointments_SuccessWithEmptyResult() throws InfyHospitalException {
        // Given
        when(patientService.getUpcomingAppointments(1L)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getUpcomingAppointments(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
        verify(patientService).getUpcomingAppointments(1L);
    }

    // Test getUpcomingAppointments - Service Exception
    @Test
    void testGetUpcomingAppointments_ServiceException() throws InfyHospitalException {
        // Given
        when(patientService.getUpcomingAppointments(1L))
                .thenThrow(new InfyHospitalException("Service.NO_UPCOMING_APPOINTMENT_FOUND"));

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getUpcomingAppointments(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(patientService).getUpcomingAppointments(1L);
    }

    // Test getAllAppointments - Success Case
    @Test
    void testGetAllAppointments_Success() throws InfyHospitalException {
        // Given
        List<AppointmentDTO> appointments = Arrays.asList(testAppointmentDTO);
        when(patientService.getAllAppointments(1L)).thenReturn(appointments);

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getAllAppointments(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        verify(patientService).getAllAppointments(1L);
    }

    // Test getAllAppointments - Success Case with Multiple Appointments
    @Test
    void testGetAllAppointments_SuccessWithMultipleAppointments() throws InfyHospitalException {
        // Given
        AppointmentDTO appointment2 = new AppointmentDTO();
        appointment2.setId(2L);
        appointment2.setReason("Follow-up");
        appointment2.setStatus(com.infy.models.AppointmentStatus.COMPLETED);
        appointment2.setType(com.infy.models.AppointmentType.PRE_BOOKED);

        List<AppointmentDTO> appointments = Arrays.asList(testAppointmentDTO, appointment2);
        when(patientService.getAllAppointments(1L)).thenReturn(appointments);

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getAllAppointments(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(patientService).getAllAppointments(1L);
    }

    // Test getAllAppointments - Service Exception
    @Test
    void testGetAllAppointments_ServiceException() throws InfyHospitalException {
        // Given
        when(patientService.getAllAppointments(1L))
                .thenThrow(new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND"));

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getAllAppointments(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(patientService).getAllAppointments(1L);
    }

    // Test cancelAppointment - Success Case
    @Test
    void testCancelAppointment_Success() throws InfyHospitalException {
        // Given
        when(patientService.cancelAppointment(1L)).thenReturn(1L);
        when(environment.getProperty("Api.CANCELING_SUCCESS")).thenReturn("Appointment cancelled successfully with ID: ");

        // When
        ResponseEntity<String> result = patientApi.cancelAppointment(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("Appointment cancelled successfully with ID: 1"));
        verify(patientService).cancelAppointment(1L);
        verify(environment).getProperty("Api.CANCELING_SUCCESS");
    }

    // Test cancelAppointment - Service Exception
    @Test
    void testCancelAppointment_ServiceException() throws InfyHospitalException {
        // Given
        when(patientService.cancelAppointment(1L))
                .thenThrow(new InfyHospitalException("Service.ALREADY_CANCELLED"));

        // When
        ResponseEntity<String> result = patientApi.cancelAppointment(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(patientService).cancelAppointment(1L);
    }

    // Test rescheduleAppointment - Success Case
    @Test
    void testRescheduleAppointment_Success() throws InfyHospitalException {
        // Given
        when(patientService.reschedule(1L, 2L)).thenReturn(testAppointmentDTO);

        // When
        ResponseEntity<AppointmentDTO> result = patientApi.rescheduleAppointment(1L, 2L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getId());
        verify(patientService).reschedule(1L, 2L);
    }

    // Test rescheduleAppointment - Service Exception
    @Test
    void testRescheduleAppointment_ServiceException() throws InfyHospitalException {
        // Given
        when(patientService.reschedule(1L, 2L))
                .thenThrow(new InfyHospitalException("Service.RESCHEDULE_NOT_ALLOWED"));

        // When
        ResponseEntity<AppointmentDTO> result = patientApi.rescheduleAppointment(1L, 2L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(patientService).reschedule(1L, 2L);
    }

    // Test getVisitHistoryFiltered - Success Case
    @Test
    void testGetVisitHistoryFiltered_Success() throws InfyHospitalException {
        // Given
        List<AppointmentDTO> appointments = Arrays.asList(testAppointmentDTO);
        when(patientService.getVisitHistoryFiltered(1L, null, null, null)).thenReturn(appointments);

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getVisitHistoryFiltered(1L, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        verify(patientService).getVisitHistoryFiltered(1L, null, null, null);
    }

    // Test getVisitHistoryFiltered - Success Case with Date Range
    @Test
    void testGetVisitHistoryFiltered_SuccessWithDateRange() throws InfyHospitalException {
        // Given
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        List<AppointmentDTO> appointments = Arrays.asList(testAppointmentDTO);
        when(patientService.getVisitHistoryFiltered(1L, startDate, endDate, null)).thenReturn(appointments);

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getVisitHistoryFiltered(1L, startDate, endDate, null);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(patientService).getVisitHistoryFiltered(1L, startDate, endDate, null);
    }

    // Test getVisitHistoryFiltered - Success Case with Doctor Filter
    @Test
    void testGetVisitHistoryFiltered_SuccessWithDoctorFilter() throws InfyHospitalException {
        // Given
        List<AppointmentDTO> appointments = Arrays.asList(testAppointmentDTO);
        when(patientService.getVisitHistoryFiltered(1L, null, null, 1L)).thenReturn(appointments);

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getVisitHistoryFiltered(1L, null, null, 1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(patientService).getVisitHistoryFiltered(1L, null, null, 1L);
    }

    // Test getVisitHistoryFiltered - Success Case with All Filters
    @Test
    void testGetVisitHistoryFiltered_SuccessWithAllFilters() throws InfyHospitalException {
        // Given
        LocalDate startDate = LocalDate.now().minusMonths(3);
        LocalDate endDate = LocalDate.now();
        List<AppointmentDTO> appointments = Arrays.asList(testAppointmentDTO);
        when(patientService.getVisitHistoryFiltered(1L, startDate, endDate, 1L)).thenReturn(appointments);

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getVisitHistoryFiltered(1L, startDate, endDate, 1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(patientService).getVisitHistoryFiltered(1L, startDate, endDate, 1L);
    }

    // Test getVisitHistoryFiltered - Success Case with Empty Result
    @Test
    void testGetVisitHistoryFiltered_SuccessWithEmptyResult() throws InfyHospitalException {
        // Given
        when(patientService.getVisitHistoryFiltered(1L, null, null, null))
                .thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getVisitHistoryFiltered(1L, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
        verify(patientService).getVisitHistoryFiltered(1L, null, null, null);
    }

    // Test getVisitHistoryFiltered - Service Exception
    @Test
    void testGetVisitHistoryFiltered_ServiceException() throws InfyHospitalException {
        // Given
        when(patientService.getVisitHistoryFiltered(1L, null, null, null))
                .thenThrow(new InfyHospitalException("Service.NO_HISTORY_FOUND"));

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getVisitHistoryFiltered(1L, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(patientService).getVisitHistoryFiltered(1L, null, null, null);
    }

    // Test getDoctorsByDepartment - Invalid Department Format
    @Test
    void testGetDoctorsByDepartment_InvalidDepartmentFormat() throws InfyHospitalException {
        // Given
        when(patientService.getDoctorsByDepartment(Department.CARDIOLOGY)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<DoctorDTO>> result = patientApi.getDoctorsByDepartment("cardiology"); // lowercase

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(patientService).getDoctorsByDepartment(Department.CARDIOLOGY);
    }

    // Test getDoctorsByDepartment - Edge Case with Whitespace
    @Test
    void testGetDoctorsByDepartment_EdgeCaseWithWhitespace() throws InfyHospitalException {
        // Given
        when(patientService.getDoctorsByDepartment(Department.GENERAL)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<DoctorDTO>> result = patientApi.getDoctorsByDepartment("  GENERAL  "); // with whitespace

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(patientService).getDoctorsByDepartment(Department.GENERAL);
    }

    // Test bookAppointment - Edge Case with Null Reason
    @Test
    void testBookAppointment_EdgeCaseWithNullReason() throws InfyHospitalException {
        // Given
        testAppointmentRequestDTO.setReason(null);
        when(patientService.bookAppointment(1L, 1L, 1L, null)).thenReturn(1L);
        when(environment.getProperty("Api.BOOKING_SUCCESS")).thenReturn("Appointment booked successfully with ID: ");

        // When
        ResponseEntity<String> result = patientApi.bookAppointment(testAppointmentRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(patientService).bookAppointment(1L, 1L, 1L, null);
    }

    // Test bookAppointment - Edge Case with Empty Reason
    @Test
    void testBookAppointment_EdgeCaseWithEmptyReason() throws InfyHospitalException {
        // Given
        testAppointmentRequestDTO.setReason("");
        when(patientService.bookAppointment(1L, 1L, 1L, "")).thenReturn(1L);
        when(environment.getProperty("Api.BOOKING_SUCCESS")).thenReturn("Appointment booked successfully with ID: ");

        // When
        ResponseEntity<String> result = patientApi.bookAppointment(testAppointmentRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(patientService).bookAppointment(1L, 1L, 1L, "");
    }

    // Test getVisitHistoryFiltered - Edge Case with Invalid Date Range
    @Test
    void testGetVisitHistoryFiltered_EdgeCaseWithInvalidDateRange() throws InfyHospitalException {
        // Given
        LocalDate startDate = LocalDate.now().plusDays(1); // Future date
        LocalDate endDate = LocalDate.now(); // Past date
        when(patientService.getVisitHistoryFiltered(1L, startDate, endDate, null))
                .thenThrow(new InfyHospitalException("Service.INVALID_TIME_RANGE"));

        // When
        ResponseEntity<List<AppointmentDTO>> result = patientApi.getVisitHistoryFiltered(1L, startDate, endDate, null);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(patientService).getVisitHistoryFiltered(1L, startDate, endDate, null);
    }
}
