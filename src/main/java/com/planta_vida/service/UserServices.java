package com.planta_vida.service;

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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Para codificar contraseñas

    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil; // Inyectar JwtUtil para generar tokens JWT

    @Autowired
    private CustomerDetailsService customerDetailsService; //

    // Método para el login de usuarios
    public ResponseEntity<?> login(Map<String, String> requestMap) {
        try {
            // Extrae las credenciales del requestMap
            String email = requestMap.get("email");
            String password = requestMap.get("password");

            // Verifica que las credenciales no sean nulas o vacías
            if (email == null || password == null) {
                return new ResponseEntity<>("Falta el correo electrónico o la contraseña", HttpStatus.BAD_REQUEST);
            }

            // Buscar el usuario en la base de datos por su email
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.UNAUTHORIZED);
            }

            User user = userOptional.get();

            // Comparar la contraseña ingresada con la contraseña codificada almacenada
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
            }

            // Autenticar al usuario utilizando el AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            // Si la autenticación es exitosa, configurar el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar el JWT para el usuario autenticado
            String token = jwtUtil.generateToken(user.getUsername());

            // Retornar el token como respuesta
            return ResponseEntity.ok(Collections.singletonMap("token", token));

        } catch (AuthenticationException e) {
            // Devolver una respuesta de error si la autenticación falla
            e.printStackTrace();
            return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // Manejo de otros errores
            e.printStackTrace();
            return new ResponseEntity<>("Algo salió mal", HttpStatus.INTERNAL_SERVER_ERROR);
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
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName("USER"); // Asignar rol de usuario
            roles.add(userRole);

            // Asignar el conjunto de roles al usuario
            user.setRoles(roles);

            // Guardar el usuario en la base de datos
            userRepository.save(user);
            return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.OK);

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
