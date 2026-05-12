package com.infy.config;


 

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;


 

import com.infy.entity.User;

import com.infy.models.Role;

import com.infy.repository.UserRepository;


 

@Configuration

public class DataInitializer {

   

    @Bean

    CommandLineRunner init(UserRepository userRepo, PasswordEncoder encoder)

    {

        return args->{

            if(userRepo.findByEmail("admin@example.com").isEmpty())

            {

                User admin=new User();

                admin.setEmail("admin@example.com");

                admin.setPassword(encoder.encode("admin123$"));

                admin.setRole(Role.ADMIN);

               

                userRepo.save(admin);

            }

            if(userRepo.findByEmail("receptionist@example.com").isEmpty())

            {

                User admin=new User();

                admin.setEmail("receptionist@example.com");

                admin.setPassword(encoder.encode("receptionist123$"));

                admin.setRole(Role.RECEPTIONIST);

               

                userRepo.save(admin);

            }

        };

       

    }


 

}
