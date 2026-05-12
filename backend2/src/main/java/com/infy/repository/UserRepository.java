package com.infy.repository;


 

import java.util.Optional;


 

import org.springframework.data.jpa.repository.JpaRepository;


 

import com.infy.entity.User;


 

public interface UserRepository extends JpaRepository<User, Long>{


 

    boolean existsByEmail(String email);

    public Optional<User> findByEmail(String email);

   

    Optional<User> findByResetToken(String resetToken);

}

