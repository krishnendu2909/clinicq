package com.infy.repository;


 

import java.util.List;


 

import org.springframework.data.jpa.repository.JpaRepository;


 

import com.infy.entity.Doctor;

import com.infy.entity.User;

import com.infy.models.Department;


 

public interface DoctorRepository extends JpaRepository<Doctor, Long>{

   

    List<Doctor> findByDepartment(Department department);

   

    Doctor findByUser(User user);


 

}


