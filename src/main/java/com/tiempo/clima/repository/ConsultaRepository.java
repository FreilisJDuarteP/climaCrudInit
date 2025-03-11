package com.tiempo.clima.repository;

import com.tiempo.clima.entity.Consulta;
import com.tiempo.clima.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // Buscar por objeto Usuario directamente
    List<Consulta> findByUsuario(Usuario usuario);

    // ✅ Buscar por nombre de usuario (String) con @Query
    @Query("SELECT c FROM Consulta c WHERE c.usuario.nombreUsuario = :nombreUsuario")
    List<Consulta> findByNombreUsuario(@Param("nombreUsuario") String nombreUsuario);
}
