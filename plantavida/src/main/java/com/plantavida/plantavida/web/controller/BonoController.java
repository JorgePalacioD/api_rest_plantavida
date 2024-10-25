package com.plantavida.plantavida.web.controller;

import com.plantavida.plantavida.persistence.entity.BonoEntity;
import com.plantavida.plantavida.service.BonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bonos")
public class BonoController {
    private final BonoService bonoService;

    @Autowired
    public BonoController(BonoService bonoService) {
        this.bonoService = bonoService;
    }

    @GetMapping
    public ResponseEntity<List<BonoEntity>> getAll() {
        return ResponseEntity.ok(this.bonoService.getAll());
    }

    @GetMapping("/{idBono}")
    public ResponseEntity<BonoEntity> get(@PathVariable int idBono) {
        return ResponseEntity.ok(this.bonoService.get(idBono));
    }

    @PostMapping
    public ResponseEntity<BonoEntity> add(@RequestBody BonoEntity bono) {
        if (bono.getIdBono() == null || !this.bonoService.exist(bono.getIdComprador())) {
            return ResponseEntity.ok(this.bonoService.save(bono));
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<BonoEntity> update(@RequestBody BonoEntity bono) {
        if (bono.getIdBono() != null && this.bonoService.exist(bono.getIdComprador())) {
            return ResponseEntity.ok(this.bonoService.save(bono));
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{idBonol}")
    public ResponseEntity<Void> delete(@PathVariable int idBono) {
        if (this.bonoService.exist(idBono)) {
            this.bonoService.delete(idBono);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }


}
