package com.tiempo.clima.controller;

import com.tiempo.clima.service.ConsultaService;
import com.tiempo.clima.dto.ContaminacionResponse;
import com.tiempo.clima.dto.Mensaje;
import com.tiempo.clima.service.ContaminacionService;
import com.tiempo.clima.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/contaminacion")
@CrossOrigin
@Tag(name = "Contaminación", description = "Endpoints para consultar calidad del aire")
public class ContaminacionController {

    private final ContaminacionService contaminacionService;
    private final UsuarioService usuarioService;
    private final ConsultaService consultaService;
    private final RateLimiterService rateLimiterService;

    public ContaminacionController(
            ContaminacionService contaminacionService,
            UsuarioService usuarioService,
            ConsultaService consultaService,
            RateLimiterService rateLimiterService) {
        this.contaminacionService = contaminacionService;
        this.usuarioService = usuarioService;
        this.consultaService = consultaService;
        this.rateLimiterService = rateLimiterService;
    }

    @Operation(
            summary = "Obtener calidad del aire por ciudad",
            description = "Obtiene la calidad del aire para una ciudad específica usando las coordenadas de OpenWeatherMap."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta de calidad de aire exitosa"),
            @ApiResponse(responseCode = "400", description = "Error al obtener la calidad de aire"),
            @ApiResponse(responseCode = "429", description = "Límite de consultas alcanzado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/ciudad/{nombreCiudad}")
    public ResponseEntity<?> obtenerCalidadAire(
            @Parameter(description = "Nombre de la ciudad a consultar", example = "Bogotá")
            @PathVariable String nombreCiudad,
            Authentication authentication) {

        String username = authentication.getName();
        Usuario usuario = usuarioService.getByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Bucket bucket = rateLimiterService.resolveBucket(username);
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body(new Mensaje("Límite de consultas alcanzado. Intenta de nuevo en una hora."));
        }

        try {
            ContaminacionResponse calidadAire = contaminacionService.obtenerCalidadAire(nombreCiudad, username);
            consultaService.registrarConsulta(usuario, nombreCiudad, calidadAire.toString());
            return ResponseEntity.ok(calidadAire);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Mensaje("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Mensaje("Error inesperado: " + e.getMessage()));
        }
    }
}
