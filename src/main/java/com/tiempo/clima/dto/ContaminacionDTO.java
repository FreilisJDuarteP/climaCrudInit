package com.tiempo.clima.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaminacionDTO {
    private String ciudad;
    private double calidadAire;
    private double pm10;
    private double pm2_5;
    private double co;
    private double no2;
    private double so2;
    private double o3;
}