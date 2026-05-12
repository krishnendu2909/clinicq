package com.infy.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.models.Gender;
import com.infy.models.Role;
import com.infy.models.TokenStatus;

@ExtendWith(MockitoExtension.class)
class TokenTest {

    private Token token;
    private Doctor doctor;
    private Patient patient;
    private Appointment appointment;
    private User doctorUser;
    private User patientUser;

    @BeforeEach
    void setUp() {
        // Setup test data
        doctorUser = new User();
        doctorUser.setId(1L);
        doctorUser.setEmail("doctor@test.com");
        doctorUser.setPassword("password");
        doctorUser.setRole(Role.DOCTOR);
        doctorUser.setCreatedAt(LocalDate.now());

        patientUser = new User();
        patientUser.setId(2L);
        patientUser.setEmail("patient@test.com");
        patientUser.setPassword("password");
        patientUser.setRole(Role.PATIENT);
        patientUser.setCreatedAt(LocalDate.now());

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        doctor.setPhone("1234567890");
        doctor.setGender(com.infy.models.Gender.MALE);
        doctor.setDepartment(com.infy.models.Department.CARDIOLOGY);
        doctor.setLocation("New York");
        doctor.setDescription("Cardiologist");
        doctor.setUser(doctorUser);

        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setPhone("9876543210");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(com.infy.models.Gender.MALE);
        patient.setUser(patientUser);

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setDoctor(doctor);
        timeSlot.setSlotDate(LocalDate.now().plusDays(1));
        timeSlot.setStartTime(LocalTime.of(10, 0));
        timeSlot.setEndTime(LocalTime.of(10, 30));
        timeSlot.setBooked(false);

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setTimeSlot(timeSlot);
        appointment.setReason("Regular checkup");
        appointment.setStatus(com.infy.models.AppointmentStatus.BOOKED);
        appointment.setType(com.infy.models.AppointmentType.PRE_BOOKED);

        token = new Token();
        token.setId(1L);
        token.setDoctor(doctor);
        token.setPatient(patient);
        token.setTokenNumber(1);
        token.setDate(LocalDate.now());
        token.setStatus(TokenStatus.WAITING);
        token.setCheckInTime(LocalDateTime.now());
        token.setAppointment(appointment);
    }

    // Test getters and setters
    @Test
    void testGettersAndSetters() {
        // Test ID
        token.setId(2L);
        assertEquals(2L, token.getId());

        // Test Doctor
        Doctor newDoctor = new Doctor();
        newDoctor.setId(2L);
        newDoctor.setName("Dr. Johnson");
        token.setDoctor(newDoctor);
        assertEquals(newDoctor, token.getDoctor());
        assertEquals("Dr. Johnson", token.getDoctor().getName());

        // Test Patient
        Patient newPatient = new Patient();
        newPatient.setId(2L);
        newPatient.setName("Jane Smith");
        token.setPatient(newPatient);
        assertEquals(newPatient, token.getPatient());
        assertEquals("Jane Smith", token.getPatient().getName());

        // Test Token Number
        token.setTokenNumber(5);
        assertEquals(5, token.getTokenNumber());

        // Test Date
        LocalDate newDate = LocalDate.now().plusDays(1);
        token.setDate(newDate);
        assertEquals(newDate, token.getDate());

        // Test Status
        token.setStatus(TokenStatus.IN_CONSULTATION);
        assertEquals(TokenStatus.IN_CONSULTATION, token.getStatus());

        // Test Check-in Time
        LocalDateTime newCheckInTime = LocalDateTime.now().plusHours(1);
        token.setCheckInTime(newCheckInTime);
        assertEquals(newCheckInTime, token.getCheckInTime());

        // Test Appointment
        Appointment newAppointment = new Appointment();
        newAppointment.setId(2L);
        newAppointment.setReason("Emergency");
        token.setAppointment(newAppointment);
        assertEquals(newAppointment, token.getAppointment());
        assertEquals("Emergency", token.getAppointment().getReason());
    }

    // Test default constructor
    @Test
    void testDefaultConstructor() {
        Token newToken = new Token();
        assertNull(newToken.getId());
        assertNull(newToken.getDoctor());
        assertNull(newToken.getPatient());
        assertEquals(0, newToken.getTokenNumber());
        assertNull(newToken.getDate());
        assertNull(newToken.getStatus());
        assertNull(newToken.getCheckInTime());
        assertNull(newToken.getAppointment());
    }

    // Test toString method
    @Test
    void testToString() {
        String tokenString = token.toString();
        assertNotNull(tokenString);
        assertTrue(tokenString.contains("1")); // token number
        assertTrue(tokenString.contains("WAITING"));
    }

    // Test equals method - Same object
    @Test
    void testEquals_SameObject() {
        assertTrue(token.equals(token));
    }

    // Test equals method - Equal objects
    @Test
    void testEquals_EqualObjects() {
        Token anotherToken = new Token();
        anotherToken.setId(1L);
        anotherToken.setDoctor(doctor);
        anotherToken.setPatient(patient);
        anotherToken.setTokenNumber(1);
        anotherToken.setDate(LocalDate.now());
        anotherToken.setStatus(TokenStatus.WAITING);
        anotherToken.setCheckInTime(LocalDateTime.now());
        anotherToken.setAppointment(appointment);

        assertEquals(token, anotherToken);
        assertEquals(token.hashCode(), anotherToken.hashCode());
    }

    // Test equals method - Different objects
    @Test
    void testEquals_DifferentObjects() {
        Token anotherToken = new Token();
        anotherToken.setId(2L);
        anotherToken.setDoctor(doctor);
        anotherToken.setPatient(patient);
        anotherToken.setTokenNumber(2);
        anotherToken.setDate(LocalDate.now());
        anotherToken.setStatus(TokenStatus.IN_CONSULTATION);
        anotherToken.setCheckInTime(LocalDateTime.now().plusMinutes(30));
        anotherToken.setAppointment(appointment);

        assertNotEquals(token, anotherToken);
    }

    // Test equals method - Null object
    @Test
    void testEquals_NullObject() {
        assertFalse(token.equals(null));
    }

    // Test equals method - Different class
    @Test
    void testEquals_DifferentClass() {
        assertFalse(token.equals("not a token"));
    }

    // Test hashCode consistency
    @Test
    void testHashCodeConsistency() {
        int initialHashCode = token.hashCode();
        assertEquals(initialHashCode, token.hashCode());
    }

    // Test hashCode with null values
    @Test
    void testHashCodeWithNullValues() {
        Token nullToken = new Token();
        // Should not throw exception
        assertNotNull(nullToken.hashCode());
    }

    // Test token with all valid data
    @Test
    void testTokenWithAllValidData() {
        assertNotNull(token.getId());
        assertNotNull(token.getDoctor());
        assertNotNull(token.getPatient());
        assertTrue(token.getTokenNumber() > 0);
        assertNotNull(token.getDate());
        assertNotNull(token.getStatus());
        assertNotNull(token.getCheckInTime());
        assertNotNull(token.getAppointment());

        assertEquals(1, token.getTokenNumber());
        assertEquals(TokenStatus.WAITING, token.getStatus());
        assertEquals(doctor, token.getDoctor());
        assertEquals(patient, token.getPatient());
        assertEquals(appointment, token.getAppointment());
    }

    // Test token with minimum valid data
    @Test
    void testTokenWithMinimumValidData() {
        Token minimalToken = new Token();
        minimalToken.setTokenNumber(1);
        minimalToken.setDate(LocalDate.now());
        minimalToken.setStatus(TokenStatus.WAITING);
        minimalToken.setCheckInTime(LocalDateTime.now());

        assertEquals(1, minimalToken.getTokenNumber());
        assertEquals(LocalDate.now(), minimalToken.getDate());
        assertEquals(TokenStatus.WAITING, minimalToken.getStatus());
        assertNotNull(minimalToken.getCheckInTime());
    }

    // Test token with all statuses
    @Test
    void testTokenWithAllStatuses() {
        // Test WAITING
        token.setStatus(TokenStatus.WAITING);
        assertEquals(TokenStatus.WAITING, token.getStatus());

        // Test IN_CONSULTATION
        token.setStatus(TokenStatus.IN_CONSULTATION);
        assertEquals(TokenStatus.IN_CONSULTATION, token.getStatus());

        // Test COMPLETED
        token.setStatus(TokenStatus.COMPLETED);
        assertEquals(TokenStatus.COMPLETED, token.getStatus());

        // Test CANCELLED
        token.setStatus(TokenStatus.CANCELLED);
        assertEquals(TokenStatus.CANCELLED, token.getStatus());
    }

    // Test token relationships
    @Test
    void testTokenRelationships() {
        // Test doctor relationship
        assertEquals(doctor, token.getDoctor());
        assertEquals(token, doctor.getTokens().stream()
                .filter(t -> t.getId().equals(token.getId())).findFirst().orElse(null));

        // Test patient relationship
        assertEquals(patient, token.getPatient());
        assertEquals(token, patient.getTokens().stream()
                .filter(t -> t.getId().equals(token.getId())).findFirst().orElse(null));

        // Test appointment relationship
        assertEquals(appointment, token.getAppointment());
        assertEquals(token, appointment.getToken());
    }

    // Test token with null doctor
    @Test
    void testTokenWithNullDoctor() {
        token.setDoctor(null);
        assertNull(token.getDoctor());
    }

    // Test token with null patient
    @Test
    void testTokenWithNullPatient() {
        token.setPatient(null);
        assertNull(token.getPatient());
    }

    // Test token with null date
    @Test
    void testTokenWithNullDate() {
        token.setDate(null);
        assertNull(token.getDate());
    }

    // Test token with null status
    @Test
    void testTokenWithNullStatus() {
        token.setStatus(null);
        assertNull(token.getStatus());
    }

    // Test token with null check-in time
    @Test
    void testTokenWithNullCheckInTime() {
        token.setCheckInTime(null);
        assertNull(token.getCheckInTime());
    }

    // Test token with null appointment
    @Test
    void testTokenWithNullAppointment() {
        token.setAppointment(null);
        assertNull(token.getAppointment());
    }

    // Test token with zero token number
    @Test
    void testTokenWithZeroTokenNumber() {
        token.setTokenNumber(0);
        assertEquals(0, token.getTokenNumber());
    }

    // Test token with negative token number
    @Test
    void testTokenWithNegativeTokenNumber() {
        token.setTokenNumber(-1);
        assertEquals(-1, token.getTokenNumber());
    }

    // Test token with very large token number
    @Test
    void testTokenWithVeryLargeTokenNumber() {
        token.setTokenNumber(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, token.getTokenNumber());
    }

    // Test token with different dates
    @Test
    void testTokenWithDifferentDates() {
        // Test today
        LocalDate today = LocalDate.now();
        token.setDate(today);
        assertEquals(today, token.getDate());

        // Test yesterday
        LocalDate yesterday = today.minusDays(1);
        token.setDate(yesterday);
        assertEquals(yesterday, token.getDate());

        // Test tomorrow
        LocalDate tomorrow = today.plusDays(1);
        token.setDate(tomorrow);
        assertEquals(tomorrow, token.getDate());

        // Test future date
        LocalDate futureDate = today.plusWeeks(2);
        token.setDate(futureDate);
        assertEquals(futureDate, token.getDate());

        // Test past date
        LocalDate pastDate = today.minusMonths(1);
        token.setDate(pastDate);
        assertEquals(pastDate, token.getDate());
    }

    // Test token with different check-in times
    @Test
    void testTokenWithDifferentCheckInTimes() {
        // Test current time
        LocalDateTime now = LocalDateTime.now();
        token.setCheckInTime(now);
        assertEquals(now, token.getCheckInTime());

        // Test morning time
        LocalDateTime morning = LocalDate.now().atTime(9, 0);
        token.setCheckInTime(morning);
        assertEquals(morning, token.getCheckInTime());

        // Test afternoon time
        LocalDateTime afternoon = LocalDate.now().atTime(14, 30);
        token.setCheckInTime(afternoon);
        assertEquals(afternoon, token.getCheckInTime());

        // Test evening time
        LocalDateTime evening = LocalDate.now().atTime(18, 45);
        token.setCheckInTime(evening);
        assertEquals(evening, token.getCheckInTime());

        // Test midnight
        LocalDateTime midnight = LocalDate.now().atTime(0, 0);
        token.setCheckInTime(midnight);
        assertEquals(midnight, token.getCheckInTime());
    }

    // Test token status transitions
    @Test
    void testTokenStatusTransitions() {
        // Initial status
        token.setStatus(TokenStatus.WAITING);
        assertEquals(TokenStatus.WAITING, token.getStatus());

        // Start consultation
        token.setStatus(TokenStatus.IN_CONSULTATION);
        assertEquals(TokenStatus.IN_CONSULTATION, token.getStatus());

        // Complete consultation
        token.setStatus(TokenStatus.COMPLETED);
        assertEquals(TokenStatus.COMPLETED, token.getStatus());

        // Cancel (can be cancelled from any state)
        token.setStatus(TokenStatus.CANCELLED);
        assertEquals(TokenStatus.CANCELLED, token.getStatus());

        // Back to waiting (for rescheduling)
        token.setStatus(TokenStatus.WAITING);
        assertEquals(TokenStatus.WAITING, token.getStatus());
    }

    // Test token with sequential token numbers
    @Test
    void testTokenWithSequentialTokenNumbers() {
        // Test multiple tokens with sequential numbers
        for (int i = 1; i <= 10; i++) {
            Token sequentialToken = new Token();
            sequentialToken.setTokenNumber(i);
            sequentialToken.setDate(LocalDate.now());
            sequentialToken.setStatus(TokenStatus.WAITING);
            sequentialToken.setCheckInTime(LocalDateTime.now());

            assertEquals(i, sequentialToken.getTokenNumber());
        }
    }

    // Test token with different doctors
    @Test
    void testTokenWithDifferentDoctors() {
        Doctor cardiologist = new Doctor();
        cardiologist.setId(1L);
        cardiologist.setName("Dr. Heart");
        cardiologist.setDepartment(com.infy.models.Department.CARDIOLOGY);

        Doctor orthopedist = new Doctor();
        orthopedist.setId(2L);
        orthopedist.setName("Dr. Bone");
        orthopedist.setDepartment(com.infy.models.Department.ORTHOPEDICS);

        token.setDoctor(cardiologist);
        assertEquals(cardiologist, token.getDoctor());
        assertEquals(com.infy.models.Department.CARDIOLOGY, token.getDoctor().getDepartment());

        token.setDoctor(orthopedist);
        assertEquals(orthopedist, token.getDoctor());
        assertEquals(com.infy.models.Department.ORTHOPEDICS, token.getDoctor().getDepartment());
    }

    // Test token with different patients
    @Test
    void testTokenWithDifferentPatients() {
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("John Doe");

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Jane Smith");

        token.setPatient(patient1);
        assertEquals(patient1, token.getPatient());
        assertEquals("John Doe", token.getPatient().getName());

        token.setPatient(patient2);
        assertEquals(patient2, token.getPatient());
        assertEquals("Jane Smith", token.getPatient().getName());
    }

    // Test token equals with different IDs
    @Test
    void testTokenEqualsWithDifferentIds() {
        Token token1 = new Token();
        token1.setId(1L);

        Token token2 = new Token();
        token2.setId(2L);

        assertNotEquals(token1, token2);
    }

    // Test token equals with null ID
    @Test
    void testTokenEqualsWithNullId() {
        Token token1 = new Token();
        token1.setId(null);

        Token token2 = new Token();
        token2.setId(null);

        // Both have null IDs, should be equal based on other fields
        assertNotEquals(token1, token2); // Since other fields are different
    }

    // Test token with same ID but different fields
    @Test
    void testTokenWithSameIdButDifferentFields() {
        Token token1 = new Token();
        token1.setId(1L);
        token1.setTokenNumber(1);

        Token token2 = new Token();
        token2.setId(1L);
        token2.setTokenNumber(2);

        // If equals is based on ID only, they should be equal
        // If equals is based on all fields, they should be different
        // This test assumes ID-based equality (common in JPA entities)
        assertEquals(token1, token2);
    }

    // Test token with same doctor and date
    @Test
    void testTokenWithSameDoctorAndDate() {
        LocalDate today = LocalDate.now();
        Token token1 = new Token();
        token1.setDoctor(doctor);
        token1.setTokenNumber(1);
        token1.setDate(today);
        token1.setStatus(TokenStatus.WAITING);
        token1.setCheckInTime(LocalDateTime.now());

        Token token2 = new Token();
        token2.setDoctor(doctor);
        token2.setTokenNumber(2);
        token2.setDate(today);
        token2.setStatus(TokenStatus.WAITING);
        token2.setCheckInTime(LocalDateTime.now().plusMinutes(5));

        assertEquals(doctor, token1.getDoctor());
        assertEquals(doctor, token2.getDoctor());
        assertEquals(today, token1.getDate());
        assertEquals(today, token2.getDate());
        assertNotEquals(token1.getTokenNumber(), token2.getTokenNumber());
    }

    // Test token with different appointment types
    @Test
    void testTokenWithDifferentAppointmentTypes() {
        // Pre-booked appointment
        Appointment preBooked = new Appointment();
        preBooked.setId(1L);
        preBooked.setType(com.infy.models.AppointmentType.PRE_BOOKED);
        token.setAppointment(preBooked);
        assertEquals(com.infy.models.AppointmentType.PRE_BOOKED, token.getAppointment().getType());

        // Walk-in appointment
        Appointment walkIn = new Appointment();
        walkIn.setId(2L);
        walkIn.setType(com.infy.models.AppointmentType.WALK_IN);
        token.setAppointment(walkIn);
        assertEquals(com.infy.models.AppointmentType.WALK_IN, token.getAppointment().getType());
    }

    // Test token with very early check-in time
    @Test
    void testTokenWithVeryEarlyCheckInTime() {
        LocalDateTime earlyTime = LocalDate.now().atTime(6, 0);
        token.setCheckInTime(earlyTime);
        assertEquals(earlyTime, token.getCheckInTime());
        assertEquals(6, token.getCheckInTime().getHour());
    }

    // Test token with very late check-in time
    @Test
    void testTokenWithVeryLateCheckInTime() {
        LocalDateTime lateTime = LocalDate.now().atTime(23, 59);
        token.setCheckInTime(lateTime);
        assertEquals(lateTime, token.getCheckInTime());
        assertEquals(23, token.getCheckInTime().getHour());
        assertEquals(59, token.getCheckInTime().getMinute());
    }
}
