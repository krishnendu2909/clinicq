package com.infy.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.infy.entity.Admin;
import com.infy.entity.Doctor;
import com.infy.exception.InfyHospitalException;
import com.infy.repository.AdminRepository;
import com.infy.repository.DoctorRepository;
import com.infy.service.AdminService;


@Component
public class SlotScheduler {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AdminService adminService;
    
    // Scheduler runs every day at 12 AM
    @Scheduled(cron = "0 0 0 * * *")
    public void generateDailySlots() throws InfyHospitalException {
        Admin admin=adminRepository.findTopByOrderByIdAsc()
                .orElse(null);
        if (admin==null) return;
        
        int daysAhead=admin.getMaxDaysInAdvance();
        
        List<Doctor> doctors=doctorRepository.findAll();
        
        for (Doctor doctor:doctors) {
            adminService.generateTimeSlots(doctor.getId(), daysAhead);
        }
        
        System.out.println("Daily time slot generation completed.");
    }
}
 