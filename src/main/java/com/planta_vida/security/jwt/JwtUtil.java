package com.planta_vida.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    // La clave secreta debe ser cargada desde el archivo de configuración o un servicio seguro
    @Value("${jwt.secret}")
    private String secretKeyBase64;

    private Key secretKey;
    private Object role;

    // Inicializa la clave secreta a partir de la clave en Base64
    @PostConstruct
    public void init() {
        try {
            if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
                throw new IllegalArgumentException("La clave secreta no puede ser nula o vacía");
            }
            // Decodifica la clave secreta y crea la clave HMAC
            secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyBase64));
            System.out.println("Clave secreta inicializada correctamente.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error al inicializar la clave secreta: " + e.getMessage());
            throw new RuntimeException("Falló la inicialización de la clave secreta JWT", e);
        }
    }

    // Extrae el nombre de usuario del token
    public String extractUsername(String token) {
                return extractClaim(token, Claims::getSubject);
    }

    // Extrae la fecha de expiración del token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrae el rol del token
    public String extractRole(String token) {
        return extractClaim(token, claims -> (String) claims.get("role"));
    }

    // Extrae una reclamación específica del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrae todas las reclamaciones del token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            System.err.println("La verificación del JWT falló: " + e.getMessage());
            throw new RuntimeException("Token JWT inválido", e);
        }
    }

    // Verifica si el token ha expirado
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Genera un nuevo token JWT
    public String generateToken(String username, Object role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", this.role);  // Ahora agregamos el rol al token
        return createToken(claims, username);
    }


    // Crea el token JWT con las reclamaciones y el sujeto
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().claims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Valida el token comparando el nombre de usuario y verificando la expiración
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
