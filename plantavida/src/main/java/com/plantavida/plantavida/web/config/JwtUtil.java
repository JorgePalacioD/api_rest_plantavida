package com.plantavida.plantavida.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private static String SECRET_KEY = "pl4nt4v1d4";
    private static Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    // Metodo para crear un token JWT
    public String create(String username) {
        return JWT.create()
                .withSubject(username) // Sujeto del token ( nombre de usuario )
                .withIssuer("plantavida") // Emisor del token
                .withIssuedAt(new Date()) // Fecha de emision
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15))) // Expira en 15 dias
                .sign(ALGORITHM); // Firma el token
    }

    // Metodo para validar el token JWT
    public boolean isValid(String jwt) {
        try {
            // Verificar el token  y su firma
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true; // si no lanza excepcion el token es valido
        } catch (JWTVerificationException e) {
            return false; // si hay un problema el token no es valido
        }
    }
    // Método para extraer el nombre de usuario del token JWT
    public String getUsername(String jwt) {
        try {
            // Verifica el token y obtiene el sujeto (nombre de usuario)
            DecodedJWT decodedJWT = JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return decodedJWT.getSubject(); // Retorna el nombre de usuario
        } catch (JWTVerificationException e) {
            return null; // Si hay un problema, retorna null
        }
    }
}