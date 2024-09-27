package com.planta_vida.service;

import com.planta_vida.dto.UserSignUpDTO;
import com.planta_vida.pojo.Role;
import com.planta_vida.pojo.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    ResponseEntity<Map<String, String>> signUp(UserSignUpDTO userSignUpDTO);

    ResponseEntity<String> signUp(Map<String,String> requestMap);

    ResponseEntity<Map<String, String>> login(Map<String,String> requestMap);

    List<User> getAllUsers();
    ResponseEntity<String> updateUser(Map<String, String> requestMap);
    ResponseEntity<String> deleteUser(int id);
    Object getRolesByUserId(Long userId);
    boolean getUserByEmail(String s);
    User createAdminUser(String admin, String s, String adminpassword);
    boolean deleteAdmin(Long id);
    Optional<User> getAdminById(Long id);
    List<User> getAllAdmins();
    Optional<User> updateAdmin(Long id, User adminDetails);

    User createUser(String name, String email, String password, String roleName);

    Set<Role> getAllRoles();
}