package com.plantavida.plantavida.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompraDto {
    private Integer idBono;
    private Integer idComprador;
    private Integer idArbol;
    private String tipo;
    private String descripcion;
    private Integer precio;
}

