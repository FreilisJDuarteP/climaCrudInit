package com.tiempo.clima.controller;

import com.tiempo.clima.dto.Mensaje;
import com.tiempo.clima.service.ContaminacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contaminacion")
@CrossOrigin
public class ContaminacionController {

    private final ContaminacionService contaminacionService;

    public ContaminacionController(ContaminacionService contaminacionService) {
        this.contaminacionService = contaminacionService;
    }

    @PreAuthorize("hasRole('USER')") // Solo usuarios autenticados pueden acceder
    @GetMapping("/ciudad/{nombreCiudad}")
    public ResponseEntity<?> obtenerCalidadAire(@PathVariable String nombreCiudad) {
        try {
            return ResponseEntity.ok(contaminacionService.obtenerCalidadAire(nombreCiudad));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Mensaje("Error al obtener calidad del aire: " + e.getMessage()));
        }
    }
}
