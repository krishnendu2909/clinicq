package com.infy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{
	
	public Optional<Admin> findTopByOrderByIdAsc();

}
