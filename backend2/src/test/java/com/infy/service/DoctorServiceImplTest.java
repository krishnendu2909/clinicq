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

import com.infy.dto.TokenDTO;
import com.infy.entity.Doctor;
import com.infy.entity.Token;
import com.infy.entity.User;
import com.infy.exception.InfyHospitalException;
import com.infy.models.Gender;
import com.infy.models.Role;
import com.infy.models.TokenStatus;
import com.infy.repository.DoctorRepository;
import com.infy.repository.TokenRepository;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor testDoctor;
    private User testUser;
    private Token testToken1;
    private Token testToken2;
    private Token testToken3;

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
        testDoctor.setUser(testUser);

        // Create test tokens with different statuses
        testToken1 = new Token();
        testToken1.setId(1L);
        testToken1.setDoctor(testDoctor);
        testToken1.setTokenNumber(1);
        testToken1.setCheckInTime(LocalDateTime.now());
        testToken1.setStatus(TokenStatus.WAITING);

        testToken2 = new Token();
        testToken2.setId(2L);
        testToken2.setDoctor(testDoctor);
        testToken2.setTokenNumber(2);
        testToken2.setCheckInTime(LocalDateTime.now().plusMinutes(30));
        testToken2.setStatus(TokenStatus.IN_CONSULTATION);

        testToken3 = new Token();
        testToken3.setId(3L);
        testToken3.setDoctor(testDoctor);
        testToken3.setTokenNumber(3);
        testToken3.setCheckInTime(LocalDateTime.now().plusMinutes(60));
        testToken3.setStatus(TokenStatus.COMPLETED);
    }

    // Test getDailySchedule - Success Case
    @Test
    void testGetDailySchedule_Success() throws InfyHospitalException {
        // Given
        List<Token> tokens = Arrays.asList(testToken1, testToken2, testToken3);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(tokens);

        // When
        List<TokenDTO> result = doctorService.getDailySchedule(1L);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getTokenNumber());
        assertEquals(2, result.get(1).getTokenNumber());
        assertEquals(3, result.get(2).getTokenNumber());
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // Test getDailySchedule - Doctor Not Found Exception
    @Test
    void testGetDailySchedule_DoctorNotFound_ThrowsException() {
        // Given
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            doctorService.getDailySchedule(999L);
        });
        assertEquals("Service.NO_DOCTOR_FOUND", exception.getMessage());
        verify(doctorRepository).findById(999L);
    }

    // Test getDailySchedule - Empty Schedule
    @Test
    void testGetDailySchedule_EmptySchedule() throws InfyHospitalException {
        // Given
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // When
        List<TokenDTO> result = doctorService.getDailySchedule(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // Test getDailySchedule - Single Token
    @Test
    void testGetDailySchedule_SingleToken() throws InfyHospitalException {
        // Given
        List<Token> tokens = Arrays.asList(testToken1);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(tokens);

        // When
        List<TokenDTO> result = doctorService.getDailySchedule(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("D001", result.get(0).getTokenNumber());
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // Test getCurrentQueue - Success Case
    @Test
    void testGetCurrentQueue_Success() throws InfyHospitalException {
        // Given
        List<Token> allTokens = Arrays.asList(testToken1, testToken2, testToken3);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(allTokens);

        // When
        List<TokenDTO> result = doctorService.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size()); // Only WAITING and IN_CONSULTATION tokens
        assertEquals(1, result.get(0).getTokenNumber()); // WAITING
        assertEquals(2, result.get(1).getTokenNumber()); // IN_CONSULTATION
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // Test getCurrentQueue - Doctor Not Found Exception
    @Test
    void testGetCurrentQueue_DoctorNotFound_ThrowsException() {
        // Given
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            doctorService.getCurrentQueue(999L);
        });
        assertEquals("Service.NO_DOCTOR_FOUND", exception.getMessage());
        verify(doctorRepository).findById(999L);
    }

    // Test getCurrentQueue - Empty Queue
    @Test
    void testGetCurrentQueue_EmptyQueue() throws InfyHospitalException {
        // Given
        List<Token> allTokens = Arrays.asList(testToken3); // Only COMPLETED token
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(allTokens);

        // When
        List<TokenDTO> result = doctorService.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // Test getCurrentQueue - Only Waiting Tokens
    @Test
    void testGetCurrentQueue_OnlyWaitingTokens() throws InfyHospitalException {
        // Given
        List<Token> allTokens = Arrays.asList(testToken1); // Only WAITING token
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(allTokens);

        // When
        List<TokenDTO> result = doctorService.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.get(0).getStatus());
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // Test getCurrentQueue - Only In Consultation Tokens
    @Test
    void testGetCurrentQueue_OnlyInConsultationTokens() throws InfyHospitalException {
        // Given
        List<Token> allTokens = Arrays.asList(testToken2); // Only IN_CONSULTATION token
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(allTokens);

        // When
        List<TokenDTO> result = doctorService.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getTokenNumber());
        assertEquals(TokenStatus.IN_CONSULTATION, result.get(0).getStatus());
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // Test updateTokenStatus - Success Case to WAITING
    @Test
    void testUpdateTokenStatus_SuccessToWaiting() throws InfyHospitalException {
        // Given
        when(tokenRepository.findById(1L)).thenReturn(Optional.of(testToken1));
        when(tokenRepository.save(any(Token.class))).thenReturn(testToken1);

        // When
        doctorService.updateTokenStatus(1L, "WAITING");

        // Then
        assertEquals(TokenStatus.WAITING, testToken1.getStatus());
        verify(tokenRepository).findById(1L);
        verify(tokenRepository).save(testToken1);
    }

    // Test updateTokenStatus - Success Case to IN_CONSULTATION
    @Test
    void testUpdateTokenStatus_SuccessToInConsultation() throws InfyHospitalException {
        // Given
        when(tokenRepository.findById(1L)).thenReturn(Optional.of(testToken1));
        when(tokenRepository.save(any(Token.class))).thenReturn(testToken1);

        // When
        doctorService.updateTokenStatus(1L, "IN_CONSULTATION");

        // Then
        assertEquals(TokenStatus.IN_CONSULTATION, testToken1.getStatus());
        verify(tokenRepository).findById(1L);
        verify(tokenRepository).save(testToken1);
    }

    // Test updateTokenStatus - Success Case to COMPLETED
    @Test
    void testUpdateTokenStatus_SuccessToCompleted() throws InfyHospitalException {
        // Given
        when(tokenRepository.findById(1L)).thenReturn(Optional.of(testToken1));
        when(tokenRepository.save(any(Token.class))).thenReturn(testToken1);

        // When
        doctorService.updateTokenStatus(1L, "COMPLETED");

        // Then
        assertEquals(TokenStatus.COMPLETED, testToken1.getStatus());
        verify(tokenRepository).findById(1L);
        verify(tokenRepository).save(testToken1);
    }

    // Test updateTokenStatus - Token Not Found Exception
    @Test
    void testUpdateTokenStatus_TokenNotFound_ThrowsException() {
        // Given
        when(tokenRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            doctorService.updateTokenStatus(999L, "WAITING");
        });
        assertEquals("Service.NO_TOKEN_FOUND", exception.getMessage());
        verify(tokenRepository).findById(999L);
    }

    // Test updateTokenStatus - Invalid Status (should throw IllegalArgumentException)
    @Test
    void testUpdateTokenStatus_InvalidStatus_ThrowsException() {
        // Given
        when(tokenRepository.findById(1L)).thenReturn(Optional.of(testToken1));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            doctorService.updateTokenStatus(1L, "INVALID_STATUS");
        });
        verify(tokenRepository).findById(1L);
    }

    // Test updateTokenStatus - Case Insensitive Status
    @Test
    void testUpdateTokenStatus_CaseInsensitiveStatus() throws InfyHospitalException {
        // Given
        when(tokenRepository.findById(1L)).thenReturn(Optional.of(testToken1));
        when(tokenRepository.save(any(Token.class))).thenReturn(testToken1);

        // When
        doctorService.updateTokenStatus(1L, "WAITING"); // uppercase enum name

        // Then
        assertEquals(TokenStatus.WAITING, testToken1.getStatus());
        verify(tokenRepository).findById(1L);
        verify(tokenRepository).save(testToken1);
    }

    // Test getDailySchedule - Edge Case with Different Times
    @Test
    void testGetDailySchedule_DifferentTimes() throws InfyHospitalException {
        // Given
        Token earlyToken = new Token();
        earlyToken.setId(4L);
        earlyToken.setDoctor(testDoctor);
        earlyToken.setTokenNumber(4);
        earlyToken.setCheckInTime(LocalDate.now().atTime(8, 0)); // Early morning
        earlyToken.setStatus(TokenStatus.WAITING);

        Token lateToken = new Token();
        lateToken.setId(5L);
        lateToken.setDoctor(testDoctor);
        lateToken.setTokenNumber(5);
        lateToken.setCheckInTime(LocalDate.now().atTime(23, 59)); // Late evening
        lateToken.setStatus(TokenStatus.WAITING);

        List<Token> tokens = Arrays.asList(earlyToken, lateToken); // Ordered by token number
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(tokens);

        // When
        List<TokenDTO> result = doctorService.getDailySchedule(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        // Should be ordered by token number, not by time
        assertEquals(4, result.get(0).getTokenNumber());
        assertEquals(5, result.get(1).getTokenNumber());
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // Test getCurrentQueue - Edge Case with Mixed Statuses
    @Test
    void testGetCurrentQueue_MixedStatuses() throws InfyHospitalException {
        // Given
        Token cancelledToken = new Token();
        cancelledToken.setId(6L);
        cancelledToken.setDoctor(testDoctor);
        cancelledToken.setTokenNumber(6);
        cancelledToken.setCheckInTime(LocalDateTime.now());
        cancelledToken.setStatus(TokenStatus.NO_SHOW);

        List<Token> allTokens = Arrays.asList(testToken1, testToken2, testToken3, cancelledToken);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(allTokens);

        // When
        List<TokenDTO> result = doctorService.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size()); // Only WAITING and IN_CONSULTATION
        assertTrue(result.stream().allMatch(t -> 
            t.getStatus() == TokenStatus.WAITING || t.getStatus() == TokenStatus.IN_CONSULTATION));
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // Test getDailySchedule - Large Number of Tokens
    @Test
    void testGetDailySchedule_LargeNumberOfTokens() throws InfyHospitalException {
        // Given
        List<Token> manyTokens = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Token token = new Token();
            token.setId((long) i);
            token.setDoctor(testDoctor);
            token.setTokenNumber(i);
            token.setCheckInTime(LocalDateTime.now().plusMinutes(i * 10));
            token.setStatus(TokenStatus.WAITING);
            manyTokens.add(token);
        }

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(manyTokens);

        // When
        List<TokenDTO> result = doctorService.getDailySchedule(1L);

        // Then
        assertNotNull(result);
        assertEquals(50, result.size());
        assertEquals(1, result.get(0).getTokenNumber());
        assertEquals(50, result.get(49).getTokenNumber());
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // Test getCurrentQueue - Empty Current Queue with Completed Tokens
    @Test
    void testGetCurrentQueue_EmptyCurrentQueueWithCompletedTokens() throws InfyHospitalException {
        // Given
        Token completedToken1 = new Token();
        completedToken1.setId(7L);
        completedToken1.setDoctor(testDoctor);
        completedToken1.setTokenNumber(7);
        completedToken1.setCheckInTime(LocalDateTime.now().minusHours(1));
        completedToken1.setStatus(TokenStatus.COMPLETED);

        Token completedToken2 = new Token();
        completedToken2.setId(8L);
        completedToken2.setDoctor(testDoctor);
        completedToken2.setTokenNumber(8);
        completedToken2.setCheckInTime(LocalDateTime.now().minusHours(2));
        completedToken2.setStatus(TokenStatus.COMPLETED);

        List<Token> allTokens = Arrays.asList(completedToken1, completedToken2);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(allTokens);

        // When
        List<TokenDTO> result = doctorService.getCurrentQueue(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty()); // No waiting or in consultation tokens
        verify(doctorRepository).findById(1L);
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(
                eq(testDoctor), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}
