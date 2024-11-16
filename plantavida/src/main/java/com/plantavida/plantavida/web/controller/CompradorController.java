package com.plantavida.plantavida.web.controller;

import com.plantavida.plantavida.persistence.entity.CompradorEntity;
import com.plantavida.plantavida.service.CompradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compradores")
@RequiredArgsConstructor
public class CompradorController {

    private final CompradorService compradorService;

    @PostMapping
    public ResponseEntity<CompradorEntity> createComprador(@RequestBody CompradorEntity comprador) {
        CompradorEntity createdComprador = compradorService.createComprador(comprador);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComprador);
    }

    @GetMapping
    public List<CompradorEntity> getAllCompradores() {
        return compradorService.getAllCompradores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompradorEntity> getCompradorById(@PathVariable Integer id) {
        return compradorService.getCompradorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompradorEntity> updateComprador(
            @PathVariable Integer id,
            @RequestBody CompradorEntity compradorDetails) {
        CompradorEntity updatedComprador = compradorService.updateComprador(id, compradorDetails);
        return ResponseEntity.ok(updatedComprador);
    }
}
