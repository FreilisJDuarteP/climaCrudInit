package com.tiempo.clima.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private String ciudad;
    private String tipoConsulta;
    private LocalDateTime fecha;

    // Constructor vacío requerido por JPA
    public Consulta() {
    }

    // Constructor con parámetros
    public Consulta(Usuario usuario, String ciudad, String tipoConsulta, LocalDateTime fecha) {
        this.usuario = usuario;
        this.ciudad = ciudad;
        this.tipoConsulta = tipoConsulta;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
