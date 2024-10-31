package com.plantavida.plantavida.web.controller;

import com.plantavida.plantavida.service.UserService;
import com.plantavida.plantavida.service.dto.LoginDto;
import com.plantavida.plantavida.web.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        if (loginDto == null || loginDto.getIdentifier() == null || loginDto.getPassword() == null) {
            return ResponseEntity.badRequest().body("Identifier and password must not be null");
        }

        var userOpt = userService.authenticateUser(loginDto.getIdentifier(), loginDto.getPassword());
        if (userOpt.isPresent()) {
            String jwt = jwtUtil.create(userOpt.get().getUsername());
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).body("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username/email or password");
        }
    }
}
