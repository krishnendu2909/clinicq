package com.infy.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.infy.models.TokenSource;
import com.infy.models.TokenStatus;

import lombok.Data;

@Data
public class TokenDTO {
	
	private Long id;
	private Integer tokenNumber;
	private PatientDTO patient;
	private DoctorDTO doctor;
	private AppointmentDTO appointment;
	private TokenStatus status;
	
	private LocalDateTime checkInTime;
	private LocalDate date;

}
