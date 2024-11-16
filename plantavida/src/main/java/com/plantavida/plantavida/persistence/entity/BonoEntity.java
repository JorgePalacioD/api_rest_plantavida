package com.plantavida.plantavida.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bono")
@Getter
@Setter
@NoArgsConstructor

public class BonoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bono", nullable = false)
    private Integer idBono;

    @Column(name = "tipo_bono", nullable = false, length = 45)
    private String tipo;

    @Column(nullable = false, length = 45)
    private String descripcion;

    @Column(nullable = false)
    private Integer precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comprador", nullable = false)
    private CompradorEntity comprador;

    @OneToOne(mappedBy = "bono", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ArbolEntity arbol;
}
