package com.infy.config;


 

import java.io.IOException;

import java.util.List;


 

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;


 

import com.infy.entity.User;

import com.infy.repository.UserRepository;


 

import jakarta.servlet.FilterChain;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;


 

@Component

@RequiredArgsConstructor

public class JwtFilter extends OncePerRequestFilter {


 

private final JwtUtil jwtUtil;

private final UserRepository userRepository;


 

@Override

protected void doFilterInternal(HttpServletRequest request,

                                HttpServletResponse response,

                                FilterChain filterChain)

        throws ServletException, IOException {


 

    String authHeader = request.getHeader("Authorization");


 

    //  1. Check header

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {

        filterChain.doFilter(request, response);

        return;

    }


 

    //  2. Extract token

    String token = authHeader.substring(7);


 

    //  3. Validate token

    if (!jwtUtil.validateToken(token)) {

        filterChain.doFilter(request, response);

        return;

    }


 

    //  4. Extract email

    String email = jwtUtil.extractEmail(token);


 

    if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null) {

    //  5. Load user

    User user = userRepository.findByEmail(email).orElse(null);  


 

    if (user != null) {

       

        List<SimpleGrantedAuthority> authorities=List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().name()));

        UsernamePasswordAuthenticationToken auth =

                new UsernamePasswordAuthenticationToken(

                        user.getEmail(),

                        null,

                        authorities

                );


 

        //  6. Set authentication

        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    }


 

    filterChain.doFilter(request, response);

}

}

