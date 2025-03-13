package com.tiempo.clima.controller;

import com.tiempo.clima.entity.Consulta;
import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.service.ConsultaService;
import com.tiempo.clima.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Consultas", description = "Endpoints para consultar el historial de búsquedas de clima")
@RestController
@RequestMapping("/consultas")
@CrossOrigin
public class ConsultaController {

    private final ConsultaService consultaService;
    private final UsuarioService usuarioService;

    public ConsultaController(ConsultaService consultaService, UsuarioService usuarioService) {
        this.consultaService = consultaService;
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Obtener historial de consultas del usuario",
            description = "Devuelve el historial de consultas realizadas por el usuario autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de consultas obtenido correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mis-consultas")
    public ResponseEntity<List<Consulta>> obtenerMisConsultas(
            @AuthenticationPrincipal UserDetails userDetails) {
        String nombreUsuario = userDetails.getUsername();
        return ResponseEntity.ok(consultaService.obtenerConsultasPorUsuario(nombreUsuario));
    }
}
