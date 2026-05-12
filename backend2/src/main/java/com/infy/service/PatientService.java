package com.infy.service;


 

import java.time.LocalDate;

import java.util.List;


 

import com.infy.dto.AppointmentDTO;

import com.infy.dto.DoctorDTO;

import com.infy.dto.TimeSlotDTO;

import com.infy.exception.InfyHospitalException;

import com.infy.models.Department;


 

public interface PatientService {

   

    //US1

    public List<DoctorDTO> getDoctorsByDepartment(Department department)throws InfyHospitalException;

   

    public DoctorDTO getDoctorById(Long id) throws InfyHospitalException;

   

    public List<TimeSlotDTO> getAllSlots(Long doctorId,LocalDate date) throws InfyHospitalException ;

   

    public Long bookAppointment(Long patientId,Long doctorId,Long slotId,String reason) throws InfyHospitalException;


 

    List<AppointmentDTO> getUpcomingAppointments(Long patientId)throws InfyHospitalException;

   

    //US3

    public Long cancelAppointment(Long appointmentId) throws InfyHospitalException;


 

    public AppointmentDTO reschedule(Long appointmentId, Long newSlotId) throws InfyHospitalException;

   

    //US2


 

    List<AppointmentDTO> getAllAppointments(Long patientId) throws InfyHospitalException;


 

    //US4

    List<AppointmentDTO> getVisitHistoryFiltered(Long patientId, LocalDate startDate, LocalDate endDate, Long doctorId) throws InfyHospitalException;
   
   // Queue position functionality
   public QueuePositionDTO getQueuePosition(String patientId) throws InfyHospitalException;
   public QueuePositionDTO getQueuePositionByPhone(String phoneNumber) throws InfyHospitalException;


 

   


 

}


