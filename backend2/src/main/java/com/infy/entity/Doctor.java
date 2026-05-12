package com.infy.entity;


 

import java.util.List;


 

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.infy.models.Department;

import com.infy.models.Gender;


 

import jakarta.persistence.CascadeType;

import jakarta.persistence.Entity;

import jakarta.persistence.EnumType;

import jakarta.persistence.Enumerated;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;

import jakarta.persistence.OneToMany;

import jakarta.persistence.OneToOne;

import jakarta.persistence.Table;

import lombok.Data;


 

@Entity

@Table(name = "doctors")

@Data

public class Doctor {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;


 

    @OneToOne(cascade = CascadeType.ALL)

    @JoinColumn(name="user_id",unique = true)

    private User user;

   

    private String name;

   

    @Enumerated(EnumType.STRING)

    private Department department;

   

    @Enumerated(EnumType.STRING)

    private Gender gender;

   

    private String phone;

    private String location;

    private String description;

   

    @OneToMany(mappedBy = "doctor", cascade=CascadeType.ALL, orphanRemoval = true)

    @JsonIgnore

    private List<DoctorSchedule> schedules;

}


