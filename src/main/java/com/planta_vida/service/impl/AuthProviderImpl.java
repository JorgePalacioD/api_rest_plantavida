package com.planta_vida.service.impl;

import com.planta_vida.security.jwt.JwtUtil;
import com.planta_vida.service.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthProviderImpl implements AuthProvider {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            String email = requestMap.get("email");
            String password = requestMap.get("password");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            final String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email or password");
        }
    }
}

