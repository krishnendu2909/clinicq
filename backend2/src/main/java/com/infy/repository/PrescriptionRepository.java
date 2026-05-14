package com.infy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.infy.entity.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {
    
    @Query("SELECT p FROM Prescription p where "
            + "p.appointment.patient.id=:patientId AND "
            + "p.appointment.id=:appointmentId AND "
            + "p.status='FINAL'")
    Optional<Prescription> findByPatientIdAndAppointmentId(@Param("patientId")Long patientId
            ,@Param("appointmentId")Long appointmentId);

     Optional<Prescription>findByAppointmentId(Long id);

}