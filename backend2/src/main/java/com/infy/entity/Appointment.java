package com.infy.entity;


 

import java.time.LocalDateTime;


 

import org.hibernate.annotations.CreationTimestamp;


 

import com.infy.models.AppointmentStatus;

import com.infy.models.AppointmentType;


 

import jakarta.persistence.Entity;

import jakarta.persistence.EnumType;

import jakarta.persistence.Enumerated;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;

import jakarta.persistence.OneToOne;

import jakarta.persistence.Table;

import lombok.Data;


 

@Entity

@Data

@Table(name = "appointments")

public class Appointment {

   

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

   

    @ManyToOne

    @JoinColumn(name = "patient_id")

    private Patient patient;

   

    @ManyToOne

    @JoinColumn(name = "doctor_id")

    private Doctor doctor;

   

    @OneToOne

    @JoinColumn(name = "time_slot_id")

    private TimeSlot timeSlot;

   

    private String reason;

   

    @Enumerated(EnumType.STRING)

    private AppointmentStatus status;


 

    @Enumerated(EnumType.STRING)

    private AppointmentType type;


 

    @CreationTimestamp

    private LocalDateTime createdAt;


 

}
