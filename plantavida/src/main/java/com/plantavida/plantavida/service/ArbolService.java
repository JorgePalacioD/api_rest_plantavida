package com.plantavida.plantavida.service;

import com.plantavida.plantavida.persistence.entity.ArbolEntity;
import com.plantavida.plantavida.persistence.entity.BonoEntity;
import com.plantavida.plantavida.persistence.repository.ArbolRepository;
import com.plantavida.plantavida.persistence.repository.BonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArbolService {

    private final ArbolRepository arbolRepository;
    private final BonoRepository bonoRepository;

    public ArbolEntity assignArbolToBono(ArbolEntity arbol, Integer idBono) {
        BonoEntity bono = bonoRepository.findById(idBono)
                .orElseThrow(() -> new RuntimeException("Bono no encontrado"));
        arbol.setBono(bono);
        return arbolRepository.save(arbol);
    }

    public Optional<ArbolEntity> getArbolByBonoId(Integer idBono) {
        return arbolRepository.findByBonoIdBono(idBono);
    }

    public Optional<ArbolEntity> getArbolById(Integer id) {
        return arbolRepository.findById(id);
    }

    public ArbolEntity updateArbol(Integer id, ArbolEntity arbolDetails) {
        ArbolEntity arbol = arbolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Árbol no encontrado"));

        arbol.setTipo(arbolDetails.getTipo());
        arbol.setDescripcion(arbolDetails.getDescripcion());
        arbol.setUbicacion(arbolDetails.getUbicacion());
        arbol.setImagen(arbolDetails.getImagen());
        return arbolRepository.save(arbol);
    }
}
