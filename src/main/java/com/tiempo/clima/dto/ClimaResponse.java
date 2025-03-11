package com.tiempo.clima.dto;

import lombok.Data;

@Data
public class ClimaResponse {
    private String ciudad;
    private double temperatura;
    private double tempMin;
    private double tempMax;
    private double sensacionTermica;
    private String descripcion;

    public ClimaResponse(String ciudad, double temperatura, double tempMin, double tempMax, double sensacionTermica, String descripcion) {
        this.ciudad = ciudad;
        this.temperatura = temperatura;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.sensacionTermica = sensacionTermica;
        this.descripcion = descripcion;
    }
}
