package com.infy.api;


 

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;


 

import com.infy.dto.PatientDTO;

import com.infy.exception.InfyHospitalException;

import com.infy.models.Role;

import com.infy.service.AuthService;


 

import jakarta.validation.Valid;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;


 

@RestController

@CrossOrigin(origins = "http://localhost:3000")

@Validated

@RequestMapping("clinicq/auth")

public class AuthApi {

    @Autowired

    private AuthService authService;

   

    @Autowired

    Environment env;

   

    @PostMapping("/login")

    public ResponseEntity<String> login(

            @RequestParam

            @NotBlank(message="{user.email.notBlank}")

            @Email(message = "{user.email.invalid}")

            String email,

            @RequestParam

            @NotBlank (message="{user.password.notBlank}")

            @Size(min = 6, max = 20, message = "{user.password.size}")

            String password,

            @RequestParam

            @NotNull (message="{user.role.notNull}")

            Role role) throws InfyHospitalException{

       

        String token=authService.login(email, password, role);

       

        return new ResponseEntity<>(token,HttpStatus.OK);

       

    }

   

    @PostMapping("/signup")

    public ResponseEntity<String> registerPatient(@Valid @RequestBody PatientDTO patientDTO)

            throws InfyHospitalException

    {

        authService.registerPatient(patientDTO);

        String message=env.getProperty("API.REG_SUCCESS");

        return new ResponseEntity<>(message,HttpStatus.OK);

       

    }

   

    @PostMapping("/forgot-password")

    public ResponseEntity<String> forgotPassword(

            @RequestParam

            @NotBlank(message="{user.email.notBlank}")

            @Email(message = "{user.email.invalid}")

            String email) throws InfyHospitalException{

        String token=authService.forgotPassword(email);

       

        return new ResponseEntity<>(token,HttpStatus.OK);

    }

   

    @PostMapping("/reset-password")

    public ResponseEntity<String> resetPassword(

            @RequestParam

            @NotBlank(message="{user.token.notBlank}")

            String token,

            @RequestParam

            @NotBlank (message="{user.password.notBlank}")

            @Size(min = 6, max = 20, message = "{user.password.size}")

            String newPassword) throws InfyHospitalException{

        authService.resetPassword(token, newPassword);

       

        String successMessage=env.getProperty("API.RESET_SUCCESS");

       

        return new ResponseEntity<>(successMessage,HttpStatus.OK);

    }

}