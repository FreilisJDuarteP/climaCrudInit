package com.tiempo.clima.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClimaDTO {
    private String ciudad;
    private String descripcion;
    private double temperatura;
    private double humedad;
    private double viento;
}
