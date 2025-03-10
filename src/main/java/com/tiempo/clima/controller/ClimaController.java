package com.tiempo.clima.controller;

import com.tiempo.clima.dto.ClimaDTO;
import com.tiempo.clima.service.ClimaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clima")
@RequiredArgsConstructor
public class ClimaController {

    private final ClimaService climaService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/actual/{ciudad}")
    public Mono<ClimaDTO> obtenerClimaActual(@PathVariable String ciudad,
                                             @RequestParam(defaultValue = "metric") String units,
                                             @RequestParam(defaultValue = "es") String lang) {
        return climaService.obtenerClimaActual(ciudad, units, lang);
    }
}
