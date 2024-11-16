package com.plantavida.plantavida.web.controller;

import com.plantavida.plantavida.persistence.entity.BonoEntity;
import com.plantavida.plantavida.service.BonoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bonos")
@RequiredArgsConstructor
public class BonoController {

    private final BonoService bonoService;

    @PostMapping("/{idComprador}")
    public ResponseEntity<BonoEntity> createBono(@RequestBody BonoEntity bono, @PathVariable Integer idComprador) {
        BonoEntity createdBono = bonoService.createBono(bono, idComprador);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBono);
    }

    @GetMapping("/comprador/{idComprador}")
    public List<BonoEntity> getBonosByComprador(@PathVariable Integer idComprador) {
        return bonoService.getBonosByCompradorId(idComprador);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BonoEntity> getBonoById(@PathVariable Integer id) {
        return bonoService.getBonoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BonoEntity> updateBono(
            @PathVariable Integer id,
            @RequestBody BonoEntity bonoDetails) {
        BonoEntity updatedBono = bonoService.updateBono(id, bonoDetails);
        return ResponseEntity.ok(updatedBono);
    }
}
