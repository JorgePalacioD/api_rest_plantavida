package com.planta_vida.security;

import com.planta_vida.dao.UserDAO;
import com.planta_vida.pojo.Role;
import com.planta_vida.pojo.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Getter
    private User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Dentro de loadUserByUsername. {}", username);
        userDetail = userDAO.findByEmail(username);
        log.info("Usuario encontrado: {}", userDetail);

        if (!Objects.isNull(userDetail) && userDetail.getEmail() != null && !userDetail.getEmail().isEmpty()
                && userDetail.getPassword() != null && !userDetail.getPassword().isEmpty()) {
            Collection<GrantedAuthority> authorities = userDetail.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    userDetail.getEmail(),
                    userDetail.getPassword(),
                    authorities
            );
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public User getUserDetail() {
        return userDetail;
    }
}
