package com.infy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.dto.QueuePositionDTO;
import com.infy.exception.InfyHospitalException;
import com.infy.entity.Patient;
import com.infy.entity.Token;
import com.infy.repository.PatientRepository;
import com.infy.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ReceptionistQueueService {

    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private TokenRepository tokenRepository;
    
    @Autowired
    private QueueNotificationService queueNotificationService;

    @Transactional
    public QueuePositionDTO getQueuePositionAfterWalkIn(Long patientId) throws InfyHospitalException {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new InfyHospitalException("Service.NO_PATIENT_FOUND"));

        // Get all tokens for this patient today
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        List<Token> patientTokens = tokenRepository.findByPatientIdAndTimestampAfter(
                patientId, todayStart);

        if (patientTokens.isEmpty()) {
            throw new InfyHospitalException("Service.NO_ACTIVE_TOKEN_FOUND");
        }

        // Get most recent token
        Token currentToken = patientTokens.stream()
                .max(Comparator.comparing(Token::getCheckInTime))
                .orElseThrow(() -> new InfyHospitalException("Service.NO_ACTIVE_TOKEN_FOUND"));

        // Calculate position in queue
        List<Token> waitingTokens = tokenRepository.findByDoctorIdAndStatusOrderByPosition(
                currentToken.getDoctor().getId(), "WAITING");

        int position = 1;
        for (Token token : waitingTokens) {
            if (token.getId().equals(currentToken.getId())) {
                break;
            }
            position++;
        }

        // Calculate estimated wait time (assuming 15 minutes per patient)
        int estimatedWaitTime = (position - 1) * 15;

        return new QueuePositionDTO(
                patientId.toString(),
                patient.getName(),
                currentToken.getTokenNumber(),
                position,
                currentToken.getStatus().toString(),
                currentToken.getDoctor().getName(),
                currentToken.getDoctor().getDepartment().toString(),
                estimatedWaitTime,
                currentToken.getCheckInTime()
        );
    }
}
