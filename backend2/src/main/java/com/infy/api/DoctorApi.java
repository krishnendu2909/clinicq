package com.infy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.PrescriptionDTO;
import com.infy.dto.TokenDTO;
import com.infy.entity.Doctor;

import com.infy.entity.User;
import com.infy.exception.InfyHospitalException;
import com.infy.repository.DoctorRepository;
import com.infy.repository.UserRepository;
import com.infy.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/clinicq/doctor")
@Tag(name = "Doctor APIs",description = "Operations related to doctors")
@Validated
public class DoctorApi {

    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private Environment environment;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private DoctorRepository doctorRepository;
    
    private Long getLoggedInDoctorId() throws InfyHospitalException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InfyHospitalException("Service.USER_NOT_FOUND"));

        Doctor doctor = doctorRepository.findByUser(user);

        return doctor.getId();
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/schedule")
    @Operation(summary = "Get doctor's daily schedule")
    public ResponseEntity<List<TokenDTO>> getDailySchedule() throws InfyHospitalException{
        
        Long doctorId=getLoggedInDoctorId();
        List<TokenDTO> schedule=doctorService.getDailySchedule(doctorId);
        return new ResponseEntity<>(schedule,HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/queue")
    @Operation(summary = "Get doctor's current queue")
    public ResponseEntity<List<TokenDTO>> getCurrentQueue() throws InfyHospitalException{
        
        Long doctorId=getLoggedInDoctorId();
        List<TokenDTO> queue=doctorService.getCurrentQueue(doctorId);
        return new ResponseEntity<>(queue,HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/token/{tokenId}/status")
    @Operation(summary = "To update patient's token status")
    public ResponseEntity<String> updateTokenStatus(
            @PathVariable 
            @NotNull(message = "{doctor.tokenId.notNull}") 
            Long tokenId, 
            @RequestParam
            @NotNull(message = "{doctor.status.notNull}")
            String status) 
                    throws InfyHospitalException{
        
        doctorService.updateTokenStatus(tokenId, status);
        String successMessage=environment.getProperty("API.TOKEN_UPDATE_SUCCESS");
        
        return new ResponseEntity<>(successMessage,HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/prescription")
    @Operation(summary = "Add prescription for a appointment")
    public ResponseEntity<String> addPrescription(@RequestBody PrescriptionDTO dto)
            throws InfyHospitalException{
        

        doctorService.saveOrUpdatePrescription(dto);
        String successMessage=environment.getProperty("API.PRESCRIPTION_SUCCESS");
        return new ResponseEntity<>(successMessage,HttpStatus.OK);
    }
}
