package com.infy.dto;


 

import java.time.LocalDate;

import java.time.LocalDateTime;


 

import com.infy.models.AppointmentStatus;

import com.infy.models.AppointmentType;


 

import lombok.AllArgsConstructor;

import lombok.Data;


 

@Data

@AllArgsConstructor

public class AppointmentSearchDTO {

   

    private String tokenDisplay;

    private String patientName;

    private String phone;

    private LocalDate dateOfBirth;

    private String doctorName;

    private AppointmentType appointmentType;

    private AppointmentStatus appointmentStatus;

    private LocalDateTime checkInTime;

   


 

}

