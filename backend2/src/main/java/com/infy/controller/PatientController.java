package com.infy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.infy.dto.QueuePositionDTO;
import com.infy.exception.InfyHospitalException;
import com.infy.service.PatientService;

@RestController
@CrossOrigin
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/queue-position")
    public ResponseEntity<QueuePositionDTO> getQueuePosition(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String phoneNumber) {
        
        try {
            QueuePositionDTO queuePosition;
            
            if (patientId != null && !patientId.trim().isEmpty()) {
                queuePosition = patientService.getQueuePosition(patientId);
            } else if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                queuePosition = patientService.getQueuePositionByPhone(phoneNumber);
            } else {
                return ResponseEntity.badRequest().build();
            }
            
            return ResponseEntity.ok(queuePosition);
            
        } catch (InfyHospitalException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
