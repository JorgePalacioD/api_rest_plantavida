package com.planta_vida.service.impl;

import com.planta_vida.Constantes.Contants;
import com.planta_vida.dao.UserDAO;
import com.planta_vida.dao.RoleDAO;
import com.planta_vida.security.CustomerDetailsService;
import com.planta_vida.security.jwt.JwtUtil;
import com.planta_vida.pojo.Role;
import com.planta_vida.pojo.User;
import com.planta_vida.service.UserService;
import com.planta_vida.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Sign Up User {}", requestMap);
        try {
            if (validateSignUp(requestMap)) {
                User user = userDAO.findByEmail(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDAO.save(getUserFromMap(requestMap));
                    return Utils.getResponseEntity("Usuario registrado con éxito!", HttpStatus.CREATED);
                } else {
                    return Utils.getResponseEntity("El usuario con ese email ya existe", HttpStatus.BAD_REQUEST);
                }
            } else {
                return Utils.getResponseEntity(Contants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Dentro de login");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (authentication.isAuthenticated()) {
                if (customerDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<>(
                            "{\"token\":\"" + jwtUtil.generateToken(customerDetailsService.getUserDetail().getEmail(),
                                    customerDetailsService.getUserDetail().getRoles().toString()) + "\"}",
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("{\"mensaje\":\"Espere la aprobación del Administrador\"}", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception exception) {
            log.error("{}", exception);
        }
        return new ResponseEntity<>("{\"mensaje\":\"Credenciales Incorrectas\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
        try {
            User user = userDAO.findById(Integer.parseInt(requestMap.get("id"))).orElse(null);
            if (user != null) {
                user.setNombreCompleto(requestMap.get("nombre"));
                user.setTelefono(requestMap.get("telefono"));
                user.setEmail(requestMap.get("email"));
                user.setDireccionCompleta(requestMap.get("direccion"));
                user.setStatus(requestMap.get("status"));
                user.setRoles(new HashSet<>(roleDAO.findByNameIn(Collections.singletonList(requestMap.get("roles")))));
                userDAO.save(user);
                return Utils.getResponseEntity("Usuario actualizado con éxito", HttpStatus.OK);
            } else {
                return Utils.getResponseEntity("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(int id) {
        try {
            userDAO.deleteById(id);
            return Utils.getResponseEntity("Usuario eliminado con éxito", HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return Utils.getResponseEntity(Contants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Object getRolesByUserId(Long userId) {
        return null;
    }

    private boolean validateSignUp(Map<String, String> requestMap) {
        return requestMap.containsKey("nombre") && requestMap.containsKey("telefono") && requestMap.containsKey("email")
                && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setNombreCompleto(requestMap.get("nombre"));
        user.setTelefono(requestMap.get("telefono"));
        user.setEmail(requestMap.get("email"));
        user.setDireccionCompleta(requestMap.get("direccion"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");

        Set<Role> roles = new HashSet<>();
        Role userRole = roleDAO.findByName("USER");
        if (userRole != null) {
            roles.add(userRole);
        }
        user.setRoles(roles);

        return user;
    }
}