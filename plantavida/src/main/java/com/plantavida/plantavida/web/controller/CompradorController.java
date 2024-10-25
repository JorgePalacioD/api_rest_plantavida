package com.plantavida.plantavida.web.controller;

import com.plantavida.plantavida.persistence.entity.CompradorEntity;
import com.plantavida.plantavida.service.CompradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compradores")
public class CompradorController {
    private final CompradorService compradorService;

    @Autowired
    public CompradorController(CompradorService compradorService) {
        this.compradorService = compradorService;
    }

    @GetMapping
    public ResponseEntity<List<CompradorEntity>> getAll() {
        return ResponseEntity.ok(this.compradorService.getAll());
    }

    @GetMapping("/{idComprador}")
    public ResponseEntity<CompradorEntity> get(@PathVariable int idComprador) {
        return ResponseEntity.ok(this.compradorService.get(idComprador));
    }

    @PostMapping
    public ResponseEntity<CompradorEntity> add(@RequestBody CompradorEntity comprador) {
        if (comprador.getIdComprador() == null || !this.compradorService.exist(comprador.getIdComprador())) {
            return ResponseEntity.ok(this.compradorService.save(comprador));
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<CompradorEntity> update(@RequestBody CompradorEntity comprador) {
        if (comprador.getIdComprador() != null && this.compradorService.exist(comprador.getIdComprador())) {
            return ResponseEntity.ok(this.compradorService.save(comprador));
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{idComprador}")
    public ResponseEntity<Void> delete(@PathVariable int idComprador) {
        if (this.compradorService.exist(idComprador)) {
            this.compradorService.delete(idComprador);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
