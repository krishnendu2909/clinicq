package com.infy.config;


 

import java.security.Key;

import java.util.Date;


 

import org.springframework.stereotype.Component;


 

import com.infy.entity.User;


 

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.JwtException;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;


 

@Component

public class JwtUtil {

    private final String SECRET="mysecretkeymysecretkeymysecretkey12345";

   

    private Key getKey()

    {

        return Keys.hmacShaKeyFor(SECRET.getBytes());

       

    }

   

    // Generate token

    public String generateToken(User user) {

        return Jwts.builder()

                .setSubject(user.getEmail())

                .claim("role", user.getRole().name())

                .setIssuedAt(new Date())

                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60)) //1 hour

                .signWith(getKey(),SignatureAlgorithm.HS256)

                .compact();

    }

   

    // Extract all claims

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(getKey())

                .build()

                .parseClaimsJws(token)

                .getBody();

    }

   

    // Extract email

    public String extractEmail(String token) {

        return extractAllClaims(token).getSubject();

    }

   

    // Extract role

    public String extractRole(String token) {

        return extractAllClaims(token).get("role", String.class);

    }

   

    // Validate token

    public boolean validateToken(String token) {

        try {

            extractAllClaims(token);

            return true;

        } catch ( JwtException | IllegalArgumentException e) {

            return false;

        }

    }

}
