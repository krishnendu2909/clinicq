package com.infy.dto;

import java.time.LocalDateTime;

import lombok.Data;



@Data
public class AdminDTO {
	
	private Long id;	
	private Integer maxPatientsPerSlot;
	private Integer maxDaysInAdvance;
	private Integer cancellationCutoffHours;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
