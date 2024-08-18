package com.planta_vida.pojo;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    private String nombreCompleto;
    private String status;
    private String telefono;
    private String email;
    private String direccionCompleta;



    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
           return "email@example.com";
    }

    // Setter para nombreCompleto
    public void setNombreCompleto(String nombre) {
        this.nombreCompleto = nombre;
    }

    // Getter para status
    public String getStatus() {
        return this.status;
    }

    // Setter para telefono
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Setter para email
    public void setEmail(String email) {
        this.email = email;
    }

    // Setter para direccionCompleta
    public void setDireccionCompleta(String direccion) {
        this.direccionCompleta = direccion;
    }

    // Setter para status
    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {

    }
}
