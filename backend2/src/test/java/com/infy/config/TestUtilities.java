package com.infy.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.infy.dto.*;
import com.infy.entity.*;
import com.infy.models.*;

/**
 * Utility class providing common test operations and helper methods.
 * This class contains static utility methods for common test scenarios,
 * data validation, and test setup operations.
 */
public class TestUtilities {

    private static final Random random = new Random();

    /**
     * Generates a random email address for testing.
     * 
     * @param prefix Email prefix
     * @return Random email address
     */
    public static String generateRandomEmail(String prefix) {
        return prefix + "_" + random.nextInt(10000) + "@test.com";
    }

    /**
     * Generates a random phone number for testing.
     * 
     * @return Random 10-digit phone number
     */
    public static String generateRandomPhone() {
        return String.format("%010d", random.nextInt(1000000000));
    }

    /**
     * Generates a random name for testing.
     * 
     * @param firstName First name
     * @param lastName Last name
     * @return Random full name
     */
    public static String generateRandomName(String firstName, String lastName) {
        return firstName + " " + lastName + random.nextInt(100);
    }

    /**
     * Generates a random date of birth for testing (between 18 and 80 years old).
     * 
     * @return Random date of birth
     */
    public static LocalDate generateRandomDateOfBirth() {
        int minAge = 18;
        int maxAge = 80;
        int randomAge = minAge + random.nextInt(maxAge - minAge + 1);
        return LocalDate.now().minusYears(randomAge);
    }

    /**
     * Generates a random future date for testing.
     * 
     * @param daysInFuture Number of days in the future
     * @return Random future date
     */
    public static LocalDate generateFutureDate(int daysInFuture) {
        return LocalDate.now().plusDays(daysInFuture + random.nextInt(30));
    }

    /**
     * Generates a random time between 8:00 AM and 6:00 PM.
     * 
     * @return Random time
     */
    public static LocalTime generateRandomTime() {
        int hour = 8 + random.nextInt(11); // 8 AM to 6 PM
        int minute = random.nextInt(60);
        return LocalTime.of(hour, minute);
    }

    /**
     * Generates a random token number for testing.
     * 
     * @return Random token number between 1 and 999
     */
    public static int generateRandomTokenNumber() {
        return 1 + random.nextInt(999);
    }

    /**
     * Creates a list of test patients for bulk testing.
     * 
     * @param count Number of patients to create
     * @return List of test patients
     */
    public static List<Patient> createTestPatients(int count) {
        List<Patient> patients = new ArrayList<>();
        TestDataBuilder builder = new TestDataBuilder();
        
        for (int i = 1; i <= count; i++) {
            Patient patient = builder.buildTestPatient(
                (long) i,
                generateRandomName("Patient", String.valueOf(i)),
                generateRandomPhone(),
                i % 2 == 0 ? Gender.MALE : Gender.FEMALE,
                generateRandomDateOfBirth()
            );
            patients.add(patient);
        }
        
        return patients;
    }

    /**
     * Creates a list of test doctors for bulk testing.
     * 
     * @param count Number of doctors to create
     * @return List of test doctors
     */
    public static List<Doctor> createTestDoctors(int count) {
        List<Doctor> doctors = new ArrayList<>();
        TestDataBuilder builder = new TestDataBuilder();
        Department[] departments = Department.values();
        
        for (int i = 1; i <= count; i++) {
            Department department = departments[i % departments.length];
            Doctor doctor = builder.buildTestDoctor(
                (long) i,
                generateRandomName("Dr.", String.valueOf(i)),
                generateRandomPhone(),
                i % 2 == 0 ? Gender.MALE : Gender.FEMALE,
                department,
                "City " + i,
                department + " Specialist"
            );
            doctors.add(doctor);
        }
        
        return doctors;
    }

    /**
     * Creates a list of test appointments for bulk testing.
     * 
     * @param count Number of appointments to create
     * @param patients List of patients
     * @param doctors List of doctors
     * @return List of test appointments
     */
    public static List<Appointment> createTestAppointments(int count, List<Patient> patients, List<Doctor> doctors) {
        List<Appointment> appointments = new ArrayList<>();
        TestDataBuilder builder = new TestDataBuilder();
        AppointmentStatus[] statuses = AppointmentStatus.values();
        AppointmentType[] types = AppointmentType.values();
        
        for (int i = 1; i <= count; i++) {
            Patient patient = patients.get(i % patients.size());
            Doctor doctor = doctors.get(i % doctors.size());
            TimeSlot timeSlot = builder.buildTestTimeSlot(
                (long) i,
                doctor,
                generateFutureDate(1),
                generateRandomTime(),
                generateRandomTime().plusMinutes(30),
                true
            );
            
            Appointment appointment = builder.buildTestAppointment(
                (long) i,
                patient,
                doctor,
                timeSlot,
                "Test appointment " + i,
                statuses[i % statuses.length],
                types[i % types.length]
            );
            appointments.add(appointment);
        }
        
        return appointments;
    }

    /**
     * Creates a list of test tokens for bulk testing.
     * 
     * @param count Number of tokens to create
     * @param patients List of patients
     * @param doctors List of doctors
     * @return List of test tokens
     */
    public static List<Token> createTestTokens(int count, List<Patient> patients, List<Doctor> doctors) {
        List<Token> tokens = new ArrayList<>();
        TestDataBuilder builder = new TestDataBuilder();
        TokenStatus[] statuses = TokenStatus.values();
        
        for (int i = 1; i <= count; i++) {
            Patient patient = patients.get(i % patients.size());
            Doctor doctor = doctors.get(i % doctors.size());
            
            Token token = builder.buildTestToken(
                (long) i,
                doctor,
                patient,
                i,
                LocalDate.now(),
                statuses[i % statuses.length],
                LocalDateTime.now().minusMinutes(i * 5)
            );
            tokens.add(token);
        }
        
        return tokens;
    }

    /**
     * Validates that two entities have the same basic properties.
     * 
     * @param entity1 First entity
     * @param entity2 Second entity
     * @return True if entities have matching properties
     */
    public static boolean validateEntityProperties(Object entity1, Object entity2) {
        if (entity1 == null && entity2 == null) return true;
        if (entity1 == null || entity2 == null) return false;
        
        if (entity1 instanceof Patient && entity2 instanceof Patient) {
            return validatePatientProperties((Patient) entity1, (Patient) entity2);
        } else if (entity1 instanceof Doctor && entity2 instanceof Doctor) {
            return validateDoctorProperties((Doctor) entity1, (Doctor) entity2);
        } else if (entity1 instanceof Appointment && entity2 instanceof Appointment) {
            return validateAppointmentProperties((Appointment) entity1, (Appointment) entity2);
        } else if (entity1 instanceof Token && entity2 instanceof Token) {
            return validateTokenProperties((Token) entity1, (Token) entity2);
        }
        
        return entity1.equals(entity2);
    }

    /**
     * Validates that two Patient entities have the same properties.
     */
    private static boolean validatePatientProperties(Patient patient1, Patient patient2) {
        return patient1.getName().equals(patient2.getName()) &&
               patient1.getPhone().equals(patient2.getPhone()) &&
               patient1.getGender().equals(patient2.getGender()) &&
               patient1.getDateOfBirth().equals(patient2.getDateOfBirth());
    }

    /**
     * Validates that two Doctor entities have the same properties.
     */
    private static boolean validateDoctorProperties(Doctor doctor1, Doctor doctor2) {
        return doctor1.getName().equals(doctor2.getName()) &&
               doctor1.getPhone().equals(doctor2.getPhone()) &&
               doctor1.getGender().equals(doctor2.getGender()) &&
               doctor1.getDepartment().equals(doctor2.getDepartment()) &&
               doctor1.getLocation().equals(doctor2.getLocation()) &&
               doctor1.getDescription().equals(doctor2.getDescription());
    }

    /**
     * Validates that two Appointment entities have the same properties.
     */
    private static boolean validateAppointmentProperties(Appointment appointment1, Appointment appointment2) {
        return appointment1.getReason().equals(appointment2.getReason()) &&
               appointment1.getStatus().equals(appointment2.getStatus()) &&
               appointment1.getType().equals(appointment2.getType());
    }

    /**
     * Validates that two Token entities have the same properties.
     */
    private static boolean validateTokenProperties(Token token1, Token token2) {
        return token1.getTokenNumber() == token2.getTokenNumber() &&
               token1.getDate().equals(token2.getDate()) &&
               token1.getStatus().equals(token2.getStatus());
    }

    /**
     * Formats a date for display in test output.
     * 
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Formats a time for display in test output.
     * 
     * @param time Time to format
     * @return Formatted time string
     */
    public static String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Formats a datetime for display in test output.
     * 
     * @param dateTime DateTime to format
     * @return Formatted datetime string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Creates a test exception with a specific message.
     * 
     * @param message Exception message
     * @return InfyHospitalException with the specified message
     */
    public static com.infy.exception.InfyHospitalException createTestException(String message) {
        return new com.infy.exception.InfyHospitalException(message);
    }

    /**
     * Asserts that two lists contain the same elements regardless of order.
     * 
     * @param <T> Type of elements
     * @param expected Expected list
     * @param actual Actual list
     */
    public static <T> void assertListEqualsIgnoreOrder(List<T> expected, List<T> actual) {
        if (expected.size() != actual.size()) {
            throw new AssertionError("List sizes differ: expected " + expected.size() + ", actual " + actual.size());
        }
        
        for (T item : expected) {
            if (!actual.contains(item)) {
                throw new AssertionError("Expected list does not contain: " + item);
            }
        }
    }

    /**
     * Creates a deep copy of a Patient entity for testing.
     * 
     * @param patient Patient to copy
     * @return Deep copy of the patient
     */
    public static Patient deepCopyPatient(Patient patient) {
        Patient copy = new Patient();
        copy.setId(patient.getId());
        copy.setName(patient.getName());
        copy.setPhone(patient.getPhone());
        copy.setGender(patient.getGender());
        copy.setDateOfBirth(patient.getDateOfBirth());
        copy.setUser(patient.getUser()); // Shallow copy of user for simplicity
        return copy;
    }

    /**
     * Creates a deep copy of a Doctor entity for testing.
     * 
     * @param doctor Doctor to copy
     * @return Deep copy of the doctor
     */
    public static Doctor deepCopyDoctor(Doctor doctor) {
        Doctor copy = new Doctor();
        copy.setId(doctor.getId());
        copy.setName(doctor.getName());
        copy.setPhone(doctor.getPhone());
        copy.setGender(doctor.getGender());
        copy.setDepartment(doctor.getDepartment());
        copy.setLocation(doctor.getLocation());
        copy.setDescription(doctor.getDescription());
        copy.setUser(doctor.getUser()); // Shallow copy of user for simplicity
        return copy;
    }

    /**
     * Creates a deep copy of an Appointment entity for testing.
     * 
     * @param appointment Appointment to copy
     * @return Deep copy of the appointment
     */
    public static Appointment deepCopyAppointment(Appointment appointment) {
        Appointment copy = new Appointment();
        copy.setId(appointment.getId());
        copy.setPatient(appointment.getPatient());
        copy.setDoctor(appointment.getDoctor());
        copy.setTimeSlot(appointment.getTimeSlot());
        copy.setReason(appointment.getReason());
        copy.setStatus(appointment.getStatus());
        copy.setType(appointment.getType());
        return copy;
    }

    /**
     * Creates a deep copy of a Token entity for testing.
     * 
     * @param token Token to copy
     * @return Deep copy of the token
     */
    public static Token deepCopyToken(Token token) {
        Token copy = new Token();
        copy.setId(token.getId());
        copy.setDoctor(token.getDoctor());
        copy.setPatient(token.getPatient());
        copy.setTokenNumber(token.getTokenNumber());
        copy.setDate(token.getDate());
        copy.setStatus(token.getStatus());
        copy.setCheckInTime(token.getCheckInTime());
        copy.setAppointment(token.getAppointment());
        return copy;
    }

    /**
     * Generates a unique identifier for test data.
     * 
     * @param prefix Prefix for the identifier
     * @return Unique identifier
     */
    public static String generateUniqueId(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" + random.nextInt(1000);
    }

    /**
     * Validates that a string is not null or empty.
     * 
     * @param value String to validate
     * @param fieldName Field name for error messages
     * @throws AssertionError if string is null or empty
     */
    public static void assertNotNullOrEmpty(String value, String fieldName) {
        if (value == null) {
            throw new AssertionError(fieldName + " cannot be null");
        }
        if (value.trim().isEmpty()) {
            throw new AssertionError(fieldName + " cannot be empty");
        }
    }

    /**
     * Validates that a number is within a specified range.
     * 
     * @param value Number to validate
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @param fieldName Field name for error messages
     * @throws AssertionError if number is outside range
     */
    public static void assertInRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new AssertionError(fieldName + " must be between " + min + " and " + max + ", but was " + value);
        }
    }

    /**
     * Validates that a date is not null and is not in the past.
     * 
     * @param date Date to validate
     * @param fieldName Field name for error messages
     * @throws AssertionError if date is null or in the past
     */
    public static void assertValidFutureDate(LocalDate date, String fieldName) {
        if (date == null) {
            throw new AssertionError(fieldName + " cannot be null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new AssertionError(fieldName + " cannot be in the past");
        }
    }

    /**
     * Validates that a time is within business hours (8 AM - 6 PM).
     * 
     * @param time Time to validate
     * @param fieldName Field name for error messages
     * @throws AssertionError if time is outside business hours
     */
    public static void assertBusinessHours(LocalTime time, String fieldName) {
        if (time.isBefore(LocalTime.of(8, 0)) || time.isAfter(LocalTime.of(18, 0))) {
            throw new AssertionError(fieldName + " must be within business hours (8 AM - 6 PM), but was " + time);
        }
    }
}
