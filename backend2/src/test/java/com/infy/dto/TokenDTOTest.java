package com.infy.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.models.Gender;
import com.infy.models.Role;
import com.infy.models.TokenStatus;

@ExtendWith(MockitoExtension.class)
class TokenDTOTest {

    private TokenDTO tokenDTO;
    private DoctorDTO doctorDTO;
    private PatientDTO patientDTO;
    private UserDTO doctorUserDTO;
    private UserDTO patientUserDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        doctorUserDTO = new UserDTO();
        doctorUserDTO.setId(1L);
        doctorUserDTO.setEmail("doctor@test.com");
        doctorUserDTO.setPassword("password");
        doctorUserDTO.setRole(Role.DOCTOR);
        doctorUserDTO.setCreatedAt(LocalDate.now());

        patientUserDTO = new UserDTO();
        patientUserDTO.setId(2L);
        patientUserDTO.setEmail("patient@test.com");
        patientUserDTO.setPassword("password");
        patientUserDTO.setRole(Role.PATIENT);
        patientUserDTO.setCreatedAt(LocalDate.now());

        doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setName("Dr. Smith");
        doctorDTO.setPhone("1234567890");
        doctorDTO.setGender(Gender.MALE);
        doctorDTO.setDepartment(com.infy.models.Department.CARDIOLOGY);
        doctorDTO.setLocation("New York");
        doctorDTO.setDescription("Cardiologist");
        doctorDTO.setUser(doctorUserDTO);

        patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setName("John Doe");
        patientDTO.setPhone("9876543210");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setGender(Gender.MALE);
        patientDTO.setUser(patientUserDTO);

        tokenDTO = new TokenDTO();
        tokenDTO.setId(1L);
        tokenDTO.setDoctor(doctorDTO);
        tokenDTO.setPatient(patientDTO);
        tokenDTO.setTokenNumber(1);
        tokenDTO.setDate(LocalDate.now());
        tokenDTO.setStatus(TokenStatus.WAITING);
        tokenDTO.setCheckInTime(LocalDateTime.now());
    }

    // Test getters and setters
    @Test
    void testGettersAndSetters() {
        // Test ID
        tokenDTO.setId(2L);
        assertEquals(2L, tokenDTO.getId());

        // Test Doctor
        DoctorDTO newDoctorDTO = new DoctorDTO();
        newDoctorDTO.setId(2L);
        newDoctorDTO.setName("Dr. Johnson");
        tokenDTO.setDoctor(newDoctorDTO);
        assertEquals(newDoctorDTO, tokenDTO.getDoctor());
        assertEquals("Dr. Johnson", tokenDTO.getDoctor().getName());

        // Test Patient
        PatientDTO newPatientDTO = new PatientDTO();
        newPatientDTO.setId(2L);
        newPatientDTO.setName("Jane Smith");
        tokenDTO.setPatient(newPatientDTO);
        assertEquals(newPatientDTO, tokenDTO.getPatient());
        assertEquals("Jane Smith", tokenDTO.getPatient().getName());

        // Test Token Number
        tokenDTO.setTokenNumber(5);
        assertEquals(5, tokenDTO.getTokenNumber());

        // Test Date
        LocalDate newDate = LocalDate.now().plusDays(1);
        tokenDTO.setDate(newDate);
        assertEquals(newDate, tokenDTO.getDate());

        // Test Status
        tokenDTO.setStatus(TokenStatus.IN_CONSULTATION);
        assertEquals(TokenStatus.IN_CONSULTATION, tokenDTO.getStatus());

        // Test Check-in Time
        LocalDateTime newCheckInTime = LocalDateTime.now().plusHours(1);
        tokenDTO.setCheckInTime(newCheckInTime);
        assertEquals(newCheckInTime, tokenDTO.getCheckInTime());
    }

    // Test default constructor
    @Test
    void testDefaultConstructor() {
        TokenDTO newTokenDTO = new TokenDTO();
        assertNull(newTokenDTO.getId());
        assertNull(newTokenDTO.getDoctor());
        assertNull(newTokenDTO.getPatient());
        assertEquals(0, newTokenDTO.getTokenNumber());
        assertNull(newTokenDTO.getDate());
        assertNull(newTokenDTO.getStatus());
        assertNull(newTokenDTO.getCheckInTime());
    }

    // Test toString method
    @Test
    void testToString() {
        String tokenDTOString = tokenDTO.toString();
        assertNotNull(tokenDTOString);
        assertTrue(tokenDTOString.contains("1")); // token number
        assertTrue(tokenDTOString.contains("WAITING"));
    }

    // Test equals method - Same object
    @Test
    void testEquals_SameObject() {
        assertTrue(tokenDTO.equals(tokenDTO));
    }

    // Test equals method - Equal objects
    @Test
    void testEquals_EqualObjects() {
        TokenDTO anotherTokenDTO = new TokenDTO();
        anotherTokenDTO.setId(1L);
        anotherTokenDTO.setDoctor(doctorDTO);
        anotherTokenDTO.setPatient(patientDTO);
        anotherTokenDTO.setTokenNumber(1);
        anotherTokenDTO.setDate(LocalDate.now());
        anotherTokenDTO.setStatus(TokenStatus.WAITING);
        anotherTokenDTO.setCheckInTime(LocalDateTime.now());

        assertEquals(tokenDTO, anotherTokenDTO);
        assertEquals(tokenDTO.hashCode(), anotherTokenDTO.hashCode());
    }

    // Test equals method - Different objects
    @Test
    void testEquals_DifferentObjects() {
        TokenDTO anotherTokenDTO = new TokenDTO();
        anotherTokenDTO.setId(2L);
        anotherTokenDTO.setDoctor(doctorDTO);
        anotherTokenDTO.setPatient(patientDTO);
        anotherTokenDTO.setTokenNumber(2);
        anotherTokenDTO.setDate(LocalDate.now());
        anotherTokenDTO.setStatus(TokenStatus.IN_CONSULTATION);
        anotherTokenDTO.setCheckInTime(LocalDateTime.now().plusMinutes(30));

        assertNotEquals(tokenDTO, anotherTokenDTO);
    }

    // Test equals method - Null object
    @Test
    void testEquals_NullObject() {
        assertFalse(tokenDTO.equals(null));
    }

    // Test equals method - Different class
    @Test
    void testEquals_DifferentClass() {
        assertFalse(tokenDTO.equals("not a token DTO"));
    }

    // Test hashCode consistency
    @Test
    void testHashCodeConsistency() {
        int initialHashCode = tokenDTO.hashCode();
        assertEquals(initialHashCode, tokenDTO.hashCode());
    }

    // Test hashCode with null values
    @Test
    void testHashCodeWithNullValues() {
        TokenDTO nullTokenDTO = new TokenDTO();
        // Should not throw exception
        assertNotNull(nullTokenDTO.hashCode());
    }

    // Test token DTO with all valid data
    @Test
    void testTokenDTOWithAllValidData() {
        assertNotNull(tokenDTO.getId());
        assertNotNull(tokenDTO.getDoctor());
        assertNotNull(tokenDTO.getPatient());
        assertTrue(tokenDTO.getTokenNumber() > 0);
        assertNotNull(tokenDTO.getDate());
        assertNotNull(tokenDTO.getStatus());
        assertNotNull(tokenDTO.getCheckInTime());

        assertEquals(1, tokenDTO.getTokenNumber());
        assertEquals(TokenStatus.WAITING, tokenDTO.getStatus());
        assertEquals(doctorDTO, tokenDTO.getDoctor());
        assertEquals(patientDTO, tokenDTO.getPatient());
    }

    // Test token DTO with minimum valid data
    @Test
    void testTokenDTOWithMinimumValidData() {
        TokenDTO minimalTokenDTO = new TokenDTO();
        minimalTokenDTO.setTokenNumber(1);
        minimalTokenDTO.setDate(LocalDate.now());
        minimalTokenDTO.setStatus(TokenStatus.WAITING);
        minimalTokenDTO.setCheckInTime(LocalDateTime.now());

        assertEquals(1, minimalTokenDTO.getTokenNumber());
        assertEquals(LocalDate.now(), minimalTokenDTO.getDate());
        assertEquals(TokenStatus.WAITING, minimalTokenDTO.getStatus());
        assertNotNull(minimalTokenDTO.getCheckInTime());
    }

    // Test token DTO with all statuses
    @Test
    void testTokenDTOWithAllStatuses() {
        // Test WAITING
        tokenDTO.setStatus(TokenStatus.WAITING);
        assertEquals(TokenStatus.WAITING, tokenDTO.getStatus());

        // Test IN_CONSULTATION
        tokenDTO.setStatus(TokenStatus.IN_CONSULTATION);
        assertEquals(TokenStatus.IN_CONSULTATION, tokenDTO.getStatus());

        // Test COMPLETED
        tokenDTO.setStatus(TokenStatus.COMPLETED);
        assertEquals(TokenStatus.COMPLETED, tokenDTO.getStatus());

        // Test CANCELLED
        tokenDTO.setStatus(TokenStatus.CANCELLED);
        assertEquals(TokenStatus.CANCELLED, tokenDTO.getStatus());
    }

    // Test token DTO relationships
    @Test
    void testTokenDTORelationships() {
        // Test doctor relationship
        assertEquals(doctorDTO, tokenDTO.getDoctor());
        assertEquals("Dr. Smith", tokenDTO.getDoctor().getName());

        // Test patient relationship
        assertEquals(patientDTO, tokenDTO.getPatient());
        assertEquals("John Doe", tokenDTO.getPatient().getName());
    }

    // Test token DTO with null doctor
    @Test
    void testTokenDTOWithNullDoctor() {
        tokenDTO.setDoctor(null);
        assertNull(tokenDTO.getDoctor());
    }

    // Test token DTO with null patient
    @Test
    void testTokenDTOWithNullPatient() {
        tokenDTO.setPatient(null);
        assertNull(tokenDTO.getPatient());
    }

    // Test token DTO with null date
    @Test
    void testTokenDTOWithNullDate() {
        tokenDTO.setDate(null);
        assertNull(tokenDTO.getDate());
    }

    // Test token DTO with null status
    @Test
    void testTokenDTOWithNullStatus() {
        tokenDTO.setStatus(null);
        assertNull(tokenDTO.getStatus());
    }

    // Test token DTO with null check-in time
    @Test
    void testTokenDTOWithNullCheckInTime() {
        tokenDTO.setCheckInTime(null);
        assertNull(tokenDTO.getCheckInTime());
    }

    // Test token DTO with zero token number
    @Test
    void testTokenDTOWithZeroTokenNumber() {
        tokenDTO.setTokenNumber(0);
        assertEquals(0, tokenDTO.getTokenNumber());
    }

    // Test token DTO with negative token number
    @Test
    void testTokenDTOWithNegativeTokenNumber() {
        tokenDTO.setTokenNumber(-1);
        assertEquals(-1, tokenDTO.getTokenNumber());
    }

    // Test token DTO with very large token number
    @Test
    void testTokenDTOWithVeryLargeTokenNumber() {
        tokenDTO.setTokenNumber(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, tokenDTO.getTokenNumber());
    }

    // Test token DTO with different dates
    @Test
    void testTokenDTOWithDifferentDates() {
        // Test today
        LocalDate today = LocalDate.now();
        tokenDTO.setDate(today);
        assertEquals(today, tokenDTO.getDate());

        // Test yesterday
        LocalDate yesterday = today.minusDays(1);
        tokenDTO.setDate(yesterday);
        assertEquals(yesterday, tokenDTO.getDate());

        // Test tomorrow
        LocalDate tomorrow = today.plusDays(1);
        tokenDTO.setDate(tomorrow);
        assertEquals(tomorrow, tokenDTO.getDate());

        // Test future date
        LocalDate futureDate = today.plusWeeks(2);
        tokenDTO.setDate(futureDate);
        assertEquals(futureDate, tokenDTO.getDate());

        // Test past date
        LocalDate pastDate = today.minusMonths(1);
        tokenDTO.setDate(pastDate);
        assertEquals(pastDate, tokenDTO.getDate());
    }

    // Test token DTO with different check-in times
    @Test
    void testTokenDTOWithDifferentCheckInTimes() {
        // Test current time
        LocalDateTime now = LocalDateTime.now();
        tokenDTO.setCheckInTime(now);
        assertEquals(now, tokenDTO.getCheckInTime());

        // Test morning time
        LocalDateTime morning = LocalDate.now().atTime(9, 0);
        tokenDTO.setCheckInTime(morning);
        assertEquals(morning, tokenDTO.getCheckInTime());

        // Test afternoon time
        LocalDateTime afternoon = LocalDate.now().atTime(14, 30);
        tokenDTO.setCheckInTime(afternoon);
        assertEquals(afternoon, tokenDTO.getCheckInTime());

        // Test evening time
        LocalDateTime evening = LocalDate.now().atTime(18, 45);
        tokenDTO.setCheckInTime(evening);
        assertEquals(evening, tokenDTO.getCheckInTime());

        // Test midnight
        LocalDateTime midnight = LocalDate.now().atTime(0, 0);
        tokenDTO.setCheckInTime(midnight);
        assertEquals(midnight, tokenDTO.getCheckInTime());
    }

    // Test token DTO status transitions
    @Test
    void testTokenDTOStatusTransitions() {
        // Initial status
        tokenDTO.setStatus(TokenStatus.WAITING);
        assertEquals(TokenStatus.WAITING, tokenDTO.getStatus());

        // Start consultation
        tokenDTO.setStatus(TokenStatus.IN_CONSULTATION);
        assertEquals(TokenStatus.IN_CONSULTATION, tokenDTO.getStatus());

        // Complete consultation
        tokenDTO.setStatus(TokenStatus.COMPLETED);
        assertEquals(TokenStatus.COMPLETED, tokenDTO.getStatus());

        // Cancel (can be cancelled from any state)
        tokenDTO.setStatus(TokenStatus.CANCELLED);
        assertEquals(TokenStatus.CANCELLED, tokenDTO.getStatus());

        // Back to waiting (for rescheduling)
        tokenDTO.setStatus(TokenStatus.WAITING);
        assertEquals(TokenStatus.WAITING, tokenDTO.getStatus());
    }

    // Test token DTO with sequential token numbers
    @Test
    void testTokenDTOWithSequentialTokenNumbers() {
        // Test multiple tokens with sequential numbers
        for (int i = 1; i <= 10; i++) {
            TokenDTO sequentialTokenDTO = new TokenDTO();
            sequentialTokenDTO.setTokenNumber(i);
            sequentialTokenDTO.setDate(LocalDate.now());
            sequentialTokenDTO.setStatus(TokenStatus.WAITING);
            sequentialTokenDTO.setCheckInTime(LocalDateTime.now());

            assertEquals(i, sequentialTokenDTO.getTokenNumber());
        }
    }

    // Test token DTO with different doctors
    @Test
    void testTokenDTOWithDifferentDoctors() {
        DoctorDTO cardiologist = new DoctorDTO();
        cardiologist.setId(1L);
        cardiologist.setName("Dr. Heart");
        cardiologist.setDepartment(com.infy.models.Department.CARDIOLOGY);

        DoctorDTO orthopedist = new DoctorDTO();
        orthopedist.setId(2L);
        orthopedist.setName("Dr. Bone");
        orthopedist.setDepartment(com.infy.models.Department.ORTHOPEDICS);

        tokenDTO.setDoctor(cardiologist);
        assertEquals(cardiologist, tokenDTO.getDoctor());
        assertEquals(com.infy.models.Department.CARDIOLOGY, tokenDTO.getDoctor().getDepartment());

        tokenDTO.setDoctor(orthopedist);
        assertEquals(orthopedist, tokenDTO.getDoctor());
        assertEquals(com.infy.models.Department.ORTHOPEDICS, tokenDTO.getDoctor().getDepartment());
    }

    // Test token DTO with different patients
    @Test
    void testTokenDTOWithDifferentPatients() {
        PatientDTO patient1 = new PatientDTO();
        patient1.setId(1L);
        patient1.setName("John Doe");

        PatientDTO patient2 = new PatientDTO();
        patient2.setId(2L);
        patient2.setName("Jane Smith");

        tokenDTO.setPatient(patient1);
        assertEquals(patient1, tokenDTO.getPatient());
        assertEquals("John Doe", tokenDTO.getPatient().getName());

        tokenDTO.setPatient(patient2);
        assertEquals(patient2, tokenDTO.getPatient());
        assertEquals("Jane Smith", tokenDTO.getPatient().getName());
    }

    // Test token DTO equals with different IDs
    @Test
    void testTokenDTOEqualsWithDifferentIds() {
        TokenDTO tokenDTO1 = new TokenDTO();
        tokenDTO1.setId(1L);

        TokenDTO tokenDTO2 = new TokenDTO();
        tokenDTO2.setId(2L);

        assertNotEquals(tokenDTO1, tokenDTO2);
    }

    // Test token DTO equals with null ID
    @Test
    void testTokenDTOEqualsWithNullId() {
        TokenDTO tokenDTO1 = new TokenDTO();
        tokenDTO1.setId(null);

        TokenDTO tokenDTO2 = new TokenDTO();
        tokenDTO2.setId(null);

        // Both have null IDs, should be equal based on other fields
        assertNotEquals(tokenDTO1, tokenDTO2); // Since other fields are different
    }

    // Test token DTO with same ID but different fields
    @Test
    void testTokenDTOWithSameIdButDifferentFields() {
        TokenDTO tokenDTO1 = new TokenDTO();
        tokenDTO1.setId(1L);
        tokenDTO1.setTokenNumber(1);

        TokenDTO tokenDTO2 = new TokenDTO();
        tokenDTO2.setId(1L);
        tokenDTO2.setTokenNumber(2);

        // If equals is based on ID only, they should be equal
        // If equals is based on all fields, they should be different
        // This test assumes ID-based equality (common in DTOs)
        assertEquals(tokenDTO1, tokenDTO2);
    }

    // Test token DTO with same doctor and date
    @Test
    void testTokenDTOWithSameDoctorAndDate() {
        LocalDate today = LocalDate.now();
        TokenDTO tokenDTO1 = new TokenDTO();
        tokenDTO1.setDoctor(doctorDTO);
        tokenDTO1.setTokenNumber(1);
        tokenDTO1.setDate(today);
        tokenDTO1.setStatus(TokenStatus.WAITING);
        tokenDTO1.setCheckInTime(LocalDateTime.now());

        TokenDTO tokenDTO2 = new TokenDTO();
        tokenDTO2.setDoctor(doctorDTO);
        tokenDTO2.setTokenNumber(2);
        tokenDTO2.setDate(today);
        tokenDTO2.setStatus(TokenStatus.WAITING);
        tokenDTO2.setCheckInTime(LocalDateTime.now().plusMinutes(5));

        assertEquals(doctorDTO, tokenDTO1.getDoctor());
        assertEquals(doctorDTO, tokenDTO2.getDoctor());
        assertEquals(today, tokenDTO1.getDate());
        assertEquals(today, tokenDTO2.getDate());
        assertNotEquals(tokenDTO1.getTokenNumber(), tokenDTO2.getTokenNumber());
    }

    // Test token DTO with very early check-in time
    @Test
    void testTokenDTOWithVeryEarlyCheckInTime() {
        LocalDateTime earlyTime = LocalDate.now().atTime(6, 0);
        tokenDTO.setCheckInTime(earlyTime);
        assertEquals(earlyTime, tokenDTO.getCheckInTime());
        assertEquals(6, tokenDTO.getCheckInTime().getHour());
    }

    // Test token DTO with very late check-in time
    @Test
    void testTokenDTOWithVeryLateCheckInTime() {
        LocalDateTime lateTime = LocalDate.now().atTime(23, 59);
        tokenDTO.setCheckInTime(lateTime);
        assertEquals(lateTime, tokenDTO.getCheckInTime());
        assertEquals(23, tokenDTO.getCheckInTime().getHour());
        assertEquals(59, tokenDTO.getCheckInTime().getMinute());
    }

    // Test token DTO with doctor having different departments
    @Test
    void testTokenDTOWithDoctorHavingDifferentDepartments() {
        // Test CARDIOLOGY
        doctorDTO.setDepartment(com.infy.models.Department.CARDIOLOGY);
        tokenDTO.setDoctor(doctorDTO);
        assertEquals(com.infy.models.Department.CARDIOLOGY, tokenDTO.getDoctor().getDepartment());

        // Test ORTHOPEDICS
        doctorDTO.setDepartment(com.infy.models.Department.ORTHOPEDICS);
        tokenDTO.setDoctor(doctorDTO);
        assertEquals(com.infy.models.Department.ORTHOPEDICS, tokenDTO.getDoctor().getDepartment());

        // Test PEDIATRICS
        doctorDTO.setDepartment(com.infy.models.Department.PEDIATRICS);
        tokenDTO.setDoctor(doctorDTO);
        assertEquals(com.infy.models.Department.PEDIATRICS, tokenDTO.getDoctor().getDepartment());

        // Test GENERAL
        doctorDTO.setDepartment(com.infy.models.Department.GENERAL);
        tokenDTO.setDoctor(doctorDTO);
        assertEquals(com.infy.models.Department.GENERAL, tokenDTO.getDoctor().getDepartment());
    }

    // Test token DTO with patient having different genders
    @Test
    void testTokenDTOWithPatientHavingDifferentGenders() {
        // Test MALE
        patientDTO.setGender(Gender.MALE);
        tokenDTO.setPatient(patientDTO);
        assertEquals(Gender.MALE, tokenDTO.getPatient().getGender());

        // Test FEMALE
        patientDTO.setGender(Gender.FEMALE);
        tokenDTO.setPatient(patientDTO);
        assertEquals(Gender.FEMALE, tokenDTO.getPatient().getGender());

        // Test OTHER
        patientDTO.setGender(Gender.OTHER);
        tokenDTO.setPatient(patientDTO);
        assertEquals(Gender.OTHER, tokenDTO.getPatient().getGender());
    }

    // Test token DTO with doctor and patient having null users
    @Test
    void testTokenDTOWithDoctorAndPatientHavingNullUsers() {
        doctorDTO.setUser(null);
        patientDTO.setUser(null);

        tokenDTO.setDoctor(doctorDTO);
        tokenDTO.setPatient(patientDTO);

        assertNull(tokenDTO.getDoctor().getUser());
        assertNull(tokenDTO.getPatient().getUser());
    }

    // Test token DTO with doctor and patient having empty user fields
    @Test
    void testTokenDTOWithDoctorAndPatientHavingEmptyUserFields() {
        UserDTO emptyUser = new UserDTO();
        emptyUser.setId(1L);
        emptyUser.setEmail("");
        emptyUser.setPassword("");
        emptyUser.setRole(Role.DOCTOR);
        emptyUser.setCreatedAt(LocalDate.now());

        doctorDTO.setUser(emptyUser);
        patientDTO.setUser(emptyUser);

        tokenDTO.setDoctor(doctorDTO);
        tokenDTO.setPatient(patientDTO);

        assertEquals("", tokenDTO.getDoctor().getUser().getEmail());
        assertEquals("", tokenDTO.getPatient().getUser().getEmail());
    }

    // Test token DTO with very large token numbers
    @Test
    void testTokenDTOWithVeryLargeTokenNumbers() {
        // Test with maximum integer value
        tokenDTO.setTokenNumber(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, tokenDTO.getTokenNumber());

        // Test with minimum integer value
        tokenDTO.setTokenNumber(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, tokenDTO.getTokenNumber());
    }

    // Test token DTO with same date but different times
    @Test
    void testTokenDTOWithSameDateButDifferentTimes() {
        LocalDate sameDate = LocalDate.now();
        LocalDateTime morningTime = sameDate.atTime(9, 0);
        LocalDateTime afternoonTime = sameDate.atTime(15, 30);

        TokenDTO morningToken = new TokenDTO();
        morningToken.setDate(sameDate);
        morningToken.setCheckInTime(morningTime);

        TokenDTO afternoonToken = new TokenDTO();
        afternoonToken.setDate(sameDate);
        afternoonToken.setCheckInTime(afternoonTime);

        assertEquals(sameDate, morningToken.getDate());
        assertEquals(sameDate, afternoonToken.getDate());
        assertNotEquals(morningToken.getCheckInTime(), afternoonToken.getCheckInTime());
    }
}
