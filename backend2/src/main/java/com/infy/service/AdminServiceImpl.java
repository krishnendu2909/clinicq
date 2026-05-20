package com.infy.service;

import java.time.DayOfWeek;
import java.time.LocalDate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.dto.AdminStatsSummaryDTO;
import com.infy.dto.DailyPatientCountDTO;
import com.infy.dto.DoctorDTO;
import com.infy.dto.DoctorScheduleDTO;
import com.infy.dto.UserDTO;
import com.infy.entity.Admin;
import com.infy.entity.Doctor;
import com.infy.entity.DoctorSchedule;
import com.infy.entity.TimeSlot;
import com.infy.entity.User;
import com.infy.exception.InfyHospitalException;
import com.infy.models.AppointmentStatus;
import com.infy.models.AppointmentType;
import com.infy.models.Department;
import com.infy.models.Gender;
import com.infy.models.Role;
import com.infy.repository.AdminRepository;
import com.infy.repository.AppointmentRepository;
import com.infy.repository.DoctorRepository;
import com.infy.repository.DoctorScheduleRepository;
import com.infy.repository.TimeSlotRepository;
import com.infy.repository.TokenRepository;
import com.infy.repository.UserRepository;

@Service
@Transactional
public class AdminServiceImpl implements AdminService{

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private TokenRepository tokenRepository;


    @Override
    public List<DoctorScheduleDTO> getAllDoctorSchedules() throws InfyHospitalException {
        // 1. Fetch all schedules from the repository
        Iterable<DoctorSchedule> schedules = doctorScheduleRepository.findAll();
        List<DoctorScheduleDTO> scheduleDTOs = new ArrayList<>();

        for (DoctorSchedule schedule : schedules) {
            // 2. Map Entity to DoctorScheduleDTO
            DoctorScheduleDTO sDto = new DoctorScheduleDTO();
            sDto.setId(schedule.getId());
            sDto.setDayOfWeek(schedule.getDayOfWeek());
            sDto.setStartTime(schedule.getStartTime());
            sDto.setEndTime(schedule.getEndTime());
            sDto.setSlotDuration(schedule.getSlotDuration());
            sDto.setCreatedAt(schedule.getCreatedAt());

            // 3. Map the nested Doctor entity to DoctorDTO
            Doctor doctor = schedule.getDoctor();
            DoctorDTO dDto = new DoctorDTO();
            dDto.setId(doctor.getId());
            dDto.setName(doctor.getName());
            dDto.setDepartment(doctor.getDepartment());
            dDto.setGender(doctor.getGender());
            dDto.setPhone(doctor.getPhone());
            dDto.setLocation(doctor.getLocation());
            dDto.setDescription(doctor.getDescription());

            // 4. Map the nested User entity to UserDTO (if needed)
            UserDTO uDto = new UserDTO();
            uDto.setEmail(doctor.getUser().getEmail());
            // Do not send passwords to the frontend!
            dDto.setUser(uDto);

            // Link them together
            sDto.setDoctor(dDto);
            scheduleDTOs.add(sDto);
        }

        return scheduleDTOs;
    }
    
    
    @Override
    public Long addDoctor(String email, String password, String name, Department department, 
                        Gender gender, String phone, String location, String description,
                        List<DayOfWeek> days, LocalTime startTime, LocalTime endTime, Integer slotDuration) 
                        throws InfyHospitalException {   
        // 1. Create User and Save
        User user = new User();
        user.setEmail(email);
        
        // Encode password before setting
        String rawPassword=password;
        String hashedPassword=passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPassword);
        
        user.setRole(Role.DOCTOR);
        user = userRepository.save(user);
        // 2. Create Doctor and Save
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setName(name);
        doctor.setDepartment(department);
        doctor.setGender(gender);
        doctor.setPhone(phone);
        doctor.setLocation(location);
        doctor.setDescription(description);
        doctor = doctorRepository.save(doctor);
     // 3. Loop through the days and save a row for each (MULTIPLE)
        for (DayOfWeek day : days) {
            DoctorSchedule schedule = new DoctorSchedule();
            schedule.setDoctor(doctor);
            schedule.setDayOfWeek(day);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setSlotDuration(slotDuration);
            doctorScheduleRepository.save(schedule);
        }
     // 2. IMPORTANT: If you have initial schedule info, generate slots immediately
        // Or ensure the Admin "Modify" step is completed.
        int daysAhead = adminRepository.findTopByOrderByIdAsc().get().getMaxDaysInAdvance();
        generateTimeSlots(doctor.getId(), daysAhead);
        return doctor.getId();
    }
   
   public void deleteDoctor(Long doctorId) throws InfyHospitalException{
       
       Doctor doctor=doctorRepository.findById(doctorId).orElseThrow(()-> 
       new InfyHospitalException("Service.NO_DOCTOR_FOUND"));
       
       Long userId = doctor.getUser().getId();

       // 2. Delete schedule entries first (Foreign Key constraint)
       tokenRepository.deleteByDoctorId(doctorId);
       appointmentRepository.deleteByDoctorId(doctorId);
       timeSlotRepository.deleteByDoctor(doctor);
   doctorScheduleRepository.deleteByDoctorId(doctorId);     
       doctorRepository.delete(doctor); //schedules will be deleted by cascade
       
       // 4. Delete the user credentials entry
       userRepository.deleteById(userId);
   }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void configureDoctorSchedule(Long doctorId, List<DayOfWeek> days, LocalTime startTime, 
            LocalTime endTime, Integer slotDuration) throws InfyHospitalException {
        
        
        // 1. Fetch the doctor entity first to verify they exist
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new InfyHospitalException("Service.NO_DOCTOR_FOUND"));

        // 2. Clear out all OLD schedule entries for this specific doctor
        doctorScheduleRepository.deleteByDoctorId(doctorId);
        
     // Delete existing unbooked slots to make room for new ones
        timeSlotRepository.deleteFutureUnbookedSlots(doctorId, LocalDate.now());
        
     // 3. Save the NEW schedule entries
        for (DayOfWeek day : days) {
            // We don't need 'exists' check because we just deleted everything
            DoctorSchedule schedule = new DoctorSchedule();
            schedule.setDoctor(doctor);
            schedule.setDayOfWeek(day);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setSlotDuration(slotDuration);
            
            doctorScheduleRepository.save(schedule);
        }
        
        // 4. Trigger slot generation logic
        Admin admin = adminRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new InfyHospitalException("Service.NO_ADMIN_FOUND"));
        
        int daysAhead = admin.getMaxDaysInAdvance();
        generateTimeSlots(doctorId, daysAhead);
        
    }
    
    public void generateTimeSlots(Long doctorId, int daysAhead) throws InfyHospitalException {
        Doctor doctor=doctorRepository.findById(doctorId).orElseThrow(()->new InfyHospitalException("Service.NO_DOCTOR_FOUND"));
        
        List<DoctorSchedule> schedules=doctorScheduleRepository.findByDoctor(doctor);
        
//        LocalDate today= LocalDate.now();
        
        // Get latest generated slot date
        LocalDate latestDate=timeSlotRepository.findLatestSlotDate(doctor);
        LocalDate startDate;
        
        // if slots exist, start generating from next day
        if (latestDate!=null) {
            startDate=latestDate.plusDays(1);
        }else {
            // first time generation
            startDate=LocalDate.now();
        }
        

//        for (int i=0; i<daysAhead; i++) {
        
        // Maintain rolling window
        LocalDate endDate=LocalDate.now().plusDays(daysAhead);
        
        List<TimeSlot> newSlots = new ArrayList<>();
        
        // Generate only missing future dates
        while(!startDate.isAfter(endDate)) {
//            LocalDate date=today.plusDays(i);
            
//            DayOfWeek day=date.getDayOfWeek();
            
            DayOfWeek currentDay=startDate.getDayOfWeek();
            
            for (DoctorSchedule schedule:schedules) {
//                String scheduleDay = schedule.getDayOfWeek().toString().substring(0, 3).toUpperCase();
//                String currentDay = day.toString().substring(0, 3).toUpperCase();

                if (!schedule.getDayOfWeek().equals(currentDay)) continue;
                
                LocalTime current=schedule.getStartTime();
                LocalTime end=schedule.getEndTime();
                int duration=schedule.getSlotDuration();
                
                while (!current.plusMinutes(duration).isAfter(end)) {
                    boolean exists=timeSlotRepository.existsByDoctorAndSlotDateAndStartTime(doctor, startDate, current);
                    
                    if (!exists) {
                        TimeSlot slot=new TimeSlot();
                        slot.setDoctor(doctor);
                        slot.setSlotDate(startDate);
                        slot.setStartTime(current);
                        slot.setEndTime(current.plusMinutes(duration));
                        slot.setBooked(false);
                        newSlots.add(slot);
                        //timeSlotRepository.save(slot);
                    }
                    // Next slot
                    current=current.plusMinutes(duration);
                }
            }
            // Move to next date
            startDate=startDate.plusDays(1);
        }
        // Bulk save
        if (!newSlots.isEmpty()) {
            timeSlotRepository.saveAll(newSlots);
        }
    }
    
    public void updateBookingRules(int maxPatients, int maxDays, int cutoffHours) throws InfyHospitalException {
        Admin config=adminRepository.findTopByOrderByIdAsc()
                .orElse(new Admin());
        
        config.setMaxDaysInAdvance(maxDays);
        config.setMaxPatientsPerSlot(maxPatients);
        config.setCancellationCutoffHours(cutoffHours);
        
        // save booking rules
        adminRepository.save(config);

        List<Doctor> doctors=doctorRepository.findAll();
        
        for (Doctor doctor:doctors) {
            // 1. delete future slots if days decreases
            LocalDate cutoff=LocalDate.now().plusDays(maxDays);
            timeSlotRepository.deleteFutureUnbookedSlots(doctor.getId(), cutoff);
            
            // 2. generate additional slots if days increase
            generateTimeSlots(doctor.getId(), maxDays);
        }
        

    }
    
    // METHODS FOR STATS
    
    @Override
    public AdminStatsSummaryDTO getSummary( Long doctorId, LocalDate startDate, LocalDate endDate) throws InfyHospitalException {
        
        AdminStatsSummaryDTO dto=new AdminStatsSummaryDTO();
        
        dto.setTotalAppointments(appointmentRepository.getTotalAppointments(doctorId, startDate, endDate));
        
        dto.setTotalWalkIns(appointmentRepository.getWalkInCount(doctorId, AppointmentType.WALK_IN, startDate, endDate));
        
        dto.setTotalCompleted(appointmentRepository.countByAppointmentStatus(doctorId, AppointmentStatus.COMPLETED, startDate, endDate));
        
        dto.setTotalNoShows(appointmentRepository.countByAppointmentStatus(doctorId, AppointmentStatus.NO_SHOW, startDate, endDate));
        
        return dto;
    }
    
    @Override
    public List<DailyPatientCountDTO> getChartData(LocalDate startDate, LocalDate endDate, Long doctorId) throws InfyHospitalException{
        
        List<Object[]> results=appointmentRepository.getDailyPatientCounts(doctorId, startDate, endDate);
                
        List<DailyPatientCountDTO> chartData=new ArrayList<>();
        
        for (Object[] row:results) {
            DailyPatientCountDTO dto=new DailyPatientCountDTO();
            dto.setDate((LocalDate)row[0]);
            dto.setPatientCount((Long)row[1]);
            
            chartData.add(dto);
        }
        
        return chartData;
    }
}
 