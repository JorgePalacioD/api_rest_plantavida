package com.planta_vida.service;

import com.planta_vida.dao.UserDAO;
import com.planta_vida.dto.LoginDTO;
import com.planta_vida.pojo.Role;
import com.planta_vida.pojo.User;
import com.planta_vida.Repository.RoleRepository;
import com.planta_vida.Repository.UserRepository;
import com.planta_vida.security.CustomerDetailsService;
import com.planta_vida.security.jwt.JwtUtil;
import com.planta_vida.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServices {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;// Para codificar contraseñas
    @Autowired
    private AuthenticationManager authenticationManager;// Inyectar correctamente el AuthenticationManager
    @Autowired
    private JwtUtil jwtUtil; // Inyectar JwtUtil para generar tokens JWT
    @Autowired
    private CustomerDetailsService customerDetailsService; //Servicio de detalles del usuario

    // Método para el login de usuarios
    public ResponseEntity<?> login(@RequestBody Map<String, String> requestMap) {
        try {

            var email = requestMap.get("email");
            var password = requestMap.get("password");

            // Validar que el email no sea nulo o vacío
            if (email == null || email.isEmpty()) {
                System.out.println("El email es nulo o está vacío");
                return new ResponseEntity<>("El email no puede estar vacío", HttpStatus.BAD_REQUEST);
            }

            // Buscar el usuario por su email
            Optional<User> userOptional = Optional.ofNullable(userDAO.findByEmail(email));

            if (userOptional.isEmpty()) {
                System.out.println("Usuario no encontrado");
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.UNAUTHORIZED);
            }

            User user = userOptional.get();

            // Mostrar las contraseñas para depuración
            System.out.println("Contraseña ingresada: " + password);
            System.out.println("Contraseña en base de datos (encriptada): " + user.getPassword());

            // Verificar la contraseña ingresada con la codificada almacenada
            if (!passwordEncoder.matches(password, user.getPassword())) {
                System.out.println("Contraseña incorrecta++++++");
                return new ResponseEntity<>("Credenciales incorrectas¿¿¿¿¿¿¿", HttpStatus.UNAUTHORIZED);
            }

            // Generar JWT si la autenticación es exitosa
            String token = jwtUtil.generateToken(user.getEmail(), user.getRoles());

            // Retornar el token JWT en la respuesta
            return ResponseEntity.ok(Collections.singletonMap("token", token));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Método para registrar un nuevo usuario
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            // Obtener datos del cuerpo de la solicitud
            String email = requestMap.get("email");
            String password = requestMap.get("password");
            String username = requestMap.get("username");

            // Verificar si el usuario ya existe
            if (userRepository.findByEmail(email).isPresent()) {
                return new ResponseEntity<>("El usuario ya existe", HttpStatus.BAD_REQUEST);
            }
            // Crear el nuevo usuario
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            // Codificar la contraseña antes de asignarla al usuario
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);

            // Crear un HashSet y añadir el rol USER
            Role userRole = roleRepository.findByName("ROLE_USER");
            if (userRole == null) {
                return new ResponseEntity<>("Rol USER no encomtrado", HttpStatus.BAD_REQUEST);
            }
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);

            // Guardar el usuario en la base de datos
            User savedUser = userRepository.save(user);
            if (savedUser == null) {
                return new ResponseEntity<>("Error al guardar el ususario", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (savedUser.getRoles() == null || savedUser.getRoles().isEmpty()) {
                return new ResponseEntity<>("Error: el usuario no tiene roles asignados ", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("El usuario se ha guardado correctamente", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return Utils.getResponseEntity("Algo salió mal", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Crear un nuevo usuario con un rol específico
    public User createUser(String name, String email, String password, String roleName) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Codificar la contraseña

        // Buscar el rol por nombre
        Role role = roleRepository.findByName(roleName);
        if (role != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    // Obtener un usuario por email
    public Optional<Optional<User>> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    // Obtener roles de un usuario por ID
    public Set<Role> getRolesByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getRoles).orElse(new HashSet<>());
    }


}
