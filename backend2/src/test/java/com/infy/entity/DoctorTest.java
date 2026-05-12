package com.infy.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.models.Department;
import com.infy.models.Gender;
import com.infy.models.Role;

@ExtendWith(MockitoExtension.class)
class DoctorTest {

    private Doctor doctor;
    private User user;

    @BeforeEach
    void setUp() {
        // Setup test data
        user = new User();
        user.setId(1L);
        user.setEmail("doctor@test.com");
        user.setPassword("password");
        user.setRole(Role.DOCTOR);
        user.setCreatedAt(java.time.LocalDate.now());

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        doctor.setPhone("1234567890");
        doctor.setGender(Gender.MALE);
        doctor.setDepartment(Department.CARDIOLOGY);
        doctor.setLocation("New York");
        doctor.setDescription("Cardiologist");
        doctor.setUser(user);
    }

    // Test getters and setters
    @Test
    void testGettersAndSetters() {
        // Test ID
        doctor.setId(2L);
        assertEquals(2L, doctor.getId());

        // Test Name
        doctor.setName("Dr. Johnson");
        assertEquals("Dr. Johnson", doctor.getName());

        // Test Phone
        doctor.setPhone("0987654321");
        assertEquals("0987654321", doctor.getPhone());

        // Test Gender
        doctor.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, doctor.getGender());

        // Test Department
        doctor.setDepartment(Department.ORTHOPEDICS);
        assertEquals(Department.ORTHOPEDICS, doctor.getDepartment());

        // Test Location
        doctor.setLocation("Los Angeles");
        assertEquals("Los Angeles", doctor.getLocation());

        // Test Description
        doctor.setDescription("Orthopedist");
        assertEquals("Orthopedist", doctor.getDescription());

        // Test User
        User newUser = new User();
        newUser.setId(2L);
        newUser.setEmail("newdoctor@test.com");
        newUser.setPassword("newpassword");
        newUser.setRole(Role.DOCTOR);
        newUser.setCreatedAt(java.time.LocalDate.now());
        doctor.setUser(newUser);
        assertEquals(newUser, doctor.getUser());
        assertEquals("newdoctor@test.com", doctor.getUser().getEmail());
    }

    // Test default constructor
    @Test
    void testDefaultConstructor() {
        Doctor newDoctor = new Doctor();
        assertNull(newDoctor.getId());
        assertNull(newDoctor.getName());
        assertNull(newDoctor.getPhone());
        assertNull(newDoctor.getGender());
        assertNull(newDoctor.getDepartment());
        assertNull(newDoctor.getLocation());
        assertNull(newDoctor.getDescription());
        assertNull(newDoctor.getUser());
    }

    // Test toString method
    @Test
    void testToString() {
        String doctorString = doctor.toString();
        assertNotNull(doctorString);
        assertTrue(doctorString.contains("Dr. Smith"));
        assertTrue(doctorString.contains("1234567890"));
        assertTrue(doctorString.contains("MALE"));
        assertTrue(doctorString.contains("CARDIOLOGY"));
    }

    // Test equals method - Same object
    @Test
    void testEquals_SameObject() {
        assertTrue(doctor.equals(doctor));
    }

    // Test equals method - Equal objects
    @Test
    void testEquals_EqualObjects() {
        Doctor anotherDoctor = new Doctor();
        anotherDoctor.setId(1L);
        anotherDoctor.setName("Dr. Smith");
        anotherDoctor.setPhone("1234567890");
        anotherDoctor.setGender(Gender.MALE);
        anotherDoctor.setDepartment(Department.CARDIOLOGY);
        anotherDoctor.setLocation("New York");
        anotherDoctor.setDescription("Cardiologist");
        anotherDoctor.setUser(user);

        assertEquals(doctor, anotherDoctor);
        assertEquals(doctor.hashCode(), anotherDoctor.hashCode());
    }

    // Test equals method - Different objects
    @Test
    void testEquals_DifferentObjects() {
        Doctor anotherDoctor = new Doctor();
        anotherDoctor.setId(2L);
        anotherDoctor.setName("Dr. Johnson");
        anotherDoctor.setPhone("0987654321");
        anotherDoctor.setGender(Gender.FEMALE);
        anotherDoctor.setDepartment(Department.ORTHOPEDICS);
        anotherDoctor.setLocation("Los Angeles");
        anotherDoctor.setDescription("Orthopedist");
        anotherDoctor.setUser(user);

        assertNotEquals(doctor, anotherDoctor);
    }

    // Test equals method - Null object
    @Test
    void testEquals_NullObject() {
        assertFalse(doctor.equals(null));
    }

    // Test equals method - Different class
    @Test
    void testEquals_DifferentClass() {
        assertFalse(doctor.equals("not a doctor"));
    }

    // Test hashCode consistency
    @Test
    void testHashCodeConsistency() {
        int initialHashCode = doctor.hashCode();
        assertEquals(initialHashCode, doctor.hashCode());
    }

    // Test hashCode with null values
    @Test
    void testHashCodeWithNullValues() {
        Doctor nullDoctor = new Doctor();
        // Should not throw exception
        assertNotNull(nullDoctor.hashCode());
    }

    // Test doctor with all valid data
    @Test
    void testDoctorWithAllValidData() {
        assertNotNull(doctor.getId());
        assertNotNull(doctor.getName());
        assertNotNull(doctor.getPhone());
        assertNotNull(doctor.getGender());
        assertNotNull(doctor.getDepartment());
        assertNotNull(doctor.getLocation());
        assertNotNull(doctor.getDescription());
        assertNotNull(doctor.getUser());

        assertEquals("Dr. Smith", doctor.getName());
        assertEquals("1234567890", doctor.getPhone());
        assertEquals(Gender.MALE, doctor.getGender());
        assertEquals(Department.CARDIOLOGY, doctor.getDepartment());
        assertEquals("New York", doctor.getLocation());
        assertEquals("Cardiologist", doctor.getDescription());
        assertEquals(user, doctor.getUser());
    }

    // Test doctor with minimum valid data
    @Test
    void testDoctorWithMinimumValidData() {
        Doctor minimalDoctor = new Doctor();
        minimalDoctor.setName("Dr. A");
        minimalDoctor.setPhone("1");
        minimalDoctor.setGender(Gender.MALE);
        minimalDoctor.setDepartment(Department.GENERAL);

        assertEquals("Dr. A", minimalDoctor.getName());
        assertEquals("1", minimalDoctor.getPhone());
        assertEquals(Gender.MALE, minimalDoctor.getGender());
        assertEquals(Department.GENERAL, minimalDoctor.getDepartment());
    }

    // Test doctor with maximum valid data
    @Test
    void testDoctorWithMaximumValidData() {
        Doctor maxDoctor = new Doctor();
        String longName = "Dr. " + "A".repeat(250);
        String longPhone = "1".repeat(20);
        String longLocation = "A".repeat(500);
        String longDescription = "A".repeat(1000);

        maxDoctor.setName(longName);
        maxDoctor.setPhone(longPhone);
        maxDoctor.setGender(Gender.FEMALE);
        maxDoctor.setDepartment(Department.PEDIATRICS);
        maxDoctor.setLocation(longLocation);
        maxDoctor.setDescription(longDescription);
        maxDoctor.setUser(user);

        assertEquals(longName, maxDoctor.getName());
        assertEquals(longPhone, maxDoctor.getPhone());
        assertEquals(Gender.FEMALE, maxDoctor.getGender());
        assertEquals(Department.PEDIATRICS, maxDoctor.getDepartment());
        assertEquals(longLocation, maxDoctor.getLocation());
        assertEquals(longDescription, maxDoctor.getDescription());
        assertEquals(user, maxDoctor.getUser());
    }

    // Test doctor with all departments
    @Test
    void testDoctorWithAllDepartments() {
        // Test CARDIOLOGY
        doctor.setDepartment(Department.CARDIOLOGY);
        assertEquals(Department.CARDIOLOGY, doctor.getDepartment());

        // Test ORTHOPEDICS
        doctor.setDepartment(Department.ORTHOPEDICS);
        assertEquals(Department.ORTHOPEDICS, doctor.getDepartment());

        // Test PEDIATRICS
        doctor.setDepartment(Department.PEDIATRICS);
        assertEquals(Department.PEDIATRICS, doctor.getDepartment());

        // Test GENERAL
        doctor.setDepartment(Department.GENERAL);
        assertEquals(Department.GENERAL, doctor.getDepartment());
    }

    // Test doctor with all genders
    @Test
    void testDoctorWithAllGenders() {
        // Test MALE
        doctor.setGender(Gender.MALE);
        assertEquals(Gender.MALE, doctor.getGender());

        // Test FEMALE
        doctor.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, doctor.getGender());

        // Test OTHER
        doctor.setGender(Gender.OTHER);
        assertEquals(Gender.OTHER, doctor.getGender());
    }

    // Test doctor with null user
    @Test
    void testDoctorWithNullUser() {
        doctor.setUser(null);
        assertNull(doctor.getUser());
    }

    // Test doctor with user relationship
    @Test
    void testDoctorUserRelationship() {
        assertEquals(user, doctor.getUser());
        assertEquals(doctor, user.getDoctor()); // Assuming bidirectional relationship
    }

    // Test doctor with null name
    @Test
    void testDoctorWithNullName() {
        doctor.setName(null);
        assertNull(doctor.getName());
    }

    // Test doctor with empty name
    @Test
    void testDoctorWithEmptyName() {
        doctor.setName("");
        assertEquals("", doctor.getName());
    }

    // Test doctor with null phone
    @Test
    void testDoctorWithNullPhone() {
        doctor.setPhone(null);
        assertNull(doctor.getPhone());
    }

    // Test doctor with empty phone
    @Test
    void testDoctorWithEmptyPhone() {
        doctor.setPhone("");
        assertEquals("", doctor.getPhone());
    }

    // Test doctor with null gender
    @Test
    void testDoctorWithNullGender() {
        doctor.setGender(null);
        assertNull(doctor.getGender());
    }

    // Test doctor with null department
    @Test
    void testDoctorWithNullDepartment() {
        doctor.setDepartment(null);
        assertNull(doctor.getDepartment());
    }

    // Test doctor with null location
    @Test
    void testDoctorWithNullLocation() {
        doctor.setLocation(null);
        assertNull(doctor.getLocation());
    }

    // Test doctor with empty location
    @Test
    void testDoctorWithEmptyLocation() {
        doctor.setLocation("");
        assertEquals("", doctor.getLocation());
    }

    // Test doctor with null description
    @Test
    void testDoctorWithNullDescription() {
        doctor.setDescription(null);
        assertNull(doctor.getDescription());
    }

    // Test doctor with empty description
    @Test
    void testDoctorWithEmptyDescription() {
        doctor.setDescription("");
        assertEquals("", doctor.getDescription());
    }

    // Test doctor with special characters in name
    @Test
    void testDoctorWithSpecialCharactersInName() {
        String specialName = "Dr. John-Doe O'Connor Jr.";
        doctor.setName(specialName);
        assertEquals(specialName, doctor.getName());
    }

    // Test doctor with phone number containing special characters
    @Test
    void testDoctorWithPhoneContainingSpecialCharacters() {
        String phoneWithSpecialChars = "+1 (555) 123-4567 ext. 123";
        doctor.setPhone(phoneWithSpecialChars);
        assertEquals(phoneWithSpecialChars, doctor.getPhone());
    }

    // Test doctor with international phone number
    @Test
    void testDoctorWithInternationalPhoneNumber() {
        String internationalPhone = "+44 20 7946 0958";
        doctor.setPhone(internationalPhone);
        assertEquals(internationalPhone, doctor.getPhone());
    }

    // Test doctor with complex location
    @Test
    void testDoctorWithComplexLocation() {
        String complexLocation = "123 Main St, Suite 456, New York, NY 10001, USA";
        doctor.setLocation(complexLocation);
        assertEquals(complexLocation, doctor.getLocation());
    }

    // Test doctor with detailed description
    @Test
    void testDoctorWithDetailedDescription() {
        String detailedDescription = "Board-certified cardiologist with 15+ years of experience. " +
                "Specializes in interventional cardiology and heart disease prevention. " +
                "Graduated from Harvard Medical School, completed residency at Johns Hopkins.";
        doctor.setDescription(detailedDescription);
        assertEquals(detailedDescription, doctor.getDescription());
    }

    // Test doctor equals with different IDs
    @Test
    void testDoctorEqualsWithDifferentIds() {
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);

        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);

        assertNotEquals(doctor1, doctor2);
    }

    // Test doctor equals with null ID
    @Test
    void testDoctorEqualsWithNullId() {
        Doctor doctor1 = new Doctor();
        doctor1.setId(null);

        Doctor doctor2 = new Doctor();
        doctor2.setId(null);

        // Both have null IDs, should be equal based on other fields
        assertNotEquals(doctor1, doctor2); // Since other fields are different
    }

    // Test doctor with same ID but different fields
    @Test
    void testDoctorWithSameIdButDifferentFields() {
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setName("Dr. Smith");

        Doctor doctor2 = new Doctor();
        doctor2.setId(1L);
        doctor2.setName("Dr. Johnson");

        // If equals is based on ID only, they should be equal
        // If equals is based on all fields, they should be different
        // This test assumes ID-based equality (common in JPA entities)
        assertEquals(doctor1, doctor2);
    }

    // Test doctor with different titles in name
    @Test
    void testDoctorWithDifferentTitlesInName() {
        // Test with Dr. prefix
        doctor.setName("Dr. John Smith");
        assertEquals("Dr. John Smith", doctor.getName());

        // Test with Prof. prefix
        doctor.setName("Prof. Jane Doe");
        assertEquals("Prof. Jane Doe", doctor.getName());

        // Test with multiple titles
        doctor.setName("Dr. Prof. Robert Johnson");
        assertEquals("Dr. Prof. Robert Johnson", doctor.getName());

        // Test without title
        doctor.setName("Alice Williams");
        assertEquals("Alice Williams", doctor.getName());
    }

    // Test doctor with various location formats
    @Test
    void testDoctorWithVariousLocationFormats() {
        // Test simple city
        doctor.setLocation("New York");
        assertEquals("New York", doctor.getLocation());

        // Test city with state
        doctor.setLocation("Boston, MA");
        assertEquals("Boston, MA", doctor.getLocation());

        // Test full address
        doctor.setLocation("456 Oak Ave, Los Angeles, CA 90001");
        assertEquals("456 Oak Ave, Los Angeles, CA 90001", doctor.getLocation());

        // Test hospital name
        doctor.setLocation("Mayo Clinic, Rochester");
        assertEquals("Mayo Clinic, Rochester", doctor.getLocation());

        // Test with department
        doctor.setLocation("St. Mary's Hospital, Cardiology Department");
        assertEquals("St. Mary's Hospital, Cardiology Department", doctor.getLocation());
    }

    // Test doctor with various description lengths
    @Test
    void testDoctorWithVariousDescriptionLengths() {
        // Test short description
        doctor.setDescription("General practitioner");
        assertEquals("General practitioner", doctor.getDescription());

        // Test medium description
        doctor.setDescription("Experienced cardiologist specializing in heart diseases and preventive care");
        assertEquals("Experienced cardiologist specializing in heart diseases and preventive care", doctor.getDescription());

        // Test very long description
        String longDescription = "A highly skilled and experienced medical professional with extensive expertise in the field of cardiology. " +
                "Completed medical education at prestigious institutions and has been practicing for over two decades. " +
                "Known for compassionate patient care and innovative treatment approaches. " +
                "Published numerous research papers and actively involved in clinical trials. " +
                "Dedicated to advancing medical knowledge and improving patient outcomes.";
        doctor.setDescription(longDescription);
        assertEquals(longDescription, doctor.getDescription());
    }
}
