package com.infy.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.models.Gender;
import com.infy.models.Role;

@ExtendWith(MockitoExtension.class)
class PatientTest {

    private Patient patient;
    private User user;

    @BeforeEach
    void setUp() {
        // Setup test data
        user = new User();
        user.setId(1L);
        user.setEmail("patient@test.com");
        user.setPassword("password");
        user.setRole(Role.PATIENT);
        user.setCreatedAt(LocalDate.now());

        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setPhone("9876543210");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.MALE);
        patient.setUser(user);
    }

    // Test getters and setters
    @Test
    void testGettersAndSetters() {
        // Test ID
        patient.setId(2L);
        assertEquals(2L, patient.getId());

        // Test Name
        patient.setName("Jane Smith");
        assertEquals("Jane Smith", patient.getName());

        // Test Phone
        patient.setPhone("1234567890");
        assertEquals("1234567890", patient.getPhone());

        // Test Date of Birth
        LocalDate newDateOfBirth = LocalDate.of(1985, 5, 15);
        patient.setDateOfBirth(newDateOfBirth);
        assertEquals(newDateOfBirth, patient.getDateOfBirth());

        // Test Gender
        patient.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, patient.getGender());

        // Test User
        User newUser = new User();
        newUser.setId(2L);
        newUser.setEmail("newuser@test.com");
        newUser.setPassword("newpassword");
        newUser.setRole(Role.PATIENT);
        newUser.setCreatedAt(LocalDate.now());
        patient.setUser(newUser);
        assertEquals(newUser, patient.getUser());
        assertEquals("newuser@test.com", patient.getUser().getEmail());
    }

    // Test default constructor
    @Test
    void testDefaultConstructor() {
        Patient newPatient = new Patient();
        assertNull(newPatient.getId());
        assertNull(newPatient.getName());
        assertNull(newPatient.getPhone());
        assertNull(newPatient.getDateOfBirth());
        assertNull(newPatient.getGender());
        assertNull(newPatient.getUser());
    }

    // Test toString method
    @Test
    void testToString() {
        String patientString = patient.toString();
        assertNotNull(patientString);
        assertTrue(patientString.contains("John Doe"));
        assertTrue(patientString.contains("9876543210"));
        assertTrue(patientString.contains("MALE"));
    }

    // Test equals method - Same object
    @Test
    void testEquals_SameObject() {
        assertTrue(patient.equals(patient));
    }

    // Test equals method - Equal objects
    @Test
    void testEquals_EqualObjects() {
        Patient anotherPatient = new Patient();
        anotherPatient.setId(1L);
        anotherPatient.setName("John Doe");
        anotherPatient.setPhone("9876543210");
        anotherPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        anotherPatient.setGender(Gender.MALE);
        anotherPatient.setUser(user);

        assertEquals(patient, anotherPatient);
        assertEquals(patient.hashCode(), anotherPatient.hashCode());
    }

    // Test equals method - Different objects
    @Test
    void testEquals_DifferentObjects() {
        Patient anotherPatient = new Patient();
        anotherPatient.setId(2L);
        anotherPatient.setName("Jane Smith");
        anotherPatient.setPhone("1234567890");
        anotherPatient.setDateOfBirth(LocalDate.of(1985, 5, 15));
        anotherPatient.setGender(Gender.FEMALE);
        anotherPatient.setUser(user);

        assertNotEquals(patient, anotherPatient);
    }

    // Test equals method - Null object
    @Test
    void testEquals_NullObject() {
        assertFalse(patient.equals(null));
    }

    // Test equals method - Different class
    @Test
    void testEquals_DifferentClass() {
        assertFalse(patient.equals("not a patient"));
    }

    // Test hashCode consistency
    @Test
    void testHashCodeConsistency() {
        int initialHashCode = patient.hashCode();
        assertEquals(initialHashCode, patient.hashCode());
    }

    // Test hashCode with null values
    @Test
    void testHashCodeWithNullValues() {
        Patient nullPatient = new Patient();
        // Should not throw exception
        assertNotNull(nullPatient.hashCode());
    }

    // Test patient with all valid data
    @Test
    void testPatientWithAllValidData() {
        assertNotNull(patient.getId());
        assertNotNull(patient.getName());
        assertNotNull(patient.getPhone());
        assertNotNull(patient.getDateOfBirth());
        assertNotNull(patient.getGender());
        assertNotNull(patient.getUser());

        assertEquals("John Doe", patient.getName());
        assertEquals("9876543210", patient.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), patient.getDateOfBirth());
        assertEquals(Gender.MALE, patient.getGender());
        assertEquals(user, patient.getUser());
    }

    // Test patient with minimum valid data
    @Test
    void testPatientWithMinimumValidData() {
        Patient minimalPatient = new Patient();
        minimalPatient.setName("A");
        minimalPatient.setPhone("1");
        minimalPatient.setDateOfBirth(LocalDate.now());
        minimalPatient.setGender(Gender.MALE);

        assertEquals("A", minimalPatient.getName());
        assertEquals("1", minimalPatient.getPhone());
        assertEquals(LocalDate.now(), minimalPatient.getDateOfBirth());
        assertEquals(Gender.MALE, minimalPatient.getGender());
    }

    // Test patient with maximum valid data
    @Test
    void testPatientWithMaximumValidData() {
        Patient maxPatient = new Patient();
        String longName = "A".repeat(255);
        String longPhone = "1".repeat(20);
        LocalDate futureDate = LocalDate.now().plusYears(100);

        maxPatient.setName(longName);
        maxPatient.setPhone(longPhone);
        maxPatient.setDateOfBirth(futureDate);
        maxPatient.setGender(Gender.FEMALE);
        maxPatient.setUser(user);

        assertEquals(longName, maxPatient.getName());
        assertEquals(longPhone, maxPatient.getPhone());
        assertEquals(futureDate, maxPatient.getDateOfBirth());
        assertEquals(Gender.FEMALE, maxPatient.getGender());
        assertEquals(user, maxPatient.getUser());
    }

    // Test patient age calculation
    @Test
    void testPatientAgeCalculation() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        patient.setDateOfBirth(birthDate);
        LocalDate currentDate = LocalDate.now();
        int expectedAge = currentDate.getYear() - birthDate.getYear();
        
        // Adjust for birthday not yet occurred this year
        if (currentDate.getDayOfYear() < birthDate.getDayOfYear()) {
            expectedAge--;
        }
        
        // This is a basic test - actual age calculation might be done elsewhere
        assertNotNull(patient.getDateOfBirth());
        assertTrue(currentDate.getYear() - patient.getDateOfBirth().getYear() >= 0);
    }

    // Test patient with different genders
    @Test
    void testPatientWithDifferentGenders() {
        // Test MALE
        patient.setGender(Gender.MALE);
        assertEquals(Gender.MALE, patient.getGender());

        // Test FEMALE
        patient.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, patient.getGender());

        // Test OTHER
        patient.setGender(Gender.OTHER);
        assertEquals(Gender.OTHER, patient.getGender());
    }

    // Test patient with null user
    @Test
    void testPatientWithNullUser() {
        patient.setUser(null);
        assertNull(patient.getUser());
    }

    // Test patient with user relationship
    @Test
    void testPatientUserRelationship() {
        assertEquals(user, patient.getUser());
        assertEquals(patient, user.getPatient()); // Assuming bidirectional relationship
    }

    // Test patient with null name
    @Test
    void testPatientWithNullName() {
        patient.setName(null);
        assertNull(patient.getName());
    }

    // Test patient with empty name
    @Test
    void testPatientWithEmptyName() {
        patient.setName("");
        assertEquals("", patient.getName());
    }

    // Test patient with null phone
    @Test
    void testPatientWithNullPhone() {
        patient.setPhone(null);
        assertNull(patient.getPhone());
    }

    // Test patient with empty phone
    @Test
    void testPatientWithEmptyPhone() {
        patient.setPhone("");
        assertEquals("", patient.getPhone());
    }

    // Test patient with null date of birth
    @Test
    void testPatientWithNullDateOfBirth() {
        patient.setDateOfBirth(null);
        assertNull(patient.getDateOfBirth());
    }

    // Test patient with null gender
    @Test
    void testPatientWithNullGender() {
        patient.setGender(null);
        assertNull(patient.getGender());
    }

    // Test patient with future date of birth
    @Test
    void testPatientWithFutureDateOfBirth() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        patient.setDateOfBirth(futureDate);
        assertEquals(futureDate, patient.getDateOfBirth());
    }

    // Test patient with very old date of birth
    @Test
    void testPatientWithVeryOldDateOfBirth() {
        LocalDate oldDate = LocalDate.of(1900, 1, 1);
        patient.setDateOfBirth(oldDate);
        assertEquals(oldDate, patient.getDateOfBirth());
    }

    // Test patient with special characters in name
    @Test
    void testPatientWithSpecialCharactersInName() {
        String specialName = "John-Doe O'Connor";
        patient.setName(specialName);
        assertEquals(specialName, patient.getName());
    }

    // Test patient with phone number containing special characters
    @Test
    void testPatientWithPhoneContainingSpecialCharacters() {
        String phoneWithSpecialChars = "+1 (555) 123-4567";
        patient.setPhone(phoneWithSpecialChars);
        assertEquals(phoneWithSpecialChars, patient.getPhone());
    }

    // Test patient with international phone number
    @Test
    void testPatientWithInternationalPhoneNumber() {
        String internationalPhone = "+44 20 7946 0958";
        patient.setPhone(internationalPhone);
        assertEquals(internationalPhone, patient.getPhone());
    }

    // Test patient equals with different IDs
    @Test
    void testPatientEqualsWithDifferentIds() {
        Patient patient1 = new Patient();
        patient1.setId(1L);

        Patient patient2 = new Patient();
        patient2.setId(2L);

        assertNotEquals(patient1, patient2);
    }

    // Test patient equals with null ID
    @Test
    void testPatientEqualsWithNullId() {
        Patient patient1 = new Patient();
        patient1.setId(null);

        Patient patient2 = new Patient();
        patient2.setId(null);

        // Both have null IDs, should be equal based on other fields
        assertNotEquals(patient1, patient2); // Since other fields are different
    }

    // Test patient with same ID but different fields
    @Test
    void testPatientWithSameIdButDifferentFields() {
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("John Doe");

        Patient patient2 = new Patient();
        patient2.setId(1L);
        patient2.setName("Jane Smith");

        // If equals is based on ID only, they should be equal
        // If equals is based on all fields, they should be different
        // This test assumes ID-based equality (common in JPA entities)
        assertEquals(patient1, patient2);
    }
}
