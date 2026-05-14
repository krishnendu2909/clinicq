package com.infy.scheduler;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.infy.repository.TokenRepository;

import jakarta.transaction.Transactional;

@Service
public class TokenScheduler {
    
    @Autowired
    private TokenRepository tokenRepository;
    
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteOldTokens()
    {
        LocalDate today=LocalDate.now();
        tokenRepository.deleteByDateBefore(today);
        System.out.println("Old tokens deleted");
        
    }
    

}