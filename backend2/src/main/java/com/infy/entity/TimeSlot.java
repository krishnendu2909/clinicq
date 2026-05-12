package com.infy.entity;


 

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.time.LocalTime;


 

import org.hibernate.annotations.CreationTimestamp;


 

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;

import lombok.Data;


 

@Entity

@Data

@Table(name = "time_slots")

public class TimeSlot {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

   

    @ManyToOne

    @JoinColumn(name = "doctor_id")

    private Doctor doctor;

   

    private LocalDate slotDate;

    private LocalTime startTime;

    private LocalTime endTime;

    @Column(name = "booked", columnDefinition = "TINYINT(1) DEFAULT 0")

    private Boolean booked;

   

    @CreationTimestamp

    private LocalDateTime createdAt;

   

}

