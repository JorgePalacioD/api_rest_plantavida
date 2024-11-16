package com.plantavida.plantavida.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "comprador")
@Getter
@Setter
@NoArgsConstructor

public class CompradorEntity {

    @Id
    @Column(name = "id_comprador",nullable = false)
    private Integer idComprador;

    @Column(name = "id_bono", nullable = false)
    private Integer idBono;

    @Column(nullable = false, length = 45, unique = true)
    private String nombre;

    @Column(nullable = false, length = 45)
    private String direccion;

    @Column(name = "correo_electronico", nullable = false, length = 45, unique = true)
    private String correoElectronico;

    @OneToMany(mappedBy = "comprador", fetch = FetchType.EAGER)
    private List<BonoEntity> bonos;
    }

