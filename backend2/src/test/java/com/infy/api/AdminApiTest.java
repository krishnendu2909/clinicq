package com.infy.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
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

import com.infy.exception.InfyHospitalException;
import com.infy.models.Department;
import com.infy.models.Gender;
import com.infy.service.AdminService;

@ExtendWith(MockitoExtension.class)
class AdminApiTest {

    @Mock
    private AdminService adminService;

    @Mock
    private Environment environment;

    @InjectMocks
    private AdminApi adminApi;

    @BeforeEach
    void setUp() {
        // Setup test data will be done in individual test methods
    }

    // Test addDoctor - Success Case
    @Test
    void testAddDoctor_Success() throws InfyHospitalException {
        // Given
        Long doctorId = 1L;
        when(adminService.addDoctor("doctor@test.com", "password", "Dr. Smith", 
                Department.CARDIOLOGY, Gender.MALE, "1234567890", "New York", "Cardiologist"))
                .thenReturn(doctorId);
        when(environment.getProperty("API.ADD_DOCTOR_SUCCESS")).thenReturn("Doctor added successfully with ID: ");

        // When
        ResponseEntity<String> result = adminApi.addDoctor("doctor@test.com", "password", "Dr. Smith", 
                Department.CARDIOLOGY, Gender.MALE, "1234567890", "New York", "Cardiologist");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("Doctor added successfully with ID: 1"));
        verify(adminService).addDoctor("doctor@test.com", "password", "Dr. Smith", 
                Department.CARDIOLOGY, Gender.MALE, "1234567890", "New York", "Cardiologist");
        verify(environment).getProperty("API.ADD_DOCTOR_SUCCESS");
    }

    // Test addDoctor - Success Case with Different Department
    @Test
    void testAddDoctor_SuccessWithDifferentDepartment() throws InfyHospitalException {
        // Given
        Long doctorId = 2L;
        when(adminService.addDoctor("doctor2@test.com", "password", "Dr. Johnson", 
                Department.ORTHOPEDICS, Gender.FEMALE, "0987654321", "Los Angeles", "Orthopedist"))
                .thenReturn(doctorId);
        when(environment.getProperty("API.ADD_DOCTOR_SUCCESS")).thenReturn("Doctor added successfully with ID: ");

        // When
        ResponseEntity<String> result = adminApi.addDoctor("doctor2@test.com", "password", "Dr. Johnson", 
                Department.ORTHOPEDICS, Gender.FEMALE, "0987654321", "Los Angeles", "Orthopedist");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().contains("Doctor added successfully with ID: 2"));
        verify(adminService).addDoctor("doctor2@test.com", "password", "Dr. Johnson", 
                Department.ORTHOPEDICS, Gender.FEMALE, "0987654321", "Los Angeles", "Orthopedist");
    }

    // Test addDoctor - Success Case with All Departments
    @Test
    void testAddDoctor_SuccessWithAllDepartments() throws InfyHospitalException {
        // Given
        Long doctorId = 3L;
        when(adminService.addDoctor("pediatrician@test.com", "password", "Dr. Wilson", 
                Department.PEDIATRICS, Gender.MALE, "5555555555", "Chicago", "Pediatrician"))
                .thenReturn(doctorId);
        when(environment.getProperty("API.ADD_DOCTOR_SUCCESS")).thenReturn("Doctor added successfully with ID: ");

        // When
        ResponseEntity<String> result = adminApi.addDoctor("pediatrician@test.com", "password", "Dr. Wilson", 
                Department.PEDIATRICS, Gender.MALE, "5555555555", "Chicago", "Pediatrician");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().contains("Doctor added successfully with ID: 3"));
        verify(adminService).addDoctor("pediatrician@test.com", "password", "Dr. Wilson", 
                Department.PEDIATRICS, Gender.MALE, "5555555555", "Chicago", "Pediatrician");
    }

    // Test addDoctor - Success Case with General Department
    @Test
    void testAddDoctor_SuccessWithGeneralDepartment() throws InfyHospitalException {
        // Given
        Long doctorId = 4L;
        when(adminService.addDoctor("general@test.com", "password", "Dr. Brown", 
                Department.GENERAL, Gender.FEMALE, "7777777777", "Boston", "General Practitioner"))
                .thenReturn(doctorId);
        when(environment.getProperty("API.ADD_DOCTOR_SUCCESS")).thenReturn("Doctor added successfully with ID: ");

        // When
        ResponseEntity<String> result = adminApi.addDoctor("general@test.com", "password", "Dr. Brown", 
                Department.GENERAL, Gender.FEMALE, "7777777777", "Boston", "General Practitioner");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().contains("Doctor added successfully with ID: 4"));
        verify(adminService).addDoctor("general@test.com", "password", "Dr. Brown", 
                Department.GENERAL, Gender.FEMALE, "7777777777", "Boston", "General Practitioner");
    }

    // Test deleteDoctor - Success Case
    @Test
    void testDeleteDoctor_Success() throws InfyHospitalException {
        // Given
        Long doctorId = 1L;
        doNothing().when(adminService).deleteDoctor(doctorId);
        when(environment.getProperty("API.DELETE_SUCCESS")).thenReturn("Doctor deleted successfully");

        // When
        ResponseEntity<String> result = adminApi.deleteDoctor(doctorId);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Doctor deleted successfully", result.getBody());
        verify(adminService).deleteDoctor(doctorId);
        verify(environment).getProperty("API.DELETE_SUCCESS");
    }

    // Test deleteDoctor - Service Exception
    @Test
    void testDeleteDoctor_ServiceException() throws InfyHospitalException {
        // Given
        Long doctorId = 999L;
        doThrow(new InfyHospitalException("Service.NO_DOCTOR_FOUND")).when(adminService).deleteDoctor(doctorId);

        // When
        ResponseEntity<String> result = adminApi.deleteDoctor(doctorId);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(adminService).deleteDoctor(doctorId);
    }

    // Test configureDoctorSchedule - Success Case
    @Test
    void testConfigureDoctorSchedule_Success() throws InfyHospitalException {
        // Given
        Long doctorId = 1L;
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        doNothing().when(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(9, 0), LocalTime.of(17, 0), 30);
        when(environment.getProperty("API.ADD_SUCCESS")).thenReturn("Schedule configured successfully");

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, days, 
                "09:00", "17:00", 30);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Schedule configured successfully", result.getBody());
        verify(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(9, 0), LocalTime.of(17, 0), 30);
        verify(environment).getProperty("API.ADD_SUCCESS");
    }

    // Test configureDoctorSchedule - Success Case with Single Day
    @Test
    void testConfigureDoctorSchedule_SuccessWithSingleDay() throws InfyHospitalException {
        // Given
        Long doctorId = 2L;
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.TUESDAY);
        doNothing().when(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(10, 0), LocalTime.of(16, 0), 45);
        when(environment.getProperty("API.ADD_SUCCESS")).thenReturn("Schedule configured successfully");

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, days, 
                "10:00", "16:00", 45);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Schedule configured successfully", result.getBody());
        verify(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(10, 0), LocalTime.of(16, 0), 45);
    }

    // Test configureDoctorSchedule - Success Case with All Weekdays
    @Test
    void testConfigureDoctorSchedule_SuccessWithAllWeekdays() throws InfyHospitalException {
        // Given
        Long doctorId = 3L;
        List<DayOfWeek> days = Arrays.asList(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, 
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        );
        doNothing().when(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(8, 0), LocalTime.of(20, 0), 15);
        when(environment.getProperty("API.ADD_SUCCESS")).thenReturn("Schedule configured successfully");

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, days, 
                "08:00", "20:00", 15);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Schedule configured successfully", result.getBody());
        verify(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(8, 0), LocalTime.of(20, 0), 15);
    }

    // Test configureDoctorSchedule - Success Case with Weekend
    @Test
    void testConfigureDoctorSchedule_SuccessWithWeekend() throws InfyHospitalException {
        // Given
        Long doctorId = 4L;
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        doNothing().when(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(9, 30), LocalTime.of(13, 30), 60);
        when(environment.getProperty("API.ADD_SUCCESS")).thenReturn("Schedule configured successfully");

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, days, 
                "09:30", "13:30", 60);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Schedule configured successfully", result.getBody());
        verify(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(9, 30), LocalTime.of(13, 30), 60);
    }

    // Test configureDoctorSchedule - Service Exception
    @Test
    void testConfigureDoctorSchedule_ServiceException() throws InfyHospitalException {
        // Given
        Long doctorId = 999L;
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.MONDAY);
        doThrow(new InfyHospitalException("Service.NO_DOCTOR_FOUND"))
                .when(adminService).configureDoctorSchedule(doctorId, days, 
                        LocalTime.of(9, 0), LocalTime.of(17, 0), 30);

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, days, 
                "09:00", "17:00", 30);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(9, 0), LocalTime.of(17, 0), 30);
    }

    // Test configureDoctorSchedule - Empty Days List
    @Test
    void testConfigureDoctorSchedule_EmptyDaysList() throws InfyHospitalException {
        // Given
        Long doctorId = 1L;
        List<DayOfWeek> emptyDays = Collections.emptyList();
        doNothing().when(adminService).configureDoctorSchedule(doctorId, emptyDays, 
                LocalTime.of(9, 0), LocalTime.of(17, 0), 30);
        when(environment.getProperty("API.ADD_SUCCESS")).thenReturn("Schedule configured successfully");

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, emptyDays, 
                "09:00", "17:00", 30);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Schedule configured successfully", result.getBody());
        verify(adminService).configureDoctorSchedule(doctorId, emptyDays, 
                LocalTime.of(9, 0), LocalTime.of(17, 0), 30);
    }

    // Test updateBookingRules - Success Case
    @Test
    void testUpdateBookingRules_Success() {
        // Given
        doNothing().when(adminService).updateBookingRules(5, 30, 24);
        when(environment.getProperty("API.UPDATE_SUCCESS")).thenReturn("Booking rules updated successfully");

        // When
        ResponseEntity<String> result = adminApi.updateBookingRules(5, 30, 24);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Booking rules updated successfully", result.getBody());
        verify(adminService).updateBookingRules(5, 30, 24);
        verify(environment).getProperty("API.UPDATE_SUCCESS");
    }

    // Test updateBookingRules - Success Case with Minimum Values
    @Test
    void testUpdateBookingRules_SuccessWithMinimumValues() {
        // Given
        doNothing().when(adminService).updateBookingRules(1, 1, 0);
        when(environment.getProperty("API.UPDATE_SUCCESS")).thenReturn("Booking rules updated successfully");

        // When
        ResponseEntity<String> result = adminApi.updateBookingRules(1, 1, 0);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Booking rules updated successfully", result.getBody());
        verify(adminService).updateBookingRules(1, 1, 0);
    }

    // Test updateBookingRules - Success Case with Large Values
    @Test
    void testUpdateBookingRules_SuccessWithLargeValues() {
        // Given
        doNothing().when(adminService).updateBookingRules(100, 365, 168);
        when(environment.getProperty("API.UPDATE_SUCCESS")).thenReturn("Booking rules updated successfully");

        // When
        ResponseEntity<String> result = adminApi.updateBookingRules(100, 365, 168);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Booking rules updated successfully", result.getBody());
        verify(adminService).updateBookingRules(100, 365, 168);
    }

    // Test addDoctor - Edge Case with Long Name
    @Test
    void testAddDoctor_EdgeCaseWithLongName() throws InfyHospitalException {
        // Given
        String longName = "Dr. " + "A".repeat(100); // Very long name
        Long doctorId = 5L;
        when(adminService.addDoctor("longname@test.com", "password", longName, 
                Department.CARDIOLOGY, Gender.MALE, "1234567890", "New York", "Cardiologist"))
                .thenReturn(doctorId);
        when(environment.getProperty("API.ADD_DOCTOR_SUCCESS")).thenReturn("Doctor added successfully with ID: ");

        // When
        ResponseEntity<String> result = adminApi.addDoctor("longname@test.com", "password", longName, 
                Department.CARDIOLOGY, Gender.MALE, "1234567890", "New York", "Cardiologist");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().contains("Doctor added successfully with ID: 5"));
        verify(adminService).addDoctor("longname@test.com", "password", longName, 
                Department.CARDIOLOGY, Gender.MALE, "1234567890", "New York", "Cardiologist");
    }

    // Test addDoctor - Edge Case with Empty Description
    @Test
    void testAddDoctor_EdgeCaseWithEmptyDescription() throws InfyHospitalException {
        // Given
        Long doctorId = 6L;
        when(adminService.addDoctor("emptydesc@test.com", "password", "Dr. Empty", 
                Department.GENERAL, Gender.FEMALE, "1234567890", "New York", ""))
                .thenReturn(doctorId);
        when(environment.getProperty("API.ADD_DOCTOR_SUCCESS")).thenReturn("Doctor added successfully with ID: ");

        // When
        ResponseEntity<String> result = adminApi.addDoctor("emptydesc@test.com", "password", "Dr. Empty", 
                Department.GENERAL, Gender.FEMALE, "1234567890", "New York", "");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().contains("Doctor added successfully with ID: 6"));
        verify(adminService).addDoctor("emptydesc@test.com", "password", "Dr. Empty", 
                Department.GENERAL, Gender.FEMALE, "1234567890", "New York", "");
    }

    // Test addDoctor - Edge Case with Special Characters in Location
    @Test
    void testAddDoctor_EdgeCaseWithSpecialCharactersInLocation() throws InfyHospitalException {
        // Given
        Long doctorId = 7L;
        String specialLocation = "New York, NY 10001 - Suite #500";
        when(adminService.addDoctor("special@test.com", "password", "Dr. Special", 
                Department.ORTHOPEDICS, Gender.MALE, "1234567890", specialLocation, "Specialist"))
                .thenReturn(doctorId);
        when(environment.getProperty("API.ADD_DOCTOR_SUCCESS")).thenReturn("Doctor added successfully with ID: ");

        // When
        ResponseEntity<String> result = adminApi.addDoctor("special@test.com", "password", "Dr. Special", 
                Department.ORTHOPEDICS, Gender.MALE, "1234567890", specialLocation, "Specialist");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().contains("Doctor added successfully with ID: 7"));
        verify(adminService).addDoctor("special@test.com", "password", "Dr. Special", 
                Department.ORTHOPEDICS, Gender.MALE, "1234567890", specialLocation, "Specialist");
    }

    // Test configureDoctorSchedule - Edge Case with Early Morning Hours
    @Test
    void testConfigureDoctorSchedule_EdgeCaseWithEarlyMorningHours() throws InfyHospitalException {
        // Given
        Long doctorId = 8L;
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.MONDAY);
        doNothing().when(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(6, 0), LocalTime.of(14, 0), 20);
        when(environment.getProperty("API.ADD_SUCCESS")).thenReturn("Schedule configured successfully");

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, days, 
                "06:00", "14:00", 20);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Schedule configured successfully", result.getBody());
        verify(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(6, 0), LocalTime.of(14, 0), 20);
    }

    // Test configureDoctorSchedule - Edge Case with Late Evening Hours
    @Test
    void testConfigureDoctorSchedule_EdgeCaseWithLateEveningHours() throws InfyHospitalException {
        // Given
        Long doctorId = 9L;
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.TUESDAY);
        doNothing().when(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(14, 0), LocalTime.of(22, 0), 25);
        when(environment.getProperty("API.ADD_SUCCESS")).thenReturn("Schedule configured successfully");

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, days, 
                "14:00", "22:00", 25);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Schedule configured successfully", result.getBody());
        verify(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(14, 0), LocalTime.of(22, 0), 25);
    }

    // Test configureDoctorSchedule - Edge Case with Midnight Hours
    @Test
    void testConfigureDoctorSchedule_EdgeCaseWithMidnightHours() throws InfyHospitalException {
        // Given
        Long doctorId = 10L;
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.WEDNESDAY);
        doNothing().when(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(0, 0), LocalTime.of(23, 59), 30);
        when(environment.getProperty("API.ADD_SUCCESS")).thenReturn("Schedule configured successfully");

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, days, 
                "00:00", "23:59", 30);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Schedule configured successfully", result.getBody());
        verify(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(0, 0), LocalTime.of(23, 59), 30);
    }

    // Test configureDoctorSchedule - Edge Case with Minimum Slot Duration
    @Test
    void testConfigureDoctorSchedule_EdgeCaseWithMinimumSlotDuration() throws InfyHospitalException {
        // Given
        Long doctorId = 11L;
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.THURSDAY);
        doNothing().when(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(9, 0), LocalTime.of(10, 0), 1);
        when(environment.getProperty("API.ADD_SUCCESS")).thenReturn("Schedule configured successfully");

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, days, 
                "09:00", "10:00", 1);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Schedule configured successfully", result.getBody());
        verify(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(9, 0), LocalTime.of(10, 0), 1);
    }

    // Test configureDoctorSchedule - Edge Case with Large Slot Duration
    @Test
    void testConfigureDoctorSchedule_EdgeCaseWithLargeSlotDuration() throws InfyHospitalException {
        // Given
        Long doctorId = 12L;
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.FRIDAY);
        doNothing().when(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(9, 0), LocalTime.of(17, 0), 240); // 4 hours
        when(environment.getProperty("API.ADD_SUCCESS")).thenReturn("Schedule configured successfully");

        // When
        ResponseEntity<String> result = adminApi.configureDoctorSchedule(doctorId, days, 
                "09:00", "17:00", 240);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Schedule configured successfully", result.getBody());
        verify(adminService).configureDoctorSchedule(doctorId, days, 
                LocalTime.of(9, 0), LocalTime.of(17, 0), 240);
    }
}
