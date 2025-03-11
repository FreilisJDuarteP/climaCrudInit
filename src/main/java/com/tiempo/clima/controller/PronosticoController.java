package com.tiempo.clima.controller;

import com.tiempo.clima.dto.Mensaje;
import com.tiempo.clima.dto.PronosticoResponse;
import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.service.ConsultaService;
import com.tiempo.clima.service.PronosticoService;
import com.tiempo.clima.service.RateLimiterService;
import com.tiempo.clima.service.UsuarioService;
import io.github.bucket4j.Bucket;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{ciudad}")
    public ResponseEntity<?> obtenerPronostico(@PathVariable String ciudad, Authentication authentication) {

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
