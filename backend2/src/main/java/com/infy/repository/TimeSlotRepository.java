package com.infy.repository;


 

import java.time.LocalDate;

import java.time.LocalTime;

import java.util.List;

import java.util.Optional;


 

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;


 

import com.infy.entity.Doctor;

import com.infy.entity.TimeSlot;


 

import jakarta.transaction.Transactional;


 

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

   

    List<TimeSlot> findByDoctorIdAndSlotDate(Long doctorId,LocalDate date);

   

    List<TimeSlot> findByDoctorIdAndSlotDateAndBookedFalse(Long doctorId,LocalDate date);

   

    Optional<TimeSlot> findByDoctorIdAndSlotDateAndStartTime(Long doctorId,LocalDate date,LocalTime startTime);

   

    Boolean existsByDoctorAndSlotDateAndStartTime(Doctor doctor, LocalDate slotDate, LocalTime startTime);


 

    @Transactional

    void deleteByDoctor(Doctor doctor);

   

    @Modifying

    @Transactional

    @Query("DELETE FROM TimeSlot t WHERE t.doctor.id = :doctorId AND t.booked = false AND t.slotDate > :today")

    void deleteFutureUnbookedSlots(@Param("doctorId") Long doctorId, @Param("today") LocalDate today);

}


