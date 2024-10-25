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

    @Column(name = "id_arbol")
    private Integer idArbol;

    @Column(name = "id_comprador",nullable = false)
    private Integer idComprador;

    @Column(name = "tipo_bono", nullable = false, length = 45)
    private String Tipo;

    @Column(nullable = false, length = 45)
    private String descripcion;

    @Column(nullable = false)
    private Integer precio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_arbol", referencedColumnName = "id_arbol", insertable = false, updatable = false)
    @JsonIgnore
    private ArbolEntity arbol;

    @ManyToOne
    @JoinColumn(name = "id_comprador", referencedColumnName = "id_comprador", insertable = false, updatable = false)
    private CompradorEntity comprador;
}
