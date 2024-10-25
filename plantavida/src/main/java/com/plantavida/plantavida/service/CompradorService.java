package com.plantavida.plantavida.service;

import com.plantavida.plantavida.persistence.entity.CompradorEntity;
import com.plantavida.plantavida.persistence.repository.CompradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompradorService {
    private final CompradorRepository compradorRepository;

    @Autowired
    public CompradorService(CompradorRepository compradorRepository) {
        this.compradorRepository = compradorRepository;
    }

    public List<CompradorEntity> getAll() {
        return this.compradorRepository.findAll();
    }

    public CompradorEntity get(int idComprador) {
        return this.compradorRepository.findById(idComprador).orElse(null);
    }

    public CompradorEntity save(CompradorEntity comprador) {
        return this.compradorRepository.save(comprador);
    }

    public boolean exist(int idComprador) {
        return this.compradorRepository.existsById(idComprador);
    }

    public void delete(int idComprador) {
        this.compradorRepository.deleteById(idComprador);
    }
}
