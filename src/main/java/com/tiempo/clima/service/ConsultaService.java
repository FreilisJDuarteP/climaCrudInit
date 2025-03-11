package com.tiempo.clima.service;

import com.tiempo.clima.entity.Consulta;
import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.repository.ConsultaRepository;
import com.tiempo.clima.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final UsuarioRepository usuarioRepository;

    public ConsultaService(ConsultaRepository consultaRepository, UsuarioRepository usuarioRepository) {
        this.consultaRepository = consultaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void registrarConsulta(Usuario usuario, String ciudad, String resultado) {
        Consulta consulta = new Consulta();
        consulta.setUsuario(usuario);
        consulta.setCiudad(ciudad);
        consulta.setResultado(resultado);
        consulta.setFechaConsulta(LocalDateTime.now());

        consultaRepository.save(consulta);
    }
    public List<Consulta> obtenerConsultasPorUsuario(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return consultaRepository.findByUsuario(usuario);
    }
}
