package com.tiempo.clima.controller;

import com.tiempo.clima.dto.JwtDto;
import com.tiempo.clima.dto.LoginUsuario;
import com.tiempo.clima.dto.Mensaje;
import com.tiempo.clima.dto.NuevoUsuario;
import com.tiempo.clima.entity.Rol;
import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.security.enums.RolNombre;
import com.tiempo.clima.security.jwt.JwtProvider;
import com.tiempo.clima.service.RolService;
import com.tiempo.clima.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthController(AuthenticationManager authenticationManager, UsuarioService usuarioService,
                          RolService rolService, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario) {
        if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario())) {
            return ResponseEntity.badRequest().body(new Mensaje("Ese nombre de usuario ya existe"));
        }
        if (usuarioService.existsByEmail(nuevoUsuario.getEmail())) {
            return ResponseEntity.badRequest().body(new Mensaje("Ese email ya existe"));
        }
        Usuario usuario = new Usuario(null, nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(),
                passwordEncoder.encode(nuevoUsuario.getPassword()), new HashSet<>());
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).orElseThrow());
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Mensaje("Usuario guardado"));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
    }
}
