package com.plantavida.plantavida.persistence.repository;

import com.plantavida.plantavida.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);// nuevo metodo para validar si esta con email o username
    boolean existsByUsername(String username);


}