package com.tiempo.clima.controller;

import com.tiempo.clima.dto.PronosticoResponse;
import com.tiempo.clima.service.PronosticoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pronostico")
@CrossOrigin
public class PronosticoController {

    private final PronosticoService pronosticoService;

    public PronosticoController(PronosticoService pronosticoService) {
        this.pronosticoService = pronosticoService;
    }

    @GetMapping("/{ciudad}")
    public ResponseEntity<PronosticoResponse> obtenerPronostico(@PathVariable String ciudad) {
        PronosticoResponse pronostico = pronosticoService.obtenerPronostico(ciudad);
        return ResponseEntity.ok(pronostico);
    }
}
