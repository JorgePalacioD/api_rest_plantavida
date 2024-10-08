package com.planta_vida.service;


import com.planta_vida.pojo.Role;
import com.planta_vida.dao.RoleDAO; // Asegúrate de tener un DAO para roles
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

    @Service
    public class RolServiceImpl {

        @Autowired
        private RoleDAO roleDAO; // Asegúrate de tener un DAO para manejar roles

        public Role addRole(Role role) {
            return roleDAO.save(role);
        }
    }
