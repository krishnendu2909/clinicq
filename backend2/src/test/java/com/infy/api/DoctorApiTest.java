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
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.infy.dto.TokenDTO;
import com.infy.dto.UserDTO;
import com.infy.dto.DoctorDTO;
import com.infy.exception.InfyHospitalException;
import com.infy.models.Gender;
import com.infy.models.Role;
import com.infy.models.TokenStatus;
import com.infy.service.DoctorService;

@ExtendWith(MockitoExtension.class)
class DoctorApiTest {

    @Mock
    private DoctorService doctorService;

    @Mock
    private Environment environment;

    @InjectMocks
    private DoctorApi doctorApi;

    private TokenDTO testTokenDTO;
    private UserDTO testUserDTO;
    private DoctorDTO testDoctorDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setEmail("doctor@test.com");
        testUserDTO.setRole(Role.DOCTOR);

        testDoctorDTO = new DoctorDTO();
        testDoctorDTO.setId(1L);
        testDoctorDTO.setName("Dr. Smith");
        testDoctorDTO.setPhone("1234567890");
        testDoctorDTO.setGender(Gender.MALE);
        testDoctorDTO.setUser(testUserDTO);

        testTokenDTO = new TokenDTO();
        testTokenDTO.setId(1L);
        testTokenDTO.setDoctor(testDoctorDTO);
        testTokenDTO.setTokenNumber("D001");
        testTokenDTO.setStatus(TokenStatus.WAITING);
    }

    // Test getDailySchedule - Success Case
    @Test
    void testGetDailySchedule_Success() throws InfyHospitalException {
        // Given
        List<TokenDTO> schedule = Arrays.asList(testTokenDTO);
        when(doctorService.getDailySchedule(1L)).thenReturn(schedule);

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getDailySchedule(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("D001", result.getBody().get(0).getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.getBody().get(0).getStatus());
        verify(doctorService).getDailySchedule(1L);
    }

    // Test getDailySchedule - Success Case with Multiple Tokens
    @Test
    void testGetDailySchedule_SuccessWithMultipleTokens() throws InfyHospitalException {
        // Given
        TokenDTO token2 = new TokenDTO();
        token2.setId(2L);
        token2.setDoctor(testDoctorDTO);
        token2.setTokenNumber("D002");
        token2.setStatus(TokenStatus.IN_CONSULTATION);

        TokenDTO token3 = new TokenDTO();
        token3.setId(3L);
        token3.setDoctor(testDoctorDTO);
        token3.setTokenNumber("D003");
        token3.setStatus(TokenStatus.COMPLETED);

        List<TokenDTO> schedule = Arrays.asList(testTokenDTO, token2, token3);
        when(doctorService.getDailySchedule(1L)).thenReturn(schedule);

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getDailySchedule(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(3, result.getBody().size());
        assertEquals("D001", result.getBody().get(0).getTokenNumber());
        assertEquals("D002", result.getBody().get(1).getTokenNumber());
        assertEquals("D003", result.getBody().get(2).getTokenNumber());
        verify(doctorService).getDailySchedule(1L);
    }

    // Test getDailySchedule - Success Case with Empty Schedule
    @Test
    void testGetDailySchedule_SuccessWithEmptySchedule() throws InfyHospitalException {
        // Given
        when(doctorService.getDailySchedule(1L)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getDailySchedule(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
        verify(doctorService).getDailySchedule(1L);
    }

    // Test getDailySchedule - Service Exception
    @Test
    void testGetDailySchedule_ServiceException() throws InfyHospitalException {
        // Given
        when(doctorService.getDailySchedule(1L))
                .thenThrow(new InfyHospitalException("Service.NO_DOCTOR_FOUND"));

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getDailySchedule(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(doctorService).getDailySchedule(1L);
    }

    // Test getCurrentQueue - Success Case
    @Test
    void testGetCurrentQueue_Success() throws InfyHospitalException {
        // Given
        List<TokenDTO> queue = Arrays.asList(testTokenDTO);
        when(doctorService.getCurrentQueue(1L)).thenReturn(queue);

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("D001", result.getBody().get(0).getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.getBody().get(0).getStatus());
        verify(doctorService).getCurrentQueue(1L);
    }

    // Test getCurrentQueue - Success Case with Multiple Tokens
    @Test
    void testGetCurrentQueue_SuccessWithMultipleTokens() throws InfyHospitalException {
        // Given
        TokenDTO token2 = new TokenDTO();
        token2.setId(2L);
        token2.setDoctor(testDoctorDTO);
        token2.setTokenNumber("D002");
        token2.setStatus(TokenStatus.IN_CONSULTATION);

        List<TokenDTO> queue = Arrays.asList(testTokenDTO, token2);
        when(doctorService.getCurrentQueue(1L)).thenReturn(queue);

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        assertEquals("D001", result.getBody().get(0).getTokenNumber());
        assertEquals("D002", result.getBody().get(1).getTokenNumber());
        verify(doctorService).getCurrentQueue(1L);
    }

    // Test getCurrentQueue - Success Case with Empty Queue
    @Test
    void testGetCurrentQueue_SuccessWithEmptyQueue() throws InfyHospitalException {
        // Given
        when(doctorService.getCurrentQueue(1L)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
        verify(doctorService).getCurrentQueue(1L);
    }

    // Test getCurrentQueue - Service Exception
    @Test
    void testGetCurrentQueue_ServiceException() throws InfyHospitalException {
        // Given
        when(doctorService.getCurrentQueue(1L))
                .thenThrow(new InfyHospitalException("Service.NO_DOCTOR_FOUND"));

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(doctorService).getCurrentQueue(1L);
    }

    // Test updateTokenStatus - Success Case to WAITING
    @Test
    void testUpdateTokenStatus_SuccessToWaiting() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(1L, "WAITING");
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(1L, "WAITING");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(1L, "WAITING");
        verify(environment).getProperty("API.TOKEN_UPDATE_SUCCESS");
    }

    // Test updateTokenStatus - Success Case to IN_CONSULTATION
    @Test
    void testUpdateTokenStatus_SuccessToInConsultation() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(1L, "IN_CONSULTATION");
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(1L, "IN_CONSULTATION");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(1L, "IN_CONSULTATION");
        verify(environment).getProperty("API.TOKEN_UPDATE_SUCCESS");
    }

    // Test updateTokenStatus - Success Case to COMPLETED
    @Test
    void testUpdateTokenStatus_SuccessToCompleted() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(1L, "COMPLETED");
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(1L, "COMPLETED");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(1L, "COMPLETED");
        verify(environment).getProperty("API.TOKEN_UPDATE_SUCCESS");
    }

    // Test updateTokenStatus - Success Case to CANCELLED
    @Test
    void testUpdateTokenStatus_SuccessToCancelled() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(1L, "CANCELLED");
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(1L, "CANCELLED");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(1L, "CANCELLED");
        verify(environment).getProperty("API.TOKEN_UPDATE_SUCCESS");
    }

    // Test updateTokenStatus - Service Exception
    @Test
    void testUpdateTokenStatus_ServiceException() throws InfyHospitalException {
        // Given
        doThrow(new InfyHospitalException("Service.NO_TOKEN_FOUND"))
                .when(doctorService).updateTokenStatus(1L, "WAITING");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(1L, "WAITING");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(doctorService).updateTokenStatus(1L, "WAITING");
    }

    // Test updateTokenStatus - Case Insensitive Status
    @Test
    void testUpdateTokenStatus_CaseInsensitiveStatus() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(1L, "waiting"); // lowercase
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(1L, "waiting");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(1L, "waiting");
        verify(environment).getProperty("API.TOKEN_UPDATE_SUCCESS");
    }

    // Test updateTokenStatus - Mixed Case Status
    @Test
    void testUpdateTokenStatus_MixedCaseStatus() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(1L, "In_Consultation"); // mixed case
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(1L, "In_Consultation");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(1L, "In_Consultation");
        verify(environment).getProperty("API.TOKEN_UPDATE_SUCCESS");
    }

    // Test getDailySchedule - Edge Case with Different Doctor ID
    @Test
    void testGetDailySchedule_EdgeCaseWithDifferentDoctorId() throws InfyHospitalException {
        // Given
        List<TokenDTO> schedule = Arrays.asList(testTokenDTO);
        when(doctorService.getDailySchedule(999L)).thenReturn(schedule);

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getDailySchedule(999L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(doctorService).getDailySchedule(999L);
    }

    // Test getCurrentQueue - Edge Case with Different Doctor ID
    @Test
    void testGetCurrentQueue_EdgeCaseWithDifferentDoctorId() throws InfyHospitalException {
        // Given
        List<TokenDTO> queue = Arrays.asList(testTokenDTO);
        when(doctorService.getCurrentQueue(123L)).thenReturn(queue);

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getCurrentQueue(123L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(doctorService).getCurrentQueue(123L);
    }

    // Test updateTokenStatus - Edge Case with Different Token ID
    @Test
    void testUpdateTokenStatus_EdgeCaseWithDifferentTokenId() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(456L, "COMPLETED");
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(456L, "COMPLETED");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(456L, "COMPLETED");
    }

    // Test getDailySchedule - Edge Case with Large Number of Tokens
    @Test
    void testGetDailySchedule_EdgeCaseWithLargeNumberOfTokens() throws InfyHospitalException {
        // Given
        List<TokenDTO> largeSchedule = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            TokenDTO token = new TokenDTO();
            token.setId((long) i);
            token.setDoctor(testDoctorDTO);
            token.setTokenNumber("D" + String.format("%03d", i));
            token.setStatus(TokenStatus.WAITING);
            largeSchedule.add(token);
        }

        when(doctorService.getDailySchedule(1L)).thenReturn(largeSchedule);

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getDailySchedule(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(50, result.getBody().size());
        assertEquals("D001", result.getBody().get(0).getTokenNumber());
        assertEquals("D050", result.getBody().get(49).getTokenNumber());
        verify(doctorService).getDailySchedule(1L);
    }

    // Test getCurrentQueue - Edge Case with Large Number of Tokens
    @Test
    void testGetCurrentQueue_EdgeCaseWithLargeNumberOfTokens() throws InfyHospitalException {
        // Given
        List<TokenDTO> largeQueue = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            TokenDTO token = new TokenDTO();
            token.setId((long) i);
            token.setDoctor(testDoctorDTO);
            token.setTokenNumber("D" + String.format("%03d", i));
            token.setStatus(TokenStatus.WAITING);
            largeQueue.add(token);
        }

        when(doctorService.getCurrentQueue(1L)).thenReturn(largeQueue);

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(25, result.getBody().size());
        assertEquals("D001", result.getBody().get(0).getTokenNumber());
        assertEquals("D025", result.getBody().get(24).getTokenNumber());
        verify(doctorService).getCurrentQueue(1L);
    }

    // Test updateTokenStatus - Edge Case with Empty Status
    @Test
    void testUpdateTokenStatus_EdgeCaseWithEmptyStatus() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(1L, "");
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(1L, "");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(1L, "");
        verify(environment).getProperty("API.TOKEN_UPDATE_SUCCESS");
    }

    // Test updateTokenStatus - Edge Case with Null Status
    @Test
    void testUpdateTokenStatus_EdgeCaseWithNullStatus() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(1L, null);
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(1L, null);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(1L, null);
        verify(environment).getProperty("API.TOKEN_UPDATE_SUCCESS");
    }

    // Test updateTokenStatus - Edge Case with Special Characters in Status
    @Test
    void testUpdateTokenStatus_EdgeCaseWithSpecialCharactersInStatus() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(1L, "WAITING_URGENT");
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(1L, "WAITING_URGENT");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(1L, "WAITING_URGENT");
        verify(environment).getProperty("API.TOKEN_UPDATE_SUCCESS");
    }

    // Test getDailySchedule - Edge Case with Zero Doctor ID
    @Test
    void testGetDailySchedule_EdgeCaseWithZeroDoctorId() throws InfyHospitalException {
        // Given
        when(doctorService.getDailySchedule(0L)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getDailySchedule(0L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
        verify(doctorService).getDailySchedule(0L);
    }

    // Test getCurrentQueue - Edge Case with Zero Doctor ID
    @Test
    void testGetCurrentQueue_EdgeCaseWithZeroDoctorId() throws InfyHospitalException {
        // Given
        when(doctorService.getCurrentQueue(0L)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<List<TokenDTO>> result = doctorApi.getCurrentQueue(0L);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
        verify(doctorService).getCurrentQueue(0L);
    }

    // Test updateTokenStatus - Edge Case with Zero Token ID
    @Test
    void testUpdateTokenStatus_EdgeCaseWithZeroTokenId() throws InfyHospitalException {
        // Given
        doNothing().when(doctorService).updateTokenStatus(0L, "WAITING");
        when(environment.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");

        // When
        ResponseEntity<String> result = doctorApi.updateTokenStatus(0L, "WAITING");

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Token status updated successfully", result.getBody());
        verify(doctorService).updateTokenStatus(0L, "WAITING");
        verify(environment).getProperty("API.TOKEN_UPDATE_SUCCESS");
    }
}
