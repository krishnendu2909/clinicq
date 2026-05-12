package com.infy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.infy.dto.QueuePositionDTO;
import com.infy.exception.InfyHospitalException;
import com.infy.service.ReceptionistService;

@RestController
@CrossOrigin
@RequestMapping("/api/queue")
public class QueueController {

    @Autowired
    private ReceptionistService receptionistService;

    @GetMapping("/position/{patientId}")
    public ResponseEntity<QueuePositionDTO> getQueuePositionAfterWalkIn(@PathVariable Long patientId) {
        try {
            QueuePositionDTO queuePosition = receptionistService.getQueuePositionAfterWalkIn(patientId);
            return ResponseEntity.ok(queuePosition);
        } catch (InfyHospitalException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
