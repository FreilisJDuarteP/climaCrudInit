package com.tiempo.clima.controller;

import com.tiempo.clima.entity.Consulta;
import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.service.ConsultaService;
import com.tiempo.clima.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mis-consultas")
    public ResponseEntity<List<Consulta>> obtenerMisConsultas(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(consultaService.obtenerConsultasPorUsuario(usuario));
    }
}
