package com.infy.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.models.AppointmentStatus;
import com.infy.models.AppointmentType;
import com.infy.models.Department;
import com.infy.models.Gender;
import com.infy.models.Role;

@ExtendWith(MockitoExtension.class)
class AppointmentTest {

    private Appointment appointment;
    private Doctor doctor;
    private Patient patient;
    private TimeSlot timeSlot;
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
        doctor.setGender(Gender.MALE);
        doctor.setDepartment(Department.CARDIOLOGY);
        doctor.setLocation("New York");
        doctor.setDescription("Cardiologist");
        doctor.setUser(doctorUser);

        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setPhone("9876543210");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.MALE);
        patient.setUser(patientUser);

        timeSlot = new TimeSlot();
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
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setType(AppointmentType.PRE_BOOKED);
    }

    // Test getters and setters
    @Test
    void testGettersAndSetters() {
        // Test ID
        appointment.setId(2L);
        assertEquals(2L, appointment.getId());

        // Test Patient
        Patient newPatient = new Patient();
        newPatient.setId(2L);
        newPatient.setName("Jane Smith");
        appointment.setPatient(newPatient);
        assertEquals(newPatient, appointment.getPatient());
        assertEquals("Jane Smith", appointment.getPatient().getName());

        // Test Doctor
        Doctor newDoctor = new Doctor();
        newDoctor.setId(2L);
        newDoctor.setName("Dr. Johnson");
        appointment.setDoctor(newDoctor);
        assertEquals(newDoctor, appointment.getDoctor());
        assertEquals("Dr. Johnson", appointment.getDoctor().getName());

        // Test TimeSlot
        TimeSlot newTimeSlot = new TimeSlot();
        newTimeSlot.setId(2L);
        newTimeSlot.setSlotDate(LocalDate.now().plusDays(2));
        newTimeSlot.setStartTime(LocalTime.of(14, 0));
        newTimeSlot.setEndTime(LocalTime.of(14, 30));
        newTimeSlot.setBooked(false);
        appointment.setTimeSlot(newTimeSlot);
        assertEquals(newTimeSlot, appointment.getTimeSlot());
        assertEquals(LocalDate.now().plusDays(2), appointment.getTimeSlot().getSlotDate());

        // Test Reason
        appointment.setReason("Follow-up consultation");
        assertEquals("Follow-up consultation", appointment.getReason());

        // Test Status
        appointment.setStatus(AppointmentStatus.COMPLETED);
        assertEquals(AppointmentStatus.COMPLETED, appointment.getStatus());

        // Test Type
        appointment.setType(AppointmentType.WALK_IN);
        assertEquals(AppointmentType.WALK_IN, appointment.getType());
    }

    // Test default constructor
    @Test
    void testDefaultConstructor() {
        Appointment newAppointment = new Appointment();
        assertNull(newAppointment.getId());
        assertNull(newAppointment.getPatient());
        assertNull(newAppointment.getDoctor());
        assertNull(newAppointment.getTimeSlot());
        assertNull(newAppointment.getReason());
        assertNull(newAppointment.getStatus());
        assertNull(newAppointment.getType());
    }

    // Test toString method
    @Test
    void testToString() {
        String appointmentString = appointment.toString();
        assertNotNull(appointmentString);
        assertTrue(appointmentString.contains("Regular checkup"));
        assertTrue(appointmentString.contains("BOOKED"));
        assertTrue(appointmentString.contains("PRE_BOOKED"));
    }

    // Test equals method - Same object
    @Test
    void testEquals_SameObject() {
        assertTrue(appointment.equals(appointment));
    }

    // Test equals method - Equal objects
    @Test
    void testEquals_EqualObjects() {
        Appointment anotherAppointment = new Appointment();
        anotherAppointment.setId(1L);
        anotherAppointment.setPatient(patient);
        anotherAppointment.setDoctor(doctor);
        anotherAppointment.setTimeSlot(timeSlot);
        anotherAppointment.setReason("Regular checkup");
        anotherAppointment.setStatus(AppointmentStatus.BOOKED);
        anotherAppointment.setType(AppointmentType.PRE_BOOKED);

        assertEquals(appointment, anotherAppointment);
        assertEquals(appointment.hashCode(), anotherAppointment.hashCode());
    }

    // Test equals method - Different objects
    @Test
    void testEquals_DifferentObjects() {
        Appointment anotherAppointment = new Appointment();
        anotherAppointment.setId(2L);
        anotherAppointment.setPatient(patient);
        anotherAppointment.setDoctor(doctor);
        anotherAppointment.setTimeSlot(timeSlot);
        anotherAppointment.setReason("Emergency consultation");
        anotherAppointment.setStatus(AppointmentStatus.COMPLETED);
        anotherAppointment.setType(AppointmentType.WALK_IN);

        assertNotEquals(appointment, anotherAppointment);
    }

    // Test equals method - Null object
    @Test
    void testEquals_NullObject() {
        assertFalse(appointment.equals(null));
    }

    // Test equals method - Different class
    @Test
    void testEquals_DifferentClass() {
        assertFalse(appointment.equals("not an appointment"));
    }

    // Test hashCode consistency
    @Test
    void testHashCodeConsistency() {
        int initialHashCode = appointment.hashCode();
        assertEquals(initialHashCode, appointment.hashCode());
    }

    // Test hashCode with null values
    @Test
    void testHashCodeWithNullValues() {
        Appointment nullAppointment = new Appointment();
        // Should not throw exception
        assertNotNull(nullAppointment.hashCode());
    }

    // Test appointment with all valid data
    @Test
    void testAppointmentWithAllValidData() {
        assertNotNull(appointment.getId());
        assertNotNull(appointment.getPatient());
        assertNotNull(appointment.getDoctor());
        assertNotNull(appointment.getTimeSlot());
        assertNotNull(appointment.getReason());
        assertNotNull(appointment.getStatus());
        assertNotNull(appointment.getType());

        assertEquals("Regular checkup", appointment.getReason());
        assertEquals(AppointmentStatus.BOOKED, appointment.getStatus());
        assertEquals(AppointmentType.PRE_BOOKED, appointment.getType());
        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(timeSlot, appointment.getTimeSlot());
    }

    // Test appointment with minimum valid data
    @Test
    void testAppointmentWithMinimumValidData() {
        Appointment minimalAppointment = new Appointment();
        minimalAppointment.setReason("A");
        minimalAppointment.setStatus(AppointmentStatus.BOOKED);
        minimalAppointment.setType(AppointmentType.PRE_BOOKED);

        assertEquals("A", minimalAppointment.getReason());
        assertEquals(AppointmentStatus.BOOKED, minimalAppointment.getStatus());
        assertEquals(AppointmentType.PRE_BOOKED, minimalAppointment.getType());
    }

    // Test appointment with all statuses
    @Test
    void testAppointmentWithAllStatuses() {
        // Test BOOKED
        appointment.setStatus(AppointmentStatus.BOOKED);
        assertEquals(AppointmentStatus.BOOKED, appointment.getStatus());

        // Test CHECKED_IN
        appointment.setStatus(AppointmentStatus.CHECKED_IN);
        assertEquals(AppointmentStatus.CHECKED_IN, appointment.getStatus());

        // Test IN_CONSULTATION
        appointment.setStatus(AppointmentStatus.IN_CONSULTATION);
        assertEquals(AppointmentStatus.IN_CONSULTATION, appointment.getStatus());

        // Test COMPLETED
        appointment.setStatus(AppointmentStatus.COMPLETED);
        assertEquals(AppointmentStatus.COMPLETED, appointment.getStatus());

        // Test CANCELLED
        appointment.setStatus(AppointmentStatus.CANCELLED);
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
    }

    // Test appointment with all types
    @Test
    void testAppointmentWithAllTypes() {
        // Test PRE_BOOKED
        appointment.setType(AppointmentType.PRE_BOOKED);
        assertEquals(AppointmentType.PRE_BOOKED, appointment.getType());

        // Test WALK_IN
        appointment.setType(AppointmentType.WALK_IN);
        assertEquals(AppointmentType.WALK_IN, appointment.getType());
    }

    // Test appointment relationships
    @Test
    void testAppointmentRelationships() {
        // Test patient relationship
        assertEquals(patient, appointment.getPatient());
        assertEquals(appointment, patient.getAppointments().stream()
                .filter(a -> a.getId().equals(appointment.getId())).findFirst().orElse(null));

        // Test doctor relationship
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(appointment, doctor.getAppointments().stream()
                .filter(a -> a.getId().equals(appointment.getId())).findFirst().orElse(null));

        // Test timeSlot relationship
        assertEquals(timeSlot, appointment.getTimeSlot());
        assertEquals(appointment, timeSlot.getAppointment());
    }

    // Test appointment with null patient
    @Test
    void testAppointmentWithNullPatient() {
        appointment.setPatient(null);
        assertNull(appointment.getPatient());
    }

    // Test appointment with null doctor
    @Test
    void testAppointmentWithNullDoctor() {
        appointment.setDoctor(null);
        assertNull(appointment.getDoctor());
    }

    // Test appointment with null timeSlot
    @Test
    void testAppointmentWithNullTimeSlot() {
        appointment.setTimeSlot(null);
        assertNull(appointment.getTimeSlot());
    }

    // Test appointment with null reason
    @Test
    void testAppointmentWithNullReason() {
        appointment.setReason(null);
        assertNull(appointment.getReason());
    }

    // Test appointment with empty reason
    @Test
    void testAppointmentWithEmptyReason() {
        appointment.setReason("");
        assertEquals("", appointment.getReason());
    }

    // Test appointment with null status
    @Test
    void testAppointmentWithNullStatus() {
        appointment.setStatus(null);
        assertNull(appointment.getStatus());
    }

    // Test appointment with null type
    @Test
    void testAppointmentWithNullType() {
        appointment.setType(null);
        assertNull(appointment.getType());
    }

    // Test appointment with special characters in reason
    @Test
    void testAppointmentWithSpecialCharactersInReason() {
        String specialReason = "Chest pain & shortness of breath (emergency)";
        appointment.setReason(specialReason);
        assertEquals(specialReason, appointment.getReason());
    }

    // Test appointment with very long reason
    @Test
    void testAppointmentWithVeryLongReason() {
        String longReason = "Patient presents with multiple symptoms including headache, fever, " +
                "nausea, dizziness, fatigue, muscle aches, joint pain, chest discomfort, " +
                "difficulty breathing, and general malaise. Symptoms have been persistent " +
                "for the past week and are progressively worsening. Patient is concerned " +
                "about potential serious underlying conditions and requests thorough evaluation.";
        appointment.setReason(longReason);
        assertEquals(longReason, appointment.getReason());
    }

    // Test appointment with medical terminology in reason
    @Test
    void testAppointmentWithMedicalTerminologyInReason() {
        String medicalReason = "Myocardial infarction evaluation - ECG, cardiac enzymes, " +
                "echocardiogram required. Patient history includes hypertension, " +
                "hyperlipidemia, and diabetes mellitus type 2.";
        appointment.setReason(medicalReason);
        assertEquals(medicalReason, appointment.getReason());
    }

    // Test appointment with different time slots
    @Test
    void testAppointmentWithDifferentTimeSlots() {
        // Test morning slot
        TimeSlot morningSlot = new TimeSlot();
        morningSlot.setId(2L);
        morningSlot.setDoctor(doctor);
        morningSlot.setSlotDate(LocalDate.now().plusDays(1));
        morningSlot.setStartTime(LocalTime.of(9, 0));
        morningSlot.setEndTime(LocalTime.of(9, 30));
        morningSlot.setBooked(false);

        appointment.setTimeSlot(morningSlot);
        assertEquals(morningSlot, appointment.getTimeSlot());
        assertEquals(LocalTime.of(9, 0), appointment.getTimeSlot().getStartTime());

        // Test afternoon slot
        TimeSlot afternoonSlot = new TimeSlot();
        afternoonSlot.setId(3L);
        afternoonSlot.setDoctor(doctor);
        afternoonSlot.setSlotDate(LocalDate.now().plusDays(2));
        afternoonSlot.setStartTime(LocalTime.of(15, 30));
        afternoonSlot.setEndTime(LocalTime.of(16, 0));
        afternoonSlot.setBooked(false);

        appointment.setTimeSlot(afternoonSlot);
        assertEquals(afternoonSlot, appointment.getTimeSlot());
        assertEquals(LocalTime.of(15, 30), appointment.getTimeSlot().getStartTime());

        // Test evening slot
        TimeSlot eveningSlot = new TimeSlot();
        eveningSlot.setId(4L);
        eveningSlot.setDoctor(doctor);
        eveningSlot.setSlotDate(LocalDate.now().plusDays(3));
        eveningSlot.setStartTime(LocalTime.of(18, 0));
        eveningSlot.setEndTime(LocalTime.of(18, 30));
        eveningSlot.setBooked(false);

        appointment.setTimeSlot(eveningSlot);
        assertEquals(eveningSlot, appointment.getTimeSlot());
        assertEquals(LocalTime.of(18, 0), appointment.getTimeSlot().getStartTime());
    }

    // Test appointment equals with different IDs
    @Test
    void testAppointmentEqualsWithDifferentIds() {
        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);

        Appointment appointment2 = new Appointment();
        appointment2.setId(2L);

        assertNotEquals(appointment1, appointment2);
    }

    // Test appointment equals with null ID
    @Test
    void testAppointmentEqualsWithNullId() {
        Appointment appointment1 = new Appointment();
        appointment1.setId(null);

        Appointment appointment2 = new Appointment();
        appointment2.setId(null);

        // Both have null IDs, should be equal based on other fields
        assertNotEquals(appointment1, appointment2); // Since other fields are different
    }

    // Test appointment with same ID but different fields
    @Test
    void testAppointmentWithSameIdButDifferentFields() {
        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        appointment1.setReason("Regular checkup");

        Appointment appointment2 = new Appointment();
        appointment2.setId(1L);
        appointment2.setReason("Emergency");

        // If equals is based on ID only, they should be equal
        // If equals is based on all fields, they should be different
        // This test assumes ID-based equality (common in JPA entities)
        assertEquals(appointment1, appointment2);
    }

    // Test appointment with different doctors
    @Test
    void testAppointmentWithDifferentDoctors() {
        Doctor cardiologist = new Doctor();
        cardiologist.setId(1L);
        cardiologist.setName("Dr. Heart");
        cardiologist.setDepartment(Department.CARDIOLOGY);

        Doctor orthopedist = new Doctor();
        orthopedist.setId(2L);
        orthopedist.setName("Dr. Bone");
        orthopedist.setDepartment(Department.ORTHOPEDICS);

        appointment.setDoctor(cardiologist);
        assertEquals(cardiologist, appointment.getDoctor());
        assertEquals(Department.CARDIOLOGY, appointment.getDoctor().getDepartment());

        appointment.setDoctor(orthopedist);
        assertEquals(orthopedist, appointment.getDoctor());
        assertEquals(Department.ORTHOPEDICS, appointment.getDoctor().getDepartment());
    }

    // Test appointment with different patients
    @Test
    void testAppointmentWithDifferentPatients() {
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("John Doe");

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Jane Smith");

        appointment.setPatient(patient1);
        assertEquals(patient1, appointment.getPatient());
        assertEquals("John Doe", appointment.getPatient().getName());

        appointment.setPatient(patient2);
        assertEquals(patient2, appointment.getPatient());
        assertEquals("Jane Smith", appointment.getPatient().getName());
    }

    // Test appointment status transitions
    @Test
    void testAppointmentStatusTransitions() {
        // Initial status
        appointment.setStatus(AppointmentStatus.BOOKED);
        assertEquals(AppointmentStatus.BOOKED, appointment.getStatus());

        // Check-in
        appointment.setStatus(AppointmentStatus.CHECKED_IN);
        assertEquals(AppointmentStatus.CHECKED_IN, appointment.getStatus());

        // In consultation
        appointment.setStatus(AppointmentStatus.IN_CONSULTATION);
        assertEquals(AppointmentStatus.IN_CONSULTATION, appointment.getStatus());

        // Completed
        appointment.setStatus(AppointmentStatus.COMPLETED);
        assertEquals(AppointmentStatus.COMPLETED, appointment.getStatus());

        // Cancelled (can be cancelled from any state)
        appointment.setStatus(AppointmentStatus.CANCELLED);
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
    }

    // Test appointment with future and past time slots
    @Test
    void testAppointmentWithFutureAndPastTimeSlots() {
        // Future appointment
        TimeSlot futureSlot = new TimeSlot();
        futureSlot.setId(5L);
        futureSlot.setDoctor(doctor);
        futureSlot.setSlotDate(LocalDate.now().plusWeeks(2));
        futureSlot.setStartTime(LocalTime.of(10, 0));
        futureSlot.setEndTime(LocalTime.of(10, 30));
        futureSlot.setBooked(false);

        appointment.setTimeSlot(futureSlot);
        assertTrue(appointment.getTimeSlot().getSlotDate().isAfter(LocalDate.now()));

        // Past appointment
        TimeSlot pastSlot = new TimeSlot();
        pastSlot.setId(6L);
        pastSlot.setDoctor(doctor);
        pastSlot.setSlotDate(LocalDate.now().minusWeeks(1));
        pastSlot.setStartTime(LocalTime.of(14, 0));
        pastSlot.setEndTime(LocalTime.of(14, 30));
        pastSlot.setBooked(true);

        appointment.setTimeSlot(pastSlot);
        assertTrue(appointment.getTimeSlot().getSlotDate().isBefore(LocalDate.now()));
    }
}
