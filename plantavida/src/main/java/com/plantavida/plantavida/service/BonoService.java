package com.plantavida.plantavida.service;

import com.plantavida.plantavida.persistence.entity.BonoEntity;
import com.plantavida.plantavida.persistence.entity.CompradorEntity;
import com.plantavida.plantavida.persistence.repository.BonoRepository;
import com.plantavida.plantavida.persistence.repository.CompradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BonoService {

    private final BonoRepository bonoRepository;
    private final CompradorRepository compradorRepository;

    public BonoEntity createBono(BonoEntity bono, Integer idComprador) {
        CompradorEntity comprador = compradorRepository.findById(idComprador)
                .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));
        bono.setComprador(comprador);
        return bonoRepository.save(bono);
    }

    public List<BonoEntity> getBonosByCompradorId(Integer idComprador) {
        return bonoRepository.findByCompradorIdComprador(idComprador);
    }

    public Optional<BonoEntity> getBonoById(Integer id) {
        return bonoRepository.findById(id);
    }

    public BonoEntity updateBono(Integer id, BonoEntity bonoDetails) {
        BonoEntity bono = bonoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bono no encontrado"));

        bono.setTipo(bonoDetails.getTipo());
        bono.setDescripcion(bonoDetails.getDescripcion());
        bono.setPrecio(bonoDetails.getPrecio());
        return bonoRepository.save(bono);
    }
}
