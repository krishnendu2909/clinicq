package com.infy.repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.infy.entity.Doctor;
import com.infy.entity.Token;
import com.infy.models.AppointmentStatus;
import com.infy.models.AppointmentType;
import com.infy.models.TokenStatus;
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    
    List<Token> findByDoctorAndCheckInTimeBetweenOrderByPositionAsc(Doctor doctor,LocalDateTime start,LocalDateTime end);

    Token findTopByDoctorIdAndDateOrderByTokenNumberDesc(Long doctorId, LocalDate now);
    List<Token> findByDoctorIdAndDateOrderByPositionAsc(Long DoctorId,LocalDate date);
    
    List<Token> findByDoctorIdAndDateAndStatusInOrderByPositionAsc(Long DoctorId,LocalDate date,List<TokenStatus> statuses);
    long countByDoctorAndDate(Doctor doctor, LocalDate date);
    

    // overall count for chart
    long countByCheckInTimeBetween(LocalDateTime start, LocalDateTime end);
    
    //total appointments
    long countByDoctorAndCheckInTimeBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);
    
    // count by appointment type
    @Query(""" 
            Select COUNT(t) 
            FROM Token t 
            WHERE t.doctor = :doctor
            AND t.appointment.type = :type
            AND t.checkInTime BETWEEN :start AND :end
            """)
    long countByAppointmentType(
            @Param("doctor") Doctor doctor,
            @Param("type") AppointmentType type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    // count by status
    @Query("""
            Select COUNT(t) 
            FROM Token t 
            WHERE t.doctor = :doctor
            AND t.appointment.status = :status
            AND t.checkInTime BETWEEN :start AND :end
            """)
    long countByAppointmentStatus(
            @Param("doctor") Doctor doctor,
            @Param("status") AppointmentStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    void deleteByDoctorId(Long doctorId);
    
    void deleteByDateBefore(LocalDate date);


}
 