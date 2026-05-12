package com.infy.dto;


 

import java.time.LocalDate;



 

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;


 

@Data

@AllArgsConstructor

@NoArgsConstructor

public class UserDTO {


 

private Long id;


 

@NotBlank(message = "{user.email.notBlank}")

@Email(message = "{user.email.invalid}")

private String email;


 

@NotBlank(message = "{user.password.notBlank}")

@Size(min = 6, max = 20, message = "{user.password.size}")

private String password;


 

private LocalDate createdAt;


 

}
