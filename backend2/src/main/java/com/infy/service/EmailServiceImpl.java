package com.infy.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Override
    public void sendResetPasswordEmail(String toEmail, String token) {
        SimpleMailMessage message=new SimpleMailMessage();
        
        message.setTo(toEmail);
        message.setSubject("ClinicQ Password Reset OTP");
        message.setText("Your OTP for password reset is: \n\n"+token+"\n\nThis OTP is valid for 15 minutes.");
        
        mailSender.send(message);
    }
    
    @Override
    public void sendAppointmentReminder(String toEmail, String patientName, 
            String doctorName,LocalDate date, LocalTime time) {
        SimpleMailMessage message=new SimpleMailMessage();
        
        message.setTo(toEmail);
        message.setSubject("ClinicQ Appointment Reminder");
        message.setText(
                "Dear "+patientName+",\n\n"
                +"Reminder: You have an appointment with Dr. "
                        +doctorName+" at "+time+" on "+date+".\n\n"
                        +"Please arrive on time. \n\n"
                        +"Regards,\nClinicQ");
        
        mailSender.send(message);
    }
    
}
 