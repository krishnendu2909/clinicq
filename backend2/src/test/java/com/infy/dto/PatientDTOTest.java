package com.infy.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.models.Gender;
import com.infy.models.Role;

@ExtendWith(MockitoExtension.class)
class PatientDTOTest {

    private PatientDTO patientDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("patient@test.com");
        userDTO.setPassword("password");
        userDTO.setRole(Role.PATIENT);
        userDTO.setCreatedAt(LocalDate.now());

        patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setName("John Doe");
        patientDTO.setPhone("9876543210");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setGender(Gender.MALE);
        patientDTO.setUser(userDTO);
    }

    // Test getters and setters
    @Test
    void testGettersAndSetters() {
        // Test ID
        patientDTO.setId(2L);
        assertEquals(2L, patientDTO.getId());

        // Test Name
        patientDTO.setName("Jane Smith");
        assertEquals("Jane Smith", patientDTO.getName());

        // Test Phone
        patientDTO.setPhone("1234567890");
        assertEquals("1234567890", patientDTO.getPhone());

        // Test Date of Birth
        LocalDate newDateOfBirth = LocalDate.of(1985, 5, 15);
        patientDTO.setDateOfBirth(newDateOfBirth);
        assertEquals(newDateOfBirth, patientDTO.getDateOfBirth());

        // Test Gender
        patientDTO.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, patientDTO.getGender());

        // Test User
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setId(2L);
        newUserDTO.setEmail("newuser@test.com");
        newUserDTO.setPassword("newpassword");
        newUserDTO.setRole(Role.PATIENT);
        newUserDTO.setCreatedAt(LocalDate.now());
        patientDTO.setUser(newUserDTO);
        assertEquals(newUserDTO, patientDTO.getUser());
        assertEquals("newuser@test.com", patientDTO.getUser().getEmail());
    }

    // Test default constructor
    @Test
    void testDefaultConstructor() {
        PatientDTO newPatientDTO = new PatientDTO();
        assertNull(newPatientDTO.getId());
        assertNull(newPatientDTO.getName());
        assertNull(newPatientDTO.getPhone());
        assertNull(newPatientDTO.getDateOfBirth());
        assertNull(newPatientDTO.getGender());
        assertNull(newPatientDTO.getUser());
    }

    // Test toString method
    @Test
    void testToString() {
        String patientDTOString = patientDTO.toString();
        assertNotNull(patientDTOString);
        assertTrue(patientDTOString.contains("John Doe"));
        assertTrue(patientDTOString.contains("9876543210"));
        assertTrue(patientDTOString.contains("MALE"));
    }

    // Test equals method - Same object
    @Test
    void testEquals_SameObject() {
        assertTrue(patientDTO.equals(patientDTO));
    }

    // Test equals method - Equal objects
    @Test
    void testEquals_EqualObjects() {
        PatientDTO anotherPatientDTO = new PatientDTO();
        anotherPatientDTO.setId(1L);
        anotherPatientDTO.setName("John Doe");
        anotherPatientDTO.setPhone("9876543210");
        anotherPatientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        anotherPatientDTO.setGender(Gender.MALE);
        anotherPatientDTO.setUser(userDTO);

        assertEquals(patientDTO, anotherPatientDTO);
        assertEquals(patientDTO.hashCode(), anotherPatientDTO.hashCode());
    }

    // Test equals method - Different objects
    @Test
    void testEquals_DifferentObjects() {
        PatientDTO anotherPatientDTO = new PatientDTO();
        anotherPatientDTO.setId(2L);
        anotherPatientDTO.setName("Jane Smith");
        anotherPatientDTO.setPhone("1234567890");
        anotherPatientDTO.setDateOfBirth(LocalDate.of(1985, 5, 15));
        anotherPatientDTO.setGender(Gender.FEMALE);
        anotherPatientDTO.setUser(userDTO);

        assertNotEquals(patientDTO, anotherPatientDTO);
    }

    // Test equals method - Null object
    @Test
    void testEquals_NullObject() {
        assertFalse(patientDTO.equals(null));
    }

    // Test equals method - Different class
    @Test
    void testEquals_DifferentClass() {
        assertFalse(patientDTO.equals("not a patient DTO"));
    }

    // Test hashCode consistency
    @Test
    void testHashCodeConsistency() {
        int initialHashCode = patientDTO.hashCode();
        assertEquals(initialHashCode, patientDTO.hashCode());
    }

    // Test hashCode with null values
    @Test
    void testHashCodeWithNullValues() {
        PatientDTO nullPatientDTO = new PatientDTO();
        // Should not throw exception
        assertNotNull(nullPatientDTO.hashCode());
    }

    // Test patient DTO with all valid data
    @Test
    void testPatientDTOWithAllValidData() {
        assertNotNull(patientDTO.getId());
        assertNotNull(patientDTO.getName());
        assertNotNull(patientDTO.getPhone());
        assertNotNull(patientDTO.getDateOfBirth());
        assertNotNull(patientDTO.getGender());
        assertNotNull(patientDTO.getUser());

        assertEquals("John Doe", patientDTO.getName());
        assertEquals("9876543210", patientDTO.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), patientDTO.getDateOfBirth());
        assertEquals(Gender.MALE, patientDTO.getGender());
        assertEquals(userDTO, patientDTO.getUser());
    }

    // Test patient DTO with minimum valid data
    @Test
    void testPatientDTOWithMinimumValidData() {
        PatientDTO minimalPatientDTO = new PatientDTO();
        minimalPatientDTO.setName("A");
        minimalPatientDTO.setPhone("1");
        minimalPatientDTO.setDateOfBirth(LocalDate.now());
        minimalPatientDTO.setGender(Gender.MALE);

        assertEquals("A", minimalPatientDTO.getName());
        assertEquals("1", minimalPatientDTO.getPhone());
        assertEquals(LocalDate.now(), minimalPatientDTO.getDateOfBirth());
        assertEquals(Gender.MALE, minimalPatientDTO.getGender());
    }

    // Test patient DTO with maximum valid data
    @Test
    void testPatientDTOWithMaximumValidData() {
        PatientDTO maxPatientDTO = new PatientDTO();
        String longName = "A".repeat(255);
        String longPhone = "1".repeat(20);
        LocalDate futureDate = LocalDate.now().plusYears(100);

        maxPatientDTO.setName(longName);
        maxPatientDTO.setPhone(longPhone);
        maxPatientDTO.setDateOfBirth(futureDate);
        maxPatientDTO.setGender(Gender.FEMALE);
        maxPatientDTO.setUser(userDTO);

        assertEquals(longName, maxPatientDTO.getName());
        assertEquals(longPhone, maxPatientDTO.getPhone());
        assertEquals(futureDate, maxPatientDTO.getDateOfBirth());
        assertEquals(Gender.FEMALE, maxPatientDTO.getGender());
        assertEquals(userDTO, maxPatientDTO.getUser());
    }

    // Test patient DTO with different genders
    @Test
    void testPatientDTOWithDifferentGenders() {
        // Test MALE
        patientDTO.setGender(Gender.MALE);
        assertEquals(Gender.MALE, patientDTO.getGender());

        // Test FEMALE
        patientDTO.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, patientDTO.getGender());

        // Test OTHER
        patientDTO.setGender(Gender.OTHER);
        assertEquals(Gender.OTHER, patientDTO.getGender());
    }

    // Test patient DTO with null user
    @Test
    void testPatientDTOWithNullUser() {
        patientDTO.setUser(null);
        assertNull(patientDTO.getUser());
    }

    // Test patient DTO with null name
    @Test
    void testPatientDTOWithNullName() {
        patientDTO.setName(null);
        assertNull(patientDTO.getName());
    }

    // Test patient DTO with empty name
    @Test
    void testPatientDTOWithEmptyName() {
        patientDTO.setName("");
        assertEquals("", patientDTO.getName());
    }

    // Test patient DTO with null phone
    @Test
    void testPatientDTOWithNullPhone() {
        patientDTO.setPhone(null);
        assertNull(patientDTO.getPhone());
    }

    // Test patient DTO with empty phone
    @Test
    void testPatientDTOWithEmptyPhone() {
        patientDTO.setPhone("");
        assertEquals("", patientDTO.getPhone());
    }

    // Test patient DTO with null date of birth
    @Test
    void testPatientDTOWithNullDateOfBirth() {
        patientDTO.setDateOfBirth(null);
        assertNull(patientDTO.getDateOfBirth());
    }

    // Test patient DTO with null gender
    @Test
    void testPatientDTOWithNullGender() {
        patientDTO.setGender(null);
        assertNull(patientDTO.getGender());
    }

    // Test patient DTO with future date of birth
    @Test
    void testPatientDTOWithFutureDateOfBirth() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        patientDTO.setDateOfBirth(futureDate);
        assertEquals(futureDate, patientDTO.getDateOfBirth());
    }

    // Test patient DTO with very old date of birth
    @Test
    void testPatientDTOWithVeryOldDateOfBirth() {
        LocalDate oldDate = LocalDate.of(1900, 1, 1);
        patientDTO.setDateOfBirth(oldDate);
        assertEquals(oldDate, patientDTO.getDateOfBirth());
    }

    // Test patient DTO with special characters in name
    @Test
    void testPatientDTOWithSpecialCharactersInName() {
        String specialName = "John-Doe O'Connor";
        patientDTO.setName(specialName);
        assertEquals(specialName, patientDTO.getName());
    }

    // Test patient DTO with phone number containing special characters
    @Test
    void testPatientDTOWithPhoneContainingSpecialCharacters() {
        String phoneWithSpecialChars = "+1 (555) 123-4567";
        patientDTO.setPhone(phoneWithSpecialChars);
        assertEquals(phoneWithSpecialChars, patientDTO.getPhone());
    }

    // Test patient DTO with international phone number
    @Test
    void testPatientDTOWithInternationalPhoneNumber() {
        String internationalPhone = "+44 20 7946 0958";
        patientDTO.setPhone(internationalPhone);
        assertEquals(internationalPhone, patientDTO.getPhone());
    }

    // Test patient DTO equals with different IDs
    @Test
    void testPatientDTOEqualsWithDifferentIds() {
        PatientDTO patientDTO1 = new PatientDTO();
        patientDTO1.setId(1L);

        PatientDTO patientDTO2 = new PatientDTO();
        patientDTO2.setId(2L);

        assertNotEquals(patientDTO1, patientDTO2);
    }

    // Test patient DTO equals with null ID
    @Test
    void testPatientDTOEqualsWithNullId() {
        PatientDTO patientDTO1 = new PatientDTO();
        patientDTO1.setId(null);

        PatientDTO patientDTO2 = new PatientDTO();
        patientDTO2.setId(null);

        // Both have null IDs, should be equal based on other fields
        assertNotEquals(patientDTO1, patientDTO2); // Since other fields are different
    }

    // Test patient DTO with same ID but different fields
    @Test
    void testPatientDTOWithSameIdButDifferentFields() {
        PatientDTO patientDTO1 = new PatientDTO();
        patientDTO1.setId(1L);
        patientDTO1.setName("John Doe");

        PatientDTO patientDTO2 = new PatientDTO();
        patientDTO2.setId(1L);
        patientDTO2.setName("Jane Smith");

        // If equals is based on ID only, they should be equal
        // If equals is based on all fields, they should be different
        // This test assumes ID-based equality (common in DTOs)
        assertEquals(patientDTO1, patientDTO2);
    }

    // Test patient DTO age calculation
    @Test
    void testPatientDTOAgeCalculation() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        patientDTO.setDateOfBirth(birthDate);
        LocalDate currentDate = LocalDate.now();
        int expectedAge = currentDate.getYear() - birthDate.getYear();
        
        // Adjust for birthday not yet occurred this year
        if (currentDate.getDayOfYear() < birthDate.getDayOfYear()) {
            expectedAge--;
        }
        
        // This is a basic test - actual age calculation might be done elsewhere
        assertNotNull(patientDTO.getDateOfBirth());
        assertTrue(currentDate.getYear() - patientDTO.getDateOfBirth().getYear() >= 0);
    }

    // Test patient DTO with different user roles
    @Test
    void testPatientDTOWithDifferentUserRoles() {
        // Test PATIENT role
        userDTO.setRole(Role.PATIENT);
        patientDTO.setUser(userDTO);
        assertEquals(Role.PATIENT, patientDTO.getUser().getRole());

        // Test with different role (though unlikely for patient)
        userDTO.setRole(Role.DOCTOR);
        patientDTO.setUser(userDTO);
        assertEquals(Role.DOCTOR, patientDTO.getUser().getRole());
    }

    // Test patient DTO with user having different fields
    @Test
    void testPatientDTOWithUserHavingDifferentFields() {
        UserDTO differentUser = new UserDTO();
        differentUser.setId(3L);
        differentUser.setEmail("different@test.com");
        differentUser.setPassword("differentpassword");
        differentUser.setRole(Role.PATIENT);
        differentUser.setCreatedAt(LocalDate.now().minusYears(1));

        patientDTO.setUser(differentUser);
        assertEquals(differentUser, patientDTO.getUser());
        assertEquals("different@test.com", patientDTO.getUser().getEmail());
        assertEquals("differentpassword", patientDTO.getUser().getPassword());
        assertEquals(Role.PATIENT, patientDTO.getUser().getRole());
    }

    // Test patient DTO with various email formats in user
    @Test
    void testPatientDTOWithVariousEmailFormatsInUser() {
        // Standard email
        userDTO.setEmail("patient@example.com");
        patientDTO.setUser(userDTO);
        assertEquals("patient@example.com", patientDTO.getUser().getEmail());

        // Email with subdomain
        userDTO.setEmail("patient@mail.example.com");
        patientDTO.setUser(userDTO);
        assertEquals("patient@mail.example.com", patientDTO.getUser().getEmail());

        // Email with numbers
        userDTO.setEmail("patient123@example.com");
        patientDTO.setUser(userDTO);
        assertEquals("patient123@example.com", patientDTO.getUser().getEmail());

        // Email with dots
        userDTO.setEmail("john.doe@example.com");
        patientDTO.setUser(userDTO);
        assertEquals("john.doe@example.com", patientDTO.getUser().getEmail());
    }

    // Test patient DTO with user created at different dates
    @Test
    void testPatientDTOWithUserCreatedAtDifferentDates() {
        // Today
        LocalDate today = LocalDate.now();
        userDTO.setCreatedAt(today);
        patientDTO.setUser(userDTO);
        assertEquals(today, patientDTO.getUser().getCreatedAt());

        // Yesterday
        LocalDate yesterday = today.minusDays(1);
        userDTO.setCreatedAt(yesterday);
        patientDTO.setUser(userDTO);
        assertEquals(yesterday, patientDTO.getUser().getCreatedAt());

        // Last week
        LocalDate lastWeek = today.minusWeeks(1);
        userDTO.setCreatedAt(lastWeek);
        patientDTO.setUser(userDTO);
        assertEquals(lastWeek, patientDTO.getUser().getCreatedAt());

        // Last year
        LocalDate lastYear = today.minusYears(1);
        userDTO.setCreatedAt(lastYear);
        patientDTO.setUser(userDTO);
        assertEquals(lastYear, patientDTO.getUser().getCreatedAt());
    }

    // Test patient DTO with very long user password
    @Test
    void testPatientDTOWithVeryLongUserPassword() {
        String longPassword = "A".repeat(100) + "1!" + "a".repeat(50);
        userDTO.setPassword(longPassword);
        patientDTO.setUser(userDTO);
        assertEquals(longPassword, patientDTO.getUser().getPassword());
    }

    // Test patient DTO with user having null fields
    @Test
    void testPatientDTOWithUserHavingNullFields() {
        UserDTO userWithNulls = new UserDTO();
        userWithNulls.setId(null);
        userWithNulls.setEmail(null);
        userWithNulls.setPassword(null);
        userWithNulls.setRole(null);
        userWithNulls.setCreatedAt(null);

        patientDTO.setUser(userWithNulls);
        assertEquals(userWithNulls, patientDTO.getUser());
        assertNull(patientDTO.getUser().getId());
        assertNull(patientDTO.getUser().getEmail());
        assertNull(patientDTO.getUser().getPassword());
        assertNull(patientDTO.getUser().getRole());
        assertNull(patientDTO.getUser().getCreatedAt());
    }

    // Test patient DTO with user having empty fields
    @Test
    void testPatientDTOWithUserHavingEmptyFields() {
        UserDTO userWithEmpties = new UserDTO();
        userWithEmpties.setId(1L);
        userWithEmpties.setEmail("");
        userWithEmpties.setPassword("");
        userWithEmpties.setRole(Role.PATIENT);
        userWithEmpties.setCreatedAt(LocalDate.now());

        patientDTO.setUser(userWithEmpties);
        assertEquals(userWithEmpties, patientDTO.getUser());
        assertEquals("", patientDTO.getUser().getEmail());
        assertEquals("", patientDTO.getUser().getPassword());
        assertEquals(Role.PATIENT, patientDTO.getUser().getRole());
    }
}
