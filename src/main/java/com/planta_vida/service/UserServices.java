package com.planta_vida.service;

import com.planta_vida.pojo.User;

public abstract class UserServices {
    public abstract User createUser(String name, String email, String password, String roleName);

    public abstract User createAdminUser(String name, String email, String password);

    public abstract User addRoleToUser(String email, String roleName);
}
