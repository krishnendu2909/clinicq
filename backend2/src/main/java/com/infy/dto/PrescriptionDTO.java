package com.infy.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.infy.models.PrescriptionStatus;

import lombok.Data;

@Data
public class PrescriptionDTO {
    
    private Long id;
    private Long appointmentId;    
    private String diagnosis;
    private String notes;
    private PrescriptionStatus status;
    private LocalDateTime createdAt;
    
    private List<PrescriptionItemDTO> medicines;

}