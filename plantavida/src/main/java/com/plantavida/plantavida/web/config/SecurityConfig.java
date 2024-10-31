package com.plantavida.plantavida.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)

public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// Desactivar CSRF para aplicaciones sin estado (stateless)
                .cors().and()// Habilitar CORS si es necesario
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**", "/api/users/register", "/error").permitAll()  // Permitir acceso libre
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/register").permitAll()  // Permitir sin autenticación para el registro y login
                .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/api/arboles/**").hasAnyRole("ADMIN", "CUSTOMER")
                .requestMatchers(HttpMethod.POST, "/api/compradores/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT).hasRole("ADMIN")
                .requestMatchers("/api/bonos/**").hasAnyRole("ADMIN", "CUSTOMER")
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}