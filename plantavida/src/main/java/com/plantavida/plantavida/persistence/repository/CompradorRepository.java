package com.plantavida.plantavida.persistence.repository;

import com.plantavida.plantavida.persistence.entity.CompradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface CompradorRepository extends JpaRepository<CompradorEntity, Integer> {
    Optional<CompradorEntity> findByCorreoElectronico(String correoElectronico);
}
