package com.planta_vida.service.impl;

import com.planta_vida.Repository.RoleRepository;
import com.planta_vida.Repository.UserRepository;  // Importar desde el paquete correcto
import com.planta_vida.dao.RoleDAO;
import com.planta_vida.dao.UserDAO;
import com.planta_vida.dto.UserSignUpDTO;
import com.planta_vida.pojo.Role;
import com.planta_vida.pojo.User;
import com.planta_vida.security.jwt.JwtUtil;
import com.planta_vida.service.UserService;
import com.planta_vida.util.Utils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;  // Repositorio para la entidad User

    @Autowired
    private RoleRepository roleRepository;  // Repositorio para la entidad Role


    @Override
    public ResponseEntity<Map<String, String>> signUp(UserSignUpDTO userSignUpDTO) {
        ResponseEntity<Map<String, String>> result;
        log.info("Sign Up User {}", userSignUpDTO);
        Map<String, String> response = new HashMap<>();
        try {
            if (validateSignUp(userSignUpDTO)) {
                User user = userDAO.findByEmail(userSignUpDTO.getEmail());
                if (Objects.isNull(user)) {
                    User newUser = userDAO.save(getUserFromDTO(userSignUpDTO));
                    String token = jwtUtil.generateToken(newUser.getEmail());
                    response.put("message", "Usuario registrado con éxito!");
                    response.put("token", token);
                    result = new ResponseEntity<>(response, HttpStatus.CREATED);
                } else {
                    response.put("message", "El usuario con ese email ya existe");
                    result = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                response.put("message", "Datos inválidos");
                result = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            log.error("Error en signUp: {}", exception.getMessage());
            response.put("message", "Algo salió mal");
            result = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, String>> login(Map<String, String> requestMap) {
        Map<String, String> response = new HashMap<>();
        try {
            // Validamos las credenciales
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );

            // Si la autenticación es exitosa, generamos el token
            final User user = userDAO.findByEmail(requestMap.get("email"));
            if (user != null) {
                String token = jwtUtil.generateToken(user.getEmail());
                response.put("message", "Inicio de sesión exitoso");
                response.put("token", token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (AuthenticationException e) {
            response.put("message", "Credenciales incorrectas");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception exception) {
            log.error("Error en login: {}", exception.getMessage());
            response.put("message", "Algo salió mal");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
        try {
            User user = userDAO.findById((int) Long.parseLong(requestMap.get("id"))).orElse(null);
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
            log.error("Error en updateUser: {}", exception.getMessage());
            return Utils.getResponseEntity("Algo salió mal", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(int id) {
        try {
            if (userDAO.existsById(id)) {
                userDAO.deleteById(id);
                return Utils.getResponseEntity("Usuario eliminado con éxito", HttpStatus.OK);
            } else {
                return Utils.getResponseEntity("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            log.error("Error en deleteUser: {}", exception.getMessage());
            return Utils.getResponseEntity("Algo salió mal", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Object getRolesByUserId(Long userId) {
        return null;
    }

    @Override
    public boolean getUserByEmail(String email) {
        return userDAO.findByEmail(email) != null;
    }

    @Override
    public User createAdminUser(String username, String email, String password) {
        User admin = new User();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setRole("ADMIN");
        admin.setStatus("true"); // Asumimos que el administrador está activo por defecto
        return userDAO.save(admin);
    }

    @Override
    public boolean deleteAdmin(Long id) {
        try {
            if (userDAO.existsById(Math.toIntExact(id))) {
                userDAO.deleteById(Math.toIntExact(id));
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            log.error("Error en deleteAdmin: {}", exception.getMessage());
            return false;
        }
    }

    @Override
    public Optional<User> getAdminById(Long id) {
        return userDAO.findById(Math.toIntExact(id));
    }

    @Override
    public List<User> getAllAdmins() {
        return userDAO.findByRole("ADMIN");
    }

    @Override
    public Optional<User> updateAdmin(Long id, User adminDetails) {
        try {
            Optional<User> existingAdmin = userDAO.findById(Math.toIntExact(id));
            if (existingAdmin.isPresent()) {
                User admin = existingAdmin.get();
                admin.setUsername(adminDetails.getUsername());
                admin.setEmail(adminDetails.getEmail());
                admin.setPassword(adminDetails.getPassword());
                // Otros detalles del administrador
                return Optional.of(userDAO.save(admin));
            }
            return Optional.empty();
        } catch (Exception exception) {
            log.error("Error en updateAdmin: {}", exception.getMessage());
            return Optional.empty();
        }
    }

    private boolean validateSignUp(UserSignUpDTO userSignUpDTO) {
        return userSignUpDTO.getNombre() != null && userSignUpDTO.getTelefono() != null && userSignUpDTO.getEmail() != null
                && userSignUpDTO.getPassword() != null;
    }

    private User getUserFromDTO(UserSignUpDTO userSignUpDTO) {
        User user = new User();
        user.setNombreCompleto(userSignUpDTO.getNombre());
        user.setTelefono(userSignUpDTO.getTelefono());
        user.setEmail(userSignUpDTO.getEmail());
        user.setDireccionCompleta(userSignUpDTO.getDireccion());
        user.setPassword(userSignUpDTO.getPassword());
        user.setStatus("false");

        Set<Role> roles = new HashSet<>();
        Role userRole = roleDAO.findByName(userSignUpDTO.getRoleName());
        if (userRole != null) {
            roles.add(userRole);
        }
        user.setRoles(roles);

        return user;
    }

    @Override
    public User createUser(String name, String email, String password, String roleName) {
        // Crear un nuevo objeto User
        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(password);

        // Obtener el rol basado en el nombre del rol proporcionado
        Role role = roleRepository.findByName(name);

        // Asignar el rol al usuario
        if (role != null) {
            user.setRoles(new HashSet<>(Collections.singletonList(role)));
        }

        // Guardar el usuario en la base de datos
        return userRepository.save(user);
    }

    @Override
    public Set<Role> getAllRoles() {
        return Set.of();
    }

}
