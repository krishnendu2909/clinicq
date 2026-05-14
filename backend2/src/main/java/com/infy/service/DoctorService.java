package com.infy.service;

import java.util.List;

import com.infy.dto.PrescriptionDTO;
import com.infy.dto.TokenDTO;
import com.infy.exception.InfyHospitalException;

public interface DoctorService {

    List<TokenDTO> getDailySchedule(Long doctorId) throws InfyHospitalException;
    
    List<TokenDTO> getCurrentQueue(Long doctorId) throws InfyHospitalException;
    
    void updateTokenStatus(Long tokenId, String status) throws InfyHospitalException;

    void saveOrUpdatePrescription(PrescriptionDTO dto) throws InfyHospitalException;
}