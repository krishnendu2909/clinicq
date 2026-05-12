package com.infy.config;



 

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


 

@Configuration

public class WebConfig implements WebMvcConfigurer {


 

    @Override

    public void addCorsMappings(CorsRegistry registry) {

        // This allows your React frontend to talk to your Spring Boot backend

        registry.addMapping("/**")

                .allowedOrigins("http://localhost:3000") // Your React URL

                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")

                .allowedHeaders("*")

                .allowCredentials(true);

    }

}