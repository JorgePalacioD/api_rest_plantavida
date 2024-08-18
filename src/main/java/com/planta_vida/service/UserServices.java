package com.planta_vida.service;

import com.planta_vida.pojo.Role;
import com.planta_vida.pojo.User;
import com.planta_vida.Repository.RoleRepository;
import com.planta_vida.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Crear un nuevo usuario con un rol
    public User createUser(String name, String email, String password, String roleName) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

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
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Obtener roles de un usuario por ID
    public Set<Role> getRolesByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getRoles).orElse(new HashSet<>());
    }
}
