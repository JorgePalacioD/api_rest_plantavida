package com.plantavida.plantavida.service;

import com.plantavida.plantavida.persistence.entity.UserEntity;
import com.plantavida.plantavida.persistence.repository.UserRepository;
import com.plantavida.plantavida.service.dto.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";

    public UserEntity saveUser(UserEntity user) {
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Contraseña inválida: debe tener al menos 8 caracteres, incluyendo letras y números");
        }
        return userRepository.save(user);
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserEntity> authenticateUser(String identifier, String password) {
        Optional<UserEntity> userOpt = userRepository.findByUsernameOrEmail(identifier, identifier);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return userOpt;
        }
        return Optional.empty(); // si no coincide el usuario o la contraseña
    }

    private boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && Pattern.matches(EMAIL_REGEX, email);
    }

    private boolean isValidPassword(String password) {
        return StringUtils.hasText(password) && Pattern.matches(PASSWORD_REGEX, password);
    }

    public void registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByUsername(userRegisterDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()) {
            System.out.println("El email ya está registrado: " + userRegisterDto.getEmail());
            throw new RuntimeException("Email already in use");
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(userRegisterDto.getEmail());
        newUser.setUsername(userRegisterDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        newUser.setLocked(false);
        newUser.setDisabled(false);
        newUser.setRole("ROLE_CUSTOMER");

        userRepository.save(newUser);
    }
}
