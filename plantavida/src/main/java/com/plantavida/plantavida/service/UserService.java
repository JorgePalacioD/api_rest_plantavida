package com.plantavida.plantavida.service;

import com.plantavida.plantavida.persistence.entity.UserEntity;
import com.plantavida.plantavida.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Expresión regular para validar email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    // Validar contraseña: al menos 8 caracteres, un número y una letra
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";

    public UserEntity saveUser(UserEntity user) {
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Contraseña inválida: debe tener al menos 8 caracteres, incluyendo letras y números");
        }

        // Guardar el usuario
        return userRepository.save(user);
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && Pattern.matches(EMAIL_REGEX, email);
    }

    private boolean isValidPassword(String password) {
        return StringUtils.hasText(password) && Pattern.matches(PASSWORD_REGEX, password);
    }
}
