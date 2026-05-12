package com.infy.repository;


 

import java.time.DayOfWeek;

import java.util.List;


 

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;


 

import com.infy.entity.Doctor;

import com.infy.entity.DoctorSchedule;


 

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long>{


 

    public List<DoctorSchedule> findByDoctor(Doctor doctor);

   

    public boolean existsByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);

//  @Modifying      // Required for DELETE or UPDATE queries

//    @Transactional  // Required to allow the deletion to happen

//    void deleteByDoctorId(Long doctorId);

    @Modifying

    @Transactional

    @Query("DELETE FROM DoctorSchedule d WHERE d.doctor.id = :doctorId")

    void deleteByDoctorId(@Param("doctorId") Long doctorId);

}


