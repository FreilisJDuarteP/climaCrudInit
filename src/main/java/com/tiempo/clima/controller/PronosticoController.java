package com.tiempo.clima.controller;

import com.tiempo.clima.dto.Mensaje;
import com.tiempo.clima.dto.PronosticoResponse;
import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.service.ConsultaService;
import com.tiempo.clima.service.PronosticoService;
import com.tiempo.clima.service.RateLimiterService;
import com.tiempo.clima.service.UsuarioService;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pronóstico", description = "Endpoints para obtener pronósticos meteorológicos")
@RestController
@RequestMapping("/api/pronostico")
@CrossOrigin
public class PronosticoController {

    private final PronosticoService pronosticoService;
    private final UsuarioService usuarioService;
    private final ConsultaService consultaService;
    private final RateLimiterService rateLimiterService;

    public PronosticoController(
            PronosticoService pronosticoService,
            UsuarioService usuarioService,
            ConsultaService consultaService,
            RateLimiterService rateLimiterService) {
        this.pronosticoService = pronosticoService;
        this.usuarioService = usuarioService;
        this.consultaService = consultaService;
        this.rateLimiterService = rateLimiterService;
    }

    @Operation(
            summary = "Obtener pronóstico del clima por ciudad",
            description = "Devuelve el pronóstico del clima para una ciudad específica usando los datos de OpenWeatherMap."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pronóstico obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al obtener el pronóstico"),
            @ApiResponse(responseCode = "429", description = "Límite de consultas alcanzado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/{ciudad}")
    public ResponseEntity<?> obtenerPronostico(
            @Parameter(description = "Nombre de la ciudad para obtener el pronóstico", example = "Bogotá")
            @PathVariable String ciudad,
            Authentication authentication) {

        String username = authentication.getName();

        Usuario usuario = usuarioService.getByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Bucket bucket = rateLimiterService.resolveBucket(username);
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body(new Mensaje("Límite de consultas alcanzado. Intenta de nuevo en una hora."));
        }

        try {
            PronosticoResponse pronostico = pronosticoService.obtenerPronostico(ciudad, usuario.getNombreUsuario());
            consultaService.registrarConsulta(usuario, ciudad, pronostico.toString());
            return ResponseEntity.ok(pronostico);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Mensaje("Error al obtener el pronóstico: " + e.getMessage()));
        }
    }
}
