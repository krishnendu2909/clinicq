package com.infy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infy.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{
    
    public Optional<Admin> findTopByOrderByIdAsc();
    
    @Query("SELECT a FROM Admin a")
    public Admin getBookingRules();

}
