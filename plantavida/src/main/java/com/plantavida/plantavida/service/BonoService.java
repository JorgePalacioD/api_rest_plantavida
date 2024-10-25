package com.plantavida.plantavida.service;

import com.plantavida.plantavida.persistence.entity.BonoEntity;
import com.plantavida.plantavida.persistence.repository.BonoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BonoService {
    private final BonoRepository bonoRepository;

    @Autowired
    public BonoService(BonoRepository bonoRepository) {
        this.bonoRepository = bonoRepository;
    }

    public List<BonoEntity>getAll() {
        return this.bonoRepository.findAll();
    }

    public BonoEntity get(int idBono) {
        return this.bonoRepository.findById(idBono).orElse(null);
    }

    public BonoEntity save(BonoEntity bono) {
        return this.bonoRepository.save(bono);
    }

    public Boolean exist(int idBono) {
        return this.bonoRepository.existsById(idBono);
    }

    public void delete(int idBono) {
        this.bonoRepository.deleteById(idBono);
    }
}
