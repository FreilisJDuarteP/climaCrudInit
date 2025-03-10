package com.tiempo.clima.controller;

import com.tiempo.clima.dto.PronosticoDTO;
import com.tiempo.clima.service.PronosticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/pronostico")
@RequiredArgsConstructor
public class PronosticoController {

    private final PronosticoService pronosticoService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/diario/{ciudad}")
    public Flux<PronosticoDTO> obtenerPronosticoDiario(@PathVariable String ciudad,
                                                       @RequestParam(defaultValue = "metric") String units,
                                                       @RequestParam(defaultValue = "es") String lang) {
        return pronosticoService.obtenerPronostico(ciudad, units, lang);
    }
}
