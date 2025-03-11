package com.tiempo.clima.service;

import com.tiempo.clima.dto.ClimaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ClimaService {

    @Value("${api.weather.url}")
    private String apiUrl;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ClimaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "clima", key = "#ciudad")
    public ClimaResponse obtenerClima(String ciudad) {
        String url = String.format("%s?q=%s&appid=%s&units=metric&lang=es", apiUrl, ciudad, apiKey);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null) {
            throw new RuntimeException("No se pudieron obtener datos del clima");
        }
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        double temperatura = (double) main.get("temp");
        double tempMin = (double) main.get("temp_min");
        double tempMax = (double) main.get("temp_max");
        double sensacionTermica = (double) main.get("feels_like");
        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
        String descripcion = weatherList.get(0).get("description").toString();

        return new ClimaResponse(
                ciudad,
                temperatura,
                tempMin,
                tempMax,
                sensacionTermica,
                traducirDescripcion(descripcion)
        );
    }

    private String traducirDescripcion(String descripcion) {
        return switch (descripcion.toLowerCase()) {
            case "clear sky" -> "Cielo despejado";
            case "few clouds" -> "Pocas nubes";
            case "scattered clouds" -> "Nubes dispersas";
            case "broken clouds" -> "Nublado";
            case "shower rain" -> "Lluvia ligera";
            case "rain" -> "Lluvia";
            case "thunderstorm" -> "Tormenta";
            case "snow" -> "Nieve";
            case "mist" -> "Neblina";
            case "overcast clouds" -> "Muy nuboso";
            default -> descripcion;
        };
    }
}
