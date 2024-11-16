package com.plantavida.plantavida.persistence.repository;

import com.plantavida.plantavida.persistence.entity.BonoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BonoRepository extends JpaRepository<BonoEntity, Integer> {
    List<BonoEntity> findByCompradorIdComprador(Integer idComprador);
}
