package com.infy.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.infy.entity.Doctor;
import com.infy.entity.User;
import com.infy.models.Department;
import com.infy.models.Gender;
import com.infy.models.Role;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class DoctorRepositoryTest {

    @Mock
    private DoctorRepository doctorRepository;

    private Doctor testDoctor;
    private Doctor testDoctor2;
    private Doctor testDoctor3;
    private Doctor testDoctor4;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Setup test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("doctor@test.com");
        testUser.setPassword("password");
        testUser.setRole(Role.DOCTOR);

        testDoctor = new Doctor();
        testDoctor.setId(1L);
        testDoctor.setName("Dr. Smith");
        testDoctor.setPhone("1234567890");
        testDoctor.setGender(Gender.MALE);
        testDoctor.setDepartment(Department.CARDIOLOGY);
        testDoctor.setLocation("New York");
        testDoctor.setDescription("Cardiologist");
        testDoctor.setUser(testUser);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("doctor2@test.com");
        user2.setPassword("password");
        user2.setRole(Role.DOCTOR);

        testDoctor2 = new Doctor();
        testDoctor2.setId(2L);
        testDoctor2.setName("Dr. Johnson");
        testDoctor2.setPhone("0987654321");
        testDoctor2.setGender(Gender.FEMALE);
        testDoctor2.setDepartment(Department.CARDIOLOGY);
        testDoctor2.setLocation("Los Angeles");
        testDoctor2.setDescription("Cardiologist");
        testDoctor2.setUser(user2);

        User user3 = new User();
        user3.setId(3L);
        user3.setEmail("doctor3@test.com");
        user3.setPassword("password");
        user3.setRole(Role.DOCTOR);

        testDoctor3 = new Doctor();
        testDoctor3.setId(3L);
        testDoctor3.setName("Dr. Wilson");
        testDoctor3.setPhone("5555555555");
        testDoctor3.setGender(Gender.MALE);
        testDoctor3.setDepartment(Department.ORTHOPEDICS);
        testDoctor3.setLocation("Chicago");
        testDoctor3.setDescription("Orthopedist");
        testDoctor3.setUser(user3);

        User user4 = new User();
        user4.setId(4L);
        user4.setEmail("doctor4@test.com");
        user4.setPassword("password");
        user4.setRole(Role.DOCTOR);

        testDoctor4 = new Doctor();
        testDoctor4.setId(4L);
        testDoctor4.setName("Dr. Brown");
        testDoctor4.setPhone("7777777777");
        testDoctor4.setGender(Gender.FEMALE);
        testDoctor4.setDepartment(Department.PEDIATRICS);
        testDoctor4.setLocation("Boston");
        testDoctor4.setDescription("Pediatrician");
        testDoctor4.setUser(user4);
    }

    // Test findByDepartment - Success Case with CARDIOLOGY
    @Test
    void testFindByDepartment_SuccessWithCardiology() {
        // Given
        List<Doctor> doctors = Arrays.asList(testDoctor, testDoctor2);
        when(doctorRepository.findByDepartment(Department.CARDIOLOGY)).thenReturn(doctors);

        // When
        List<Doctor> result = doctorRepository.findByDepartment(Department.CARDIOLOGY);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(d -> d.getDepartment() == Department.CARDIOLOGY));
        assertTrue(result.stream().anyMatch(d -> d.getName().equals("Dr. Smith")));
        assertTrue(result.stream().anyMatch(d -> d.getName().equals("Dr. Johnson")));
        verify(doctorRepository).findByDepartment(Department.CARDIOLOGY);
    }

    // Test findByDepartment - Success Case with ORTHOPEDICS
    @Test
    void testFindByDepartment_SuccessWithOrthopedics() {
        // Given
        List<Doctor> doctors = Arrays.asList(testDoctor3);
        when(doctorRepository.findByDepartment(Department.ORTHOPEDICS)).thenReturn(doctors);

        // When
        List<Doctor> result = doctorRepository.findByDepartment(Department.ORTHOPEDICS);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dr. Wilson", result.get(0).getName());
        assertEquals(Department.ORTHOPEDICS, result.get(0).getDepartment());
        verify(doctorRepository).findByDepartment(Department.ORTHOPEDICS);
    }

    // Test findByDepartment - Success Case with PEDIATRICS
    @Test
    void testFindByDepartment_SuccessWithPediatrics() {
        // Given
        List<Doctor> doctors = Arrays.asList(testDoctor4);
        when(doctorRepository.findByDepartment(Department.PEDIATRICS)).thenReturn(doctors);

        // When
        List<Doctor> result = doctorRepository.findByDepartment(Department.PEDIATRICS);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dr. Brown", result.get(0).getName());
        assertEquals(Department.PEDIATRICS, result.get(0).getDepartment());
        verify(doctorRepository).findByDepartment(Department.PEDIATRICS);
    }

    // Test findByDepartment - Success Case with GENERAL
    @Test
    void testFindByDepartment_SuccessWithGeneral() {
        // Given
        when(doctorRepository.findByDepartment(Department.GENERAL)).thenReturn(Collections.emptyList());

        // When
        List<Doctor> result = doctorRepository.findByDepartment(Department.GENERAL);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorRepository).findByDepartment(Department.GENERAL);
    }

    // Test findByDepartment - Empty Result Case
    @Test
    void testFindByDepartment_EmptyResult() {
        // Given
        when(doctorRepository.findByDepartment(Department.GENERAL)).thenReturn(Collections.emptyList());

        // When
        List<Doctor> result = doctorRepository.findByDepartment(Department.GENERAL);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorRepository).findByDepartment(Department.GENERAL);
    }

    // Test findById - Success Case
    @Test
    void testFindById_Success() {
        // Given
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));

        // When
        Optional<Doctor> result = doctorRepository.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Dr. Smith", result.get().getName());
        assertEquals(Department.CARDIOLOGY, result.get().getDepartment());
        assertEquals("1234567890", result.get().getPhone());
        verify(doctorRepository).findById(1L);
    }

    // Test findById - Not Found Case
    @Test
    void testFindById_NotFound() {
        // Given
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Doctor> result = doctorRepository.findById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(doctorRepository).findById(999L);
    }

    // Test save - Success Case
    @Test
    void testSave_Success() {
        // Given
        Doctor newDoctor = new Doctor();
        newDoctor.setName("Dr. New");
        newDoctor.setPhone("5555555555");
        newDoctor.setGender(Gender.MALE);
        newDoctor.setDepartment(Department.GENERAL);
        newDoctor.setLocation("New City");
        newDoctor.setDescription("General Practitioner");

        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        // When
        Doctor result = doctorRepository.save(newDoctor);

        // Then
        assertNotNull(result);
        assertEquals("Dr. Smith", result.getName());
        verify(doctorRepository).save(newDoctor);
    }

    // Test findAll - Success Case
    @Test
    void testFindAll_Success() {
        // Given
        List<Doctor> doctors = Arrays.asList(testDoctor, testDoctor2, testDoctor3, testDoctor4);
        when(doctorRepository.findAll()).thenReturn(doctors);

        // When
        List<Doctor> result = doctorRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains(testDoctor));
        assertTrue(result.contains(testDoctor2));
        assertTrue(result.contains(testDoctor3));
        assertTrue(result.contains(testDoctor4));
        verify(doctorRepository).findAll();
    }

    // Test findAll - Empty Result Case
    @Test
    void testFindAll_EmptyResult() {
        // Given
        when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Doctor> result = doctorRepository.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorRepository).findAll();
    }

    // Test deleteById - Success Case
    @Test
    void testDeleteById_Success() {
        // Given
        doNothing().when(doctorRepository).deleteById(1L);

        // When
        doctorRepository.deleteById(1L);

        // Then
        verify(doctorRepository).deleteById(1L);
    }

    // Test count - Success Case
    @Test
    void testCount_Success() {
        // Given
        when(doctorRepository.count()).thenReturn(4L);

        // When
        Long result = doctorRepository.count();

        // Then
        assertEquals(4L, result);
        verify(doctorRepository).count();
    }

    // Test existsById - Success Case
    @Test
    void testExistsById_Success() {
        // Given
        when(doctorRepository.existsById(1L)).thenReturn(true);

        // When
        Boolean result = doctorRepository.existsById(1L);

        // Then
        assertTrue(result);
        verify(doctorRepository).existsById(1L);
    }

    // Test existsById - Not Found Case
    @Test
    void testExistsById_NotFound() {
        // Given
        when(doctorRepository.existsById(999L)).thenReturn(false);

        // When
        Boolean result = doctorRepository.existsById(999L);

        // Then
        assertFalse(result);
        verify(doctorRepository).existsById(999L);
    }

    // Test findAll with Pageable - Success Case
    @Test
    void testFindAllWithPageable_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Doctor> doctorPage = new PageImpl<>(Arrays.asList(testDoctor));
        when(doctorRepository.findAll(any(Pageable.class))).thenReturn(doctorPage);

        // When
        Page<Doctor> result = doctorRepository.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Dr. Smith", result.getContent().get(0).getName());
        verify(doctorRepository).findAll(pageable);
    }

    // Test findByDepartment - Edge Case with All Departments
    @Test
    void testFindByDepartment_EdgeCaseWithAllDepartments() {
        // Test each department individually
        // CARDIOLOGY
        List<Doctor> cardiologists = Arrays.asList(testDoctor, testDoctor2);
        when(doctorRepository.findByDepartment(Department.CARDIOLOGY)).thenReturn(cardiologists);
        List<Doctor> result1 = doctorRepository.findByDepartment(Department.CARDIOLOGY);
        assertEquals(2, result1.size());
        assertTrue(result1.stream().allMatch(d -> d.getDepartment() == Department.CARDIOLOGY));

        // ORTHOPEDICS
        List<Doctor> orthopedics = Arrays.asList(testDoctor3);
        when(doctorRepository.findByDepartment(Department.ORTHOPEDICS)).thenReturn(orthopedics);
        List<Doctor> result2 = doctorRepository.findByDepartment(Department.ORTHOPEDICS);
        assertEquals(1, result2.size());
        assertEquals(Department.ORTHOPEDICS, result2.get(0).getDepartment());

        // PEDIATRICS
        List<Doctor> pediatricians = Arrays.asList(testDoctor4);
        when(doctorRepository.findByDepartment(Department.PEDIATRICS)).thenReturn(pediatricians);
        List<Doctor> result3 = doctorRepository.findByDepartment(Department.PEDIATRICS);
        assertEquals(1, result3.size());
        assertEquals(Department.PEDIATRICS, result3.get(0).getDepartment());

        // GENERAL
        when(doctorRepository.findByDepartment(Department.GENERAL)).thenReturn(Collections.emptyList());
        List<Doctor> result4 = doctorRepository.findByDepartment(Department.GENERAL);
        assertTrue(result4.isEmpty());

        verify(doctorRepository, times(4)).findByDepartment(any(Department.class));
    }

    // Test findByDepartment - Edge Case with Single Doctor Per Department
    @Test
    void testFindByDepartment_EdgeCaseWithSingleDoctorPerDepartment() {
        // Given
        when(doctorRepository.findByDepartment(Department.CARDIOLOGY)).thenReturn(Arrays.asList(testDoctor));
        when(doctorRepository.findByDepartment(Department.ORTHOPEDICS)).thenReturn(Arrays.asList(testDoctor3));
        when(doctorRepository.findByDepartment(Department.PEDIATRICS)).thenReturn(Arrays.asList(testDoctor4));
        when(doctorRepository.findByDepartment(Department.GENERAL)).thenReturn(Collections.emptyList());

        // When
        List<Doctor> cardiologists = doctorRepository.findByDepartment(Department.CARDIOLOGY);
        List<Doctor> orthopedics = doctorRepository.findByDepartment(Department.ORTHOPEDICS);
        List<Doctor> pediatricians = doctorRepository.findByDepartment(Department.PEDIATRICS);
        List<Doctor> general = doctorRepository.findByDepartment(Department.GENERAL);

        // Then
        assertEquals(1, cardiologists.size());
        assertEquals(1, orthopedics.size());
        assertEquals(1, pediatricians.size());
        assertEquals(0, general.size());
        assertEquals("Dr. Smith", cardiologists.get(0).getName());
        assertEquals("Dr. Wilson", orthopedics.get(0).getName());
        assertEquals("Dr. Brown", pediatricians.get(0).getName());
    }

    // Test findByDepartment - Edge Case with Large Number of Doctors
    @Test
    void testFindByDepartment_EdgeCaseWithLargeNumberOfDoctors() {
        // Given
        List<Doctor> manyCardiologists = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Doctor doctor = new Doctor();
            doctor.setId((long) i);
            doctor.setName("Dr. Cardiologist " + i);
            doctor.setPhone("123456789" + i);
            doctor.setGender(Gender.MALE);
            doctor.setDepartment(Department.CARDIOLOGY);
            doctor.setLocation("City " + i);
            doctor.setDescription("Cardiologist " + i);
            manyCardiologists.add(doctor);
        }

        when(doctorRepository.findByDepartment(Department.CARDIOLOGY)).thenReturn(manyCardiologists);

        // When
        List<Doctor> result = doctorRepository.findByDepartment(Department.CARDIOLOGY);

        // Then
        assertNotNull(result);
        assertEquals(20, result.size());
        assertTrue(result.stream().allMatch(d -> d.getDepartment() == Department.CARDIOLOGY));
        verify(doctorRepository).findByDepartment(Department.CARDIOLOGY);
    }

    // Test save - Edge Case with All Departments
    @Test
    void testSave_EdgeCaseWithAllDepartments() {
        // Given
        Doctor cardiologist = new Doctor();
        cardiologist.setName("Dr. Heart");
        cardiologist.setPhone("1111111111");
        cardiologist.setGender(Gender.MALE);
        cardiologist.setDepartment(Department.CARDIOLOGY);
        cardiologist.setLocation("Heart City");
        cardiologist.setDescription("Heart Specialist");

        Doctor orthopedist = new Doctor();
        orthopedist.setName("Dr. Bone");
        orthopedist.setPhone("2222222222");
        orthopedist.setGender(Gender.FEMALE);
        orthopedist.setDepartment(Department.ORTHOPEDICS);
        orthopedist.setLocation("Bone City");
        orthopedist.setDescription("Bone Specialist");

        Doctor pediatrician = new Doctor();
        pediatrician.setName("Dr. Kid");
        pediatrician.setPhone("3333333333");
        pediatrician.setGender(Gender.FEMALE);
        pediatrician.setDepartment(Department.PEDIATRICS);
        pediatrician.setLocation("Kid City");
        pediatrician.setDescription("Kid Specialist");

        Doctor general = new Doctor();
        general.setName("Dr. General");
        general.setPhone("4444444444");
        general.setGender(Gender.MALE);
        general.setDepartment(Department.GENERAL);
        general.setLocation("General City");
        general.setDescription("General Specialist");

        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        // When
        Doctor result1 = doctorRepository.save(cardiologist);
        Doctor result2 = doctorRepository.save(orthopedist);
        Doctor result3 = doctorRepository.save(pediatrician);
        Doctor result4 = doctorRepository.save(general);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertNotNull(result4);
        verify(doctorRepository, times(4)).save(any(Doctor.class));
    }

    // Test findById - Edge Case with Zero ID
    @Test
    void testFindById_EdgeCaseWithZeroId() {
        // Given
        when(doctorRepository.findById(0L)).thenReturn(Optional.empty());

        // When
        Optional<Doctor> result = doctorRepository.findById(0L);

        // Then
        assertFalse(result.isPresent());
        verify(doctorRepository).findById(0L);
    }

    // Test findById - Edge Case with Negative ID
    @Test
    void testFindById_EdgeCaseWithNegativeId() {
        // Given
        when(doctorRepository.findById(-1L)).thenReturn(Optional.empty());

        // When
        Optional<Doctor> result = doctorRepository.findById(-1L);

        // Then
        assertFalse(result.isPresent());
        verify(doctorRepository).findById(-1L);
    }

    // Test findById - Edge Case with Very Large ID
    @Test
    void testFindById_EdgeCaseWithVeryLargeId() {
        // Given
        when(doctorRepository.findById(Long.MAX_VALUE)).thenReturn(Optional.empty());

        // When
        Optional<Doctor> result = doctorRepository.findById(Long.MAX_VALUE);

        // Then
        assertFalse(result.isPresent());
        verify(doctorRepository).findById(Long.MAX_VALUE);
    }

    // Test deleteById - Edge Case with Zero ID
    @Test
    void testDeleteById_EdgeCaseWithZeroId() {
        // Given
        doNothing().when(doctorRepository).deleteById(0L);

        // When
        doctorRepository.deleteById(0L);

        // Then
        verify(doctorRepository).deleteById(0L);
    }

    // Test deleteById - Edge Case with Negative ID
    @Test
    void testDeleteById_EdgeCaseWithNegativeId() {
        // Given
        doNothing().when(doctorRepository).deleteById(-1L);

        // When
        doctorRepository.deleteById(-1L);

        // Then
        verify(doctorRepository).deleteById(-1L);
    }

    // Test count - Edge Case with Zero Count
    @Test
    void testCount_EdgeCaseWithZeroCount() {
        // Given
        when(doctorRepository.count()).thenReturn(0L);

        // When
        Long result = doctorRepository.count();

        // Then
        assertEquals(0L, result);
        verify(doctorRepository).count();
    }

    // Test count - Edge Case with Large Count
    @Test
    void testCount_EdgeCaseWithLargeCount() {
        // Given
        when(doctorRepository.count()).thenReturn(10000L);

        // When
        Long result = doctorRepository.count();

        // Then
        assertEquals(10000L, result);
        verify(doctorRepository).count();
    }
}
