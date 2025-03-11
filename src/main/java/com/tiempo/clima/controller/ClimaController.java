package com.tiempo.clima.controller;

import com.tiempo.clima.dto.ClimaResponse;
import com.tiempo.clima.dto.Mensaje;
import com.tiempo.clima.service.ClimaService;
import com.tiempo.clima.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clima")
@CrossOrigin
public class ClimaController {

    private final ClimaService climaService;
    private final RateLimiterService rateLimiterService;

    public ClimaController(ClimaService climaService, RateLimiterService rateLimiterService) {
        this.climaService = climaService;
        this.rateLimiterService = rateLimiterService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/ciudad/{nombreCiudad}")
    public ResponseEntity<?> obtenerClima(@PathVariable String nombreCiudad, Authentication authentication) {
        String username = authentication.getName();
        Bucket bucket = rateLimiterService.resolveBucket(username);

        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body(new Mensaje("Límite de consultas alcanzado. Intenta de nuevo en una hora."));
        }

        try {
            ClimaResponse clima = climaService.obtenerClima(nombreCiudad);
            return ResponseEntity.ok(clima);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Mensaje("Error al obtener el clima: " + e.getMessage()));
        }
    }
}
