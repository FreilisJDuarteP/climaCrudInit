package com.tiempo.clima.controller;

import com.tiempo.clima.dto.Mensaje;
import com.tiempo.clima.dto.PronosticoResponse;
import com.tiempo.clima.service.PronosticoService;
import io.github.bucket4j.Bucket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pronostico")
@CrossOrigin
public class PronosticoController {

    private final PronosticoService pronosticoService;
    private final Bucket bucket;

    public PronosticoController(PronosticoService pronosticoService, Bucket bucket) {
        this.pronosticoService = pronosticoService;
        this.bucket = bucket;
    }

    @GetMapping("/{ciudad}")
    public ResponseEntity<?> obtenerPronostico(@PathVariable String ciudad) {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body(new Mensaje("Límite de consultas alcanzado. Intenta de nuevo en una hora."));
        }

        try {
            PronosticoResponse pronostico = pronosticoService.obtenerPronostico(ciudad);
            return ResponseEntity.ok(pronostico);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Mensaje("Error al obtener el pronóstico: " + e.getMessage()));
        }
    }
}
