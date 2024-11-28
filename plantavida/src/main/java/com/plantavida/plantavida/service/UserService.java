package com.plantavida.plantavida.service;

import com.plantavida.plantavida.persistence.entity.UserEntity;
import com.plantavida.plantavida.persistence.entity.UserRoleEntity;
import com.plantavida.plantavida.persistence.repository.UserRepository;
import com.plantavida.plantavida.persistence.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity register(UserEntity userDetails) {
        // Validar existencia de usuario o correo
        if (userRepository.existsByUsername(userDetails.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        if (userRepository.existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está en uso");
        }

        // Encriptar contraseña
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        userDetails.setLocked(false);
        userDetails.setDisabled(false);

        // Guardar usuario
        UserEntity savedUser = userRepository.save(userDetails);

        // Asignar rol CUSTOMER
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUsername(savedUser.getUsername());
        userRole.setRole("CUSTOMER");
        userRole.setGrantedDate(LocalDateTime.now());
        userRole.setUser(savedUser);

        userRoleRepository.save(userRole);

        return savedUser;
    }
}



