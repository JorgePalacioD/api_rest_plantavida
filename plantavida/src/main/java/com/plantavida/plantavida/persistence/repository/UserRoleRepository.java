package com.plantavida.plantavida.persistence.repository;

import com.plantavida.plantavida.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {
}
