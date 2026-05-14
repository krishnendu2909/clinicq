package com.infy.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.infy.models.AppointmentStatus;
import com.infy.models.AppointmentType;
import com.infy.models.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppointmentSearchDTO {
    
    private String tokenDisplay;
    private String patientName;
    private String phone;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String email;
    private String doctorName;
    private    AppointmentType appointmentType;
    private AppointmentStatus appointmentStatus;
    private LocalDateTime checkInTime;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;

}