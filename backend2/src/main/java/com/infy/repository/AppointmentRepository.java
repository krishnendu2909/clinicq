package com.infy.repository;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.infy.dto.AppointmentSearchDTO;
import com.infy.entity.Appointment;
import com.infy.models.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByDoctorId(Long doctorId);
    
    List<Appointment> findByStatus(AppointmentStatus status);
    
    Optional<Appointment> findByTimeSlotId(Long timeSlotId);
    
    @Query("""
            SELECT a FROM Appointment a
            WHERE a.patient.id = :patientId
            AND a.status = 'COMPLETED'
            AND (:doctorId IS NULL OR a.doctor.id = :doctorId)
            AND (:startDate IS NULL OR a.timeSlot.slotDate >= :startDate)
            AND (:endDate IS NULL OR a.timeSlot.slotDate <= :endDate)
            ORDER BY a.timeSlot.slotDate DESC, a.timeSlot.startTime DESC
            """)
            List<Appointment> findFilteredHistory(
            Long patientId,
            Long doctorId,
            LocalDate startDate,
            LocalDate endDate);
    
    List<Appointment> findByTimeSlot_SlotDate(LocalDate slotDate);

    @Query("""
            SELECT a FROM Appointment a
            WHERE a.status = 'BOOKED'
            AND (a.timeSlot.slotDate<:currentDate OR
                (a.timeSlot.slotDate=:currentDate AND a.timeSlot.startTime<:currentTime))""")    
    List<Appointment> missedAppointments(@Param("currentDate") LocalDate currentDate,
            @Param("currentTime") LocalTime currentTime);
    
    @Query("""
            SELECT new com.infy.dto.AppointmentSearchDTO(
            t.tokenDisplay,
            p.name,
            p.phone,
            p.dateOfBirth,
            p.gender,
            u.email,
            d.name,
            a.type,
            a.status,
            t.checkInTime,
            ts.slotDate,
            ts.startTime,
            ts.endTime
            
            )
            FROM Appointment a
            LEFT JOIN a.timeSlot ts 
            JOIN a.patient p 
            JOIN p.user u
            JOIN a.doctor d        
            LEFT JOIN Token t 
                ON t.appointment =a AND t.date=CURRENT_DATE    
            WHERE (
            LOWER(p.name) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR p.phone LIKE CONCAT('%',:keyword,'%'))    
            """)    
    List<AppointmentSearchDTO> findByPatientNameOrPhone(String keyword);
    
    @Query("""
            SELECT a FROM Appointment a where a.patient.id=:patientId
            AND a.timeSlot.slotDate=:date
            AND a.timeSlot.startTime=:time            
        """)
    Optional<Appointment> findConflictingAppointments( 
            @Param("patientId") Long patientId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
            );
    
    void deleteByDoctorId(Long doctorId);

} 
