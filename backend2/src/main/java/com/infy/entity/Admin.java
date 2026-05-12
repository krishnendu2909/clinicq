package com.infy.entity;


 

import java.time.LocalDateTime;


 

import org.hibernate.annotations.CreationTimestamp;

import org.hibernate.annotations.UpdateTimestamp;


 

import jakarta.persistence.CascadeType;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;

import jakarta.persistence.OneToOne;

import jakarta.persistence.Table;

import lombok.Data;


 

@Entity

@Data

@Table(name="admin")

public class Admin {

   

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private Integer maxPatientsPerSlot;

    private Integer maxDaysInAdvance;

    private Integer cancellationCutoffHours;

   

    @CreationTimestamp

    private LocalDateTime createdAt;

   

    @UpdateTimestamp

    private LocalDateTime updatedAt;

}


