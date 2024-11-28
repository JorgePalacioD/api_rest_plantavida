package com.plantavida.plantavida.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "comprador")
@Getter
@Setter
@NoArgsConstructor

public class CompradorEntity {

    @Id
    @NotNull(message = "El ID del comprador no puede ser nulo")
    @Column(name = "id_comprador", nullable = false)
    private Integer idComprador;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 45, message = "El nombre no puede superar los 45 caracteres")
    @Column(nullable = false, length = 45, unique = true)
    private String nombre;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 45, message = "La dirección no puede superar los 45 caracteres")
    @Column(nullable = false, length = 45)
    private String direccion;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El correo electrónico debe ser válido")
    @Size(max = 45, message = "El correo electrónico no puede superar los 45 caracteres")
    @Column(name = "correo_electronico", nullable = false, length = 45, unique = true)
    private String correoElectronico;

    @OneToMany(mappedBy = "comprador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BonoEntity> bonos;
    }

