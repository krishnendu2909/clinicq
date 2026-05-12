package com.infy.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

@Data
public class TimeSlotDTO {
	
	private Long id;
	private DoctorDTO doctor;
	private LocalDate slotDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private Boolean booked;
	private LocalDateTime createdAt;

}
