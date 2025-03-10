package com.tiempo.clima.controller;

import com.tiempo.clima.dto.Mensaje;
import com.tiempo.clima.service.ClimaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clima")
@CrossOrigin
public class ClimaController {

    private final ClimaService climaService;

    public ClimaController(ClimaService climaService) {
        this.climaService = climaService;
    }

    @PreAuthorize("hasRole('USER')") // Solo usuarios autenticados pueden acceder
    @GetMapping("/ciudad/{nombreCiudad}")
    public ResponseEntity<?> obtenerClima(@PathVariable String nombreCiudad) {
        try {
            return ResponseEntity.ok(climaService.obtenerClima(nombreCiudad));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Mensaje("Error al obtener el clima: " + e.getMessage()));
        }
    }
}
