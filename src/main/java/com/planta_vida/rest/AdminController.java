package com.planta_vida.rest;

import com.planta_vida.pojo.User;
import com.planta_vida.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userServices; // Corregido el nombre del campo a userService

    // Crear un nuevo administrador
    @PostMapping("/create")
    public ResponseEntity<User> createAdmin(@RequestBody User admin) {
        User createdAdmin = userServices.createAdminUser(admin.getUsername(), admin.getEmail(), admin.getPassword());
        return ResponseEntity.ok(createdAdmin);
    }

    // Obtener todos los administradores
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllAdmins() {
        List<User> admins = userServices.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    // Obtener un administrador por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getAdminById(@PathVariable Long id) {
        Optional<User> admin = userServices.getAdminById(id);
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar un administrador
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateAdmin(@PathVariable Long id, @RequestBody User adminDetails) {
        Optional<User> updatedAdmin = userServices.updateAdmin(id, adminDetails);
        return updatedAdmin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar un administrador
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        boolean deleted = userServices.deleteAdmin(id); // Corregido el nombre del método a deleteAdmin
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
