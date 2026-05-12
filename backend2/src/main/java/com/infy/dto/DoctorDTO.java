package com.infy.dto;

import com.infy.models.Department;
import com.infy.models.Gender;

import lombok.Data;

@Data
public class DoctorDTO {
	
	private Long id;
	private UserDTO user;	
	private String name;
	private Department department;
	private Gender gender;	
	private String phone;
	private String location;
	private String description;

}
