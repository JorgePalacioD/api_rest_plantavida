package com.planta_vida.security.jwt;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static  final Logger logger = (Logger) LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Ignorar las rutas de login y signup
        String requestPath = request.getServletPath();
        if (requestPath.equals("/user/login") || requestPath.equals("/user/signup")) {
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Comprueba que el encabezado de autorizacion tenga un JWT valido

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                logger.info("Extracted Username: {} ", username);
            } catch (Exception e) {
                logger.error("Error extracting username from JWT: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Enviar 401 si hay un error con el JWT
                return;
            }
        } else {
            logger.warn("Authorization header is missing or doesn't start with Bearer");
            chain.doFilter(request, response);
            return;
        }

        // Validar el token si el username es válido y no hay autenticación existente

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            try {
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecer el contexto de seguridad con el usuario autenticado
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    logger.warn("JWT Token is invalid");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Responder con 401 si el token no es válido
                    return;
                }
            } catch (Exception e) {
                logger.error("JWT Token validation failed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Responder con 401 si la validación falla
                return;
            }
        }
        // Continuar con el resto de los filtros si todo es válido
        chain.doFilter(request, response);
    }
}
