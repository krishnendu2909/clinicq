package com.infy.repository;

import java.util.Optional;


 

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.entity.Patient;

import com.infy.entity.User;


 

public interface PatientRepository extends JpaRepository<Patient, Long>{



 

        Optional<Patient> findByPhone(String phone);

        Patient findByUser(User user);

   

}


