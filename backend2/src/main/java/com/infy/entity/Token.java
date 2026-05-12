package com.infy.entity;


 

import java.time.LocalDate;

import java.time.LocalDateTime;


 

import com.infy.models.TokenStatus;


 

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

@Table(name = "tokens")

public class Token {


 

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private Integer tokenNumber;


 

    private String tokenDisplay;

    @ManyToOne

    @JoinColumn(name = "patient_id")

    private Patient patient;

   

    @ManyToOne

    @JoinColumn(name = "doctor_id")

    private Doctor doctor;

   

    @OneToOne

    @JoinColumn(name = "appointment_id")

    private Appointment appointment;    

   

    @Enumerated(EnumType.STRING)

    private TokenStatus status;

    private LocalDate date;

    private LocalDateTime checkInTime;  

}

