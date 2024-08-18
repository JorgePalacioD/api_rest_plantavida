package com.planta_vida.rest;

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
import org.springframework.web.bind.annotation.*;
import com.planta_vida.service.UserService;
import com.planta_vida.util.Utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> registrarUsuario(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
            return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String,String> requestMap) {
        try {
            return userService.login(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody Map<String, String> requestMap) {
        try {
            return userService.updateUser(requestMap);
        } catch (Exception exception) {
            exception.printStackTrace();
            return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            return userService.deleteUser(id);
        } catch (Exception exception) {
            exception.printStackTrace();
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
            exception.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getRoles/{userId}")
    public ResponseEntity<?> getRolesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getRolesByUserId(userId));
    }
    @GetMapping("/role/all")
    public ResponseEntity<Set<Role>> getAllRoles() {
        UserController roleService = null;
        Set<Role> roles = (Set<Role>) roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}
