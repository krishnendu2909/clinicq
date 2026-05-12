package com.infy.entity;


 

import java.time.LocalDate;

import java.time.LocalDateTime;


 

import org.hibernate.annotations.CreationTimestamp;


 

import com.infy.models.Role;


 

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.EnumType;

import jakarta.persistence.Enumerated;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import jakarta.persistence.Table;

import lombok.Data;


 

@Entity

@Data

@Table(name = "users")

public class User {

   

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

   

    private String email;

   

    private String password;


 

    @Enumerated(EnumType.STRING)

    private Role role;

   

    @Column(name="created_at")

    @CreationTimestamp

    private LocalDate createdAt;

   

    private String resetToken;

    private LocalDateTime tokenExpiry;

   

    private int failedAttempts;

    private Boolean accountLocked;

    private LocalDateTime lockTime;

}




 