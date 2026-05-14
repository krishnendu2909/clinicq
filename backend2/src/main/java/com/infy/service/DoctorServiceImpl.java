package com.infy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.dto.PrescriptionDTO;
import com.infy.dto.TokenDTO;
import com.infy.entity.Appointment;
import com.infy.entity.Doctor;
import com.infy.entity.Prescription;
import com.infy.entity.PrescriptionItem;
import com.infy.entity.Token;
import com.infy.exception.InfyHospitalException;
import com.infy.models.AppointmentStatus;
import com.infy.models.PrescriptionStatus;
import com.infy.models.TokenStatus;
import com.infy.repository.AppointmentRepository;
import com.infy.repository.DoctorRepository;
import com.infy.repository.PrescriptionRepository;
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
    
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
    private ModelMapper modelMapper=new ModelMapper();

    public List<TokenDTO> getDailySchedule(Long doctorId) throws InfyHospitalException{
        Doctor doctor=doctorRepository.findById(doctorId).orElseThrow(()-> new InfyHospitalException("Service.NO_DOCTOR_FOUND"));
        
        LocalDate today=LocalDate.now();
        LocalDateTime start=today.atStartOfDay();
        LocalDateTime end=today.atTime(LocalTime.MAX);
        
        return tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByPositionAsc(doctor, start,end)
                .stream()
                .map(token->modelMapper.map(token, TokenDTO.class))
                .toList();
    }
    
    public List<TokenDTO> getCurrentQueue(Long doctorId) throws InfyHospitalException{
        Doctor doctor=doctorRepository.findById(doctorId).orElseThrow(()-> new InfyHospitalException("Service.NO_DOCTOR_FOUND"));
        
        LocalDate today=LocalDate.now();
        LocalDateTime start=today.atStartOfDay();
        LocalDateTime end=today.atTime(LocalTime.MAX);
        
        return tokenRepository.findByDoctorAndCheckInTimeBetweenOrderByPositionAsc(doctor, start,end)
                .stream()
                .filter(token->
                token.getStatus().equals(TokenStatus.IN_CONSULTATION) || 
                token.getStatus().equals(TokenStatus.WAITING))
                .map(token->modelMapper.map(token, TokenDTO.class))
                .toList();
    }
    
    private void reorderQueue(Long doctorId)
    {
        List<TokenStatus> activeStatuses=List.of(TokenStatus.WAITING ,TokenStatus.IN_CONSULTATION);
        List<Token> queue=tokenRepository
                .findByDoctorIdAndDateAndStatusInOrderByPositionAsc(doctorId, LocalDate.now(), activeStatuses);
        
        for(int i=0;i<queue.size();i++)
        {
            queue.get(i).setPosition(i+1);
        }
        
        tokenRepository.saveAll(queue);
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
                
                Prescription prescription=prescriptionRepository
                        .findByAppointmentId(appointment.getId())
                        .orElse(null);
                if(prescription!=null)
                {
                    prescription.setStatus(PrescriptionStatus.FINAL);
                    prescriptionRepository.save(prescription);
                }
                
                
                reorderQueue(token.getDoctor().getId());
                
                break;
            case NO_SHOW:
                appointment.setStatus(AppointmentStatus.NO_SHOW);
                
                reorderQueue(token.getDoctor().getId());
                
                break;
            default:
                break;
    
        
        }
        
        appointmentRepository.save(appointment);
        tokenRepository.save(token);
    }
    
    @Override
    public void saveOrUpdatePrescription(PrescriptionDTO dto) throws InfyHospitalException
    {
        Appointment appointment=appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(()->new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND"));
        
        if(appointment.getStatus().equals(AppointmentStatus.BOOKED)||
                appointment.getStatus().equals(AppointmentStatus.CHECKED_IN)||
                appointment.getStatus().equals(AppointmentStatus.CANCELLED)||
                appointment.getStatus().equals(AppointmentStatus.NO_SHOW))
            
        {
            throw new InfyHospitalException("Service.CANNOT_ADD_PRESCRIPTION");
        }
        
        Prescription tempPrescription=prescriptionRepository
                .findByAppointmentId(dto.getAppointmentId())
                .orElse(null);
        
        
        if(tempPrescription !=null && 
                PrescriptionStatus.FINAL.equals(tempPrescription.getStatus()))
        {
            throw new InfyHospitalException("Service.CANNOT_UPDATE_FINAL_PRESCRIPTION");
        }
        if(tempPrescription==null)
        {
            tempPrescription=new Prescription();
            tempPrescription.setAppointment(appointment);
            tempPrescription.setStatus(PrescriptionStatus.DRAFT);
        }
        final Prescription prescription=tempPrescription;
            
        prescription.setDiagnosis(dto.getDiagnosis());
        prescription.setNotes(dto.getNotes());
        
        if(appointment.getStatus().equals(AppointmentStatus.IN_CONSULTATION))
        {
            prescription.setStatus(PrescriptionStatus.DRAFT);
        }
        else
        {
            prescription.setStatus(PrescriptionStatus.FINAL);
        }
        
        if(prescription.getMedicines()==null)
        {
            prescription.setMedicines(new ArrayList<>());
        }
        else
        {
            prescription.getMedicines().clear();
        }

        
        
        List<PrescriptionItem> newItems=dto.getMedicines()
                .stream()
                .map(m->
                {
                PrescriptionItem item=new PrescriptionItem();
                item.setPrescription(prescription);
                item.setMedicineName(m.getMedicineName());
                item.setDosage(m.getDosage());
                item.setFrequency(m.getFrequency());
                item.setDuration(m.getDuration());
                return item;
                
                }).toList();
        
        
        prescription.getMedicines().addAll(newItems);
        
        prescriptionRepository.save(prescription);
    }
    
    
}