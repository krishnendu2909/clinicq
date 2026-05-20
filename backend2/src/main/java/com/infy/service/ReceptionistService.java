package com.infy.service;

import java.util.List;
import com.infy.dto.AppointmentDTO;
import com.infy.dto.AppointmentSearchDTO;
import com.infy.dto.PatientDTO;
import com.infy.dto.TokenDTO;
import com.infy.entity.Doctor;
import com.infy.exception.InfyHospitalException;


public interface ReceptionistService {

    TokenDTO registerWalkIn(PatientDTO PatientDTO, Long doctorId) throws InfyHospitalException;
    
    public TokenDTO checkIn(Long appointmentId) throws InfyHospitalException;
    List<AppointmentSearchDTO> searchPatient(String keyword) throws InfyHospitalException;

    List<TokenDTO> getQueue(Long doctorId) throws InfyHospitalException;

    List<AppointmentDTO> getTodaysAppointment() throws InfyHospitalException;

    void markNoShowAppointments();

    TokenDTO moveToken(Long tokenId, Doctor newDoctor) throws InfyHospitalException;

    void reorderQueue(Long doctorId, List<Long> tokenIds) throws InfyHospitalException;

    List<TokenDTO> getAllQueues();
    
    

}
 