package com.tiempo.clima.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PronosticoDTO {
    private LocalDate fecha;
    private String descripcion;
    private double temperaturaMin;
    private double temperaturaMax;
    private double humedad;
}
