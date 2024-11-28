package com.plantavida.plantavida.service;

import com.plantavida.plantavida.persistence.entity.CompradorEntity;
import com.plantavida.plantavida.persistence.repository.CompradorRepository;
import jakarta.persistence.EntityNotFoundException;
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
        // Validación para evitar duplicados del ID manual
        if (compradorRepository.existsById(comprador.getIdComprador())) {
            throw new IllegalArgumentException("El ID del comprador ya está registrado");
        }
        // Validación para evitar duplicados del correo electrónico
        if (compradorRepository.findByCorreoElectronico(comprador.getCorreoElectronico()).isPresent()) {
            throw new IllegalArgumentException("Correo electrónico ya registrado");
        }
        return compradorRepository.save(comprador);
    }

    public List<CompradorEntity> getAllCompradores() {
        return compradorRepository.findAll();
    }

    public Optional<CompradorEntity> getCompradorById(Integer id) {
        return Optional.ofNullable(compradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado")));
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
