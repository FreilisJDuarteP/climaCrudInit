package com.tiempo.clima.controller;

import com.tiempo.clima.dto.ContaminacionDTO;
import com.tiempo.clima.service.ContaminacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/contaminacion")
@RequiredArgsConstructor
public class ContaminacionController {

    private final ContaminacionService contaminacionService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/actual/{ciudad}")
    public Mono<ContaminacionDTO> obtenerContaminacionActual(@PathVariable String ciudad) {
        return contaminacionService.obtenerCalidadAire(ciudad);
    }
}