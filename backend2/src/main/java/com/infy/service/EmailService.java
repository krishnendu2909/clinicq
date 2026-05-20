package com.infy.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EmailService {

    void sendResetPasswordEmail(String toEmail, String token);

    void sendAppointmentReminder(String toEmail, String patientName, String doctorName, LocalDate date, LocalTime time);

}
 