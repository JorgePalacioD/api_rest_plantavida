package com.plantavida.plantavida.service;

import com.plantavida.plantavida.persistence.entity.ArbolEntity;
import com.plantavida.plantavida.persistence.repository.ArbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArbolService {
    private final ArbolRepository arbolRepository;

    @Autowired
    public ArbolService(ArbolRepository arbolRepository) {
        this.arbolRepository = arbolRepository;
    }

    public List<ArbolEntity>getAll(){
        return this.arbolRepository.findAll();
    }

    public ArbolEntity get(int idArbol){
        return this.arbolRepository.findById(idArbol).orElse(null);
    }

    public ArbolEntity save(ArbolEntity arbol){
        return this.arbolRepository.save(arbol);
    }

    public Boolean exist(int idArbol) {
        return this.arbolRepository.existsById(idArbol);
    }

    public void delete(int idArbol) {
        this.arbolRepository.deleteById(idArbol);
    }
}
