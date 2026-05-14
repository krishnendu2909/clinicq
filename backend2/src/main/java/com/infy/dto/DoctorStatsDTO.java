package com.infy.dto;



import lombok.Data;



@Data

public class DoctorStatsDTO {



private Long doctorId;

private String doctorName;

private Long appointmentCount;

private Long walkInCount;

private Long completedCount;

private Long noShowCount;

}

