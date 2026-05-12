package com.infy.dto;


 

import java.time.LocalDate;


 

import com.infy.models.Gender;


 

import jakarta.validation.Valid;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Past;

import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;


 

@Data

@AllArgsConstructor

@NoArgsConstructor

public class PatientDTO {


 

private Long id;


 

@Valid

@NotNull(message = "{user.notNull}")

private UserDTO user;


 

@NotBlank(message = "{patient.name.notBlank}")

@Pattern(regexp = "^[A-Za-z ]+$", message = "{patient.name.invalid}")

private String name;


 

@NotNull(message = "{patient.dob.notNull}")

@Past(message = "{patient.dob.past}")

private LocalDate dateOfBirth;


 

@NotNull(message = "{patient.gender.notNull}")

private Gender gender;


 

@NotBlank(message = "{patient.phone.notBlank}")

@Pattern(

    regexp = "^[6-9][0-9]{9}$",

    message = "{patient.phone.invalid}"

)

private String phone;


 

}
