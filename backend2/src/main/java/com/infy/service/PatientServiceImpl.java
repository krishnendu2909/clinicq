package com.infy.service;


 

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.ArrayList;

import java.util.Comparator;

import java.util.List;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.infy.dto.AppointmentDTO;

import com.infy.dto.DoctorDTO;

import com.infy.dto.TimeSlotDTO;

import com.infy.dto.UserDTO;

import com.infy.entity.Admin;

import com.infy.entity.Appointment;

import com.infy.entity.Doctor;

import com.infy.entity.Patient;

import com.infy.entity.TimeSlot;

import com.infy.exception.InfyHospitalException;

import com.infy.models.AppointmentStatus;

import com.infy.models.AppointmentType;

import com.infy.models.Department;

import com.infy.repository.AdminRepository;

import com.infy.repository.AppointmentRepository;

import com.infy.repository.DoctorRepository;

import com.infy.repository.PatientRepository;

import com.infy.repository.TimeSlotRepository;



 

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

   

    ModelMapper modelMapper =new ModelMapper();


 

    //US1

    //Book Appointment

   

    //getDoctorByDepartment

    @Override

    public List<DoctorDTO> getDoctorsByDepartment(Department department) throws InfyHospitalException

    {

        List<Doctor> doctors=doctorRepository.findByDepartment(department);

        if(doctors.isEmpty())

        {

            throw new InfyHospitalException("Service.NO_DOCTORS_FOUND");

        }

        List<DoctorDTO> doctorDTOs=new ArrayList<>();

       

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

        Doctor doctor=doctorRepository.findById(doctorId).orElseThrow(

                ()->new InfyHospitalException("Service.NO_DOCTOR_FOUND") );

       

        List<TimeSlot> timeSlots=timeSlotRepository.findByDoctorIdAndSlotDate(doctorId, date);

        if(timeSlots.isEmpty())

        {

            throw new InfyHospitalException("Service.NO_TIMESLOTS_FOUND");

        }

        List<TimeSlotDTO> timeSlotDTOs=new ArrayList<>();

               

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


 

    // 1. Check patient exists

    Patient patient = patientRepository.findById(patientId)

            .orElseThrow(() -> new InfyHospitalException("Service.NO_PATIENT_FOUND"));


 

    //  2. Fetch appointments

    List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);


 

    // 3. Check if patient has any appointments

    if (appointments.isEmpty()) {

        throw new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND");

    }


 

    // 4. Filter upcoming (BOOKED)

    List<AppointmentDTO> result = appointments.stream()

            .filter(a -> a.getStatus() == AppointmentStatus.BOOKED)

            .sorted(

                Comparator.comparing((Appointment a) -> a.getTimeSlot().getSlotDate())

                          .thenComparing(a -> a.getTimeSlot().getStartTime())

            )

            .map(a -> modelMapper.map(a, AppointmentDTO.class))

            .toList();


 

    //  5. If no upcoming appointments

    if (result.isEmpty()) {

        throw new InfyHospitalException("Service.NO_UPCOMING_APPOINTMENT_FOUND");

    }


 

    return result;

    }


 

   

    //US2

   

    @Override

    public List<AppointmentDTO> getAllAppointments(Long patientId) throws InfyHospitalException {


 

    // 1. Check patient exists

    Patient patient = patientRepository.findById(patientId)

            .orElseThrow(() -> new InfyHospitalException("Service.NO_PATIENT_FOUND"));


 

    //  2. Fetch appointments

    List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);


 

    // 3. Check if patient has any appointments

    if (appointments.isEmpty()) {

        throw new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND");

    }


 

    List<AppointmentDTO> result = appointments.stream()

            .sorted(

                Comparator.comparing((Appointment a) -> a.getTimeSlot().getSlotDate())

                          .thenComparing(a -> a.getTimeSlot().getStartTime())

            )

            .map(a -> modelMapper.map(a, AppointmentDTO.class))

            .toList();


 

    //  5. If no appointments

    if (result.isEmpty()) {

        throw new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND");

    }


 

    return result;

    }



 

    //US3

   

    @Override

    @Transactional(rollbackFor = Exception.class)

    public Long cancelAppointment(Long appointmentId) throws InfyHospitalException {


 

        Appointment appointment = appointmentRepository.findById(appointmentId)

                .orElseThrow(() -> new InfyHospitalException("Service.APPOINTMENT_NOT_FOUND"));

       

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


 

        TimeSlot oldSlot = appointment.getTimeSlot();

     // 1. Get the current time

        LocalDateTime now = LocalDateTime.now();


 

        // 2. Build the appointment time from the old slot

        LocalDateTime oldAppointmentDateTime = LocalDateTime.of(

            oldSlot.getSlotDate(),

            oldSlot.getStartTime()

        );


 

        // 3. Logic: Only allow rescheduling if the appointment is in the FUTURE


 

        if (now.isAfter(oldAppointmentDateTime)) {

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

    public List<AppointmentDTO> getVisitHistoryFiltered(

    Long patientId,

    LocalDate startDate,

    LocalDate endDate,

    Long doctorId) throws InfyHospitalException {


 

    patientRepository.findById(patientId)

            .orElseThrow(() -> new InfyHospitalException("Service.NO_PATIENT_FOUND"));


 

    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {

        throw new InfyHospitalException("Service.INVALID_TIME_RANGE");

    }


 

    List<AppointmentDTO>appointments= appointmentRepository

            .findFilteredHistory(patientId, doctorId, startDate, endDate)

            .stream()

            .map(a -> modelMapper.map(a, AppointmentDTO.class))

            .toList();

   

    if(appointments.isEmpty())

    {

        throw new InfyHospitalException("Service.NO_HISTORY_FOUND");

}

return appointments;

}

@Override
@Transactional
public QueuePositionDTO getQueuePosition(String patientId) throws InfyHospitalException {
    Patient patient = patientRepository.findById(Long.valueOf(patientId))
            .orElseThrow(() -> new InfyHospitalException("Service.NO_PATIENT_FOUND"));

    // Get all tokens for this patient today
    LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
    List<Token> patientTokens = tokenRepository.findByPatientIdAndTimestampAfter(
            Long.valueOf(patientId), todayStart);

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
            patientId,
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

@Override
@Transactional
public QueuePositionDTO getQueuePositionByPhone(String phoneNumber) throws InfyHospitalException {
    Patient patient = patientRepository.findByPhone(phoneNumber)
            .orElseThrow(() -> new InfyHospitalException("Service.NO_PATIENT_FOUND"));

    return getQueuePosition(patient.getId().toString());
}
}
