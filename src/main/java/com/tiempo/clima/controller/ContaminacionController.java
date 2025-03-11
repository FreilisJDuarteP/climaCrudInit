package com.tiempo.clima.controller;

import com.tiempo.clima.dto.Mensaje;
import com.tiempo.clima.service.ContaminacionService;
import io.github.bucket4j.Bucket;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/contaminacion")
@CrossOrigin
public class ContaminacionController {

    private final ContaminacionService contaminacionService;
    private final Bucket bucket;

    public ContaminacionController(ContaminacionService contaminacionService, Bucket bucket) {
        this.contaminacionService = contaminacionService;
        this.bucket = bucket;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/ciudad/{nombreCiudad}")
    public ResponseEntity<?> obtenerCalidadAire(@PathVariable String nombreCiudad) {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body(new Mensaje("Límite de consultas alcanzado. Intenta de nuevo en una hora."));
        }

        try {
            Map<String, Object> calidadAire = contaminacionService.obtenerCalidadAire(nombreCiudad);
            return ResponseEntity.ok(calidadAire);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Mensaje("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Mensaje("Error inesperado: " + e.getMessage()));
        }
    }
}
