package com.plantavida.plantavida.service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
public class PagoRequestDto {

    // Getters y Setters
    @Getter
    private Double amount;          // Monto del pago
    @Getter
    private String description;     // Descripción del producto/servicio
    @Getter
    private String emailComprador;  // Email del comprador
    @Setter
    private Long compradorId;       // ID del comprador (en tu sistema)


    public Integer getCompradorId() {
        return Math.toIntExact(compradorId);
    }


}
