package com.infy.dto;

import java.time.LocalDateTime;

import com.infy.models.AppointmentStatus;
import com.infy.models.AppointmentType;

import lombok.Data;


@Data
public class AppointmentDTO {
	
	private Long id;
	private PatientDTO patient;
	private DoctorDTO doctor;
	private TimeSlotDTO timeSlot;	
	private String reason;
	private AppointmentStatus status;
	private AppointmentType type;
	private LocalDateTime createdAt;

}
