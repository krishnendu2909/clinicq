package com.infy.entity;


 

import java.time.DayOfWeek;

import java.time.LocalDateTime;

import java.time.LocalTime;


 

import org.hibernate.annotations.CreationTimestamp;


 

import jakarta.persistence.Entity;

import jakarta.persistence.EnumType;

import jakarta.persistence.Enumerated;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;

import lombok.Data;


 

@Entity

@Data

public class DoctorSchedule {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

   

    @ManyToOne

    @JoinColumn(name = "doctor_id")

    private Doctor doctor;

   

    @Enumerated(EnumType.STRING)

    private DayOfWeek dayOfWeek;

   

    private LocalTime startTime;

    private LocalTime endTime;

   

    private Integer slotDuration;


 

    @CreationTimestamp

    private LocalDateTime createdAt;

   

   

   

}


