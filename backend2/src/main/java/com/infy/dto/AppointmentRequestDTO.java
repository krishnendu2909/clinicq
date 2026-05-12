package com.infy.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Add this
@AllArgsConstructor
public class AppointmentRequestDTO {
	
	@Max(value=50, message="{doctor.doctorId.max}")
	@NotNull(message = "{doctor.doctorId.notNull}")
    @Min(value = 1, message = "{doctor.doctorId.min}")
	private Long doctorId;

	@NotNull(message = "{timeSlot.timeSlotId.notNull}")
    @Min(value = 1, message = "{timeSlot.timeSlotId.min}")
	private Long slotId;
	 
	 
	private String reason;

}
