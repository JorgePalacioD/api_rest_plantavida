package com.plantavida.plantavida.web.controller;

import com.plantavida.plantavida.service.UserService;
import com.plantavida.plantavida.service.dto.AuthResponse;
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
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto loginDto) {
        // Verifica si el DTO es nulo o si las credenciales son nulas
        if (loginDto == null || (loginDto.getUsername() == null && loginDto.getEmail() == null) || loginDto.getPassword() == null) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Identifier and password must not be null"));
        }

        // Crear un token de autenticación
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername() != null ? loginDto.getUsername() : loginDto.getEmail(),
                loginDto.getPassword());

        try {
            // Intentar autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Si la autenticación es exitosa, se genera el token JWT
            String jwt = jwtUtil.create(authentication.getName());

            // Responder con el token y mensaje en el cuerpo de la respuesta
            return ResponseEntity.ok(new AuthResponse(jwt, "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, "Invalid username or password"));
        }
    }
}
