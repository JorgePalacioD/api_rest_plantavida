package com.planta_vida.security;

import com.planta_vida.security.jwt.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    // Usar BCryptPasswordEncoder para codificar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // configuracion de la cadena de filtros de seguridad

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Configuracion de cors
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/user/signup", "/user/login").permitAll() // Rutas públicas
                        .requestMatchers("/admin/signup").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Solo ADMIN puede acceder a rutas bajo /admin
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // Usuarios y ADMIN pueden acceder a rutas bajo /user
                        .requestMatchers(HttpMethod.POST, "/roles").permitAll() // Ruta pública para crear roles
                        .anyRequest().authenticated()) // Todas las demás rutas requieren autenticación
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin estado
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Añadir el filtro JWT antes del filtro de autenticación por nombre de usuario y contraseña

        return httpSecurity.build();
    }
    // Manejo de accesos denegados personalizados
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            response.getWriter().write("{\"message\": \"Acceso denegado\"}");
        };
    }
    // Configuracion del AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
