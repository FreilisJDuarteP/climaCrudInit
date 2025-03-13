package com.tiempo.clima.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private String ciudad;

    @Column(columnDefinition = "LONGTEXT")
    private String resultado;

    private LocalDateTime fechaConsulta;

    @Column(nullable = false) // Asegura que el tipo de consulta siempre tenga un valor
    private String tipoConsulta; // Nuevo campo para guardar el tipo de consulta (clima, contaminación, etc.)
}
