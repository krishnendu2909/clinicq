package com.infy.api;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.infy.dto.AppointmentDTO;
import com.infy.dto.AppointmentSearchDTO;
import com.infy.dto.PatientDTO;
import com.infy.dto.TokenDTO;
import com.infy.entity.Doctor;
import com.infy.exception.InfyHospitalException;
import com.infy.service.ReceptionistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("clinicq/receptionist")
@CrossOrigin(origins="http://localhost:3000")
@Validated
@Tag(name = "Receptionist APIs",description = "Operations related to Receptionist")
public class ReceptionistApi {

@Autowired
private ReceptionistService receptionistService;

@Autowired
private Environment env;
/**
* Register walk-in patient and generate token
* @throws InfyHospitalException 
*/

@PreAuthorize("hasRole('RECEPTIONIST')")
@PostMapping("/walkin/{doctorId}")
@Operation(summary = "Register walk in Patients")
public ResponseEntity<TokenDTO> registerWalkIn(
       @Valid @RequestBody PatientDTO dto,
       @NotNull(message = "{doctor.doctorId.notNull}")
       @Min(value = 1, message = "{doctor.doctorId.min}")
        @Max(value=50, message="{doctor.doctorId.max}")
       @PathVariable Long doctorId
) throws InfyHospitalException {

   TokenDTO token = receptionistService.registerWalkIn(dto, doctorId);

   return new ResponseEntity<>(token, HttpStatus.CREATED);
}

/**
* Check-in for appointment
* @throws InfyHospitalException 
*/
@PreAuthorize("hasRole('RECEPTIONIST')")
@PostMapping("/checkin/{appointmentId}")
@Operation(summary = "Check In Patients who have pre-booked the appointment")
public ResponseEntity<TokenDTO> checkIn(
       @PathVariable @NotNull(message = "{appointment.appointmentId.notNull}")
       @Min(value = 1, message = "{appointment.appointmentId.min}") Long appointmentId
) throws InfyHospitalException {

   TokenDTO token = receptionistService.checkIn(appointmentId);

   return ResponseEntity.ok(token);
}

/**
* Search patient by keyword (name/phone)
* @throws InfyHospitalException 
*/
@PreAuthorize("hasRole('RECEPTIONIST')")
@GetMapping("/search")
@Operation(summary = "Search for Patient Details Based on Name or Phone Number")
public ResponseEntity<List<AppointmentSearchDTO>> searchPatient(
       @RequestParam @NotBlank(message = "{receptionist.searchKeyword.notBlank}") String keyword
) throws InfyHospitalException {

   List<AppointmentSearchDTO> patients = receptionistService.searchPatient(keyword);

   if (patients == null || patients.isEmpty()) {
       return ResponseEntity.noContent().build();
   }

   return ResponseEntity.ok(patients);
}

@PreAuthorize("hasRole('RECEPTIONIST')")
@GetMapping("/today")
@Operation(summary = "Get All Todays Appointments")
public ResponseEntity<List<AppointmentDTO>> getTodaysAppointments() throws InfyHospitalException
{
    List<AppointmentDTO> appointments=receptionistService.getTodaysAppointment();
    
    return ResponseEntity.ok(appointments);
    
}

/**
* Get queue for a doctor
* @throws InfyHospitalException 
*/
@PreAuthorize("hasRole('RECEPTIONIST')")
@GetMapping("/queue/{doctorId}")
@Operation(summary = "Get Patient Queue Based on Doctor Id")
public ResponseEntity<List<TokenDTO>> getQueue(
         @NotNull(message = "{doctor.doctorId.notNull}")
         @Min(value = 1, message = "{doctor.doctorId.min}")
         @Max(value=50, message="{doctor.doctorId.max}")
       @PathVariable  Long doctorId
) throws InfyHospitalException {

   List<TokenDTO> queue = receptionistService.getQueue(doctorId);

   if (queue == null || queue.isEmpty()) {
       return ResponseEntity.noContent().build();
   }

   return ResponseEntity.ok(queue);
}


@PreAuthorize("hasRole('RECEPTIONIST')")
@PutMapping("/move")
@Operation(summary = "Move Patient from one doctor to other")
public ResponseEntity<TokenDTO> movePatient(
         @NotNull(message = "{token.tokenId.notNull}")
        @RequestParam Long tokenId,
         @NotNull(message = "{doctor.doctorId.notNull}")
         @Min(value = 1, message = "{doctor.doctorId.min}")
         @Max(value=50, message="{doctor.doctorId.max}")
       @RequestParam  Long newDoctorId
) throws InfyHospitalException {

    Doctor doctor=new Doctor();
    doctor.setId(newDoctorId);
   TokenDTO token = receptionistService.moveToken(tokenId, doctor);

   return ResponseEntity.ok(token);
}

@PreAuthorize("hasRole('RECEPTIONIST')")
@PutMapping("/reorder/{doctorId}")
@Operation(summary = "Reorder patient position within queue")
public ResponseEntity<String> reorderQueue(
        
         @NotNull(message = "{doctor.doctorId.notNull}")
         @Min(value = 1, message = "{doctor.doctorId.min}")
         @Max(value=50, message="{doctor.doctorId.max}")
       @PathVariable  Long doctorId,
       @RequestBody List<Long> tokenIds
) throws InfyHospitalException {
    receptionistService.reorderQueue(doctorId, tokenIds);
    String message=env.getProperty("API.REORDER_SUCCESS");

   return ResponseEntity.ok(message);
}

@PreAuthorize("hasRole('RECEPTIONIST')")
@GetMapping("/queue/all")
@Operation(summary = "Get All queues")
public ResponseEntity<List<TokenDTO>> getAllQueues() {
  return ResponseEntity.ok(receptionistService.getAllQueues());
}



}
 