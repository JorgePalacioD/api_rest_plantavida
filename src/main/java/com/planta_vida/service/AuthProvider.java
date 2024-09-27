package com.planta_vida.service;

import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface AuthProvider {
    ResponseEntity<String> login(Map<String, String> requestMap);
}
