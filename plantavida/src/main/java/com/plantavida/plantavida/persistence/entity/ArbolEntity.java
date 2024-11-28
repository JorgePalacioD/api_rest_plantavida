package com.plantavida.plantavida.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "arbol")
@Getter
@Setter
@NoArgsConstructor

public class ArbolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_arbol")
    private Integer idArbol;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String ubicacion;

    @Column(nullable = false, length = 50)
    private String imagen;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bono", nullable = false)
    @JsonBackReference
    private BonoEntity bono;
}
