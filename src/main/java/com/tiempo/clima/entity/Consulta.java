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
    @JoinColumn(name = "usuario_id", nullable = false) // ✅ Enlace con la clave foránea
    private Usuario usuario;

    private String ciudad;
    private String resultado;
    private LocalDateTime fechaConsulta;
}
