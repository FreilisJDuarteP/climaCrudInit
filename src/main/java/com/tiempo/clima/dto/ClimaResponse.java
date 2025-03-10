package com.tiempo.clima.dto;

import lombok.Data;

@Data
public class ClimaResponse {
    private String ciudad;
    private double temperatura;
    private String descripcion;
}
