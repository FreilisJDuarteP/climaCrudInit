package com.tiempo.clima.repository;

import com.tiempo.clima.entity.Consulta;
import com.tiempo.clima.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByUsuario(Usuario usuario);
}
