package com.plantavida.plantavida.web.controller;

import com.plantavida.plantavida.persistence.entity.ArbolEntity;
import com.plantavida.plantavida.service.ArbolService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arboles")
@RequiredArgsConstructor
public class ArbolController {

    private final ArbolService arbolService;

    @PostMapping("/{idBono}")
    public ResponseEntity<ArbolEntity> assignArbolToBono(@RequestBody ArbolEntity arbol, @PathVariable Integer idBono) {
        ArbolEntity assignedArbol = arbolService.assignArbolToBono(arbol, idBono);
        return ResponseEntity.status(HttpStatus.CREATED).body(assignedArbol);
    }

    @GetMapping("/bono/{idBono}")
    public ResponseEntity<ArbolEntity> getArbolByBonoId(@PathVariable Integer idBono) {
        return arbolService.getArbolByBonoId(idBono)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArbolEntity> getArbolById(@PathVariable Integer id) {
        return arbolService.getArbolById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArbolEntity> updateArbol(
            @PathVariable Integer id,
            @RequestBody ArbolEntity arbolDetails) {
        ArbolEntity updatedArbol = arbolService.updateArbol(id, arbolDetails);
        return ResponseEntity.ok(updatedArbol);
    }
}
