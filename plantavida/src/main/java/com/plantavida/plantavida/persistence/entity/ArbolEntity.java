package com.plantavida.plantavida.persistence.entity;

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

    @Column(name = "id_bono", nullable = false)
    private Integer idBono;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String ubicacion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bono", referencedColumnName = "id_bono", insertable = false, updatable = false)
    @JsonIgnore
    private BonoEntity bono;
}
