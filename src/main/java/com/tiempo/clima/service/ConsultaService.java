package com.tiempo.clima.service;

import com.tiempo.clima.entity.Consulta;
import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public List<Consulta> obtenerConsultasPorUsuario(Usuario usuario) {
        return consultaRepository.findByUsuario(usuario);
    }
}
