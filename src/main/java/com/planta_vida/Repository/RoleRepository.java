package com.planta_vida.Repository;

import com.planta_vida.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Método personalizado para encontrar un rol por su nombre
    Role findByName(String name);

}
