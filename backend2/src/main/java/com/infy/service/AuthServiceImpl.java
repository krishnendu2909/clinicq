package com.infy.service;

import java.time.LocalDateTime;

import java.util.UUID;


 

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;


 

import com.infy.config.JwtUtil;

import com.infy.dto.PatientDTO;

import com.infy.entity.Patient;

import com.infy.entity.User;

import com.infy.exception.InfyHospitalException;

import com.infy.models.Role;

import com.infy.repository.PatientRepository;

import com.infy.repository.UserRepository;


 

@Service

public class AuthServiceImpl implements AuthService {


 

    @Autowired

    private PasswordEncoder passwordEncoder;

    @Autowired

    private UserRepository userRepository;

    @Autowired

    private PatientRepository patientRepository;

    @Autowired

    private JwtUtil jwtUtil;


 

    @Override

    public String login(String email, String password, Role role) throws InfyHospitalException {

        User user=userRepository.findByEmail(email).orElseThrow(()->new InfyHospitalException("Service.INVALID_USER"));

       

        // Check if account is locked

        if (user.getAccountLocked().equals(Boolean.TRUE)){

            // LOCK TIME SET AS 1 MINUTE

            if(user.getLockTime()!=null && user.getLockTime().plusMinutes(1).isAfter(LocalDateTime.now())) {

                throw new InfyHospitalException("Service.ACCOUNT_LOCKED");

            } else {

                // Unlock after time passes

                user.setAccountLocked(false);

                user.setFailedAttempts(0);

                user.setLockTime(null);

            }

        }

       

        // Check password

        if (!passwordEncoder.matches(password, user.getPassword())) {

            int attempts=user.getFailedAttempts();

            attempts++;

           

            user.setFailedAttempts(attempts);

           

            // Lock condition (ATTEMPTS SET AS 2)

            if (attempts>2) {

                System.out.println(attempts);

                user.setAccountLocked(true);

                user.setLockTime(LocalDateTime.now());

               

                userRepository.save(user);

               

                throw new InfyHospitalException("Service.ACCOUNT_LOCKED");

            }

           

            userRepository.save(user);

           

            throw new InfyHospitalException("Service.INVALID_CREDENTIALS");

        }

       

        // Reset attempts on success

        user.setFailedAttempts(0);

        user.setAccountLocked(false);

        user.setLockTime(null);

       

        userRepository.save(user);

       

        // Check authorization

        if (user.getRole()!=role) {

            throw new InfyHospitalException("Service.UNAUTHORIZED_ACCESS");

        }

       

        return jwtUtil.generateToken(user);

    }

   

    @Override

    public void registerPatient(PatientDTO dto) throws InfyHospitalException

    {

        if(userRepository.existsByEmail(dto.getUser().getEmail()))

        {

            throw new InfyHospitalException("Service.USER_ALREADY_PRESENT");

        }

       

        User user=new User();

        user.setEmail(dto.getUser().getEmail());

        user.setPassword(passwordEncoder.encode(dto.getUser().getPassword()));

        user.setRole(Role.PATIENT);

       

        User savedUser=userRepository.save(user);

       

        Patient patient =new Patient();

        patient.setName(dto.getName());

        patient.setPhone(dto.getPhone());

        patient.setDateOfBirth(dto.getDateOfBirth());

        patient.setGender(dto.getGender());

        patient.setUser(savedUser);

       

        patientRepository.save(patient);

    }

   

    @Override

    public String forgotPassword(String email) throws InfyHospitalException {

       User user=userRepository.findByEmail(email).orElseThrow(()->new InfyHospitalException("Service.INVALID_USER"));

       

       // Generate reset token

       String token=UUID.randomUUID().toString();

       

       user.setResetToken(token);

       

       // Set token expiry to 15 min

       user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));

       

       userRepository.save(user);

       

       // For testing

       System.out.println("Reset token: "+token);

       

       // Token sent to UI

       return token;

       

    }

   

    @Override

    public void resetPassword(String token, String newPassword) throws InfyHospitalException {

       User user=userRepository.findByResetToken(token).orElseThrow(()->new InfyHospitalException("Service.INVALID_USER"));


 

       if (user.getTokenExpiry()==null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {

           throw new InfyHospitalException("Service.TOKEN_EXPIRED");

       }

       

       user.setPassword(passwordEncoder.encode(newPassword));

       

       // Clear token after use

       user.setResetToken(null);

       user.setTokenExpiry(null);

       

       userRepository.save(user);

    }


 

}
