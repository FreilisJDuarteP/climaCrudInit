package com.tiempo.clima.controller;

import com.tiempo.clima.dto.Mensaje;
import com.tiempo.clima.service.ClimaService;
import io.github.bucket4j.Bucket;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clima")
@CrossOrigin
public class ClimaController {

    private final ClimaService climaService;
    private final Bucket bucket;

    public ClimaController(ClimaService climaService, Bucket bucket) {
        this.climaService = climaService;
        this.bucket = bucket;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/ciudad/{nombreCiudad}")
    public ResponseEntity<?> obtenerClima(@PathVariable String nombreCiudad) {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body(new Mensaje("Límite de consultas alcanzado. Intenta de nuevo en una hora."));
        }

        try {
            return ResponseEntity.ok(climaService.obtenerClima(nombreCiudad));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Mensaje("Error al obtener el clima: " + e.getMessage()));
        }
    }
}
