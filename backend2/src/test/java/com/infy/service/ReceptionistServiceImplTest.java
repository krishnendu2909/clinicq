package com.infy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.infy.dto.AppointmentDTO;
import com.infy.dto.PatientDTO;
import com.infy.dto.TokenDTO;
import com.infy.dto.UserDTO;
import com.infy.entity.Appointment;
import com.infy.entity.Doctor;
import com.infy.entity.Patient;
import com.infy.entity.Token;
import com.infy.entity.User;
import com.infy.exception.InfyHospitalException;
import com.infy.models.AppointmentStatus;
import com.infy.models.AppointmentType;
import com.infy.models.Gender;
import com.infy.models.Role;
import com.infy.models.TokenStatus;
import com.infy.repository.AppointmentRepository;
import com.infy.repository.DoctorRepository;
import com.infy.repository.PatientRepository;
import com.infy.repository.TokenRepository;
import com.infy.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ReceptionistServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReceptionistServiceImpl receptionistService;

    private Doctor testDoctor;
    private Patient testPatient;
    private User testUser;
    private Token testToken;
    private Appointment testAppointment;
    private PatientDTO testPatientDTO;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("patient@test.com");
        testUser.setPassword("password");
        testUser.setRole(Role.PATIENT);
        testUser.setCreatedAt(LocalDate.now());

        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setEmail("patient@test.com");
        testUserDTO.setPassword("password");
        testUserDTO.setRole(Role.PATIENT);

        testDoctor = new Doctor();
        testDoctor.setId(1L);
        testDoctor.setName("Dr. Smith");
        testDoctor.setPhone("1234567890");
        testDoctor.setGender(Gender.MALE);

        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setName("John Doe");
        testPatient.setPhone("9876543210");
        testPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPatient.setGender(Gender.MALE);
        testPatient.setUser(testUser);

        testPatientDTO = new PatientDTO();
        testPatientDTO.setId(1L);
        testPatientDTO.setName("John Doe");
        testPatientDTO.setPhone("9876543210");
        testPatientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPatientDTO.setGender(Gender.MALE);
        testPatientDTO.setUser(testUserDTO);

        testAppointment = new Appointment();
        testAppointment.setId(1L);
        testAppointment.setDoctor(testDoctor);
        testAppointment.setPatient(testPatient);
        testAppointment.setStatus(AppointmentStatus.BOOKED);
        testAppointment.setType(AppointmentType.PRE_BOOKED);

        testToken = new Token();
        testToken.setId(1L);
        testToken.setDoctor(testDoctor);
        testToken.setPatient(testPatient);
        testToken.setTokenNumber(1);
        testToken.setDate(LocalDate.now());
        testToken.setStatus(TokenStatus.WAITING);
        testToken.setCheckInTime(LocalDateTime.now());
        testToken.setAppointment(testAppointment);
    }

    // Test registerWalkIn - Success Case with New User
    @Test
    void testRegisterWalkIn_SuccessWithNewUser() throws InfyHospitalException {
        // Given
        testUserDTO.setId(null); // New user
        testPatientDTO.setUser(testUserDTO);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now()))
                .thenReturn(null);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);
        when(tokenRepository.save(any(Token.class))).thenReturn(testToken);

        // When
        TokenDTO result = receptionistService.registerWalkIn(testPatientDTO, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.getStatus());
        verify(doctorRepository).findById(1L);
        verify(userRepository).save(any(User.class));
        verify(patientRepository).save(any(Patient.class));
        verify(tokenRepository).findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now());
        verify(appointmentRepository).save(any(Appointment.class));
        verify(tokenRepository).save(any(Token.class));
    }

    // Test registerWalkIn - Success Case with Existing User
    @Test
    void testRegisterWalkIn_SuccessWithExistingUser() throws InfyHospitalException {
        // Given
        testPatientDTO.setUser(testUserDTO); // Existing user

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now()))
                .thenReturn(null);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);
        when(tokenRepository.save(any(Token.class))).thenReturn(testToken);

        // When
        TokenDTO result = receptionistService.registerWalkIn(testPatientDTO, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.getStatus());
        verify(doctorRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(patientRepository).save(any(Patient.class));
        verify(tokenRepository).findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now());
        verify(appointmentRepository).save(any(Appointment.class));
        verify(tokenRepository).save(any(Token.class));
    }

    // Test registerWalkIn - Success Case with Sequential Token Number
    @Test
    void testRegisterWalkIn_SuccessWithSequentialTokenNumber() throws InfyHospitalException {
        // Given
        Token existingToken = new Token();
        existingToken.setId(2L);
        existingToken.setTokenNumber(5); // Last token was 5

        testPatientDTO.setUser(testUserDTO);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now()))
                .thenReturn(existingToken);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);
        
        // Create a token with the expected token number (6)
        Token newToken = new Token();
        newToken.setId(3L);
        newToken.setTokenNumber(6);
        newToken.setDoctor(testDoctor);
        newToken.setDate(LocalDate.now());
        newToken.setPatient(testPatient);
        
        when(tokenRepository.save(any(Token.class))).thenReturn(newToken);

        // When
        TokenDTO result = receptionistService.registerWalkIn(testPatientDTO, 1L);

        // Then
        assertNotNull(result);
        assertEquals(6, result.getTokenNumber()); // Should be 5 + 1
        verify(tokenRepository).findTopByDoctorIdAndDateOrderByTokenNumberDesc(eq(1L), eq(LocalDate.now()));
    }

    // Test registerWalkIn - Doctor Not Found Exception
    @Test
    void testRegisterWalkIn_DoctorNotFound_ThrowsException() {
        // Given
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setName("John Doe");
        patientDTO.setPhone("9876543210");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setGender(Gender.MALE);
        patientDTO.setUser(testUserDTO);
        
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            receptionistService.registerWalkIn(patientDTO, 999L);
        });
        
        assertEquals("Service.NO_DOCTOR_FOUND", exception.getMessage());
        verify(doctorRepository).findById(999L);
        verify(patientRepository, never()).save(any(Patient.class));
        verify(userRepository, never()).save(any(User.class));
        verify(tokenRepository, never()).findTopByDoctorIdAndDateOrderByTokenNumberDesc(anyLong(), any(LocalDate.class));
        verify(tokenRepository, never()).save(any(Token.class));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    // Test registerWalkIn - User Details Required Exception
    @Test
    void testRegisterWalkIn_UserDetailsRequired_ThrowsException() {
        // Given
        testPatientDTO.setUser(null);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            receptionistService.registerWalkIn(testPatientDTO, 1L);
        });
        assertEquals("User details are required", exception.getMessage());
        verify(doctorRepository).findById(1L);
    }

    // Test registerWalkIn - User Not Found Exception
    @Test
    void testRegisterWalkIn_UserNotFound_ThrowsException() {
        // Given
        testUserDTO.setId(999L);
        testPatientDTO.setUser(testUserDTO);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            receptionistService.registerWalkIn(testPatientDTO, 1L);
        });
        assertEquals("User not found", exception.getMessage());
        verify(doctorRepository).findById(1L);
        verify(userRepository).findById(999L);
    }

    // Test registerWalkIn - Email and Password Required Exception
    @Test
    void testRegisterWalkIn_EmailAndPasswordRequired_ThrowsException() {
        // Given
        testUserDTO.setId(null);
        testUserDTO.setEmail(null);
        testUserDTO.setPassword(null);
        testPatientDTO.setUser(testUserDTO);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            receptionistService.registerWalkIn(testPatientDTO, 1L);
        });
        assertEquals("Email and Password are required", exception.getMessage());
        verify(doctorRepository).findById(1L);
    }

    // Test registerWalkIn - Email Required Exception
    @Test
    void testRegisterWalkIn_EmailRequired_ThrowsException() {
        // Given
        testUserDTO.setId(null);
        testUserDTO.setEmail(null);
        testUserDTO.setPassword("password");
        testPatientDTO.setUser(testUserDTO);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            receptionistService.registerWalkIn(testPatientDTO, 1L);
        });
        assertEquals("Email and Password are required", exception.getMessage());
        verify(doctorRepository).findById(1L);
    }

    // Test registerWalkIn - Password Required Exception
    @Test
    void testRegisterWalkIn_PasswordRequired_ThrowsException() {
        // Given
        testUserDTO.setId(null);
        testUserDTO.setEmail("test@test.com");
        testUserDTO.setPassword(null);
        testPatientDTO.setUser(testUserDTO);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            receptionistService.registerWalkIn(testPatientDTO, 1L);
        });
        assertEquals("Email and Password are required", exception.getMessage());
        verify(doctorRepository).findById(1L);
    }

    // Test checkIn - Success Case
    @Test
    void testCheckIn_Success() throws InfyHospitalException {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now()))
                .thenReturn(null);
        when(tokenRepository.save(any(Token.class))).thenReturn(testToken);

        // When
        TokenDTO result = receptionistService.checkIn(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.getStatus());
        assertEquals(AppointmentStatus.CHECKED_IN, testAppointment.getStatus());
        verify(appointmentRepository).findById(1L);
        verify(appointmentRepository).save(testAppointment);
        verify(tokenRepository).findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now());
        verify(tokenRepository).save(any(Token.class));
    }

    // Test checkIn - Success Case with Sequential Token Number
    @Test
    void testCheckIn_SuccessWithSequentialTokenNumber() throws InfyHospitalException {
        // Given
        Token existingToken = new Token();
        existingToken.setId(2L);
        existingToken.setTokenNumber(3); // Last token was 3

        // Create a new token that will be returned by save() with the expected token number
        Token newToken = new Token();
        newToken.setId(3L);
        newToken.setDoctor(testDoctor);
        newToken.setPatient(testPatient);
        newToken.setTokenNumber(4); // Should be 3 + 1
        newToken.setDate(LocalDate.now());
        newToken.setStatus(TokenStatus.WAITING);
        newToken.setCheckInTime(LocalDateTime.now());
        newToken.setAppointment(testAppointment);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now()))
                .thenReturn(existingToken);
        when(tokenRepository.save(any(Token.class))).thenReturn(newToken);

        // When
        TokenDTO result = receptionistService.checkIn(1L);

        // Then
        assertNotNull(result);
        assertEquals(4, result.getTokenNumber()); // Should be 3 + 1
        verify(appointmentRepository).findById(1L);
        verify(appointmentRepository).save(any(Appointment.class));
        verify(tokenRepository).findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now());
        verify(tokenRepository).save(any(Token.class));
    }

    // Test checkIn - Appointment Not Found Exception
    @Test
    void testCheckIn_AppointmentNotFound_ThrowsException() {
        // Given
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            receptionistService.checkIn(999L);
        });
        assertEquals("Service.APPOINTMENT_NOT_FOUND", exception.getMessage());
        verify(appointmentRepository).findById(999L);
    }

    // Test searchPatient - Success Case with Name Match
    @Test
    void testSearchPatient_SuccessWithNameMatch() throws InfyHospitalException {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("John", "John"))
                .thenReturn(patients);

        // When
        List<PatientDTO> result = receptionistService.searchPatient("John");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("9876543210", result.get(0).getPhone());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("John", "John");
    }

    // Test searchPatient - Success Case with Phone Match
    @Test
    void testSearchPatient_SuccessWithPhoneMatch() throws InfyHospitalException {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("9876543210", "9876543210"))
                .thenReturn(patients);

        // When
        List<PatientDTO> result = receptionistService.searchPatient("9876543210");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("9876543210", result.get(0).getPhone());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("9876543210", "9876543210");
    }

    // Test searchPatient - Success Case with Partial Name Match
    @Test
    void testSearchPatient_SuccessWithPartialNameMatch() throws InfyHospitalException {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("Joh", "Joh"))
                .thenReturn(patients);

        // When
        List<PatientDTO> result = receptionistService.searchPatient("Joh");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("Joh", "Joh");
    }

    // Test searchPatient - Success Case with Multiple Matches
    @Test
    void testSearchPatient_SuccessWithMultipleMatches() throws InfyHospitalException {
        // Given
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("John Smith");
        patient2.setPhone("1234567890");
        patient2.setDateOfBirth(LocalDate.of(1985, 5, 15));
        patient2.setGender(Gender.MALE);

        List<Patient> patients = Arrays.asList(testPatient, patient2);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("John", "John"))
                .thenReturn(patients);

        // When
        List<PatientDTO> result = receptionistService.searchPatient("John");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("John Doe")));
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("John Smith")));
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("John", "John");
    }

    // Test searchPatient - Success Case with Empty Result
    @Test
    void testSearchPatient_SuccessWithEmptyResult() throws InfyHospitalException {
        // Given
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("Unknown", "Unknown"))
                .thenReturn(Collections.emptyList());

        // When
        List<PatientDTO> result = receptionistService.searchPatient("Unknown");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("Unknown", "Unknown");
    }

    // Test searchPatient - Success Case with Case Insensitive Search
    @Test
    void testSearchPatient_SuccessWithCaseInsensitiveSearch() throws InfyHospitalException {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("john", "john"))
                .thenReturn(patients);

        // When
        List<PatientDTO> result = receptionistService.searchPatient("john");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("john", "john");
    }

    // Test getQueue - Success Case
    @Test
    void testGetQueue_Success() throws InfyHospitalException {
        // Given
        List<Token> tokens = Arrays.asList(testToken);
        when(tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, LocalDate.now()))
                .thenReturn(tokens);

        // When
        List<TokenDTO> result = receptionistService.getQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.get(0).getStatus());
        verify(tokenRepository).findByDoctorIdAndDateOrderByTokenNumberAsc(1L, LocalDate.now());
    }

    // Test getQueue - Success Case with Multiple Tokens
    @Test
    void testGetQueue_SuccessWithMultipleTokens() throws InfyHospitalException {
        // Given
        Token token2 = new Token();
        token2.setId(2L);
        token2.setDoctor(testDoctor);
        token2.setPatient(testPatient);
        token2.setTokenNumber(2);
        token2.setDate(LocalDate.now());
        token2.setStatus(TokenStatus.WAITING);
        token2.setCheckInTime(LocalDateTime.now().plusMinutes(30));

        List<Token> tokens = Arrays.asList(testToken, token2);
        when(tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, LocalDate.now()))
                .thenReturn(tokens);

        // When
        List<TokenDTO> result = receptionistService.getQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getTokenNumber());
        assertEquals(2, result.get(1).getTokenNumber());
        verify(tokenRepository).findByDoctorIdAndDateOrderByTokenNumberAsc(1L, LocalDate.now());
    }

    // Test getQueue - No Queue Exception
    @Test
    void testGetQueue_NoQueue_ThrowsException() {
        // Given
        when(tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, LocalDate.now()))
                .thenReturn(Collections.emptyList());

        // When & Then
        InfyHospitalException exception = assertThrows(InfyHospitalException.class, () -> {
            receptionistService.getQueue(1L);
        });
        assertEquals("No queue for this doctor today", exception.getMessage());
        verify(tokenRepository).findByDoctorIdAndDateOrderByTokenNumberAsc(1L, LocalDate.now());
    }

    // Test getQueue - Filter Non-Waiting Tokens
    @Test
    void testGetQueue_FilterNonWaitingTokens() throws InfyHospitalException {
        // Given
        Token inConsultationToken = new Token();
        inConsultationToken.setId(3L);
        inConsultationToken.setDoctor(testDoctor);
        inConsultationToken.setPatient(testPatient);
        inConsultationToken.setTokenNumber(3);
        inConsultationToken.setDate(LocalDate.now());
        inConsultationToken.setStatus(TokenStatus.IN_CONSULTATION);
        inConsultationToken.setCheckInTime(LocalDateTime.now().plusMinutes(60));

        Token completedToken = new Token();
        completedToken.setId(4L);
        completedToken.setDoctor(testDoctor);
        completedToken.setPatient(testPatient);
        completedToken.setTokenNumber(4);
        completedToken.setDate(LocalDate.now());
        completedToken.setStatus(TokenStatus.COMPLETED);
        completedToken.setCheckInTime(LocalDateTime.now().plusMinutes(90));

        List<Token> allTokens = Arrays.asList(testToken, inConsultationToken, completedToken);
        when(tokenRepository.findByDoctorIdAndDateOrderByTokenNumberAsc(1L, LocalDate.now()))
                .thenReturn(allTokens);

        // When
        List<TokenDTO> result = receptionistService.getQueue(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size()); // Only waiting token
        assertEquals(1, result.get(0).getTokenNumber());
        assertEquals(TokenStatus.WAITING, result.get(0).getStatus());
        verify(tokenRepository).findByDoctorIdAndDateOrderByTokenNumberAsc(1L, LocalDate.now());
    }

    // Test registerWalkIn - Edge Case with Empty Patient Name
    @Test
    void testRegisterWalkIn_EmptyPatientName() throws InfyHospitalException {
        // Given
        testPatientDTO.setName("");
        testPatientDTO.setUser(testUserDTO);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now()))
                .thenReturn(null);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);
        when(tokenRepository.save(any(Token.class))).thenReturn(testToken);

        // When
        TokenDTO result = receptionistService.registerWalkIn(testPatientDTO, 1L);

        // Then
        assertNotNull(result);
        verify(patientRepository).save(any(Patient.class));
    }

    // Test registerWalkIn - Edge Case with Very Long Patient Name
    @Test
    void testRegisterWalkIn_VeryLongPatientName() throws InfyHospitalException {
        // Given
        String longName = "A".repeat(200); // Very long name
        testPatientDTO.setName(longName);
        testPatientDTO.setUser(testUserDTO);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);
        when(tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(1L, LocalDate.now()))
                .thenReturn(null);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);
        when(tokenRepository.save(any(Token.class))).thenReturn(testToken);

        // When
        TokenDTO result = receptionistService.registerWalkIn(testPatientDTO, 1L);

        // Then
        assertNotNull(result);
        verify(patientRepository).save(any(Patient.class));
    }

    // Test searchPatient - Edge Case with Empty Keyword
    @Test
    void testSearchPatient_EmptyKeyword() throws InfyHospitalException {
        // Given
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("", ""))
                .thenReturn(Collections.emptyList());

        // When
        List<PatientDTO> result = receptionistService.searchPatient("");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("", "");
    }

    // Test searchPatient - Edge Case with Special Characters
    @Test
    void testSearchPatient_SpecialCharacters() throws InfyHospitalException {
        // Given
        Patient specialPatient = new Patient();
        specialPatient.setId(3L);
        specialPatient.setName("John-Doe");
        specialPatient.setPhone("123-456-7890");
        specialPatient.setDateOfBirth(LocalDate.of(1992, 3, 15));
        specialPatient.setGender(Gender.MALE);

        List<Patient> patients = Arrays.asList(specialPatient);
        when(patientRepository.findByNameContainingIgnoreCaseOrPhoneContaining("John-Doe", "John-Doe"))
                .thenReturn(patients);

        // When
        List<PatientDTO> result = receptionistService.searchPatient("John-Doe");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John-Doe", result.get(0).getName());
        verify(patientRepository).findByNameContainingIgnoreCaseOrPhoneContaining("John-Doe", "John-Doe");
    }
}
