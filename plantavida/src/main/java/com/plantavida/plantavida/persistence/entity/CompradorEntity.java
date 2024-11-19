package com.plantavida.plantavida.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "comprador")
@Getter
@Setter
@NoArgsConstructor

public class CompradorEntity {

    @Id
    @Column(name = "id_comprador", nullable = false)
    private Integer idComprador;

    @Column(nullable = false, length = 45, unique = true)
    private String nombre;

    @Column(nullable = false, length = 45)
    private String direccion;

    @Column(name = "correo_electronico", nullable = false, length = 45, unique = true)
    private String correoElectronico;

    @OneToMany(mappedBy = "comprador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BonoEntity> bonos;
    // Almacena las transacciones asociadas al comprador
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "transacciones", joinColumns = @JoinColumn(name = "id_comprador"))
    @Column(name = "transaction_id")
    private List<String> transacciones = new ArrayList<>();

    // Método para agregar una transacción
    public void addTransaction(String transactionId) {
        this.transacciones.add(transactionId);
    }
}

