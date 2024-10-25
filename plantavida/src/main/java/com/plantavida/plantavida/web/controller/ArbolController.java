package com.plantavida.plantavida.web.controller;

import com.plantavida.plantavida.persistence.entity.ArbolEntity;
import com.plantavida.plantavida.service.ArbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arboles")
public class ArbolController {
    private final ArbolService arbolService;

    @Autowired
    public ArbolController(ArbolService arbolService) {
        this.arbolService = arbolService;
    }

    @GetMapping
    public ResponseEntity<List<ArbolEntity>> getAll() {
        return ResponseEntity.ok(this.arbolService.getAll());
    }

    @GetMapping("/{idArbol}")
    public ResponseEntity<ArbolEntity> get(@PathVariable int idArbol) {
        return ResponseEntity.ok(this.arbolService.get(idArbol));
    }

    @PostMapping
    public ResponseEntity<ArbolEntity> add(@RequestBody ArbolEntity arbol) {
        if (arbol.getIdArbol() == null || !this.arbolService.exist(arbol.getIdArbol())) {
            return ResponseEntity.ok(this.arbolService.save(arbol));
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<ArbolEntity> update(@RequestBody ArbolEntity arbol) {
        if (arbol.getIdArbol() != null && this.arbolService.exist(arbol.getIdArbol())) {
            return ResponseEntity.ok(this.arbolService.save(arbol));
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{idArbol}")
    public ResponseEntity<Void> delete(@PathVariable int idArbol) {
        if (this.arbolService.exist(idArbol)) {
            this.arbolService.delete(idArbol);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
