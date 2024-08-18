package com.planta_vida.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.planta_vida.pojo.User;

@Repository

public interface UserDAO  extends JpaRepository<User,Integer> {
    User findByEmail(@Param(("email")) String email);
}

