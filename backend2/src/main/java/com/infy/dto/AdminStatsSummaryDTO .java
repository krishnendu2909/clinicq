package com.infy.dto;

import java.util.List;

import lombok.Data;

@Data
public class AdminStatsSummaryDTO {

    private Long totalAppointments;
    private Long totalWalkIns;
    private Long totalCompleted;
    private Long totalNoShows;
    private List<DoctorStatsDTO> doctorBreakdown;
}
