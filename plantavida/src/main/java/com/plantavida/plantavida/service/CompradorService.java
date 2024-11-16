package com.plantavida.plantavida.service;

import com.plantavida.plantavida.persistence.entity.CompradorEntity;
import com.plantavida.plantavida.persistence.repository.CompradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompradorService {

    private final CompradorRepository compradorRepository;

    public CompradorEntity createComprador(CompradorEntity comprador) {
        return compradorRepository.save(comprador);
    }

    public List<CompradorEntity> getAllCompradores() {
        return compradorRepository.findAll();
    }

    public Optional<CompradorEntity> getCompradorById(Integer id) {
        return compradorRepository.findById(id);
    }

    public CompradorEntity updateComprador(Integer id, CompradorEntity compradorDetails) {
        CompradorEntity comprador = compradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));

        comprador.setNombre(compradorDetails.getNombre());
        comprador.setDireccion(compradorDetails.getDireccion());
        comprador.setCorreoElectronico(compradorDetails.getCorreoElectronico());
        return compradorRepository.save(comprador);
    }
}
