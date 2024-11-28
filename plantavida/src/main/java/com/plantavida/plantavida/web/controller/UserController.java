package com.plantavida.plantavida.web.controller;

import com.plantavida.plantavida.persistence.entity.UserEntity;
import com.plantavida.plantavida.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@RequestBody @Valid UserEntity userDetails) {
        UserEntity registeredUser = userService.register(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}
