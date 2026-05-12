package com.infy.config;


 

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


 

import com.infy.exception.InfyHospitalException;


 

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;


 

@Configuration

@EnableWebSecurity

@RequiredArgsConstructor

public class SecurityConfig {


 

    private final JwtFilter jwtFilter;

   

    @Bean

    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

   

    @Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

       

       

        http.csrf(csrf->csrf.disable())

        .sessionManagement(session->

        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .exceptionHandling

        (ex->ex

                .authenticationEntryPoint((request,response,authException)-> {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        })

        .accessDeniedHandler((request,response,accessDeniedException)->{

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            })

        )

        .authorizeHttpRequests(auth->auth

               

                // allow swagger

                .requestMatchers("/v3/api-docs/**",

                        "/swagger-ui/**",

                        "/swagger-ui.html")

                .permitAll()

               

                // allow login/signup

                .requestMatchers("/clinicq/auth/**")

                .permitAll()

               

                .requestMatchers("/clinicq/admin/**")

                .hasRole("ADMIN")

               

                .requestMatchers("/clinicq/doctor/**")

                .hasRole("DOCTOR")

               

                .requestMatchers("/clinicq/patient/**")

                .hasAnyRole("PATIENT","RECEPTIONIST")

               

                .requestMatchers("/clinicq/receptionist/**")

                .hasRole("RECEPTIONIST")

                                           

                // protect others

                .anyRequest().authenticated()

                );

       

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

       

    }

}


