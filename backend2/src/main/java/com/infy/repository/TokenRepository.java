package com.infy.repository;



 

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.List;


 

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


 

import com.infy.entity.Doctor;

import com.infy.entity.Token;

@Repository

public interface TokenRepository extends JpaRepository<Token, Long> {

   

    List<Token> findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(Doctor doctor,LocalDateTime start,LocalDateTime end);


 

    //Token findTopByDoctorIdAndDate(Long doctorId,LocalDate createdAt);

    List<Token> findByDoctorIdAndDateOrderByTokenNumberAsc(Long doctorId,LocalDate createdAt);

    Token findTopByDoctorIdAndDateOrderByTokenNumberDesc(Long doctorId, LocalDate now);

   

    long countByDoctorAndDate(Doctor doctor, LocalDate date);

    // Queue position functionality
    List<Token> findByPatientIdAndTimestampAfter(Long patientId, LocalDateTime timestamp);
    List<Token> findByDoctorIdAndStatusOrderByPosition(Long doctorId, String status); 

}
