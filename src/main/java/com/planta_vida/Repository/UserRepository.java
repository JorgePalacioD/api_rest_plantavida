package com.planta_vida.Repository;

import com.planta_vida.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método personalizado para encontrar un usuario por su email
    User findByEmail(String email);
}
