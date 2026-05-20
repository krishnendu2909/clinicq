package com.infy.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.dto.AdminDTO;
import com.infy.dto.AppointmentDTO;
import com.infy.dto.DoctorDTO;
import com.infy.dto.PrescriptionDTO;
import com.infy.dto.QueuePositionDTO;
import com.infy.dto.TimeSlotDTO;
import com.infy.entity.Admin;
import com.infy.entity.Appointment;
import com.infy.entity.Doctor;
import com.infy.entity.DoctorSchedule;
import com.infy.entity.Patient;
import com.infy.entity.Prescription;
import com.infy.entity.TimeSlot;
import com.infy.entity.Token;
import com.infy.exception.InfyHospitalException;
import com.infy.models.AppointmentStatus;
import com.infy.models.AppointmentType;
import com.infy.models.Department;
import com.infy.repository.AdminRepository;
import com.infy.repository.AppointmentRepository;
import com.infy.repository.DoctorRepository;
import com.infy.repository.DoctorScheduleRepository;
import com.infy.repository.PatientRepository;
import com.infy.repository.PrescriptionRepository;
import com.infy.repository.TimeSlotRepository;
import com.infy.repository.TokenRepository;


@Service
@Transactional
public class PatientServiceImpl implements PatientService{

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private TokenRepository tokenRepository;
    
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;
    
    ModelMapper modelMapper =new ModelMapper();

    //US1
    //Book Appointment
    
    //getDoctorByDepartment
    @Override
    public List<DoctorDTO> getDoctorsByDepartment(Department department) throws InfyHospitalException
    {
        List<Doctor> doctors=doctorRepository.findByDepartment(department);
        
        List<DoctorDTO> doctorDTOs=new ArrayList<>();
        if(doctors.isEmpty())
        {
            return doctorDTOs;
        }
        for(Doctor doctor:doctors)
        {
            DoctorDTO doctorDTO=modelMapper.map(doctor, DoctorDTO.class);        
            doctorDTOs.add(doctorDTO);        
        }
        
        return doctorDTOs;
        
    }
    //get doctor Details
    @Override
    public DoctorDTO getDoctorById(Long id) throws InfyHospitalException
    {
        Doctor doctor=doctorRepository.findById(id).orElseThrow(
                ()->new InfyHospitalException("Service.NO_DOCTOR_FOUND") );
        return modelMapper.map(doctor, DoctorDTO.class);
        
    }
    
    @Override
    public List<TimeSlotDTO> getAllSlots(Long doctorId,LocalDate date) throws InfyHospitalException 
    {
//        Doctor doctor=doctorRepository.findById(doctorId).orElseThrow(
//                ()->new InfyHospitalException("Service.NO_DOCTOR_FOUND") );
        
        List<TimeSlot> timeSlots=timeSlotRepository.findByDoctorIdAndSlotDate(doctorId, date);
        
        List<TimeSlotDTO> timeSlotDTOs=new ArrayList<>();
        
        if(timeSlots.isEmpty())
        {
            return timeSlotDTOs;
        }
                
                for(TimeSlot timeSlot:timeSlots)
                {
                    TimeSlotDTO timeSlotDTO=modelMapper.map(timeSlot, TimeSlotDTO.class);
                    DoctorDTO doctorDTO=modelMapper.map(timeSlot.getDoctor(), DoctorDTO.class);
                    timeSlotDTO.setDoctor(doctorDTO);                    
                    timeSlotDTOs.add(timeSlotDTO);
                    
                }
                
            return timeSlotDTOs;            
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long bookAppointment(Long patientId,Long doctorId,Long slotId,String reason) throws InfyHospitalException
    {
        Patient patient=patientRepository.findById(patientId).orElseThrow(
                ()->new InfyHospitalException("Service.NO_PATIENT_FOUND") );
        
       Doctor doctor=doctorRepository.findById(doctorId).orElseThrow(
                        ()->new InfyHospitalException("Service.NO_DOCTOR_FOUND") );
       
       TimeSlot timeSlot=timeSlotRepository.findById(slotId).orElseThrow(
                ()->new InfyHospitalException("Service.NO_TIMESLOT_FOUND") );
       
       LocalDateTime slotDateTime=LocalDateTime.of(timeSlot.getSlotDate(), timeSlot.getStartTime());
       
       Optional<Appointment> a=appointmentRepository.findConflictingAppointments(patientId,
               timeSlot.getSlotDate(), timeSlot.getStartTime());
       if(a.isPresent())
       {
           throw new InfyHospitalException("Service.PATIENT_ALREADY_HAS_APPOINTMENT_AT_THIS_TIME");
       }
       
       if(slotDateTime.isBefore(LocalDateTime.now()))
       {
           throw new InfyHospitalException("Service.CANNOT_BOOK_PAST_APPOINTMENT");
       }
       
       if(!timeSlot.getDoctor().getId().equals(doctor.getId()))
       {
           throw new InfyHospitalException("Service.SLOT_DOES_NOT_BELONG_TO_DOCTOR");
       }
       
       if(timeSlot.getBooked())
       {
           throw new InfyHospitalException("Service.ALREADY_BOOKED");
       }
       
       Appointment appointment=new Appointment();
    
       
       appointment.setPatient(patient);
       appointment.setDoctor(doctor);
       appointment.setTimeSlot(timeSlot);
       appointment.setReason(reason);
       appointment.setStatus(AppointmentStatus.BOOKED);
       appointment.setType(AppointmentType.PRE_BOOKED);
  
       timeSlot.setBooked(true);
       timeSlotRepository.save(timeSlot);
       
       Appointment savedAppointment=appointmentRepository.save(appointment);

        return savedAppointment.getId();
       
        
    }
    
    @Override
    public List<AppointmentDTO> getUpcomingAppointments(Long patientId) throws InfyHospitalException {

    List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

    return appointments.stream()
            .filter(a -> a.getStatus() == AppointmentStatus.BOOKED)
            .sorted(
                Comparator.comparing((Appointment a) -> a.getTimeSlot().getSlotDate())
                          .thenComparing(a -> a.getTimeSlot().getStartTime())
            )
            .map(a -> modelMapper.map(a, AppointmentDTO.class))
            .toList();
    }

    
    //US2
    
    @Override
    public List<AppointmentDTO> getAllAppointments(Long patientId) throws InfyHospitalException {

    List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);


    return appointments.stream()
            .sorted(
                Comparator.<Appointment,LocalDateTime>comparing( a ->
                a.getTimeSlot()!=null ? 
                    a.getTimeSlot().getSlotDate().atTime(a.getTimeSlot().getStartTime())
                        :a.getCreatedAt())
                .reversed()
                )
            
            .map(a -> modelMapper.map(a, AppointmentDTO.class))
            .toList();
    }


    //US3
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long cancelAppointment(Long appointmentId) throws InfyHospitalException {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND"));
        
        Token token=tokenRepository.findByAppointmentId(appointmentId).orElse(null);
        if(token!=null)
        {
                throw new InfyHospitalException("Service.CANCELLATION_NOT_ALLOWED");
        }
        
        if(appointment.getStatus().equals(AppointmentStatus.CANCELLED))
        {
            throw new InfyHospitalException("Service.ALREADY_CANCELLED");
        }
        
        Admin admin=adminRepository.findTopByOrderByIdAsc().orElseThrow(()->
        new InfyHospitalException("Service.NO_ADMIN_FOUND"));
        
        int cancelHours=admin.getCancellationCutoffHours();
        TimeSlot slot=appointment.getTimeSlot();
        LocalDateTime appointmentDateTime=LocalDateTime.of(slot.getSlotDate(), slot.getStartTime());
        LocalDateTime now=LocalDateTime.now();
        
        if(now.isAfter(appointmentDateTime.minusHours(cancelHours)))
        {
            throw new InfyHospitalException("Service.CANCELLATION_NOT_ALLOWED");
        }

        Long id=appointment.getId();
        appointment.setStatus(AppointmentStatus.CANCELLED);
        slot.setBooked(false);

        timeSlotRepository.save(slot);
        appointmentRepository.save(appointment);

        return id;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppointmentDTO reschedule(Long appointmentId, Long newSlotId) throws InfyHospitalException {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND"));
        
        
        Admin admin=adminRepository.findTopByOrderByIdAsc().orElseThrow(()->
        new InfyHospitalException("Service.NO_ADMIN_FOUND"));
        
        int cancelHours=admin.getCancellationCutoffHours();


        TimeSlot oldSlot = appointment.getTimeSlot();
     // 1. Get the current time
        LocalDateTime now = LocalDateTime.now();

        // 2. Build the appointment time from the old slot
        LocalDateTime oldAppointmentDateTime = LocalDateTime.of(
            oldSlot.getSlotDate(), 
            oldSlot.getStartTime()
        );

        // 3. Logic: Only allow rescheduling if the appointment is in the FUTURE and before cutoff hours

        if (now.isAfter(oldAppointmentDateTime.minusHours(cancelHours))) {
            throw new InfyHospitalException("Service.RESCHEDULE_NOT_ALLOWED");
        }
     // 1. Free the old slot
        oldSlot.setBooked(false);
        timeSlotRepository.save(oldSlot);
        
     // 2. Fetch and Validate New Slot
        TimeSlot newSlot = timeSlotRepository.findById(newSlotId)
                .orElseThrow(() -> new InfyHospitalException("Service.NO_TIMESLOTS_FOUND"));

        if (Boolean.TRUE.equals(newSlot.getBooked())) {
            throw new InfyHospitalException("Service.ALREADY_BOOKED");
        }

        newSlot.setBooked(true);
        timeSlotRepository.save(newSlot);

        // 4. Update Appointment
        appointment.setTimeSlot(newSlot);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return modelMapper.map(updatedAppointment, AppointmentDTO.class);
        
    }
    

    
    @Override
    public List<AppointmentDTO> getFilteredHistory(
           Long patientId,
           LocalDate startDate,
           LocalDate endDate,
           Long doctorId
    ) throws InfyHospitalException
    {

       List<Appointment> appointments =
               appointmentRepository.findByPatientIdAndStatus(
                       patientId,
                       AppointmentStatus.COMPLETED
               );

       // FILTER BY DOCTOR

       if(doctorId != null)
       {
           appointments = appointments.stream()
                   .filter(a ->
                           a.getDoctor() != null
                           &&
                           a.getDoctor().getId().equals(doctorId)
                   )
                   .toList();
       }

       // FILTER BY START DATE

       if(startDate != null)
       {
           appointments = appointments.stream()
                   .filter(a -> {

                       LocalDate appointmentDate;

                       if(a.getTimeSlot() != null)
                       {
                           appointmentDate =
                                   a.getTimeSlot().getSlotDate();
                       }
                       else
                       {
                           appointmentDate =
                                   a.getCreatedAt().toLocalDate();
                       }

                       return !appointmentDate.isBefore(startDate);

                   })
                   .toList();
       }

       // FILTER BY END DATE

       if(endDate != null)
       {
           appointments = appointments.stream()
                   .filter(a -> {

                       LocalDate appointmentDate;

                       if(a.getTimeSlot() != null)
                       {
                           appointmentDate =
                                   a.getTimeSlot().getSlotDate();
                       }
                       else
                       {
                           appointmentDate =
                                   a.getCreatedAt().toLocalDate();
                       }

                       return !appointmentDate.isAfter(endDate);

                   })
                   .toList();
       }

       // SORT MOST RECENT FIRST

       appointments = appointments.stream()
               .sorted((a1, a2) -> {

                   LocalDateTime d1;

                   if(a1.getTimeSlot() != null)
                   {
                       d1 = LocalDateTime.of(
                               a1.getTimeSlot().getSlotDate(),
                               a1.getTimeSlot().getStartTime()
                       );
                   }
                   else
                   {
                       d1 = a1.getCreatedAt();
                   }

                   LocalDateTime d2;

                   if(a2.getTimeSlot() != null)
                   {
                       d2 = LocalDateTime.of(
                               a2.getTimeSlot().getSlotDate(),
                               a2.getTimeSlot().getStartTime()
                       );
                   }
                   else
                   {
                       d2 = a2.getCreatedAt();
                   }

                   return d2.compareTo(d1);

               })
               .toList();

       return appointments.stream()
               .map(a -> {

                   AppointmentDTO dto =
                           modelMapper.map(a, AppointmentDTO.class);

                   LocalDate appointmentDate;

                   if(a.getTimeSlot() != null)
                   {
                       appointmentDate =
                               a.getTimeSlot().getSlotDate();
                   }
                   else
                   {
                       appointmentDate =
                               a.getCreatedAt().toLocalDate();
                   }
                  

                   LocalTime appointmentTime;

                   if(a.getTimeSlot() != null)
                   {
                       appointmentTime =
                               a.getTimeSlot().getStartTime();
                   }
                   else
                   {
                       appointmentTime =
                               a.getCreatedAt().toLocalTime();
                   }

                   TimeSlotDTO timeSlot=new TimeSlotDTO();
                   timeSlot.setSlotDate(appointmentDate);
                   timeSlot.setStartTime(appointmentTime);
                   dto.setTimeSlot(timeSlot);
                  

                   return dto;

               })
               .toList();
    }
    
    @Override
    public AdminDTO getBookingRules() throws InfyHospitalException
    {
        Admin rules=adminRepository.getBookingRules();
        
        if(rules==null)
        {
            throw new InfyHospitalException("Service.NO_ADMIN_FOUND");
        }
        return modelMapper.map(rules, AdminDTO.class);
        
    }
    
    
    @Override
    public PrescriptionDTO getPatientPrescription(Long patientId, Long appointmentId) throws InfyHospitalException
    {
        Prescription p =prescriptionRepository
                .findByPatientIdAndAppointmentId(patientId, appointmentId)
                .orElseThrow(()->new InfyHospitalException("Service.Service.PRESCRIPTION_NOT_FOUND"));        
        
        return modelMapper.map(p, PrescriptionDTO.class);
        
    }

       // -------------------------------------------------------
       // Queue Position (merged from PatientController)
       // -------------------------------------------------------

       /**
        * Resolves queue position by patient's numeric ID (passed as a String from the
        * request param, matching the original PatientController signature).
        */
       @Override
       public QueuePositionDTO getQueuePosition(Long patientId) throws InfyHospitalException {

           Patient patient = patientRepository.findById(patientId)
                   .orElseThrow(() -> new InfyHospitalException("Service.NO_PATIENT_FOUND"));

           return buildQueuePosition(patient);
       }

       /**
        * Resolves queue position by patient's phone number.
        */
       @Override
       public QueuePositionDTO getQueuePositionByPhone(String phoneNumber)
               throws InfyHospitalException {

           Patient patient = patientRepository.findByPhone(phoneNumber.trim())
                   .orElseThrow(() -> new InfyHospitalException("Service.NO_PATIENT_FOUND"));

           return buildQueuePosition(patient);
       }

       // -------------------------------------------------------
       // Private helper – shared logic for queue position lookup
       // -------------------------------------------------------

       private QueuePositionDTO buildQueuePosition(Patient patient)
               throws InfyHospitalException {

           LocalDate today = LocalDate.now();

           // Find all tokens for this patient today, most-recent first
           List<Token> patientTokens = tokenRepository.findByPatientIdAndDate(
                   patient.getId(), today);

           if (patientTokens.isEmpty()) {
               throw new InfyHospitalException("Service.NO_ACTIVE_TOKEN_FOUND");
           }

           // Most-recent check-in
           Token currentToken = patientTokens.stream()
                   .max(Comparator.comparing(Token::getCheckInTime))
                   .orElseThrow(() -> new InfyHospitalException("Service.NO_ACTIVE_TOKEN_FOUND"));

           Integer position=currentToken.getPosition();



           Doctor doctor = currentToken.getDoctor();
           
           DoctorSchedule schedule=doctorScheduleRepository
                   .findByDoctorIdAndDayOfWeek(doctor.getId(), today.getDayOfWeek());
           
           Integer slotDuration=schedule.getSlotDuration();
           
         int buffer=5;
         int effectiveSlot=slotDuration+buffer;
         long estimatedWaitMinutes=(long)(position-1)*effectiveSlot;
         
         long currentPauseMinutes=0;
         if(Boolean.TRUE.equals(doctor.getPaused()))
         {
                     currentPauseMinutes=Duration.between(doctor.getPausedAt(), LocalDateTime.now()).toMinutes();
         }
         
         Long totalPausedMinutes=doctor.getTotalPausedMinutes();
         if(totalPausedMinutes==null)
         {
                 totalPausedMinutes=0L;
             
         }
         
         estimatedWaitMinutes+=totalPausedMinutes+currentPauseMinutes;
         if(currentToken.getPosition()==0)
         {
                 estimatedWaitMinutes=0;
         }

           return new QueuePositionDTO(
                   patient.getId().toString(),
                   patient.getName(),
                   currentToken.getTokenDisplay(),
                   position,
                   currentToken.getStatus().toString(),
                   doctor.getName(),
                   doctor.getDepartment().toString(),
                   doctor.getPaused(),
                   (int) estimatedWaitMinutes,
                   currentToken.getCheckInTime()
           );
       }
}
 