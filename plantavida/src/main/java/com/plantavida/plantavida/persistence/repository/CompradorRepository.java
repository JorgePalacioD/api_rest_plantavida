package com.plantavida.plantavida.persistence.repository;

import com.plantavida.plantavida.persistence.entity.CompradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface CompradorRepository extends JpaRepository<CompradorEntity, Integer> {
}
