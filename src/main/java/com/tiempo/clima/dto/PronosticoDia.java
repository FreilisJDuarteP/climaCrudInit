package com.tiempo.clima.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class PronosticoDia {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // ⬅️ Esto corrige el formato de fecha
    private LocalDateTime dt_txt;
    private PronosticoMain main;
    private PronosticoWeather[] weather;

    public LocalDateTime getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(LocalDateTime dt_txt) {
        this.dt_txt = dt_txt;
    }

    public PronosticoMain getMain() {
        return main;
    }

    public void setMain(PronosticoMain main) {
        this.main = main;
    }

    public PronosticoWeather[] getWeather() {
        return weather;
    }

    public void setWeather(PronosticoWeather[] weather) {
        this.weather = weather;
    }
}
