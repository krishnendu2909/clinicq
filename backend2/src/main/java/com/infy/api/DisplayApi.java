package com.infy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.NowServingResponseDTO;
import com.infy.service.DisplayService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/clinicq/display")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Display APIs",description = "Operations related to Now serving")
public class DisplayApi {
    
    @Autowired
    DisplayService displayService;
    @GetMapping("/nowserving")
    public ResponseEntity<NowServingResponseDTO> getNowServing() {
       return ResponseEntity.ok(displayService.getNowServing());
    }

}
 