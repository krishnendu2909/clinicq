package com.infy.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DailyPatientCountDTO {

    private LocalDate date;
    private Long patientCount;
}
