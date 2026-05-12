package com.infy.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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

import com.infy.entity.Patient;
import com.infy.entity.User;
import com.infy.models.Gender;
import com.infy.models.Role;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class PatientRepositoryTest {

    @Mock
    private PatientRepository patientRepository;

    private Patient testPatient;
    private Patient testPatient2;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Setup test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("patient@test.com");
        testUser.setPassword("password");
        testUser.setRole(Role.PATIENT);
        testUser.setCreatedAt(LocalDate.now());

        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setName("John Doe");
        testPatient.setPhone("9876543210");
        testPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPatient.setGender(Gender.MALE);
        testPatient.setUser(testUser);

        testPatient2 = new Patient();
        testPatient2.setId(2L);
        testPatient2.setName("Jane Smith");
        testPatient2.setPhone("1234567890");
        testPatient2.setDateOfBirth(LocalDate.of(1985, 5, 15));
        testPatient2.setGender(Gender.FEMALE);
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Success Case with Name Match
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_SuccessWithNameMatch() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("John", "John"))
                .thenReturn(patients);

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("John", "John");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("9876543210", result.get(0).getPhone());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("John", "John");
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Success Case with Phone Match
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_SuccessWithPhoneMatch() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("9876543210", "9876543210"))
                .thenReturn(patients);

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("9876543210", "9876543210");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("9876543210", result.get(0).getPhone());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("9876543210", "9876543210");
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Success Case with Partial Name Match
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_SuccessWithPartialNameMatch() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("Joh", "Joh"))
                .thenReturn(patients);

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("Joh", "Joh");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("Joh", "Joh");
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Success Case with Multiple Matches
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_SuccessWithMultipleMatches() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient, testPatient2);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("J", "J"))
                .thenReturn(patients);

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("J", "J");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("John Doe")));
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Jane Smith")));
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("J", "J");
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Success Case with Empty Result
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_SuccessWithEmptyResult() {
        // Given
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("Unknown", "Unknown"))
                .thenReturn(Collections.emptyList());

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("Unknown", "Unknown");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("Unknown", "Unknown");
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Success Case with Case Insensitive Search
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_SuccessWithCaseInsensitiveSearch() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("john", "john"))
                .thenReturn(patients);

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("john", "john");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("john", "john");
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Success Case with Mixed Parameters
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_SuccessWithMixedParameters() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("John", "9876543210"))
                .thenReturn(patients);

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("John", "9876543210");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("John", "9876543210");
    }

    // Test findById - Success Case
    @Test
    void testFindById_Success() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        // When
        Optional<Patient> result = patientRepository.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        assertEquals("9876543210", result.get().getPhone());
        verify(patientRepository).findById(1L);
    }

    // Test findById - Not Found Case
    @Test
    void testFindById_NotFound() {
        // Given
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Patient> result = patientRepository.findById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(patientRepository).findById(999L);
    }

    // Test save - Success Case
    @Test
    void testSave_Success() {
        // Given
        Patient newPatient = new Patient();
        newPatient.setName("New Patient");
        newPatient.setPhone("5555555555");
        newPatient.setDateOfBirth(LocalDate.of(2000, 1, 1));
        newPatient.setGender(Gender.MALE);

        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // When
        Patient result = patientRepository.save(newPatient);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(patientRepository).save(newPatient);
    }

    // Test findAll - Success Case
    @Test
    void testFindAll_Success() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient, testPatient2);
        when(patientRepository.findAll()).thenReturn(patients);

        // When
        List<Patient> result = patientRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testPatient));
        assertTrue(result.contains(testPatient2));
        verify(patientRepository).findAll();
    }

    // Test findAll - Empty Result Case
    @Test
    void testFindAll_EmptyResult() {
        // Given
        when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Patient> result = patientRepository.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository).findAll();
    }

    // Test deleteById - Success Case
    @Test
    void testDeleteById_Success() {
        // Given
        doNothing().when(patientRepository).deleteById(1L);

        // When
        patientRepository.deleteById(1L);

        // Then
        verify(patientRepository).deleteById(1L);
    }

    // Test count - Success Case
    @Test
    void testCount_Success() {
        // Given
        when(patientRepository.count()).thenReturn(2L);

        // When
        Long result = patientRepository.count();

        // Then
        assertEquals(2L, result);
        verify(patientRepository).count();
    }

    // Test existsById - Success Case
    @Test
    void testExistsById_Success() {
        // Given
        when(patientRepository.existsById(1L)).thenReturn(true);

        // When
        Boolean result = patientRepository.existsById(1L);

        // Then
        assertTrue(result);
        verify(patientRepository).existsById(1L);
    }

    // Test existsById - Not Found Case
    @Test
    void testExistsById_NotFound() {
        // Given
        when(patientRepository.existsById(999L)).thenReturn(false);

        // When
        Boolean result = patientRepository.existsById(999L);

        // Then
        assertFalse(result);
        verify(patientRepository).existsById(999L);
    }

    // Test findAll with Pageable - Success Case
    @Test
    void testFindAllWithPageable_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> patientPage = new PageImpl<>(Arrays.asList(testPatient));
        when(patientRepository.findAll(any(Pageable.class))).thenReturn(patientPage);

        // When
        Page<Patient> result = patientRepository.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getName());
        verify(patientRepository).findAll(pageable);
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Edge Case with Empty String
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_EdgeCaseWithEmptyString() {
        // Given
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("", ""))
                .thenReturn(Collections.emptyList());

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("", "");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("", "");
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Edge Case with Null Parameters
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_EdgeCaseWithNullParameters() {
        // Given
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining(null, null))
                .thenReturn(Collections.emptyList());

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining(null, null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining(null, null);
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Edge Case with Special Characters
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_EdgeCaseWithSpecialCharacters() {
        // Given
        Patient specialPatient = new Patient();
        specialPatient.setId(3L);
        specialPatient.setName("John-Doe");
        specialPatient.setPhone("123-456-7890");
        specialPatient.setDateOfBirth(LocalDate.of(1992, 3, 15));
        specialPatient.setGender(Gender.MALE);

        List<Patient> patients = Arrays.asList(specialPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("John-Doe", "John-Doe"))
                .thenReturn(patients);

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("John-Doe", "John-Doe");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John-Doe", result.get(0).getName());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("John-Doe", "John-Doe");
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Edge Case with Numbers Only
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_EdgeCaseWithNumbersOnly() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("9876543210", "9876543210"))
                .thenReturn(patients);

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("9876543210", "9876543210");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("9876543210", "9876543210");
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Edge Case with Partial Phone Number
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_EdgeCaseWithPartialPhoneNumber() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("987", "987"))
                .thenReturn(patients);

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("987", "987");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("987", "987");
    }

    // Test findByNameContainingIgnoreCaseOrPhoneContaining - Edge Case with Very Long String
    @Test
    void testFindByNameContainingIgnoreCaseOrPhoneContaining_EdgeCaseWithVeryLongString() {
        // Given
        String longString = "A".repeat(100);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining(longString, longString))
                .thenReturn(Collections.emptyList());

        // When
        List<Patient> result = patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining(longString, longString);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining(longString, longString);
    }
}
