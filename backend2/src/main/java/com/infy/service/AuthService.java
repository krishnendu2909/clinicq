package com.infy.service;


 

import com.infy.dto.PatientDTO;

import com.infy.exception.InfyHospitalException;

import com.infy.models.Role;


 

public interface AuthService {


 

    void registerPatient(PatientDTO dto) throws InfyHospitalException;


 

    String forgotPassword(String email) throws InfyHospitalException;


 

    void resetPassword(String token, String newPassword) throws InfyHospitalException;


 

    String login(String email, String password, Role role) throws InfyHospitalException;

}


