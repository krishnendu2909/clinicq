package com.infy.api;


 

import java.time.LocalDate;

import java.util.List;


 

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;


 

import com.infy.dto.AppointmentDTO;

import com.infy.dto.AppointmentRequestDTO;

import com.infy.dto.DoctorDTO;

import com.infy.dto.TimeSlotDTO;

import com.infy.entity.Patient;

import com.infy.entity.User;

import com.infy.exception.InfyHospitalException;

import com.infy.models.Department;

import com.infy.repository.PatientRepository;

import com.infy.repository.UserRepository;

import com.infy.service.PatientService;


 

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import jakarta.validation.constraints.Max;

import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Pattern;


 

@RestController

@RequestMapping("clinicq/patient")

@CrossOrigin(origins="http://localhost:3000")

@Validated

@Tag(name = "Patient APIs",description = "Operations related to patients")


 

public class PatientApi {


 

    @Autowired

    private PatientService patientService;

   

    @Autowired

    private UserRepository userRepository;

   

    @Autowired

    private PatientRepository patientRepository;

   

    @Autowired

    Environment env;

   


 

    @PreAuthorize("hasAnyRole('PATIENT','RECEPTIONIST')")

    @GetMapping("/doctors")

    @Operation(summary = "Get doctors by department")

    public ResponseEntity<List<DoctorDTO>> getDoctorsByDepartment(

            @RequestParam

            @NotNull(message = "{doctor.department.notNull}")

            @Pattern(regexp = "^(CARDIOLOGY|ORTHOPEDICS|PEDIATRICS|GENERAL)$",

            message= "{doctor.department.invalid}")

            String department)

                    throws InfyHospitalException

    {

        Department dept=Department.valueOf(department.trim().toUpperCase());

        List<DoctorDTO> doctorDTOs=patientService.getDoctorsByDepartment(dept);

        return new ResponseEntity<>(doctorDTOs,HttpStatus.OK);

       

    }

   

    @PreAuthorize("hasRole('PATIENT')")

    @GetMapping("/doctors/{doctorId}")

    @Operation(summary = "Get doctors by Id")

    public ResponseEntity<DoctorDTO> getDoctorsById(

            @PathVariable

             @NotNull(message = "{doctor.doctorId.notNull}")

            @Min(value = 1, message = "{doctor.doctorId.min}")

            @Max(value=50, message="{doctor.doctorId.max}")

            Long doctorId)

                    throws InfyHospitalException

    {

        DoctorDTO doctorDTO=patientService.getDoctorById(doctorId);

        return new ResponseEntity<>(doctorDTO,HttpStatus.OK);

       

    }


 

    @PreAuthorize("hasRole('PATIENT')")

    @GetMapping("/slots")

    @Operation(summary = "Get TimeSlots based on doctorsId and date")

    public ResponseEntity<List<TimeSlotDTO>> getSlots

    (       @RequestParam

            @NotNull(message = "{doctor.doctorId.notNull}")

            @Min(value = 1, message = "{doctor.doctorId.min}")

            @Max(value=50, message="{doctor.doctorId.max}")

            Long doctorId,


 

            @RequestParam

            @NotNull(message = "{timeSlot.date.notNull}")

            @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)

            LocalDate date)

                    throws InfyHospitalException

    {

        List<TimeSlotDTO> timeSlotDTOs=patientService.getAllSlots(doctorId, date);

        return new ResponseEntity<>(timeSlotDTOs,HttpStatus.OK);

       

    }


 

    // ===================== Helper Method =====================


 

    private Long getLoggedInPatientId() throws InfyHospitalException {


 

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();


 

        User user = userRepository.findByEmail(email)

                .orElseThrow(() -> new InfyHospitalException("Service.USER_NOT_FOUND"));


 

        Patient patient = patientRepository.findByUser(user);


 

        return patient.getId();

    }



 

    @PreAuthorize("hasRole('PATIENT')")

    @PostMapping("/appointment")

    @Operation(summary = "Book Appointment")

    public ResponseEntity<String> bookAppointment(

            @RequestBody @Valid AppointmentRequestDTO request)

            throws InfyHospitalException {


 

        Long patientId = getLoggedInPatientId();


 

        Long appointmentId = patientService.bookAppointment(

                patientId,

                request.getDoctorId(),

                request.getSlotId(),

                request.getReason()

        );


 

        String message = env.getProperty("Api.BOOKING_SUCCESS") + appointmentId;

        return new ResponseEntity<>(message, HttpStatus.CREATED);

    }



 

    @PreAuthorize("hasRole('PATIENT')")

    @GetMapping("/appointments/upcoming")

    @Operation(summary = "Get Upcoming Appointments")

    public ResponseEntity<List<AppointmentDTO>> getUpcomingAppointments()

            throws InfyHospitalException {


 

        Long patientId = getLoggedInPatientId();


 

        return new ResponseEntity<>(

                patientService.getUpcomingAppointments(patientId),

                HttpStatus.OK

        );

    }



 

    @PreAuthorize("hasRole('PATIENT')")

    @GetMapping("/appointments")

    @Operation(summary = "Get All Appointments")

    public ResponseEntity<List<AppointmentDTO>> getAllAppointments()

            throws InfyHospitalException {


 

        Long patientId = getLoggedInPatientId();


 

        return new ResponseEntity<>(

                patientService.getAllAppointments(patientId),

                HttpStatus.OK

        );

    }



 

    @PreAuthorize("hasRole('PATIENT')")

    @DeleteMapping("/appointments/cancel/{appointmentId}")

    @Operation(summary = "Cancel Appointment")

    public ResponseEntity<String> cancelAppointment(

            @PathVariable

            @NotNull(message = "{appointment.appointmentId.notNull}")

            @Min(value = 1, message = "{appointment.appointmentId.min}")

            Long appointmentId) throws InfyHospitalException {


 

        Long id = patientService.cancelAppointment(appointmentId);


 

        String message = env.getProperty("Api.CANCELING_SUCCESS") + id;


 

        return new ResponseEntity<>(message, HttpStatus.OK);

    }



 

    @PreAuthorize("hasRole('PATIENT')")

    @PutMapping("/appointments/reschedule")

    @Operation(summary = "Reschedule Appointment")

    public ResponseEntity<AppointmentDTO> rescheduleAppointment(

            @RequestParam

            @NotNull(message = "{appointment.appointmentId.notNull}")

            @Min(value = 1, message = "{appointment.appointmentId.min}")

            Long appointmentId,


 

            @RequestParam

            @NotNull(message = "{timeSlot.timeSlotId.notNull}")

            @Min(value = 1, message = "{timeSlot.timeSlotId.min}")

            Long newSlotId) throws InfyHospitalException {


 

        return new ResponseEntity<>(

                patientService.reschedule(appointmentId, newSlotId),

                HttpStatus.OK

        );

    }



 

    @PreAuthorize("hasRole('PATIENT')")

    @GetMapping("/appointments/history")

    @Operation(summary = "Get Visit History")

    public ResponseEntity<List<AppointmentDTO>> getVisitHistoryFiltered(


 

            @RequestParam(required = false)

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)

            LocalDate startDate,


 

            @RequestParam(required = false)

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)

            LocalDate endDate,


 

            @RequestParam(required = false)

            Long doctorId) throws InfyHospitalException {


 

        Long patientId = getLoggedInPatientId();


 

        return new ResponseEntity<>(

                patientService.getVisitHistoryFiltered(

                        patientId, startDate, endDate, doctorId),

                HttpStatus.OK

        );

    }


 

}

