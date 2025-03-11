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

@RestController
@RequestMapping("/contaminacion")
@CrossOrigin
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

    @GetMapping("/ciudad/{nombreCiudad}")
    public ResponseEntity<?> obtenerCalidadAire(@PathVariable String nombreCiudad, Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuarioService.getByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Bucket bucket = rateLimiterService.resolveBucket(username);
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body(new Mensaje("Límite de consultas alcanzado. Intenta de nuevo en una hora."));
        }

        try {
            ContaminacionResponse calidadAire = contaminacionService.obtenerCalidadAire(nombreCiudad, username);
            consultaService.registrarConsulta(usuario, nombreCiudad, calidadAire.toString()); // ✅ Guardar el objeto Usuario
            return ResponseEntity.ok(calidadAire);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new Mensaje("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Mensaje("Error inesperado: " + e.getMessage()));
        }
    }
}
