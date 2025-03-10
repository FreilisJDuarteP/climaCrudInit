package com.tiempo.clima.service;

import com.tiempo.clima.entity.Consulta;
import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public void registrarConsulta(Usuario usuario, String ciudad, String tipoConsulta) {
        Consulta consulta = new Consulta(null, usuario, ciudad, tipoConsulta, LocalDateTime.now());
        consultaRepository.save(consulta);
    }

    public List<Consulta> obtenerConsultasPorUsuario(Usuario usuario) {
        return consultaRepository.findByUsuario(usuario);
    }
}
