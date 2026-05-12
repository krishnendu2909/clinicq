package com.infy.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.sql.DataSource;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Test database configuration for setting up an in-memory database for testing.
 * This configuration provides a clean, isolated database environment for each test run.
 */
@TestConfiguration
@Profile("test")
public class TestDatabaseConfig {

    /**
     * Creates an embedded H2 database for testing.
     * 
     * @return Embedded H2 DataSource
     */
    @Bean
    @Primary
    public DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:test-data.sql")
                .build();
    }

    /**
     * Creates a JdbcTemplate for database operations in tests.
     * 
     * @param dataSource Test data source
     * @return JdbcTemplate configured for testing
     */
    @Bean
    @Primary
    public JdbcTemplate testJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Database cleaner utility for cleaning test data between tests.
     * 
     * @param jdbcTemplate JdbcTemplate for database operations
     * @return DatabaseCleaner utility
     */
    @Bean
    public DatabaseCleaner databaseCleaner(JdbcTemplate jdbcTemplate) {
        return new DatabaseCleaner(jdbcTemplate);
    }

    /**
     * Test data initializer for setting up test data.
     * 
     * @param jdbcTemplate JdbcTemplate for database operations
     * @return TestDataInitializer utility
     */
    @Bean
    public TestDataInitializer testDataInitializer(JdbcTemplate jdbcTemplate) {
        return new TestDataInitializer(jdbcTemplate);
    }

    /**
     * Utility class for cleaning database tables between tests.
     */
    public static class DatabaseCleaner {
        
        private final JdbcTemplate jdbcTemplate;
        
        public DatabaseCleaner(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }
        
        /**
         * Cleans all tables in the database.
         */
        public void cleanAllTables() {
            // Disable foreign key checks
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
            
            // Clean all tables in dependency order
            jdbcTemplate.execute("TRUNCATE TABLE tokens");
            jdbcTemplate.execute("TRUNCATE TABLE appointments");
            jdbcTemplate.execute("TRUNCATE TABLE time_slots");
            jdbcTemplate.execute("TRUNCATE TABLE doctor_schedules");
            jdbcTemplate.execute("TRUNCATE TABLE patients");
            jdbcTemplate.execute("TRUNCATE TABLE doctors");
            jdbcTemplate.execute("TRUNCATE TABLE users");
            jdbcTemplate.execute("TRUNCATE TABLE admin_settings");
            
            // Re-enable foreign key checks
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
        
        /**
         * Cleans a specific table.
         * 
         * @param tableName Table name to clean
         */
        public void cleanTable(String tableName) {
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
        }
        
        /**
         * Resets auto-increment counters for all tables.
         */
        public void resetAutoIncrementCounters() {
            jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.execute("ALTER TABLE doctors ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.execute("ALTER TABLE patients ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.execute("ALTER TABLE appointments ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.execute("ALTER TABLE time_slots ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.execute("ALTER TABLE tokens ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.execute("ALTER TABLE doctor_schedules ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.execute("ALTER TABLE admin_settings ALTER COLUMN id RESTART WITH 1");
        }
        
        /**
         * Executes a custom SQL script for cleaning.
         * 
         * @param sql SQL script to execute
         */
        public void executeCleanScript(String sql) {
            jdbcTemplate.execute(sql);
        }
    }

    /**
     * Utility class for initializing test data in the database.
     */
    public static class TestDataInitializer {
        
        private final JdbcTemplate jdbcTemplate;
        
        public TestDataInitializer(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }
        
        /**
         * Initializes basic test data.
         */
        public void initializeTestData() {
            // Insert test users
            insertTestUsers();
            
            // Insert test doctors
            insertTestDoctors();
            
            // Insert test patients
            insertTestPatients();
            
            // Insert test admin settings
            insertTestAdminSettings();
        }
        
        /**
         * Inserts test users into the database.
         */
        private void insertTestUsers() {
            String sql = "INSERT INTO users (id, email, password, role, created_at) VALUES (?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql, 1L, "doctor@test.com", "password", "DOCTOR", LocalDate.now());
            jdbcTemplate.update(sql, 2L, "patient@test.com", "password", "PATIENT", LocalDate.now());
            jdbcTemplate.update(sql, 3L, "admin@test.com", "password", "ADMIN", LocalDate.now());
            jdbcTemplate.update(sql, 4L, "receptionist@test.com", "password", "RECEPTIONIST", LocalDate.now());
        }
        
        /**
         * Inserts test doctors into the database.
         */
        private void insertTestDoctors() {
            String sql = "INSERT INTO doctors (id, name, phone, gender, department, location, description, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql, 1L, "Dr. Smith", "1234567890", "MALE", "CARDIOLOGY", "New York", "Cardiologist", 1L);
            jdbcTemplate.update(sql, 2L, "Dr. Johnson", "0987654321", "FEMALE", "ORTHOPEDICS", "Los Angeles", "Orthopedist", 1L);
            jdbcTemplate.update(sql, 3L, "Dr. Wilson", "5555555555", "MALE", "PEDIATRICS", "Chicago", "Pediatrician", 1L);
        }
        
        /**
         * Inserts test patients into the database.
         */
        private void insertTestPatients() {
            String sql = "INSERT INTO patients (id, name, phone, gender, date_of_birth, user_id) VALUES (?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql, 1L, "John Doe", "9876543210", "MALE", LocalDate.of(1990, 1, 1), 2L);
            jdbcTemplate.update(sql, 2L, "Jane Smith", "1234567890", "FEMALE", LocalDate.of(1985, 5, 15), 2L);
            jdbcTemplate.update(sql, 3L, "Bob Johnson", "5555555555", "MALE", LocalDate.of(1975, 8, 20), 2L);
        }
        
        /**
         * Inserts test admin settings into the database.
         */
        private void insertTestAdminSettings() {
            String sql = "INSERT INTO admin_settings (id, max_days_in_advance, max_patients_per_slot, cancellation_cutoff_hours) VALUES (?, ?, ?, ?)";
            
            jdbcTemplate.update(sql, 1L, 30, 5, 24);
        }
        
        /**
         * Inserts test time slots for a specific doctor.
         * 
         * @param doctorId Doctor ID
         * @param date Date for the time slots
         * @param startTime Start time
         * @param endTime End time
         * @param slotDuration Duration of each slot in minutes
         */
        public void insertTestTimeSlots(Long doctorId, LocalDate date, LocalTime startTime, LocalTime endTime, int slotDuration) {
            String sql = "INSERT INTO time_slots (id, doctor_id, slot_date, start_time, end_time, booked) VALUES (?, ?, ?, ?, ?, ?)";
            
            LocalTime currentTime = startTime;
            long slotId = 1L;
            
            while (currentTime.isBefore(endTime)) {
                LocalTime slotEndTime = currentTime.plusMinutes(slotDuration);
                if (!slotEndTime.isAfter(endTime)) {
                    jdbcTemplate.update(sql, slotId++, doctorId, date, currentTime, slotEndTime, false);
                }
                currentTime = currentTime.plusMinutes(slotDuration);
            }
        }
        
        /**
         * Inserts test appointments.
         * 
         * @param patientId Patient ID
         * @param doctorId Doctor ID
         * @param timeSlotId Time slot ID
         * @param reason Appointment reason
         * @param status Appointment status
         * @param type Appointment type
         */
        public void insertTestAppointment(Long patientId, Long doctorId, Long timeSlotId, String reason, String status, String type) {
            String sql = "INSERT INTO appointments (id, patient_id, doctor_id, time_slot_id, reason, status, type) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql, 1L, patientId, doctorId, timeSlotId, reason, status, type);
        }
        
        /**
         * Inserts test tokens.
         * 
         * @param doctorId Doctor ID
         * @param patientId Patient ID
         * @param tokenNumber Token number
         * @param date Token date
         * @param status Token status
         * @param checkInTime Check-in time
         * @param appointmentId Appointment ID (optional)
         */
        public void insertTestToken(Long doctorId, Long patientId, int tokenNumber, LocalDate date, String status, LocalDateTime checkInTime, Long appointmentId) {
            String sql = appointmentId != null ? 
                    "INSERT INTO tokens (id, doctor_id, patient_id, token_number, date, status, check_in_time, appointment_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)" :
                    "INSERT INTO tokens (id, doctor_id, patient_id, token_number, date, status, check_in_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            if (appointmentId != null) {
                jdbcTemplate.update(sql, 1L, doctorId, patientId, tokenNumber, date, status, checkInTime, appointmentId);
            } else {
                jdbcTemplate.update(sql, 1L, doctorId, patientId, tokenNumber, date, status, checkInTime);
            }
        }
        
        /**
         * Inserts test doctor schedules.
         * 
         * @param doctorId Doctor ID
         * @param dayOfWeek Day of week
         * @param startTime Start time
         * @param endTime End time
         * @param slotDuration Slot duration in minutes
         */
        public void insertTestDoctorSchedule(Long doctorId, String dayOfWeek, LocalTime startTime, LocalTime endTime, int slotDuration) {
            String sql = "INSERT INTO doctor_schedules (id, doctor_id, day_of_week, start_time, end_time, slot_duration) VALUES (?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql, 1L, doctorId, dayOfWeek, startTime, endTime, slotDuration);
        }
        
        /**
         * Executes a custom SQL script for data initialization.
         * 
         * @param sql SQL script to execute
         */
        public void executeInitScript(String sql) {
            jdbcTemplate.execute(sql);
        }
        
        /**
         * Gets the current count of records in a table.
         * 
         * @param tableName Table name
         * @return Number of records in the table
         */
        public int getRecordCount(String tableName) {
            String sql = "SELECT COUNT(*) FROM " + tableName;
            return jdbcTemplate.queryForObject(sql, Integer.class);
        }
        
        /**
         * Verifies that test data has been properly initialized.
         * 
         * @return True if test data is properly initialized
         */
        public boolean verifyTestDataInitialized() {
            return getRecordCount("users") >= 4 &&
                   getRecordCount("doctors") >= 3 &&
                   getRecordCount("patients") >= 3 &&
                   getRecordCount("admin_settings") >= 1;
        }
    }
}
