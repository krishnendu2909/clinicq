package com.infy.entity;

import java.time.LocalDate;

import com.infy.models.Gender;

import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.EnumType;

import jakarta.persistence.Enumerated;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;

import jakarta.persistence.OneToOne;

import jakarta.persistence.Table;

import lombok.Data;


 

@Entity

@Data

@Table(name = "patients")

public class Patient {

   

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

   

    @OneToOne(cascade = CascadeType.ALL)

    @JoinColumn(name="user_id",unique = true)

    private User user;

   

    private String name;

   

    private LocalDate dateOfBirth;

   

    @Enumerated(EnumType.STRING)

    private Gender gender;


 

    @Column(unique = true,nullable = false)

    private String phone;

   

}

