package com.planta_vida.util;



import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class KeyGenerator {
    public static void main(String[] args) {
        // Genera una clave HMAC con una longitud de 256 bits
        Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String secretKey = java.util.Base64.getEncoder().encodeToString(key.getEncoded());

        // Imprime la clave secreta en Base64
        System.out.println("Clave secreta generada: " + secretKey);
    }
}

