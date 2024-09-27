package com.planta_vida.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.planta_vida.dto.UserSignUpDTO;
import com.planta_vida.pojo.Role;
import com.planta_vida.pojo.User;
import com.planta_vida.Constantes.Contants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.planta_vida.service.UserService;
import com.planta_vida.util.Utils;

import java.util.*;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Logger should be initialized correctly
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public ResponseEntity<?> registrarUsuario(@Validated @RequestBody UserSignUpDTO userSignUpDTO, BindingResult bindingResult) {

        try {
            // Validate user input using BindingResult
            if (bindingResult.hasErrors()) {
                // Handle validation errors
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }

            // Proceed with user creation logic
            String nombre = userSignUpDTO.getNombre();

            // Create a ResponseEntity based on the result of user creation
            ResponseEntity<Map<String, String>> response;
            try {
                response = userService.signUp(userSignUpDTO);
            } catch (Exception e) {
                logger.error("Error during user creation: {}", e.getMessage(), e);
                throw e;
            }

            // Log the response after successful creation
            logger.info("Registration response: {}", response);
            return response;
        } catch (Exception exception) {
            logger.error("Error during registration: {}", exception.getMessage(), exception);
            return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        } catch (Exception exception) {
            logger.error("Error during login: {}", exception.getMessage(), exception);
            return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching all users: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, String> requestMap) {
        try {
            return userService.updateUser(requestMap);
        } catch (Exception exception) {
            logger.error("Error updating user: {}", exception.getMessage(), exception);
            return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            return userService.deleteUser(id);
        } catch (Exception exception) {
            logger.error("Error deleting user: {}", exception.getMessage(), exception);
            return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error getting current user: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getRoles/{userId}")
    public ResponseEntity<?> getRolesByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.getRolesByUserId(userId));
        } catch (Exception exception) {
            logger.error("Error fetching roles by user ID: {}", exception.getMessage(), exception);
            return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/role/all")
    public ResponseEntity<Set<Role>> getAllRoles() {
        try {
            return ResponseEntity.ok(userService.getAllRoles());
        } catch (Exception exception) {
            logger.error("Error fetching all roles: {}", exception.getMessage(), exception);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
