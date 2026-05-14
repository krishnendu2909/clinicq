package com.infy.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.infy.dto.AppointmentDTO;
import com.infy.dto.AppointmentSearchDTO;
import com.infy.dto.PatientDTO;
import com.infy.dto.TokenDTO;
import com.infy.entity.Appointment;
import com.infy.entity.Doctor;
import com.infy.entity.Patient;
import com.infy.entity.Token;
import com.infy.entity.User;
import com.infy.exception.InfyHospitalException;
import com.infy.models.AppointmentStatus;
import com.infy.models.AppointmentType;
import com.infy.models.Role;
import com.infy.models.TokenStatus;
import com.infy.repository.AppointmentRepository;
import com.infy.repository.DoctorRepository;
import com.infy.repository.PatientRepository;
import com.infy.repository.TokenRepository;
import com.infy.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReceptionistServiceImpl implements ReceptionistService{
    
    
    @Autowired 
    private PatientRepository patientRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired 
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    ModelMapper modelMapper =new ModelMapper();
    
    public static final Logger LOGGER = LogManager.getLogger(ReceptionistServiceImpl.class);

    @Override
    public TokenDTO registerWalkIn(PatientDTO dto, Long doctorId) throws InfyHospitalException {

    // 1. Validate doctor
    Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new InfyHospitalException("Service.NO_DOCTOR_FOUND"));
    
    if (dto.getUser() == null) {
        throw new InfyHospitalException("Service.USER_DETAILS_REQUIRED");
    }
    
    String email=dto.getUser().getEmail();
    
    User user=userRepository.findByEmail(email)
            .orElseGet(()->{
                
                User newUser = new User();
                newUser.setEmail(dto.getUser().getEmail());
                newUser.setPassword(passwordEncoder.encode(dto.getUser().getPassword()));
                newUser.setRole(Role.PATIENT);
                return userRepository.save(newUser);
            });
    
    Patient patient=patientRepository.findByPhone(dto.getPhone())
            .orElseGet(()->{    
                
                Patient newPatient = new Patient();
                newPatient.setName(dto.getName());
                newPatient.setDateOfBirth(dto.getDateOfBirth());
                newPatient.setGender(dto.getGender());
                newPatient.setPhone(dto.getPhone());
                newPatient.setUser(user);
                return  patientRepository.save(newPatient);
                
            });


    // 5. Generate Token
    Token lastToken = tokenRepository
            .findTopByDoctorIdAndDateOrderByTokenNumberDesc(doctorId, LocalDate.now());

    int nextToken = (lastToken == null) ? 1 : lastToken.getTokenNumber() + 1;    
    
    String deptLetter=doctor.getDepartment().name().substring(0,1);
    String doctorCode="D"+doctor.getId();
    String formattedTokenNumber=String.format("%03d", nextToken);
    String tokenDisplay=deptLetter+doctorCode+"-"+formattedTokenNumber;
    
    Appointment appointment =new Appointment();
    appointment.setDoctor(doctor);
    appointment.setPatient(patient);
    appointment.setStatus(AppointmentStatus.CHECKED_IN);
    appointment.setType(AppointmentType.WALK_IN);
    
    Appointment appointment1=appointmentRepository.save(appointment);
    List<Token> queue=tokenRepository
            .findByDoctorIdAndDateOrderByPositionAsc(appointment.getDoctor().getId(), LocalDate.now());
    int nextPosition=queue.size()+1;

    Token token = new Token();
    
    token.setDoctor(doctor);
    token.setPatient(patient);
    token.setTokenNumber(nextToken);
    token.setTokenDisplay(tokenDisplay);
    token.setDate(LocalDate.now());
    token.setStatus(TokenStatus.WAITING);
    token.setCheckInTime(LocalDateTime.now());
    token.setAppointment(appointment1);
    token.setPosition(nextPosition);
    token = tokenRepository.save(token);
    
    return modelMapper.map(token, TokenDTO.class);

    }

    @Override
    public TokenDTO checkIn(Long appointmentId) throws InfyHospitalException {

    Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND"));

    Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
            .orElseThrow(() -> new InfyHospitalException("Service.NO_DOCTOR_FOUND"));
    appointment.setStatus(AppointmentStatus.CHECKED_IN);
    appointmentRepository.save(appointment);

    Token lastToken = tokenRepository.findTopByDoctorIdAndDateOrderByTokenNumberDesc(appointment.getDoctor().getId(),LocalDate.now());

    int nextToken = (lastToken == null) ? 1 : lastToken.getTokenNumber() + 1;
    String deptLetter=doctor.getDepartment().name().substring(0,1);
    String doctorCode="D"+doctor.getId();
    String formattedTokenNumber=String.format("%03d", nextToken);
    String tokenDisplay=deptLetter+doctorCode+"-"+formattedTokenNumber;

    Token token = new Token();
    token.setPatient(appointment.getPatient());
    token.setDoctor(appointment.getDoctor());
    token.setTokenNumber(nextToken);
    token.setTokenDisplay(tokenDisplay);
    token.setCheckInTime(LocalDateTime.now());
    token.setStatus(TokenStatus.WAITING);
    token.setDate(LocalDate.now());
    token.setAppointment(appointment);

    
    List<Token> queue=tokenRepository
            .findByDoctorIdAndDateOrderByPositionAsc(appointment.getDoctor().getId(), LocalDate.now());
    int nextPosition=queue.size()+1;
    
    token.setPosition(nextPosition);
    
    Token token1 = tokenRepository.save(token);
    return modelMapper.map(token1, TokenDTO.class);
    }

    @Override
    public List<AppointmentSearchDTO> searchPatient(String keyword) throws InfyHospitalException {
        if(keyword==null ||keyword.trim().isEmpty())
        {
            return List.of();
        }
        List<AppointmentSearchDTO> appointments=appointmentRepository.findByPatientNameOrPhone(keyword);
        if(appointments.isEmpty())
        {
            return List.of();
        }
        
        return appointments;

    
    }

    @Override
    public List<TokenDTO> getQueue(Long doctorId) throws InfyHospitalException {

    List<Token> tokens =tokenRepository
            .findByDoctorIdAndDateOrderByPositionAsc(doctorId, LocalDate.now())
            .stream()
            .filter(t->t.getStatus()==TokenStatus.WAITING).collect(Collectors.toList());

    if (tokens.isEmpty()) {
    throw new InfyHospitalException("Service.NO_QUEUE_FOUND");
    }

    return tokens.stream().map(t -> modelMapper.map(t, TokenDTO.class)).collect(Collectors.toList());
    }
    
    //get all appointments based on today date
    @Override
    public List<AppointmentDTO> getTodaysAppointment() throws InfyHospitalException{
        
        LocalDate today=LocalDate.now();
        List<Appointment> appointments=appointmentRepository.findByTimeSlot_SlotDate(today);
        
        if(appointments.isEmpty())
        {
            throw new InfyHospitalException("Service.NO_UPCOMING_APPOINTMENT_FOUND");
        }
        return appointments.stream()
                .map(t->modelMapper.map(t, AppointmentDTO.class))
                .collect(Collectors.toList());
        
    }
    
    @Override
    public void markNoShowAppointments()
    {

        List<Appointment> missedAppointments=appointmentRepository.missedAppointments(LocalDate.now(),
                LocalTime.now().minusMinutes(5));
        
        if(missedAppointments.isEmpty())
        {
            LOGGER.info("No Appointments to mark as NO_SHOW");
            return;
        }
        
        
        for(Appointment appointment:missedAppointments)
        {
            appointment.setStatus(AppointmentStatus.NO_SHOW);
        }
        
        LOGGER.info("Marked {} appointments as NO_SHOW",missedAppointments.size());
        
    }
    
    @Override
    public TokenDTO moveToken(Long tokenId,Doctor newDoctor) throws InfyHospitalException{
        
        Token token=tokenRepository
                .findById(tokenId)
                .orElseThrow(()->new InfyHospitalException("Service.NO_TOKEN_FOUND"));
        
        if(!token.getStatus().equals(TokenStatus.WAITING))
        {
            throw new InfyHospitalException("Service.CANNOT_MOVE");
        }
        
        Long oldDoctorId =token.getDoctor().getId();
        
        List<Token> oldQueue=tokenRepository
                .findByDoctorIdAndDateOrderByPositionAsc(oldDoctorId, LocalDate.now());
        
        oldQueue.removeIf(t->t.getId().equals(tokenId));
        
        for(int i=0;i<oldQueue.size();i++)
        {
            oldQueue.get(i).setPosition(i+1);
        }
        
        tokenRepository.saveAll(oldQueue);
    
        Token nextToken=tokenRepository
                .findTopByDoctorIdAndDateOrderByTokenNumberDesc(newDoctor.getId(), LocalDate.now());
        
        int nextTokenNumber=(nextToken==null)?1 :nextToken.getTokenNumber()+1;
        int nextPosition=tokenRepository
                .findByDoctorIdAndDateOrderByPositionAsc(newDoctor.getId(), LocalDate.now()).size()+1;
        
        token.setDoctor(newDoctor);
        token.setTokenNumber(nextTokenNumber);
        token.setPosition(nextPosition);
        token.setStatus(TokenStatus.WAITING);
        
        Appointment appointment=token.getAppointment();
        if(appointment!=null)
        {
            appointment.setDoctor(newDoctor);
            appointmentRepository.save(appointment);
        }
        
        Token res=tokenRepository.save(token);
        return modelMapper.map(res, TokenDTO.class);
        
    }
    
    @Override
    public void reorderQueue(Long doctorId,List<Long> tokenIds) throws InfyHospitalException
    {
        List<Token> tokens=tokenRepository
                .findByDoctorIdAndDateOrderByPositionAsc(doctorId, LocalDate.now())
                .stream()
                .filter(token->token.getStatus()==TokenStatus.WAITING)
                .collect(Collectors.toList());
        
        Map<Long,Token> tokenMap=tokens
                .stream()
                .collect(Collectors.toMap(Token::getId, t->t));
        
        if(tokenIds.size()!=tokens.size())
        {
            throw new InfyHospitalException("Service.INVALID_REORDER_LIST");
        }
        
        for(int i=0;i<tokenIds.size();i++)
        {
            Long id=tokenIds.get(i);
            
            Token token=tokenMap.get(id);
            if(token==null)
            {
                throw new InfyHospitalException("Service.NO_TOKEN_FOUND");
            }
            
            token.setPosition(i+1);
        }        
        tokenRepository.saveAll(tokens);
        
    }

}