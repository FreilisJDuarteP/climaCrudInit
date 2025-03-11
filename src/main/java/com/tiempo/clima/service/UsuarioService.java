package com.tiempo.clima.service;

import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param nombreUsuario Nombre del usuario a buscar
     * @return Un Optional que contiene el usuario si existe, o vacío si no existe
     */
    public Optional<Usuario> getByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     *
     * @param nombreUsuario Nombre de usuario a verificar
     * @return true si el usuario existe, false si no
     */
    public boolean existsByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    /**
     * Verifica si existe un usuario con el email especificado.
     *
     * @param email Email a verificar
     * @return true si el email está registrado, false si no
     */
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * Guarda un nuevo usuario o actualiza uno existente.
     *
     * @param usuario Objeto Usuario a guardar
     */
    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    /**
     * Obtiene un usuario por su nombre de usuario y lanza una excepción si no se encuentra.
     *
     * @param nombreUsuario Nombre del usuario a buscar
     * @return El objeto Usuario si se encuentra
     * @throws RuntimeException si el usuario no existe
     */
    public Usuario getUsuarioByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + nombreUsuario));
    }
}
