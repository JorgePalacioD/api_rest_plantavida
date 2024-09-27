package com.planta_vida.initializer;

import com.planta_vida.pojo.User;
import com.planta_vida.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class UserInitializer {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initAdminUser() {
        try {
            // Si getUserByEmail devuelve un booleano, verifica si el usuario existe con true o false
            boolean userExists = userService.getUserByEmail("admin@planta_vida.com");
            if (!userExists) {
                // Si no existe, crea un nuevo usuario administrador
                userService.createAdminUser("Admin", "admin@planta_vida.com", "adminpassword");
                System.out.println("Admin user created successfully");
            } else {
                System.out.println("Admin user already exists");
            }
        } catch (Exception e) {
            System.err.println("Error initializing admin user: " + e.getMessage());
            e.printStackTrace(); // Esto puede ser útil para el desarrollo, pero podrías quitarlo en producción
        }
    }
}
