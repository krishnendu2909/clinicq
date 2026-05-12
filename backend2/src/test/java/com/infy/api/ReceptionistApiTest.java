package com.infy.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.infy.dto.PatientDTO;
import com.infy.dto.TokenDTO;
import com.infy.dto.UserDTO;
import com.infy.exception.InfyHospitalException;
import com.infy.models.Gender;
import com.infy.models.Role;
import com.infy.models.TokenStatus;
import com.infy.service.ReceptionistService;

@ExtendWith(MockitoExtension.class)
class ReceptionistApiTest {

    @Mock
    private ReceptionistService receptionistService;

    @InjectMocks
    private ReceptionistApi receptionistApi;

    private PatientDTO testPatientDTO;
    private TokenDTO testTokenDTO;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setEmail("patient@test.com");
        testUserDTO.setPassword("password");
        testUserDTO.setRole(Role.PATIENT);

        testPatientDTO = new PatientDTO();
        testPatientDTO.setId(1L);
        testPatientDTO.setName("John Doe");
        testPatientDTO.setPhone("9876543210");
        testPatientDTO.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));
        testPatientDTO.setGender(Gender.MALE);
        testPatientDTO.setUser(testUserDTO);

        testTokenDTO = new TokenDTO();
        testTokenDTO.setId(1L);
        testTokenDTO.setTokenNumber("D001");
        testTokenDTO.setStatus(TokenStatus.WAITING);
    }

    // Test registerWalkIn - Success Case
    @Test
    void testRegisterWalkIn_Success() throws InfyHospitalException {
        // Given
        when(receptionistService.registerWalkIn(testPatientDTO, 1L)).thenReturn(testTokenDTO);

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.registerWalkIn(testPatientDTO, 1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("D001", result.getBody().getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.getBody().getStatus());
        verify(receptionistService).registerWalkIn(testPatientDTO, 1L);
    }

    // Test registerWalkIn - Success Case with Different Doctor
    @Test
    void testRegisterWalkIn_SuccessWithDifferentDoctor() throws InfyHospitalException {
        // Given
        when(receptionistService.registerWalkIn(testPatientDTO, 2L)).thenReturn(testTokenDTO);

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.registerWalkIn(testPatientDTO, 2L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("D001", result.getBody().getTokenNumber());
        verify(receptionistService).registerWalkIn(testPatientDTO, 2L);
    }

    // Test registerWalkIn - Service Exception
    @Test
    void testRegisterWalkIn_ServiceException() throws InfyHospitalException {
        // Given
        when(receptionistService.registerWalkIn(testPatientDTO, 1L))
                .thenThrow(new InfyHospitalException("Service.NO_DOCTOR_FOUND"));

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.registerWalkIn(testPatientDTO, 1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(receptionistService).registerWalkIn(testPatientDTO, 1L);
    }

    // Test checkIn - Success Case
    @Test
    void testCheckIn_Success() throws InfyHospitalException {
        // Given
        when(receptionistService.checkIn(1L)).thenReturn(testTokenDTO);

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.checkIn(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("D001", result.getBody().getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.getBody().getStatus());
        verify(receptionistService).checkIn(1L);
    }

    // Test checkIn - Success Case with Different Appointment
    @Test
    void testCheckIn_SuccessWithDifferentAppointment() throws InfyHospitalException {
        // Given
        when(receptionistService.checkIn(2L)).thenReturn(testTokenDTO);

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.checkIn(2L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("D001", result.getBody().getTokenNumber());
        verify(receptionistService).checkIn(2L);
    }

    // Test checkIn - Service Exception
    @Test
    void testCheckIn_ServiceException() throws InfyHospitalException {
        // Given
        when(receptionistService.checkIn(1L))
                .thenThrow(new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND"));

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.checkIn(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(receptionistService).checkIn(1L);
    }

    // Test searchPatient - Success Case
    @Test
    void testSearchPatient_Success() throws InfyHospitalException {
        // Given
        List<PatientDTO> patients = Arrays.asList(testPatientDTO);
        when(receptionistService.searchPatient("John")).thenReturn(patients);

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("John");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("John Doe", result.getBody().get(0).getName());
        assertEquals("9876543210", result.getBody().get(0).getPhone());
        verify(receptionistService).searchPatient("John");
    }

    // Test searchPatient - Success Case with Phone Search
    @Test
    void testSearchPatient_SuccessWithPhoneSearch() throws InfyHospitalException {
        // Given
        List<PatientDTO> patients = Arrays.asList(testPatientDTO);
        when(receptionistService.searchPatient("9876543210")).thenReturn(patients);

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("9876543210");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals("John Doe", result.getBody().get(0).getName());
        verify(receptionistService).searchPatient("9876543210");
    }

    // Test searchPatient - Success Case with Partial Name
    @Test
    void testSearchPatient_SuccessWithPartialName() throws InfyHospitalException {
        // Given
        List<PatientDTO> patients = Arrays.asList(testPatientDTO);
        when(receptionistService.searchPatient("Joh")).thenReturn(patients);

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("Joh");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals("John Doe", result.getBody().get(0).getName());
        verify(receptionistService).searchPatient("Joh");
    }

    // Test searchPatient - Success Case with Multiple Patients
    @Test
    void testSearchPatient_SuccessWithMultiplePatients() throws InfyHospitalException {
        // Given
        PatientDTO patient2 = new PatientDTO();
        patient2.setId(2L);
        patient2.setName("John Smith");
        patient2.setPhone("1234567890");
        patient2.setDateOfBirth(java.time.LocalDate.of(1985, 5, 15));
        patient2.setGender(Gender.MALE);

        List<PatientDTO> patients = Arrays.asList(testPatientDTO, patient2);
        when(receptionistService.searchPatient("John")).thenReturn(patients);

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("John");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        assertTrue(result.getBody().stream().anyMatch(p -> p.getName().equals("John Doe")));
        assertTrue(result.getBody().stream().anyMatch(p -> p.getName().equals("John Smith")));
        verify(receptionistService).searchPatient("John");
    }

    // Test searchPatient - Success Case with Empty Result
    @Test
    void testSearchPatient_SuccessWithEmptyResult() throws InfyHospitalException {
        // Given
        when(receptionistService.searchPatient("Unknown")).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("Unknown");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(receptionistService).searchPatient("Unknown");
    }

    // Test searchPatient - Success Case with Null Result
    @Test
    void testSearchPatient_SuccessWithNullResult() throws InfyHospitalException {
        // Given
        when(receptionistService.searchPatient("Null")).thenReturn(null);

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("Null");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(receptionistService).searchPatient("Null");
    }

    // Test searchPatient - Service Exception
    @Test
    void testSearchPatient_ServiceException() throws InfyHospitalException {
        // Given
        when(receptionistService.searchPatient("Error"))
                .thenThrow(new InfyHospitalException("Database error"));

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("Error");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(receptionistService).searchPatient("Error");
    }

    // Test getQueue - Success Case
    @Test
    void testGetQueue_Success() throws InfyHospitalException {
        // Given
        List<TokenDTO> queue = Arrays.asList(testTokenDTO);
        when(receptionistService.getQueue(1L)).thenReturn(queue);

        // When
        ResponseEntity<List<TokenDTO>> result = receptionistApi.getQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("D001", result.getBody().get(0).getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.getBody().get(0).getStatus());
        verify(receptionistService).getQueue(1L);
    }

    // Test getQueue - Success Case with Multiple Tokens
    @Test
    void testGetQueue_SuccessWithMultipleTokens() throws InfyHospitalException {
        // Given
        TokenDTO token2 = new TokenDTO();
        token2.setId(2L);
        token2.setTokenNumber("D002");
        token2.setStatus(TokenStatus.WAITING);

        List<TokenDTO> queue = Arrays.asList(testTokenDTO, token2);
        when(receptionistService.getQueue(1L)).thenReturn(queue);

        // When
        ResponseEntity<List<TokenDTO>> result = receptionistApi.getQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        assertEquals("D001", result.getBody().get(0).getTokenNumber());
        assertEquals("D002", result.getBody().get(1).getTokenNumber());
        verify(receptionistService).getQueue(1L);
    }

    // Test getQueue - Success Case with Different Doctor
    @Test
    void testGetQueue_SuccessWithDifferentDoctor() throws InfyHospitalException {
        // Given
        List<TokenDTO> queue = Arrays.asList(testTokenDTO);
        when(receptionistService.getQueue(2L)).thenReturn(queue);

        // When
        ResponseEntity<List<TokenDTO>> result = receptionistApi.getQueue(2L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(receptionistService).getQueue(2L);
    }

    // Test getQueue - Success Case with Empty Result
    @Test
    void testGetQueue_SuccessWithEmptyResult() throws InfyHospitalException {
        // Given
        when(receptionistService.getQueue(1L)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<TokenDTO>> result = receptionistApi.getQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(receptionistService).getQueue(1L);
    }

    // Test getQueue - Success Case with Null Result
    @Test
    void testGetQueue_SuccessWithNullResult() throws InfyHospitalException {
        // Given
        when(receptionistService.getQueue(1L)).thenReturn(null);

        // When
        ResponseEntity<List<TokenDTO>> result = receptionistApi.getQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(receptionistService).getQueue(1L);
    }

    // Test getQueue - Service Exception
    @Test
    void testGetQueue_ServiceException() throws InfyHospitalException {
        // Given
        when(receptionistService.getQueue(1L))
                .thenThrow(new InfyHospitalException("No queue for this doctor today"));

        // When
        ResponseEntity<List<TokenDTO>> result = receptionistApi.getQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(receptionistService).getQueue(1L);
    }

    // Test registerWalkIn - Edge Case with Minimum Doctor ID
    @Test
    void testRegisterWalkIn_EdgeCaseWithMinimumDoctorId() throws InfyHospitalException {
        // Given
        when(receptionistService.registerWalkIn(testPatientDTO, 1L)).thenReturn(testTokenDTO);

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.registerWalkIn(testPatientDTO, 1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(receptionistService).registerWalkIn(testPatientDTO, 1L);
    }

    // Test registerWalkIn - Edge Case with Maximum Doctor ID
    @Test
    void testRegisterWalkIn_EdgeCaseWithMaximumDoctorId() throws InfyHospitalException {
        // Given
        when(receptionistService.registerWalkIn(testPatientDTO, 50L)).thenReturn(testTokenDTO);

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.registerWalkIn(testPatientDTO, 50L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(receptionistService).registerWalkIn(testPatientDTO, 50L);
    }

    // Test checkIn - Edge Case with Minimum Appointment ID
    @Test
    void testCheckIn_EdgeCaseWithMinimumAppointmentId() throws InfyHospitalException {
        // Given
        when(receptionistService.checkIn(1L)).thenReturn(testTokenDTO);

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.checkIn(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(receptionistService).checkIn(1L);
    }

    // Test getQueue - Edge Case with Minimum Doctor ID
    @Test
    void testGetQueue_EdgeCaseWithMinimumDoctorId() throws InfyHospitalException {
        // Given
        List<TokenDTO> queue = Arrays.asList(testTokenDTO);
        when(receptionistService.getQueue(1L)).thenReturn(queue);

        // When
        ResponseEntity<List<TokenDTO>> result = receptionistApi.getQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(receptionistService).getQueue(1L);
    }

    // Test getQueue - Edge Case with Maximum Doctor ID
    @Test
    void testGetQueue_EdgeCaseWithMaximumDoctorId() throws InfyHospitalException {
        // Given
        List<TokenDTO> queue = Arrays.asList(testTokenDTO);
        when(receptionistService.getQueue(50L)).thenReturn(queue);

        // When
        ResponseEntity<List<TokenDTO>> result = receptionistApi.getQueue(50L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(receptionistService).getQueue(50L);
    }

    // Test searchPatient - Edge Case with Empty Keyword
    @Test
    void testSearchPatient_EdgeCaseWithEmptyKeyword() throws InfyHospitalException {
        // Given
        when(receptionistService.searchPatient("")).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(receptionistService).searchPatient("");
    }

    // Test searchPatient - Edge Case with Single Character
    @Test
    void testSearchPatient_EdgeCaseWithSingleCharacter() throws InfyHospitalException {
        // Given
        List<PatientDTO> patients = Arrays.asList(testPatientDTO);
        when(receptionistService.searchPatient("J")).thenReturn(patients);

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("J");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(receptionistService).searchPatient("J");
    }

    // Test searchPatient - Edge Case with Special Characters
    @Test
    void testSearchPatient_EdgeCaseWithSpecialCharacters() throws InfyHospitalException {
        // Given
        PatientDTO specialPatient = new PatientDTO();
        specialPatient.setId(3L);
        specialPatient.setName("John-Doe");
        specialPatient.setPhone("123-456-7890");
        specialPatient.setDateOfBirth(java.time.LocalDate.of(1992, 3, 15));
        specialPatient.setGender(Gender.MALE);

        List<PatientDTO> patients = Arrays.asList(specialPatient);
        when(receptionistService.searchPatient("John-Doe")).thenReturn(patients);

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("John-Doe");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals("John-Doe", result.getBody().get(0).getName());
        verify(receptionistService).searchPatient("John-Doe");
    }

    // Test searchPatient - Edge Case with Numbers Only
    @Test
    void testSearchPatient_EdgeCaseWithNumbersOnly() throws InfyHospitalException {
        // Given
        List<PatientDTO> patients = Arrays.asList(testPatientDTO);
        when(receptionistService.searchPatient("9876543210")).thenReturn(patients);

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("9876543210");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(receptionistService).searchPatient("9876543210");
    }

    // Test searchPatient - Edge Case with Case Insensitive Search
    @Test
    void testSearchPatient_EdgeCaseWithCaseInsensitiveSearch() throws InfyHospitalException {
        // Given
        List<PatientDTO> patients = Arrays.asList(testPatientDTO);
        when(receptionistService.searchPatient("john")).thenReturn(patients);

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient("john");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals("John Doe", result.getBody().get(0).getName());
        verify(receptionistService).searchPatient("john");
    }

    // Test registerWalkIn - Edge Case with Large Doctor ID
    @Test
    void testRegisterWalkIn_EdgeCaseWithLargeDoctorId() throws InfyHospitalException {
        // Given
        when(receptionistService.registerWalkIn(testPatientDTO, 100L)).thenReturn(testTokenDTO);

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.registerWalkIn(testPatientDTO, 100L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(receptionistService).registerWalkIn(testPatientDTO, 100L);
    }

    // Test checkIn - Edge Case with Large Appointment ID
    @Test
    void testCheckIn_EdgeCaseWithLargeAppointmentId() throws InfyHospitalException {
        // Given
        when(receptionistService.checkIn(1000L)).thenReturn(testTokenDTO);

        // When
        ResponseEntity<TokenDTO> result = receptionistApi.checkIn(1000L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(receptionistService).checkIn(1000L);
    }

    // Test getQueue - Edge Case with Large Doctor ID
    @Test
    void testGetQueue_EdgeCaseWithLargeDoctorId() throws InfyHospitalException {
        // Given
        List<TokenDTO> queue = Arrays.asList(testTokenDTO);
        when(receptionistService.getQueue(100L)).thenReturn(queue);

        // When
        ResponseEntity<List<TokenDTO>> result = receptionistApi.getQueue(100L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(receptionistService).getQueue(100L);
    }

    // Test searchPatient - Edge Case with Very Long Keyword
    @Test
    void testSearchPatient_EdgeCaseWithVeryLongKeyword() throws InfyHospitalException {
        // Given
        String longKeyword = "A".repeat(100); // Very long keyword
        when(receptionistService.searchPatient(longKeyword)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<PatientDTO>> result = receptionistApi.searchPatient(longKeyword);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(receptionistService).searchPatient(longKeyword);
    }
}
