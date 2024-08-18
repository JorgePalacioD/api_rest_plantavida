package com.planta_vida.dao;

import com.planta_vida.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
    Role findByName(String name);

    List<Role> findByNameIn(List<String> names);
}



