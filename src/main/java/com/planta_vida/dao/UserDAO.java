package com.planta_vida.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.planta_vida.pojo.User;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

    User findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRole(@Param("roleName") String roleName);

}
