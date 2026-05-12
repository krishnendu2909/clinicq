package com.infy.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.models.Department;
import com.infy.models.Gender;
import com.infy.models.Role;

@ExtendWith(MockitoExtension.class)
class DoctorDTOTest {

    private DoctorDTO doctorDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("doctor@test.com");
        userDTO.setPassword("password");
        userDTO.setRole(Role.DOCTOR);
        userDTO.setCreatedAt(java.time.LocalDate.now());

        doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setName("Dr. Smith");
        doctorDTO.setPhone("1234567890");
        doctorDTO.setGender(Gender.MALE);
        doctorDTO.setDepartment(Department.CARDIOLOGY);
        doctorDTO.setLocation("New York");
        doctorDTO.setDescription("Cardiologist");
        doctorDTO.setUser(userDTO);
    }

    // Test getters and setters
    @Test
    void testGettersAndSetters() {
        // Test ID
        doctorDTO.setId(2L);
        assertEquals(2L, doctorDTO.getId());

        // Test Name
        doctorDTO.setName("Dr. Johnson");
        assertEquals("Dr. Johnson", doctorDTO.getName());

        // Test Phone
        doctorDTO.setPhone("0987654321");
        assertEquals("0987654321", doctorDTO.getPhone());

        // Test Gender
        doctorDTO.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, doctorDTO.getGender());

        // Test Department
        doctorDTO.setDepartment(Department.ORTHOPEDICS);
        assertEquals(Department.ORTHOPEDICS, doctorDTO.getDepartment());

        // Test Location
        doctorDTO.setLocation("Los Angeles");
        assertEquals("Los Angeles", doctorDTO.getLocation());

        // Test Description
        doctorDTO.setDescription("Orthopedist");
        assertEquals("Orthopedist", doctorDTO.getDescription());

        // Test User
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setId(2L);
        newUserDTO.setEmail("newdoctor@test.com");
        newUserDTO.setPassword("newpassword");
        newUserDTO.setRole(Role.DOCTOR);
        newUserDTO.setCreatedAt(java.time.LocalDate.now());
        doctorDTO.setUser(newUserDTO);
        assertEquals(newUserDTO, doctorDTO.getUser());
        assertEquals("newdoctor@test.com", doctorDTO.getUser().getEmail());
    }

    // Test default constructor
    @Test
    void testDefaultConstructor() {
        DoctorDTO newDoctorDTO = new DoctorDTO();
        assertNull(newDoctorDTO.getId());
        assertNull(newDoctorDTO.getName());
        assertNull(newDoctorDTO.getPhone());
        assertNull(newDoctorDTO.getGender());
        assertNull(newDoctorDTO.getDepartment());
        assertNull(newDoctorDTO.getLocation());
        assertNull(newDoctorDTO.getDescription());
        assertNull(newDoctorDTO.getUser());
    }

    // Test toString method
    @Test
    void testToString() {
        String doctorDTOString = doctorDTO.toString();
        assertNotNull(doctorDTOString);
        assertTrue(doctorDTOString.contains("Dr. Smith"));
        assertTrue(doctorDTOString.contains("1234567890"));
        assertTrue(doctorDTOString.contains("MALE"));
        assertTrue(doctorDTOString.contains("CARDIOLOGY"));
    }

    // Test equals method - Same object
    @Test
    void testEquals_SameObject() {
        assertTrue(doctorDTO.equals(doctorDTO));
    }

    // Test equals method - Equal objects
    @Test
    void testEquals_EqualObjects() {
        DoctorDTO anotherDoctorDTO = new DoctorDTO();
        anotherDoctorDTO.setId(1L);
        anotherDoctorDTO.setName("Dr. Smith");
        anotherDoctorDTO.setPhone("1234567890");
        anotherDoctorDTO.setGender(Gender.MALE);
        anotherDoctorDTO.setDepartment(Department.CARDIOLOGY);
        anotherDoctorDTO.setLocation("New York");
        anotherDoctorDTO.setDescription("Cardiologist");
        anotherDoctorDTO.setUser(userDTO);

        assertEquals(doctorDTO, anotherDoctorDTO);
        assertEquals(doctorDTO.hashCode(), anotherDoctorDTO.hashCode());
    }

    // Test equals method - Different objects
    @Test
    void testEquals_DifferentObjects() {
        DoctorDTO anotherDoctorDTO = new DoctorDTO();
        anotherDoctorDTO.setId(2L);
        anotherDoctorDTO.setName("Dr. Johnson");
        anotherDoctorDTO.setPhone("0987654321");
        anotherDoctorDTO.setGender(Gender.FEMALE);
        anotherDoctorDTO.setDepartment(Department.ORTHOPEDICS);
        anotherDoctorDTO.setLocation("Los Angeles");
        anotherDoctorDTO.setDescription("Orthopedist");
        anotherDoctorDTO.setUser(userDTO);

        assertNotEquals(doctorDTO, anotherDoctorDTO);
    }

    // Test equals method - Null object
    @Test
    void testEquals_NullObject() {
        assertFalse(doctorDTO.equals(null));
    }

    // Test equals method - Different class
    @Test
    void testEquals_DifferentClass() {
        assertFalse(doctorDTO.equals("not a doctor DTO"));
    }

    // Test hashCode consistency
    @Test
    void testHashCodeConsistency() {
        int initialHashCode = doctorDTO.hashCode();
        assertEquals(initialHashCode, doctorDTO.hashCode());
    }

    // Test hashCode with null values
    @Test
    void testHashCodeWithNullValues() {
        DoctorDTO nullDoctorDTO = new DoctorDTO();
        // Should not throw exception
        assertNotNull(nullDoctorDTO.hashCode());
    }

    // Test doctor DTO with all valid data
    @Test
    void testDoctorDTOWithAllValidData() {
        assertNotNull(doctorDTO.getId());
        assertNotNull(doctorDTO.getName());
        assertNotNull(doctorDTO.getPhone());
        assertNotNull(doctorDTO.getGender());
        assertNotNull(doctorDTO.getDepartment());
        assertNotNull(doctorDTO.getLocation());
        assertNotNull(doctorDTO.getDescription());
        assertNotNull(doctorDTO.getUser());

        assertEquals("Dr. Smith", doctorDTO.getName());
        assertEquals("1234567890", doctorDTO.getPhone());
        assertEquals(Gender.MALE, doctorDTO.getGender());
        assertEquals(Department.CARDIOLOGY, doctorDTO.getDepartment());
        assertEquals("New York", doctorDTO.getLocation());
        assertEquals("Cardiologist", doctorDTO.getDescription());
        assertEquals(userDTO, doctorDTO.getUser());
    }

    // Test doctor DTO with minimum valid data
    @Test
    void testDoctorDTOWithMinimumValidData() {
        DoctorDTO minimalDoctorDTO = new DoctorDTO();
        minimalDoctorDTO.setName("Dr. A");
        minimalDoctorDTO.setPhone("1");
        minimalDoctorDTO.setGender(Gender.MALE);
        minimalDoctorDTO.setDepartment(Department.GENERAL);

        assertEquals("Dr. A", minimalDoctorDTO.getName());
        assertEquals("1", minimalDoctorDTO.getPhone());
        assertEquals(Gender.MALE, minimalDoctorDTO.getGender());
        assertEquals(Department.GENERAL, minimalDoctorDTO.getDepartment());
    }

    // Test doctor DTO with maximum valid data
    @Test
    void testDoctorDTOWithMaximumValidData() {
        DoctorDTO maxDoctorDTO = new DoctorDTO();
        String longName = "Dr. " + "A".repeat(250);
        String longPhone = "1".repeat(20);
        String longLocation = "A".repeat(500);
        String longDescription = "A".repeat(1000);

        maxDoctorDTO.setName(longName);
        maxDoctorDTO.setPhone(longPhone);
        maxDoctorDTO.setGender(Gender.FEMALE);
        maxDoctorDTO.setDepartment(Department.PEDIATRICS);
        maxDoctorDTO.setLocation(longLocation);
        maxDoctorDTO.setDescription(longDescription);
        maxDoctorDTO.setUser(userDTO);

        assertEquals(longName, maxDoctorDTO.getName());
        assertEquals(longPhone, maxDoctorDTO.getPhone());
        assertEquals(Gender.FEMALE, maxDoctorDTO.getGender());
        assertEquals(Department.PEDIATRICS, maxDoctorDTO.getDepartment());
        assertEquals(longLocation, maxDoctorDTO.getLocation());
        assertEquals(longDescription, maxDoctorDTO.getDescription());
        assertEquals(userDTO, maxDoctorDTO.getUser());
    }

    // Test doctor DTO with all departments
    @Test
    void testDoctorDTOWithAllDepartments() {
        // Test CARDIOLOGY
        doctorDTO.setDepartment(Department.CARDIOLOGY);
        assertEquals(Department.CARDIOLOGY, doctorDTO.getDepartment());

        // Test ORTHOPEDICS
        doctorDTO.setDepartment(Department.ORTHOPEDICS);
        assertEquals(Department.ORTHOPEDICS, doctorDTO.getDepartment());

        // Test PEDIATRICS
        doctorDTO.setDepartment(Department.PEDIATRICS);
        assertEquals(Department.PEDIATRICS, doctorDTO.getDepartment());

        // Test GENERAL
        doctorDTO.setDepartment(Department.GENERAL);
        assertEquals(Department.GENERAL, doctorDTO.getDepartment());
    }

    // Test doctor DTO with all genders
    @Test
    void testDoctorDTOWithAllGenders() {
        // Test MALE
        doctorDTO.setGender(Gender.MALE);
        assertEquals(Gender.MALE, doctorDTO.getGender());

        // Test FEMALE
        doctorDTO.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, doctorDTO.getGender());

        // Test OTHER
        doctorDTO.setGender(Gender.OTHER);
        assertEquals(Gender.OTHER, doctorDTO.getGender());
    }

    // Test doctor DTO with null user
    @Test
    void testDoctorDTOWithNullUser() {
        doctorDTO.setUser(null);
        assertNull(doctorDTO.getUser());
    }

    // Test doctor DTO with null name
    @Test
    void testDoctorDTOWithNullName() {
        doctorDTO.setName(null);
        assertNull(doctorDTO.getName());
    }

    // Test doctor DTO with empty name
    @Test
    void testDoctorDTOWithEmptyName() {
        doctorDTO.setName("");
        assertEquals("", doctorDTO.getName());
    }

    // Test doctor DTO with null phone
    @Test
    void testDoctorDTOWithNullPhone() {
        doctorDTO.setPhone(null);
        assertNull(doctorDTO.getPhone());
    }

    // Test doctor DTO with empty phone
    @Test
    void testDoctorDTOWithEmptyPhone() {
        doctorDTO.setPhone("");
        assertEquals("", doctorDTO.getPhone());
    }

    // Test doctor DTO with null gender
    @Test
    void testDoctorDTOWithNullGender() {
        doctorDTO.setGender(null);
        assertNull(doctorDTO.getGender());
    }

    // Test doctor DTO with null department
    @Test
    void testDoctorDTOWithNullDepartment() {
        doctorDTO.setDepartment(null);
        assertNull(doctorDTO.getDepartment());
    }

    // Test doctor DTO with null location
    @Test
    void testDoctorDTOWithNullLocation() {
        doctorDTO.setLocation(null);
        assertNull(doctorDTO.getLocation());
    }

    // Test doctor DTO with empty location
    @Test
    void testDoctorDTOWithEmptyLocation() {
        doctorDTO.setLocation("");
        assertEquals("", doctorDTO.getLocation());
    }

    // Test doctor DTO with null description
    @Test
    void testDoctorDTOWithNullDescription() {
        doctorDTO.setDescription(null);
        assertNull(doctorDTO.getDescription());
    }

    // Test doctor DTO with empty description
    @Test
    void testDoctorDTOWithEmptyDescription() {
        doctorDTO.setDescription("");
        assertEquals("", doctorDTO.getDescription());
    }

    // Test doctor DTO with special characters in name
    @Test
    void testDoctorDTOWithSpecialCharactersInName() {
        String specialName = "Dr. John-Doe O'Connor Jr.";
        doctorDTO.setName(specialName);
        assertEquals(specialName, doctorDTO.getName());
    }

    // Test doctor DTO with phone number containing special characters
    @Test
    void testDoctorDTOWithPhoneContainingSpecialCharacters() {
        String phoneWithSpecialChars = "+1 (555) 123-4567 ext. 123";
        doctorDTO.setPhone(phoneWithSpecialChars);
        assertEquals(phoneWithSpecialChars, doctorDTO.getPhone());
    }

    // Test doctor DTO with international phone number
    @Test
    void testDoctorDTOWithInternationalPhoneNumber() {
        String internationalPhone = "+44 20 7946 0958";
        doctorDTO.setPhone(internationalPhone);
        assertEquals(internationalPhone, doctorDTO.getPhone());
    }

    // Test doctor DTO with complex location
    @Test
    void testDoctorDTOWithComplexLocation() {
        String complexLocation = "123 Main St, Suite 456, New York, NY 10001, USA";
        doctorDTO.setLocation(complexLocation);
        assertEquals(complexLocation, doctorDTO.getLocation());
    }

    // Test doctor DTO with detailed description
    @Test
    void testDoctorDTOWithDetailedDescription() {
        String detailedDescription = "Board-certified cardiologist with 15+ years of experience. " +
                "Specializes in interventional cardiology and heart disease prevention. " +
                "Graduated from Harvard Medical School, completed residency at Johns Hopkins.";
        doctorDTO.setDescription(detailedDescription);
        assertEquals(detailedDescription, doctorDTO.getDescription());
    }

    // Test doctor DTO equals with different IDs
    @Test
    void testDoctorDTOEqualsWithDifferentIds() {
        DoctorDTO doctorDTO1 = new DoctorDTO();
        doctorDTO1.setId(1L);

        DoctorDTO doctorDTO2 = new DoctorDTO();
        doctorDTO2.setId(2L);

        assertNotEquals(doctorDTO1, doctorDTO2);
    }

    // Test doctor DTO equals with null ID
    @Test
    void testDoctorDTOEqualsWithNullId() {
        DoctorDTO doctorDTO1 = new DoctorDTO();
        doctorDTO1.setId(null);

        DoctorDTO doctorDTO2 = new DoctorDTO();
        doctorDTO2.setId(null);

        // Both have null IDs, should be equal based on other fields
        assertNotEquals(doctorDTO1, doctorDTO2); // Since other fields are different
    }

    // Test doctor DTO with same ID but different fields
    @Test
    void testDoctorDTOWithSameIdButDifferentFields() {
        DoctorDTO doctorDTO1 = new DoctorDTO();
        doctorDTO1.setId(1L);
        doctorDTO1.setName("Dr. Smith");

        DoctorDTO doctorDTO2 = new DoctorDTO();
        doctorDTO2.setId(1L);
        doctorDTO2.setName("Dr. Johnson");

        // If equals is based on ID only, they should be equal
        // If equals is based on all fields, they should be different
        // This test assumes ID-based equality (common in DTOs)
        assertEquals(doctorDTO1, doctorDTO2);
    }

    // Test doctor DTO with different titles in name
    @Test
    void testDoctorDTOWithDifferentTitlesInName() {
        // Test with Dr. prefix
        doctorDTO.setName("Dr. John Smith");
        assertEquals("Dr. John Smith", doctorDTO.getName());

        // Test with Prof. prefix
        doctorDTO.setName("Prof. Jane Doe");
        assertEquals("Prof. Jane Doe", doctorDTO.getName());

        // Test with multiple titles
        doctorDTO.setName("Dr. Prof. Robert Johnson");
        assertEquals("Dr. Prof. Robert Johnson", doctorDTO.getName());

        // Test without title
        doctorDTO.setName("Alice Williams");
        assertEquals("Alice Williams", doctorDTO.getName());
    }

    // Test doctor DTO with various location formats
    @Test
    void testDoctorDTOWithVariousLocationFormats() {
        // Test simple city
        doctorDTO.setLocation("New York");
        assertEquals("New York", doctorDTO.getLocation());

        // Test city with state
        doctorDTO.setLocation("Boston, MA");
        assertEquals("Boston, MA", doctorDTO.getLocation());

        // Test full address
        doctorDTO.setLocation("456 Oak Ave, Los Angeles, CA 90001");
        assertEquals("456 Oak Ave, Los Angeles, CA 90001", doctorDTO.getLocation());

        // Test hospital name
        doctorDTO.setLocation("Mayo Clinic, Rochester");
        assertEquals("Mayo Clinic, Rochester", doctorDTO.getLocation());

        // Test with department
        doctorDTO.setLocation("St. Mary's Hospital, Cardiology Department");
        assertEquals("St. Mary's Hospital, Cardiology Department", doctorDTO.getLocation());
    }

    // Test doctor DTO with various description lengths
    @Test
    void testDoctorDTOWithVariousDescriptionLengths() {
        // Test short description
        doctorDTO.setDescription("General practitioner");
        assertEquals("General practitioner", doctorDTO.getDescription());

        // Test medium description
        doctorDTO.setDescription("Experienced cardiologist specializing in heart diseases and preventive care");
        assertEquals("Experienced cardiologist specializing in heart diseases and preventive care", doctorDTO.getDescription());

        // Test very long description
        String longDescription = "A highly skilled and experienced medical professional with extensive expertise in the field of cardiology. " +
                "Completed medical education at prestigious institutions and has been practicing for over two decades. " +
                "Known for compassionate patient care and innovative treatment approaches. " +
                "Published numerous research papers and actively involved in clinical trials. " +
                "Dedicated to advancing medical knowledge and improving patient outcomes.";
        doctorDTO.setDescription(longDescription);
        assertEquals(longDescription, doctorDTO.getDescription());
    }

    // Test doctor DTO with user having different roles
    @Test
    void testDoctorDTOWithUserHavingDifferentRoles() {
        // Test DOCTOR role
        userDTO.setRole(Role.DOCTOR);
        doctorDTO.setUser(userDTO);
        assertEquals(Role.DOCTOR, doctorDTO.getUser().getRole());

        // Test with different role (though unlikely for doctor)
        userDTO.setRole(Role.PATIENT);
        doctorDTO.setUser(userDTO);
        assertEquals(Role.PATIENT, doctorDTO.getUser().getRole());
    }

    // Test doctor DTO with user having different fields
    @Test
    void testDoctorDTOWithUserHavingDifferentFields() {
        UserDTO differentUser = new UserDTO();
        differentUser.setId(3L);
        differentUser.setEmail("different@test.com");
        differentUser.setPassword("differentpassword");
        differentUser.setRole(Role.DOCTOR);
        differentUser.setCreatedAt(java.time.LocalDate.now().minusYears(1));

        doctorDTO.setUser(differentUser);
        assertEquals(differentUser, doctorDTO.getUser());
        assertEquals("different@test.com", doctorDTO.getUser().getEmail());
        assertEquals("differentpassword", doctorDTO.getUser().getPassword());
        assertEquals(Role.DOCTOR, doctorDTO.getUser().getRole());
    }

    // Test doctor DTO with user having null fields
    @Test
    void testDoctorDTOWithUserHavingNullFields() {
        UserDTO userWithNulls = new UserDTO();
        userWithNulls.setId(null);
        userWithNulls.setEmail(null);
        userWithNulls.setPassword(null);
        userWithNulls.setRole(null);
        userWithNulls.setCreatedAt(null);

        doctorDTO.setUser(userWithNulls);
        assertEquals(userWithNulls, doctorDTO.getUser());
        assertNull(doctorDTO.getUser().getId());
        assertNull(doctorDTO.getUser().getEmail());
        assertNull(doctorDTO.getUser().getPassword());
        assertNull(doctorDTO.getUser().getRole());
        assertNull(doctorDTO.getUser().getCreatedAt());
    }

    // Test doctor DTO with user having empty fields
    @Test
    void testDoctorDTOWithUserHavingEmptyFields() {
        UserDTO userWithEmpties = new UserDTO();
        userWithEmpties.setId(1L);
        userWithEmpties.setEmail("");
        userWithEmpties.setPassword("");
        userWithEmpties.setRole(Role.DOCTOR);
        userWithEmpties.setCreatedAt(java.time.LocalDate.now());

        doctorDTO.setUser(userWithEmpties);
        assertEquals(userWithEmpties, doctorDTO.getUser());
        assertEquals("", doctorDTO.getUser().getEmail());
        assertEquals("", doctorDTO.getUser().getPassword());
        assertEquals(Role.DOCTOR, doctorDTO.getUser().getRole());
    }

    // Test doctor DTO with very long user password
    @Test
    void testDoctorDTOWithVeryLongUserPassword() {
        String longPassword = "A".repeat(100) + "1!" + "a".repeat(50);
        userDTO.setPassword(longPassword);
        doctorDTO.setUser(userDTO);
        assertEquals(longPassword, doctorDTO.getUser().getPassword());
    }

    // Test doctor DTO with user created at different dates
    @Test
    void testDoctorDTOWithUserCreatedAtDifferentDates() {
        // Today
        java.time.LocalDate today = java.time.LocalDate.now();
        userDTO.setCreatedAt(today);
        doctorDTO.setUser(userDTO);
        assertEquals(today, doctorDTO.getUser().getCreatedAt());

        // Yesterday
        java.time.LocalDate yesterday = today.minusDays(1);
        userDTO.setCreatedAt(yesterday);
        doctorDTO.setUser(userDTO);
        assertEquals(yesterday, doctorDTO.getUser().getCreatedAt());

        // Last week
        java.time.LocalDate lastWeek = today.minusWeeks(1);
        userDTO.setCreatedAt(lastWeek);
        doctorDTO.setUser(userDTO);
        assertEquals(lastWeek, doctorDTO.getUser().getCreatedAt());

        // Last year
        java.time.LocalDate lastYear = today.minusYears(1);
        userDTO.setCreatedAt(lastYear);
        doctorDTO.setUser(userDTO);
        assertEquals(lastYear, doctorDTO.getUser().getCreatedAt());
    }
}
