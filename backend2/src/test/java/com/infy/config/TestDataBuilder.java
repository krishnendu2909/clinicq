package com.infy.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.infy.dto.*;
import com.infy.entity.*;
import com.infy.models.*;

/**
 * Utility class for building test data objects.
 * This class provides factory methods for creating consistent test entities and DTOs
 * across all test classes, ensuring test data consistency and reducing boilerplate code.
 */
public class TestDataBuilder {

    /**
     * Creates a test User entity with default values.
     * 
     * @return User entity with test data
     */
    public User buildTestUser() {
        return buildTestUser(1L, "test@example.com", "password", Role.PATIENT);
    }

    /**
     * Creates a test User entity with specified values.
     * 
     * @param id User ID
     * @param email User email
     * @param password User password
     * @param role User role
     * @return User entity with specified test data
     */
    public User buildTestUser(Long id, String email, String password, Role role) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setCreatedAt(LocalDate.now());
        return user;
    }

    /**
     * Creates a test Patient entity with default values.
     * 
     * @return Patient entity with test data
     */
    public Patient buildTestPatient() {
        return buildTestPatient(1L, "John Doe", "9876543210", Gender.MALE, LocalDate.of(1990, 1, 1));
    }

    /**
     * Creates a test Patient entity with specified values.
     * 
     * @param id Patient ID
     * @param name Patient name
     * @param phone Patient phone
     * @param gender Patient gender
     * @param dateOfBirth Patient date of birth
     * @return Patient entity with specified test data
     */
    public Patient buildTestPatient(Long id, String name, String phone, Gender gender, LocalDate dateOfBirth) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setName(name);
        patient.setPhone(phone);
        patient.setGender(gender);
        patient.setDateOfBirth(dateOfBirth);
        patient.setUser(buildTestUser(id, "patient" + id + "@test.com", "password", Role.PATIENT));
        return patient;
    }

    /**
     * Creates a test Doctor entity with default values.
     * 
     * @return Doctor entity with test data
     */
    public Doctor buildTestDoctor() {
        return buildTestDoctor(1L, "Dr. Smith", "1234567890", Gender.MALE, Department.CARDIOLOGY, "New York", "Cardiologist");
    }

    /**
     * Creates a test Doctor entity with specified values.
     * 
     * @param id Doctor ID
     * @param name Doctor name
     * @param phone Doctor phone
     * @param gender Doctor gender
     * @param department Doctor department
     * @param location Doctor location
     * @param description Doctor description
     * @return Doctor entity with specified test data
     */
    public Doctor buildTestDoctor(Long id, String name, String phone, Gender gender, Department department, String location, String description) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setName(name);
        doctor.setPhone(phone);
        doctor.setGender(gender);
        doctor.setDepartment(department);
        doctor.setLocation(location);
        doctor.setDescription(description);
        doctor.setUser(buildTestUser(id, "doctor" + id + "@test.com", "password", Role.DOCTOR));
        return doctor;
    }

    /**
     * Creates a test TimeSlot entity with default values.
     * 
     * @return TimeSlot entity with test data
     */
    public TimeSlot buildTestTimeSlot() {
        return buildTestTimeSlot(1L, buildTestDoctor(), LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30), false);
    }

    /**
     * Creates a test TimeSlot entity with specified values.
     * 
     * @param id TimeSlot ID
     * @param doctor Doctor entity
     * @param slotDate Slot date
     * @param startTime Start time
     * @param endTime End time
     * @param booked Whether the slot is booked
     * @return TimeSlot entity with specified test data
     */
    public TimeSlot buildTestTimeSlot(Long id, Doctor doctor, LocalDate slotDate, LocalTime startTime, LocalTime endTime, boolean booked) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(id);
        timeSlot.setDoctor(doctor);
        timeSlot.setSlotDate(slotDate);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        timeSlot.setBooked(booked);
        return timeSlot;
    }

    /**
     * Creates a test Appointment entity with default values.
     * 
     * @return Appointment entity with test data
     */
    public Appointment buildTestAppointment() {
        return buildTestAppointment(1L, buildTestPatient(), buildTestDoctor(), buildTestTimeSlot(), "Regular checkup", AppointmentStatus.BOOKED, AppointmentType.PRE_BOOKED);
    }

    /**
     * Creates a test Appointment entity with specified values.
     * 
     * @param id Appointment ID
     * @param patient Patient entity
     * @param doctor Doctor entity
     * @param timeSlot TimeSlot entity
     * @param reason Appointment reason
     * @param status Appointment status
     * @param type Appointment type
     * @return Appointment entity with specified test data
     */
    public Appointment buildTestAppointment(Long id, Patient patient, Doctor doctor, TimeSlot timeSlot, String reason, AppointmentStatus status, AppointmentType type) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setTimeSlot(timeSlot);
        appointment.setReason(reason);
        appointment.setStatus(status);
        appointment.setType(type);
        return appointment;
    }

    /**
     * Creates a test Token entity with default values.
     * 
     * @return Token entity with test data
     */
    public Token buildTestToken() {
        return buildTestToken(1L, buildTestDoctor(), buildTestPatient(), 1, LocalDate.now(), TokenStatus.WAITING, LocalDateTime.now(), buildTestAppointment());
    }

    /**
     * Creates a test Token entity with specified values.
     * 
     * @param id Token ID
     * @param doctor Doctor entity
     * @param patient Patient entity
     * @param tokenNumber Token number
     * @param date Token date
     * @param status Token status
     * @param checkInTime Check-in time
     * @param appointment Appointment entity
     * @return Token entity with specified test data
     */
    public Token buildTestToken(Long id, Doctor doctor, Patient patient, int tokenNumber, LocalDate date, TokenStatus status, LocalDateTime checkInTime, Appointment appointment) {
        Token token = new Token();
        token.setId(id);
        token.setDoctor(doctor);
        token.setPatient(patient);
        token.setTokenNumber(tokenNumber);
        token.setDate(date);
        token.setStatus(status);
        token.setCheckInTime(checkInTime);
        token.setAppointment(appointment);
        return token;
    }

    /**
     * Creates a test UserDTO with default values.
     * 
     * @return UserDTO with test data
     */
    public UserDTO buildTestUserDTO() {
        return buildTestUserDTO(1L, "test@example.com", "password", Role.PATIENT);
    }

    /**
     * Creates a test UserDTO with specified values.
     * 
     * @param id User ID
     * @param email User email
     * @param password User password
     * @param role User role
     * @return UserDTO with specified test data
     */
    public UserDTO buildTestUserDTO(Long id, String email, String password, Role role) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setRole(role);
        userDTO.setCreatedAt(LocalDate.now());
        return userDTO;
    }

    /**
     * Creates a test PatientDTO with default values.
     * 
     * @return PatientDTO with test data
     */
    public PatientDTO buildTestPatientDTO() {
        return buildTestPatientDTO(1L, "John Doe", "9876543210", Gender.MALE, LocalDate.of(1990, 1, 1));
    }

    /**
     * Creates a test PatientDTO with specified values.
     * 
     * @param id Patient ID
     * @param name Patient name
     * @param phone Patient phone
     * @param gender Patient gender
     * @param dateOfBirth Patient date of birth
     * @return PatientDTO with specified test data
     */
    public PatientDTO buildTestPatientDTO(Long id, String name, String phone, Gender gender, LocalDate dateOfBirth) {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(id);
        patientDTO.setName(name);
        patientDTO.setPhone(phone);
        patientDTO.setGender(gender);
        patientDTO.setDateOfBirth(dateOfBirth);
        patientDTO.setUser(buildTestUserDTO(id, "patient" + id + "@test.com", "password", Role.PATIENT));
        return patientDTO;
    }

    /**
     * Creates a test DoctorDTO with default values.
     * 
     * @return DoctorDTO with test data
     */
    public DoctorDTO buildTestDoctorDTO() {
        return buildTestDoctorDTO(1L, "Dr. Smith", "1234567890", Gender.MALE, Department.CARDIOLOGY, "New York", "Cardiologist");
    }

    /**
     * Creates a test DoctorDTO with specified values.
     * 
     * @param id Doctor ID
     * @param name Doctor name
     * @param phone Doctor phone
     * @param gender Doctor gender
     * @param department Doctor department
     * @param location Doctor location
     * @param description Doctor description
     * @return DoctorDTO with specified test data
     */
    public DoctorDTO buildTestDoctorDTO(Long id, String name, String phone, Gender gender, Department department, String location, String description) {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(id);
        doctorDTO.setName(name);
        doctorDTO.setPhone(phone);
        doctorDTO.setGender(gender);
        doctorDTO.setDepartment(department);
        doctorDTO.setLocation(location);
        doctorDTO.setDescription(description);
        doctorDTO.setUser(buildTestUserDTO(id, "doctor" + id + "@test.com", "password", Role.DOCTOR));
        return doctorDTO;
    }

    /**
     * Creates a test TimeSlotDTO with default values.
     * 
     * @return TimeSlotDTO with test data
     */
    public TimeSlotDTO buildTestTimeSlotDTO() {
        return buildTestTimeSlotDTO(1L, buildTestDoctorDTO(), LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30), false);
    }

    /**
     * Creates a test TimeSlotDTO with specified values.
     * 
     * @param id TimeSlot ID
     * @param doctor DoctorDTO
     * @param slotDate Slot date
     * @param startTime Start time
     * @param endTime End time
     * @param booked Whether the slot is booked
     * @return TimeSlotDTO with specified test data
     */
    public TimeSlotDTO buildTestTimeSlotDTO(Long id, DoctorDTO doctor, LocalDate slotDate, LocalTime startTime, LocalTime endTime, boolean booked) {
        TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
        timeSlotDTO.setId(id);
        timeSlotDTO.setDoctor(doctor);
        timeSlotDTO.setSlotDate(slotDate);
        timeSlotDTO.setStartTime(startTime);
        timeSlotDTO.setEndTime(endTime);
        timeSlotDTO.setBooked(booked);
        return timeSlotDTO;
    }

    /**
     * Creates a test AppointmentDTO with default values.
     * 
     * @return AppointmentDTO with test data
     */
    public AppointmentDTO buildTestAppointmentDTO() {
        return buildTestAppointmentDTO(1L, "Regular checkup", AppointmentStatus.BOOKED, AppointmentType.PRE_BOOKED);
    }

    /**
     * Creates a test AppointmentDTO with specified values.
     * 
     * @param id Appointment ID
     * @param reason Appointment reason
     * @param status Appointment status
     * @param type Appointment type
     * @return AppointmentDTO with specified test data
     */
    public AppointmentDTO buildTestAppointmentDTO(Long id, String reason, AppointmentStatus status, AppointmentType type) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(id);
        appointmentDTO.setReason(reason);
        appointmentDTO.setStatus(status);
        appointmentDTO.setType(type);
        return appointmentDTO;
    }

    /**
     * Creates a test TokenDTO with default values.
     * 
     * @return TokenDTO with test data
     */
    public TokenDTO buildTestTokenDTO() {
        return buildTestTokenDTO(1L, buildTestDoctorDTO(), buildTestPatientDTO(), 1, LocalDate.now(), TokenStatus.WAITING, LocalDateTime.now());
    }

    /**
     * Creates a test TokenDTO with specified values.
     * 
     * @param id Token ID
     * @param doctor DoctorDTO
     * @param patient PatientDTO
     * @param tokenNumber Token number
     * @param date Token date
     * @param status Token status
     * @param checkInTime Check-in time
     * @return TokenDTO with specified test data
     */
    public TokenDTO buildTestTokenDTO(Long id, DoctorDTO doctor, PatientDTO patient, int tokenNumber, LocalDate date, TokenStatus status, LocalDateTime checkInTime) {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setId(id);
        tokenDTO.setDoctor(doctor);
        tokenDTO.setPatient(patient);
        tokenDTO.setTokenNumber(tokenNumber);
        tokenDTO.setDate(date);
        tokenDTO.setStatus(status);
        tokenDTO.setCheckInTime(checkInTime);
        return tokenDTO;
    }

    /**
     * Creates a test AppointmentRequestDTO with default values.
     * 
     * @return AppointmentRequestDTO with test data
     */
    public AppointmentRequestDTO buildTestAppointmentRequestDTO() {
        return buildTestAppointmentRequestDTO(1L, 1L, 1L, "Regular checkup");
    }

    /**
     * Creates a test AppointmentRequestDTO with specified values.
     * 
     * @param patientId Patient ID
     * @param doctorId Doctor ID
     * @param slotId TimeSlot ID
     * @param reason Appointment reason
     * @return AppointmentRequestDTO with specified test data
     */
    public AppointmentRequestDTO buildTestAppointmentRequestDTO(Long patientId, Long doctorId, Long slotId, String reason) {
        AppointmentRequestDTO appointmentRequestDTO = new AppointmentRequestDTO();
        appointmentRequestDTO.setPatientId(patientId);
        appointmentRequestDTO.setDoctorId(doctorId);
        appointmentRequestDTO.setSlotId(slotId);
        appointmentRequestDTO.setReason(reason);
        return appointmentRequestDTO;
    }

    /**
     * Creates a test Admin entity with default values.
     * 
     * @return Admin entity with test data
     */
    public Admin buildTestAdmin() {
        return buildTestAdmin(1L, 30, 1, 24);
    }

    /**
     * Creates a test Admin entity with specified values.
     * 
     * @param id Admin ID
     * @param maxDaysInAdvance Maximum days in advance for booking
     * @param maxPatientsPerSlot Maximum patients per slot
     * @param cancellationCutoffHours Cancellation cutoff hours
     * @return Admin entity with specified test data
     */
    public Admin buildTestAdmin(Long id, int maxDaysInAdvance, int maxPatientsPerSlot, int cancellationCutoffHours) {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setMaxDaysInAdvance(maxDaysInAdvance);
        admin.setMaxPatientsPerSlot(maxPatientsPerSlot);
        admin.setCancellationCutoffHours(cancellationCutoffHours);
        return admin;
    }

    /**
     * Creates a test DoctorSchedule entity with default values.
     * 
     * @return DoctorSchedule entity with test data
     */
    public DoctorSchedule buildTestDoctorSchedule() {
        return buildTestDoctorSchedule(1L, buildTestDoctor(), java.time.DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), 30);
    }

    /**
     * Creates a test DoctorSchedule entity with specified values.
     * 
     * @param id Schedule ID
     * @param doctor Doctor entity
     * @param dayOfWeek Day of week
     * @param startTime Start time
     * @param endTime End time
     * @param slotDuration Slot duration in minutes
     * @return DoctorSchedule entity with specified test data
     */
    public DoctorSchedule buildTestDoctorSchedule(Long id, Doctor doctor, java.time.DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, int slotDuration) {
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId(id);
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(dayOfWeek);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setSlotDuration(slotDuration);
        return schedule;
    }
}
