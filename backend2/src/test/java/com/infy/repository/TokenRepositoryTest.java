package com.infy.repository;

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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.infy.entity.Doctor;
import com.infy.entity.Patient;
import com.infy.entity.Token;
import com.infy.entity.User;
import com.infy.models.Gender;
import com.infy.models.Role;
import com.infy.models.TokenStatus;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class TokenRepositoryTest {

    @Mock
    private TokenRepository tokenRepository;

    private Token testToken;
    private Token testToken2;
    private Token testToken3;
    private Doctor testDoctor;
    private Patient testPatient;

    @BeforeEach
    void setUp() {
        // Setup test data
        User doctorUser = new User();
        doctorUser.setId(1L);
        doctorUser.setEmail("doctor@test.com");
        doctorUser.setPassword("password");
        doctorUser.setRole(Role.DOCTOR);

        User patientUser = new User();
        patientUser.setId(2L);
        patientUser.setEmail("patient@test.com");
        patientUser.setPassword("password");
        patientUser.setRole(Role.PATIENT);

        testDoctor = new Doctor();
        testDoctor.setId(1L);
        testDoctor.setName("Dr. Smith");
        testDoctor.setPhone("1234567890");
        testDoctor.setGender(Gender.MALE);
        testDoctor.setUser(doctorUser);

        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setName("John Doe");
        testPatient.setPhone("9876543210");
        testPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPatient.setGender(Gender.MALE);
        testPatient.setUser(patientUser);

        testToken = new Token();
        testToken.setId(1L);
        testToken.setDoctor(testDoctor);
        testToken.setPatient(testPatient);
        testToken.setTokenNumber(1);
        testToken.setDate(LocalDate.now());
        testToken.setStatus(TokenStatus.WAITING);
        testToken.setCheckInTime(LocalDateTime.now());

        testToken2 = new Token();
        testToken2.setId(2L);
        testToken2.setDoctor(testDoctor);
        testToken2.setPatient(testPatient);
        testToken2.setTokenNumber(2);
        testToken2.setDate(LocalDate.now());
        testToken2.setStatus(TokenStatus.IN_CONSULTATION);
        testToken2.setCheckInTime(LocalDateTime.now().plusMinutes(30));

        testToken3 = new Token();
        testToken3.setId(3L);
        testToken3.setDoctor(testDoctor);
        testToken3.setPatient(testPatient);
        testToken3.setTokenNumber(3);
        testToken3.setDate(LocalDate.now());
        testToken3.setStatus(TokenStatus.COMPLETED);
        testToken3.setCheckInTime(LocalDateTime.now().plusMinutes(60));
    }

    // Test findByDoctorIdAndDateOrderByTokenNumberAsc - Success Case
    @Test
    void testFindByDoctorIdAndDateOrderByTokenNumberAsc_Success() {
        // Given
        LocalDate today = LocalDate.now();
        List<Token> tokens = Arrays.asList(testToken, testToken2, testToken3);
        when(tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, today))
                .thenReturn(tokens);

        // When
        List<Token> result = tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, today);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getTokenNumber());
        assertEquals(2, result.get(1).getTokenNumber());
        assertEquals(3, result.get(2).getTokenNumber());
        verify(tokenRepository).findByDoctorIdAndDateOrderByTokenNumberAsc(1L, today);
    }

    // Test findByDoctorIdAndDateOrderByTokenNumberAsc - Empty Result Case
    @Test
    void testFindByDoctorIdAndDateOrderByTokenNumberAsc_EmptyResult() {
        // Given
        LocalDate today = LocalDate.now();
        when(tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(999L, today))
                .thenReturn(Collections.emptyList());

        // When
        List<Token> result = tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(999L, today);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tokenRepository).findByDoctorIdAndDateOrderByTokenNumberAsc(999L, today);
    }

    // Test findByDoctorIdAndDateOrderByTokenNumberAsc - Success Case with Different Date
    @Test
    void testFindByDoctorIdAndDateOrderByTokenNumberAsc_SuccessWithDifferentDate() {
        // Given
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Token> tokens = Arrays.asList(testToken);
        when(tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, yesterday))
                .thenReturn(tokens);

        // When
        List<Token> result = tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, yesterday);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getTokenNumber());
        verify(tokenRepository).findByDoctorIdAndDateOrderByTokenNumberAsc(1L, yesterday);
    }

    // Test findTopByDoctorIdAndDateOrderByTokenNumberDesc - Success Case
    @Test
    void testFindTopByDoctorIdAndDateOrderByTokenNumberDesc_Success() {
        // Given
        LocalDate today = LocalDate.now();
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, today))
                .thenReturn(testToken3); // Highest token number

        // When
        Token result = tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, today);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getTokenNumber());
        assertEquals(TokenStatus.COMPLETED, result.getStatus());
        verify(tokenRepository).findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, today);
    }

    // Test findTopByDoctorIdAndDateOrderByTokenNumberDesc - Null Result Case
    @Test
    void testFindTopByDoctorIdAndDateOrderByTokenNumberDesc_NullResult() {
        // Given
        LocalDate today = LocalDate.now();
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(999L, today))
                .thenReturn(null);

        // When
        Token result = tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(999L, today);

        // Then
        assertNull(result);
        verify(tokenRepository).findTopByDoctorIdAndDateOrderByTokenNumberDesc(999L, today);
    }

    // Test findTopByDoctorIdAndDateOrderByTokenNumberDesc - Success Case with Different Date
    @Test
    void testFindTopByDoctorIdAndDateOrderByTokenNumberDesc_SuccessWithDifferentDate() {
        // Given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, tomorrow))
                .thenReturn(testToken);

        // When
        Token result = tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, tomorrow);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTokenNumber());
        verify(tokenRepository).findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, tomorrow);
    }

    // Test findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc - Success Case
    @Test
    void testFindByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc_Success() {
        // Given
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        List<Token> tokens = Arrays.asList(testToken, testToken2, testToken3);
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end))
                .thenReturn(tokens);

        // When
        List<Token> result = tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getTokenNumber());
        assertEquals(2, result.get(1).getTokenNumber());
        assertEquals(3, result.get(2).getTokenNumber());
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end);
    }

    // Test findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc - Empty Result Case
    @Test
    void testFindByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc_EmptyResult() {
        // Given
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end))
                .thenReturn(Collections.emptyList());

        // When
        List<Token> result = tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end);
    }

    // Test findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc - Success Case with Time Range
    @Test
    void testFindByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc_SuccessWithTimeRange() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        List<Token> tokens = Arrays.asList(testToken, testToken2);
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end))
                .thenReturn(tokens);

        // When
        List<Token> result = tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end);
    }

    // Test findById - Success Case
    @Test
    void testFindById_Success() {
        // Given
        when(tokenRepository.findById(1L)).thenReturn(Optional.of(testToken));

        // When
        Optional<Token> result = tokenRepository.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.get().getStatus());
        verify(tokenRepository).findById(1L);
    }

    // Test findById - Not Found Case
    @Test
    void testFindById_NotFound() {
        // Given
        when(tokenRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Token> result = tokenRepository.findById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(tokenRepository).findById(999L);
    }

    // Test save - Success Case
    @Test
    void testSave_Success() {
        // Given
        Token newToken = new Token();
        newToken.setDoctor(testDoctor);
        newToken.setPatient(testPatient);
        newToken.setTokenNumber(4);
        newToken.setDate(LocalDate.now());
        newToken.setStatus(TokenStatus.WAITING);
        newToken.setCheckInTime(LocalDateTime.now());

        when(tokenRepository.save(any(Token.class))).thenReturn(testToken);

        // When
        Token result = tokenRepository.save(newToken);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTokenNumber());
        verify(tokenRepository).save(newToken);
    }

    // Test findAll - Success Case
    @Test
    void testFindAll_Success() {
        // Given
        List<Token> tokens = Arrays.asList(testToken, testToken2, testToken3);
        when(tokenRepository.findAll()).thenReturn(tokens);

        // When
        List<Token> result = tokenRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(testToken));
        assertTrue(result.contains(testToken2));
        assertTrue(result.contains(testToken3));
        verify(tokenRepository).findAll();
    }

    // Test findAll - Empty Result Case
    @Test
    void testFindAll_EmptyResult() {
        // Given
        when(tokenRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Token> result = tokenRepository.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tokenRepository).findAll();
    }

    // Test deleteById - Success Case
    @Test
    void testDeleteById_Success() {
        // Given
        doNothing().when(tokenRepository).deleteById(1L);

        // When
        tokenRepository.deleteById(1L);

        // Then
        verify(tokenRepository).deleteById(1L);
    }

    // Test count - Success Case
    @Test
    void testCount_Success() {
        // Given
        when(tokenRepository.count()).thenReturn(3L);

        // When
        Long result = tokenRepository.count();

        // Then
        assertEquals(3L, result);
        verify(tokenRepository).count();
    }

    // Test existsById - Success Case
    @Test
    void testExistsById_Success() {
        // Given
        when(tokenRepository.existsById(1L)).thenReturn(true);

        // When
        Boolean result = tokenRepository.existsById(1L);

        // Then
        assertTrue(result);
        verify(tokenRepository).existsById(1L);
    }

    // Test existsById - Not Found Case
    @Test
    void testExistsById_NotFound() {
        // Given
        when(tokenRepository.existsById(999L)).thenReturn(false);

        // When
        Boolean result = tokenRepository.existsById(999L);

        // Then
        assertFalse(result);
        verify(tokenRepository).existsById(999L);
    }

    // Test findAll with Pageable - Success Case
    @Test
    void testFindAllWithPageable_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Token> tokenPage = new PageImpl<>(Arrays.asList(testToken));
        when(tokenRepository.findAll(any(Pageable.class))).thenReturn(tokenPage);

        // When
        Page<Token> result = tokenRepository.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getContent().get(0).getTokenNumber());
        verify(tokenRepository).findAll(pageable);
    }

    // Test findByDoctorIdAndDateOrderByTokenNumberAsc - Edge Case with Future Date
    @Test
    void testFindByDoctorIdAndDateOrderByTokenNumberAsc_EdgeCaseWithFutureDate() {
        // Given
        LocalDate futureDate = LocalDate.now().plusDays(7);
        when(tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, futureDate))
                .thenReturn(Collections.emptyList());

        // When
        List<Token> result = tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, futureDate);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tokenRepository).findByDoctorIdAndDateOrderByTokenNumberAsc(1L, futureDate);
    }

    // Test findByDoctorIdAndDateOrderByTokenNumberAsc - Edge Case with Past Date
    @Test
    void testFindByDoctorIdAndDateOrderByTokenNumberAsc_EdgeCaseWithPastDate() {
        // Given
        LocalDate pastDate = LocalDate.now().minusDays(30);
        Token pastToken = new Token();
        pastToken.setId(4L);
        pastToken.setDoctor(testDoctor);
        pastToken.setPatient(testPatient);
        pastToken.setTokenNumber(4);
        pastToken.setDate(pastDate);
        pastToken.setStatus(TokenStatus.COMPLETED);
        pastToken.setCheckInTime(pastDate.atTime(10, 0));

        List<Token> tokens = Arrays.asList(pastToken);
        when(tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, pastDate))
                .thenReturn(tokens);

        // When
        List<Token> result = tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, pastDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getTokenNumber());
        verify(tokenRepository).findByDoctorIdAndDateOrderByTokenNumberAsc(1L, pastDate);
    }

    // Test findTopByDoctorIdAndDateOrderByTokenNumberDesc - Edge Case with No Tokens
    @Test
    void testFindTopByDoctorIdAndDateOrderByTokenNumberDesc_EdgeCaseWithNoTokens() {
        // Given
        LocalDate today = LocalDate.now();
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, today))
                .thenReturn(null);

        // When
        Token result = tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, today);

        // Then
        assertNull(result);
        verify(tokenRepository).findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, today);
    }

    // Test findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc - Edge Case with Very Small Time Range
    @Test
    void testFindByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc_EdgeCaseWithVerySmallTimeRange() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusMinutes(5);
        LocalDateTime end = LocalDateTime.now().plusMinutes(5);
        List<Token> tokens = Arrays.asList(testToken);
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end))
                .thenReturn(tokens);

        // When
        List<Token> result = tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end);
    }

    // Test findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc - Edge Case with Large Time Range
    @Test
    void testFindByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc_EdgeCaseWithLargeTimeRange() {
        // Given
        LocalDateTime start = LocalDate.now().minusMonths(1).atStartOfDay();
        LocalDateTime end = LocalDate.now().plusMonths(1).atTime(LocalTime.MAX);
        List<Token> tokens = Arrays.asList(testToken, testToken2, testToken3);
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end))
                .thenReturn(tokens);

        // When
        List<Token> result = tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, start, end);
    }

    // Test findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc - Edge Case with Same Start and End Time
    @Test
    void testFindByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc_EdgeCaseWithSameStartAndEndTime() {
        // Given
        LocalDateTime sameTime = LocalDateTime.now();
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, sameTime, sameTime))
                .thenReturn(Collections.emptyList());

        // When
        List<Token> result = tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, sameTime, sameTime);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(testDoctor, sameTime, sameTime);
    }

    // Test findByDoctorIdAndDateOrderByTokenNumberAsc - Edge Case with Large Number of Tokens
    @Test
    void testFindByDoctorIdAndDateOrderByTokenNumberAsc_EdgeCaseWithLargeNumberOfTokens() {
        // Given
        LocalDate today = LocalDate.now();
        List<Token> manyTokens = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Token token = new Token();
            token.setId((long) i);
            token.setDoctor(testDoctor);
            token.setPatient(testPatient);
            token.setTokenNumber(i);
            token.setDate(today);
            token.setStatus(TokenStatus.WAITING);
            token.setCheckInTime(LocalDateTime.now().plusMinutes(i * 10));
            manyTokens.add(token);
        }

        when(tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, today))
                .thenReturn(manyTokens);

        // When
        List<Token> result = tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, today);

        // Then
        assertNotNull(result);
        assertEquals(50, result.size());
        assertEquals(1, result.get(0).getTokenNumber());
        assertEquals(50, result.get(49).getTokenNumber());
        verify(tokenRepository).findByDoctorIdAndDateOrderByTokenNumberAsc(1L, today);
    }

    // Test findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc - Edge Case with Different Doctor
    @Test
    void testFindByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc_EdgeCaseWithDifferentDoctor() {
        // Given
        Doctor differentDoctor = new Doctor();
        differentDoctor.setId(2L);
        differentDoctor.setName("Dr. Johnson");
        differentDoctor.setPhone("0987654321");
        differentDoctor.setGender(Gender.FEMALE);

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        when(tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(differentDoctor, start, end))
                .thenReturn(Collections.emptyList());

        // When
        List<Token> result = tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(differentDoctor, start, end);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tokenRepository).findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(differentDoctor, start, end);
    }

    // Test save - Edge Case with All Token Statuses
    @Test
    void testSave_EdgeCaseWithAllTokenStatuses() {
        // Given
        Token waitingToken = new Token();
        waitingToken.setDoctor(testDoctor);
        waitingToken.setPatient(testPatient);
        waitingToken.setTokenNumber(5);
        waitingToken.setDate(LocalDate.now());
        waitingToken.setStatus(TokenStatus.WAITING);
        waitingToken.setCheckInTime(LocalDateTime.now());

        Token inConsultationToken = new Token();
        inConsultationToken.setDoctor(testDoctor);
        inConsultationToken.setPatient(testPatient);
        inConsultationToken.setTokenNumber(6);
        inConsultationToken.setDate(LocalDate.now());
        inConsultationToken.setStatus(TokenStatus.IN_CONSULTATION);
        inConsultationToken.setCheckInTime(LocalDateTime.now());

        Token completedToken = new Token();
        completedToken.setDoctor(testDoctor);
        completedToken.setPatient(testPatient);
        completedToken.setTokenNumber(7);
        completedToken.setDate(LocalDate.now());
        completedToken.setStatus(TokenStatus.COMPLETED);
        completedToken.setCheckInTime(LocalDateTime.now());

        Token cancelledToken = new Token();
        cancelledToken.setDoctor(testDoctor);
        cancelledToken.setPatient(testPatient);
        cancelledToken.setTokenNumber(8);
        cancelledToken.setDate(LocalDate.now());
        cancelledToken.setStatus(TokenStatus.CANCELLED);
        cancelledToken.setCheckInTime(LocalDateTime.now());

        when(tokenRepository.save(any(Token.class))).thenReturn(testToken);

        // When
        Token result1 = tokenRepository.save(waitingToken);
        Token result2 = tokenRepository.save(inConsultationToken);
        Token result3 = tokenRepository.save(completedToken);
        Token result4 = tokenRepository.save(cancelledToken);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertNotNull(result4);
        verify(tokenRepository, times(4)).save(any(Token.class));
    }
}
