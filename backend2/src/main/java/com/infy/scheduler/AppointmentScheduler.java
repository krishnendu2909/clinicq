package com.infy.scheduler;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Component;

import com.infy.service.ReceptionistService;

import lombok.RequiredArgsConstructor;

@Component

@RequiredArgsConstructor

public class AppointmentScheduler {


    @Autowired

    private  ReceptionistService receptionistService;

    @Scheduled(fixedRate = 120000)

    public void updateAppointmentNoShowAppointments()

    {

        receptionistService.markNoShowAppointments();

    }

   

   

   


 

}


 