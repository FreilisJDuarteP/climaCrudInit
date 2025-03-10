package com.tiempo.clima.security.service;

import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario));

        return User.builder()
                .username(usuario.getNombreUsuario())
                .password(usuario.getPassword())
                .roles(usuario.getRoles().stream()
                        .map(rol -> rol.getNombre().name()) // Convertir RolNombre a String
                        .toArray(String[]::new)) // Pasar como array de Strings
                .build();
    }
}
