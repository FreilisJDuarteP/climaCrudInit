package com.tiempo.clima.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
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

    public Map<String, Object> obtenerClima(String ciudad) {
        String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, ciudad, apiKey);
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getBody();
    }
}
