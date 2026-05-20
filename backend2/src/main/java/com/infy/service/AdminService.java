package com.infy.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.infy.dto.AdminStatsSummaryDTO;
import com.infy.dto.DailyPatientCountDTO;
import com.infy.dto.DoctorScheduleDTO;
import com.infy.exception.InfyHospitalException;
import com.infy.models.Department;
import com.infy.models.Gender;

public interface AdminService {

    public Long addDoctor(String email, String password, String name, Department department, 
            Gender gender, String phone, String location, String description,
            List<DayOfWeek> days, LocalTime startTime, LocalTime endTime, Integer slotDuration)throws InfyHospitalException;
    
    public void deleteDoctor(Long doctorId) throws InfyHospitalException;
    
    public void configureDoctorSchedule(Long doctorId, List<DayOfWeek> days, LocalTime startTime, 
            LocalTime endTime, Integer slotDuration) throws InfyHospitalException;
    
    public void generateTimeSlots(Long doctorId, int daysAhead) throws InfyHospitalException;
    
    public void updateBookingRules(int maxPatients, int maxDays, int cutoffHours) throws InfyHospitalException;
    
    List<DoctorScheduleDTO> getAllDoctorSchedules() throws InfyHospitalException;

    List<DailyPatientCountDTO> getChartData(LocalDate startDate, LocalDate endDate, Long doctorId)
            throws InfyHospitalException;

    AdminStatsSummaryDTO getSummary(Long doctorId, LocalDate startDate, LocalDate endDate) throws InfyHospitalException;
}
 