package com.infy.service;


 

import java.util.List;


 

import com.infy.dto.AppointmentDTO;

import com.infy.dto.AppointmentSearchDTO;

import com.infy.dto.PatientDTO;

import com.infy.dto.TokenDTO;


 

import com.infy.exception.InfyHospitalException;



 

public interface ReceptionistService {


 

    TokenDTO registerWalkIn(PatientDTO PatientDTO, Long doctorId) throws InfyHospitalException;
   
   // Queue position functionality for walk-in registration
   QueuePositionDTO getQueuePositionAfterWalkIn(Long patientId) throws InfyHospitalException;


 

    public TokenDTO checkIn(Long appointmentId) throws InfyHospitalException;

    List<AppointmentSearchDTO> searchPatient(String keyword) throws InfyHospitalException;


 

    List<TokenDTO> getQueue(Long doctorId) throws InfyHospitalException;


 

    List<AppointmentDTO> getTodaysAppointment() throws InfyHospitalException;


 

    void markNoShowAppointments();

   
}