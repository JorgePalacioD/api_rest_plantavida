package com.plantavida.plantavida.persistence.repository;

import com.plantavida.plantavida.persistence.entity.ArbolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ArbolRepository extends JpaRepository<ArbolEntity, Integer> {
    Optional<ArbolEntity> findByBonoIdBono(Integer idBono);
}
