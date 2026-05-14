package com.infy.api;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.AdminStatsSummaryDTO;
import com.infy.dto.DailyPatientCountDTO;
import com.infy.dto.DoctorScheduleDTO;
import com.infy.exception.InfyHospitalException;
import com.infy.models.Department;
import com.infy.models.Gender;
import com.infy.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@RestController
@Validated
@RequestMapping("/clinicq/admin")

@CrossOrigin(origins = "http://localhost:3000")

@Tag(name = "Admin APIs",description = "Operations related to admin")
public class AdminApi {
    @Autowired
    private AdminService adminService;
    @Autowired
    private Environment environment;

    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorScheduleDTO>> getAllDoctors() throws InfyHospitalException {
        List<DoctorScheduleDTO> schedules = adminService.getAllDoctorSchedules();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/doctor")
    @Operation(summary = "Add doctor")
    public ResponseEntity<String> addDoctor(
            @RequestParam @Email @NotNull(message = "{admin.email.notNull}") String email, 
            @RequestParam @NotNull(message = "{admin.password.notNull}") String password, 
            @RequestParam @NotNull(message = "{admin.name.notNull}") String name, 
            @RequestParam @NotNull(message = "{admin.department.notNull}") Department department, 
            @RequestParam @NotNull(message = "{admin.gender.notNull}") Gender gender, 
            @RequestParam @NotNull(message = "{admin.phone.notNull}") @Pattern(regexp = "[0-9]{10}",
            message = "{admin.phone.pattern}") String phone, 
            @RequestParam @NotNull(message = "{admin.location.notNull}") String location, 
            @RequestParam String description,
            @RequestParam List<DayOfWeek> days,      
            @RequestParam @NotNull(message = "{admin.startTime.notNull}") String startTime,    
            @RequestParam @NotNull(message = "{admin.endTime.notNull}") String endTime,      
            @RequestParam @NotNull(message = "{admin.slotDuration.notNull}") @Min(1) Integer slotDuration )throws InfyHospitalException{
        
        Long doctorId=adminService.addDoctor(email, password, name, department, gender, phone, location, description,
                days, LocalTime.parse(startTime), LocalTime.parse(endTime), slotDuration);
        
        String successMessage=environment.getProperty("API.ADD_DOCTOR_SUCCESS")+doctorId;
        
        return new ResponseEntity<>(successMessage,HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/doctor/{doctorId}")
    @Operation(summary = "Delete doctor")
    public ResponseEntity<String> deleteDoctor(
            @PathVariable 
            @NotNull(message = "{doctor.doctorId.notNull}") 
            Long doctorId) 
                    throws InfyHospitalException{
        adminService.deleteDoctor(doctorId);
        
        String successMessage=environment.getProperty("API.DELETE_SUCCESS");
        
        return new ResponseEntity<>(successMessage,HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/doctor/{doctorId}")
    @Operation(summary = "Configure doctor's schedule")
    public ResponseEntity<String> configureDoctorSchedule(
            @PathVariable @NotNull(message = "{doctor.doctorId.notNull}") Long doctorId, 
            @RequestParam List<DayOfWeek> days, 
            @RequestParam @NotNull(message = "{admin.startTime.notNull}") String startTime, 
            @RequestParam @NotNull(message = "{admin.endTime.notNull}") String endTime, 
            @RequestParam @NotNull(message = "{admin.slotDuration.notNull}") @Min(1) Integer slotDuration) throws InfyHospitalException{
        
        adminService.configureDoctorSchedule(
                doctorId, 
                days, 
                LocalTime.parse(startTime), 
                LocalTime.parse(endTime), 
                slotDuration);        
        String successMessage=environment.getProperty("API.CONFIGURE_SUCCESS")+ doctorId;
        
        return new ResponseEntity<>(successMessage,HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/booking-rules")
    @Operation(summary = "Update booking rules")
    public ResponseEntity<String> updateBookingRules(
            @RequestParam @Min(1) int maxPatients, 
            @RequestParam @Min(1) int maxDays, 
            @RequestParam @Min(0) int cutoffHours) throws InfyHospitalException{
        
        adminService.updateBookingRules(maxPatients, maxDays, cutoffHours);
        
        String successMessage=environment.getProperty("API.UPDATE_SUCCESS");
        return new ResponseEntity<>(successMessage,HttpStatus.OK);
    }
    
    // API for admin analytics
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stats/summary")
    @Operation(summary = "Get stats summary")
    public ResponseEntity<AdminStatsSummaryDTO> getSummary(
            @RequestParam LocalDate startDate, 
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Long doctorId) throws InfyHospitalException{
        
        AdminStatsSummaryDTO summary=adminService.getSummary(startDate, endDate, doctorId);
        
        return new ResponseEntity<>(summary,HttpStatus.OK);
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stats/chart")
    @Operation(summary = "Get data for daily patient count chart")
    public ResponseEntity<List<DailyPatientCountDTO>> getChartData(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam Long doctorId) throws InfyHospitalException{
        
        List<DailyPatientCountDTO> chartData=adminService.getChartData(startDate, endDate, doctorId);
        
        return new ResponseEntity<List<DailyPatientCountDTO>>(chartData,HttpStatus.OK);
    }
}
 