package com.infy.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

@Data
public class DoctorScheduleDTO {
	
	private Long id;
	private DoctorDTO doctor;	
	private DayOfWeek dayOfWeek;	
	private LocalTime startTime;
	private LocalTime endTime;	
	private Integer slotDuration;	
	private LocalDateTime createdAt;

}
