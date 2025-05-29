package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.EstadisticasDTO;
import com.example.clinicalolimsa.services.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    @GetMapping
    public ResponseEntity<EstadisticasDTO> getEstadisticas() {
        return ResponseEntity.ok(estadisticasService.obtenerEstadisticas());
    }
}
