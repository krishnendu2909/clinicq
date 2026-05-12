package com.infy.service;


 

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.time.LocalTime;

import java.util.List;


 

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


 

import com.infy.dto.TokenDTO;

import com.infy.entity.Appointment;

import com.infy.entity.Doctor;

import com.infy.entity.Token;

import com.infy.exception.InfyHospitalException;

import com.infy.models.AppointmentStatus;

import com.infy.models.TokenStatus;

import com.infy.repository.AppointmentRepository;

import com.infy.repository.DoctorRepository;

import com.infy.repository.TokenRepository;



 

@Service

@Transactional

public class DoctorServiceImpl implements DoctorService {

    @Autowired

    private DoctorRepository doctorRepository;

    @Autowired

    private TokenRepository tokenRepository;


 

    @Autowired

    private AppointmentRepository appointmentRepository;

   

    private ModelMapper modelMapper=new ModelMapper();


 

    public List<TokenDTO> getDailySchedule(Long doctorId) throws InfyHospitalException{

        Doctor doctor=doctorRepository.findById(doctorId).orElseThrow(()-> new InfyHospitalException("Service.NO_DOCTOR_FOUND"));

       

        LocalDate today=LocalDate.now();

        LocalDateTime start=today.atStartOfDay();

        LocalDateTime end=today.atTime(LocalTime.MAX);

       

        return tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(doctor, start,end)

                .stream()

                .map(token->modelMapper.map(token, TokenDTO.class))

                .toList();

    }

   

    public List<TokenDTO> getCurrentQueue(Long doctorId) throws InfyHospitalException{

        Doctor doctor=doctorRepository.findById(doctorId).orElseThrow(()-> new InfyHospitalException("Service.NO_DOCTOR_FOUND"));

       

        LocalDate today=LocalDate.now();

        LocalDateTime start=today.atStartOfDay();

        LocalDateTime end=today.atTime(LocalTime.MAX);

       

        return tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByTokenNumberAsc(doctor, start,end)

                .stream()

                .filter(token->

                token.getStatus().equals(TokenStatus.IN_CONSULTATION) ||

                token.getStatus().equals(TokenStatus.WAITING))

                .map(token->modelMapper.map(token, TokenDTO.class))

                .toList();

    }

   

    public void updateTokenStatus(Long tokenId, String status) throws InfyHospitalException{

        Token token=tokenRepository.findById(tokenId).orElseThrow(()->

        new InfyHospitalException("Service.NO_TOKEN_FOUND"));

           

        token.setStatus(TokenStatus.valueOf(status));

       

        Appointment appointment=appointmentRepository.findById(token.getAppointment().getId())

                .orElseThrow(()-> new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND"));

       

        TokenStatus tokenStatus=TokenStatus.valueOf(status);

       

        switch(tokenStatus){

       

            case IN_CONSULTATION:

                appointment.setStatus(AppointmentStatus.IN_CONSULTATION);

                break;

            case COMPLETED:

                appointment.setStatus(AppointmentStatus.COMPLETED);

                break;

            case NO_SHOW:

                appointment.setStatus(AppointmentStatus.NO_SHOW);

                break;

            default:

                break;

   

       

        }

       

        appointmentRepository.save(appointment);

        tokenRepository.save(token);

    }

}
