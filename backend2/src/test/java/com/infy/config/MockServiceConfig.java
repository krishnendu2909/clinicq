package com.infy.config;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.infy.dto.*;
import com.infy.entity.*;
import com.infy.models.*;
import com.infy.repository.*;
import com.infy.service.*;

/**
 * Configuration class for setting up mock services and repositories for testing.
 * This class provides pre-configured mock beans that can be used across all test classes
 * to reduce boilerplate code and ensure consistent mock behavior.
 */
@TestConfiguration
@Profile("test")
public class MockServiceConfig {

    /**
     * Creates a mock PatientRepository with default behavior.
     * 
     * @return Mock PatientRepository
     */
    @Bean
    @Primary
    public PatientRepository mockPatientRepository() {
        PatientRepository mock = Mockito.mock(PatientRepository.class);
        
        // Default mock behaviors can be configured here
        TestDataBuilder builder = new TestDataBuilder();
        Patient testPatient = builder.buildTestPatient();
        
        Mockito.when(mock.findById(1L)).thenReturn(Optional.of(testPatient));
        Mockito.when(mock.findById(999L)).thenReturn(Optional.empty());
        Mockito.when(mock.findAll()).thenReturn(Arrays.asList(testPatient));
        Mockito.when(mock.save(Mockito.any(Patient.class))).thenReturn(testPatient);
        Mockito.when(mock.count()).thenReturn(1L);
        Mockito.when(mock.existsById(1L)).thenReturn(true);
        Mockito.when(mock.existsById(999L)).thenReturn(false);
        
        return mock;
    }

    /**
     * Creates a mock DoctorRepository with default behavior.
     * 
     * @return Mock DoctorRepository
     */
    @Bean
    @Primary
    public DoctorRepository mockDoctorRepository() {
        DoctorRepository mock = Mockito.mock(DoctorRepository.class);
        
        TestDataBuilder builder = new TestDataBuilder();
        Doctor testDoctor = builder.buildTestDoctor();
        
        Mockito.when(mock.findById(1L)).thenReturn(Optional.of(testDoctor));
        Mockito.when(mock.findById(999L)).thenReturn(Optional.empty());
        Mockito.when(mock.findAll()).thenReturn(Arrays.asList(testDoctor));
        Mockito.when(mock.save(Mockito.any(Doctor.class))).thenReturn(testDoctor);
        Mockito.when(mock.count()).thenReturn(1L);
        Mockito.when(mock.existsById(1L)).thenReturn(true);
        Mockito.when(mock.existsById(999L)).thenReturn(false);
        Mockito.when(mock.findByDepartment(Department.CARDIOLOGY)).thenReturn(Arrays.asList(testDoctor));
        Mockito.when(mock.findByDepartment(Department.ORTHOPEDICS)).thenReturn(Arrays.asList());
        
        return mock;
    }

    /**
     * Creates a mock AppointmentRepository with default behavior.
     * 
     * @return Mock AppointmentRepository
     */
    @Bean
    @Primary
    public AppointmentRepository mockAppointmentRepository() {
        AppointmentRepository mock = Mockito.mock(AppointmentRepository.class);
        
        TestDataBuilder builder = new TestDataBuilder();
        Appointment testAppointment = builder.buildTestAppointment();
        
        Mockito.when(mock.findById(1L)).thenReturn(Optional.of(testAppointment));
        Mockito.when(mock.findById(999L)).thenReturn(Optional.empty());
        Mockito.when(mock.findAll()).thenReturn(Arrays.asList(testAppointment));
        Mockito.when(mock.save(Mockito.any(Appointment.class))).thenReturn(testAppointment);
        Mockito.when(mock.count()).thenReturn(1L);
        Mockito.when(mock.existsById(1L)).thenReturn(true);
        Mockito.when(mock.existsById(999L)).thenReturn(false);
        Mockito.when(mock.findByPatientId(1L)).thenReturn(Arrays.asList(testAppointment));
        Mockito.when(mock.findByDoctorId(1L)).thenReturn(Arrays.asList(testAppointment));
        Mockito.when(mock.findByStatus(AppointmentStatus.BOOKED)).thenReturn(Arrays.asList(testAppointment));
        Mockito.when(mock.findByTimeSlotId(1L)).thenReturn(Optional.of(testAppointment));
        
        return mock;
    }

    /**
     * Creates a mock TokenRepository with default behavior.
     * 
     * @return Mock TokenRepository
     */
    @Bean
    @Primary
    public TokenRepository mockTokenRepository() {
        TokenRepository mock = Mockito.mock(TokenRepository.class);
        
        TestDataBuilder builder = new TestDataBuilder();
        Token testToken = builder.buildTestToken();
        Doctor testDoctor = builder.buildTestDoctor();
        
        Mockito.when(mock.findById(1L)).thenReturn(Optional.of(testToken));
        Mockito.when(mock.findById(999L)).thenReturn(Optional.empty());
        Mockito.when(mock.findAll()).thenReturn(Arrays.asList(testToken));
        Mockito.when(mock.save(Mockito.any(Token.class))).thenReturn(testToken);
        Mockito.when(mock.count()).thenReturn(1L);
        Mockito.when(mock.existsById(1L)).thenReturn(true);
        Mockito.when(mock.existsById(999L)).thenReturn(false);
        Mockito.when(mock.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, Mockito.any(LocalDate.class)))
                .thenReturn(Arrays.asList(testToken));
        Mockito.when(mock.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, Mockito.any(LocalDate.class)))
                .thenReturn(testToken);
        Mockito.when(mock.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(Mockito.eq(testDoctor), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(testToken));
        
        return mock;
    }

    /**
     * Creates a mock UserRepository with default behavior.
     * 
     * @return Mock UserRepository
     */
    @Bean
    @Primary
    public UserRepository mockUserRepository() {
        UserRepository mock = Mockito.mock(UserRepository.class);
        
        TestDataBuilder builder = new TestDataBuilder();
        User testUser = builder.buildTestUser();
        
        Mockito.when(mock.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(mock.findById(999L)).thenReturn(Optional.empty());
        Mockito.when(mock.findAll()).thenReturn(Arrays.asList(testUser));
        Mockito.when(mock.save(Mockito.any(User.class))).thenReturn(testUser);
        Mockito.when(mock.count()).thenReturn(1L);
        Mockito.when(mock.existsById(1L)).thenReturn(true);
        Mockito.when(mock.existsById(999L)).thenReturn(false);
        Mockito.when(mock.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        Mockito.when(mock.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());
        
        return mock;
    }

    /**
     * Creates a mock PatientService with default behavior.
     * 
     * @return Mock PatientService
     */
    @Bean
    @Primary
    public PatientService mockPatientService() {
        PatientService mock = Mockito.mock(PatientService.class);
        
        TestDataBuilder builder = new TestDataBuilder();
        DoctorDTO testDoctor = builder.buildTestDoctorDTO();
        TimeSlotDTO testTimeSlot = builder.buildTestTimeSlotDTO();
        AppointmentDTO testAppointment = builder.buildTestAppointmentDTO();
        
        Mockito.when(mock.getDoctorsByDepartment(Department.CARDIOLOGY)).thenReturn(Arrays.asList(testDoctor));
        Mockito.when(mock.getDoctorById(1L)).thenReturn(testDoctor);
        Mockito.when(mock.getAllSlots(1L, Mockito.any(LocalDate.class))).thenReturn(Arrays.asList(testTimeSlot));
        Mockito.when(mock.bookAppointment(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(1L);
        Mockito.when(mock.getUpcomingAppointments(1L)).thenReturn(Arrays.asList(testAppointment));
        Mockito.when(mock.getAllAppointments(1L)).thenReturn(Arrays.asList(testAppointment));
        Mockito.when(mock.cancelAppointment(1L)).thenReturn(1L);
        Mockito.when(mock.reschedule(1L, 2L)).thenReturn(testAppointment);
        Mockito.when(mock.getVisitHistoryFiltered(Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList(testAppointment));
        
        // Exception scenarios
        Mockito.when(mock.getDoctorById(999L))
                .thenThrow(new com.infy.exception.InfyHospitalException("Service.NO_DOCTOR_FOUND"));
        Mockito.when(mock.bookAppointment(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new com.infy.exception.InfyHospitalException("Service.ALREADY_BOOKED"));
        
        return mock;
    }

    /**
     * Creates a mock DoctorService with default behavior.
     * 
     * @return Mock DoctorService
     */
    @Bean
    @Primary
    public DoctorService mockDoctorService() {
        DoctorService mock = Mockito.mock(DoctorService.class);
        
        TestDataBuilder builder = new TestDataBuilder();
        TokenDTO testToken = builder.buildTestTokenDTO();
        
        Mockito.when(mock.getDailySchedule(1L)).thenReturn(Arrays.asList(testToken));
        Mockito.when(mock.getCurrentQueue(1L)).thenReturn(Arrays.asList(testToken));
        Mockito.doNothing().when(mock).updateTokenStatus(Mockito.anyLong(), Mockito.anyString());
        
        // Exception scenarios
        Mockito.when(mock.getDailySchedule(999L))
                .thenThrow(new com.infy.exception.InfyHospitalException("Service.NO_DOCTOR_FOUND"));
        Mockito.when(mock.getCurrentQueue(999L))
                .thenThrow(new com.infy.exception.InfyHospitalException("Service.NO_DOCTOR_FOUND"));
        Mockito.doThrow(new com.infy.exception.InfyHospitalException("Service.NO_TOKEN_FOUND"))
                .when(mock).updateTokenStatus(999L, Mockito.anyString());
        
        return mock;
    }

    /**
     * Creates a mock ReceptionistService with default behavior.
     * 
     * @return Mock ReceptionistService
     */
    @Bean
    @Primary
    public ReceptionistService mockReceptionistService() {
        ReceptionistService mock = Mockito.mock(ReceptionistService.class);
        
        TestDataBuilder builder = new TestDataBuilder();
        TokenDTO testToken = builder.buildTestTokenDTO();
        PatientDTO testPatient = builder.buildTestPatientDTO();
        
        Mockito.when(mock.registerWalkIn(Mockito.any(PatientDTO.class), Mockito.anyLong())).thenReturn(testToken);
        Mockito.when(mock.checkIn(Mockito.anyLong())).thenReturn(testToken);
        Mockito.when(mock.searchPatient(Mockito.anyString())).thenReturn(Arrays.asList(testPatient));
        Mockito.when(mock.getQueue(Mockito.anyLong())).thenReturn(Arrays.asList(testToken));
        
        // Exception scenarios
        Mockito.when(mock.registerWalkIn(Mockito.any(PatientDTO.class), Mockito.eq(999L)))
                .thenThrow(new com.infy.exception.InfyHospitalException("Service.NO_DOCTOR_FOUND"));
        Mockito.when(mock.checkIn(999L))
                .thenThrow(new com.infy.exception.InfyHospitalException("Service.APPOINTMENT_NOT_FOUND"));
        Mockito.when(mock.getQueue(999L))
                .thenThrow(new com.infy.exception.InfyHospitalException("No queue for this doctor today"));
        
        return mock;
    }

    /**
     * Creates a mock AdminService with default behavior.
     * 
     * @return Mock AdminService
     */
    @Bean
    @Primary
    public AdminService mockAdminService() {
        AdminService mock = Mockito.mock(AdminService.class);
        
        Mockito.when(mock.addDoctor(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                Mockito.any(Department.class), Mockito.any(Gender.class), Mockito.anyString(), 
                Mockito.anyString(), Mockito.anyString())).thenReturn(1L);
        Mockito.doNothing().when(mock).deleteDoctor(Mockito.anyLong());
        Mockito.doNothing().when(mock).configureDoctorSchedule(Mockito.anyLong(), Mockito.anyList(), 
                Mockito.any(LocalTime.class), Mockito.any(LocalTime.class), Mockito.anyInt()));
        Mockito.doNothing().when(mock).updateBookingRules(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()));
        
        // Exception scenarios
        Mockito.when(mock.addDoctor(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), 
                Mockito.any(Department.class), Mockito.any(Gender.class), Mockito.anyString(), 
                Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new com.infy.exception.InfyHospitalException("Service.DOCTOR_ALREADY_EXISTS"));
        Mockito.doThrow(new com.infy.exception.InfyHospitalException("Service.NO_DOCTOR_FOUND"))
                .when(mock).deleteDoctor(999L);
        Mockito.doThrow(new com.infy.exception.InfyHospitalException("Service.NO_DOCTOR_FOUND"))
                .when(mock).configureDoctorSchedule(999L, Mockito.anyList(), Mockito.any(LocalTime.class), 
                        Mockito.any(LocalTime.class), Mockito.anyInt());
        
        return mock;
    }

    /**
     * Creates a mock Environment for testing.
     * 
     * @return Mock Environment
     */
    @Bean
    @Primary
    public org.springframework.core.env.Environment mockEnvironment() {
        org.springframework.core.env.Environment mock = Mockito.mock(org.springframework.core.env.Environment.class);
        
        Mockito.when(mock.getProperty("API.ADD_DOCTOR_SUCCESS")).thenReturn("Doctor added successfully with ID: ");
        Mockito.when(mock.getProperty("API.DELETE_SUCCESS")).thenReturn("Doctor deleted successfully");
        Mockito.when(mock.getProperty("API.ADD_SUCCESS")).thenReturn("Operation completed successfully");
        Mockito.when(mock.getProperty("API.UPDATE_SUCCESS")).thenReturn("Settings updated successfully");
        Mockito.when(mock.getProperty("API.BOOKING_SUCCESS")).thenReturn("Appointment booked successfully with ID: ");
        Mockito.when(mock.getProperty("API.CANCELING_SUCCESS")).thenReturn("Appointment cancelled successfully with ID: ");
        Mockito.when(mock.getProperty("API.TOKEN_UPDATE_SUCCESS")).thenReturn("Token status updated successfully");
        
        return mock;
    }

    /**
     * Creates a mock ModelMapper for testing.
     * 
     * @return Mock ModelMapper
     */
    @Bean
    @Primary
    public org.modelmapper.ModelMapper mockModelMapper() {
        org.modelmapper.ModelMapper mock = Mockito.mock(org.modelmapper.ModelMapper.class);
        
        TestDataBuilder builder = new TestDataBuilder();
        
        // Entity to DTO mappings
        Mockito.when(mock.map(Mockito.any(Patient.class), Mockito.eq(PatientDTO.class)))
                .thenReturn(builder.buildTestPatientDTO());
        Mockito.when(mock.map(Mockito.any(Doctor.class), Mockito.eq(DoctorDTO.class)))
                .thenReturn(builder.buildTestDoctorDTO());
        Mockito.when(mock.map(Mockito.any(Appointment.class), Mockito.eq(AppointmentDTO.class)))
                .thenReturn(builder.buildTestAppointmentDTO());
        Mockito.when(mock.map(Mockito.any(Token.class), Mockito.eq(TokenDTO.class)))
                .thenReturn(builder.buildTestTokenDTO());
        
        // DTO to Entity mappings
        Mockito.when(mock.map(Mockito.any(PatientDTO.class), Mockito.eq(Patient.class)))
                .thenReturn(builder.buildTestPatient());
        Mockito.when(mock.map(Mockito.any(DoctorDTO.class), Mockito.eq(Doctor.class)))
                .thenReturn(builder.buildTestDoctor());
        Mockito.when(mock.map(Mockito.any(AppointmentDTO.class), Mockito.eq(Appointment.class)))
                .thenReturn(builder.buildTestAppointment());
        Mockito.when(mock.map(Mockito.any(TokenDTO.class), Mockito.eq(Token.class)))
                .thenReturn(builder.buildTestToken());
        
        return mock;
    }

    /**
     * Creates a mock BindingResult for testing validation scenarios.
     * 
     * @return Mock BindingResult
     */
    @Bean
    @Primary
    public org.springframework.validation.BindingResult mockBindingResult() {
        org.springframework.validation.BindingResult mock = Mockito.mock(org.springframework.validation.BindingResult.class);
        
        Mockito.when(mock.hasErrors()).thenReturn(false);
        Mockito.when(mock.getErrorCount()).thenReturn(0);
        Mockito.when(mock.getAllErrors()).thenReturn(Arrays.asList());
        
        return mock;
    }

    /**
     * Creates a mock BindingResult with validation errors for testing error scenarios.
     * 
     * @return Mock BindingResult with errors
     */
    @Bean("mockBindingResultWithErrors")
    public org.springframework.validation.BindingResult mockBindingResultWithErrors() {
        org.springframework.validation.BindingResult mock = Mockito.mock(org.springframework.validation.BindingResult.class);
        
        Mockito.when(mock.hasErrors()).thenReturn(true);
        Mockito.when(mock.getErrorCount()).thenReturn(1);
        Mockito.when(mock.getAllErrors()).thenReturn(Arrays.asList(
                new org.springframework.validation.FieldError("test", "field", "defaultMessage", false, null, null, "error message")
        ));
        
        return mock;
    }
}
