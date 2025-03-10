package com.tiempo.clima.service;

import com.tiempo.clima.dto.PronosticoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PronosticoService {

    @Value("${api.forecast.url}")
    private String apiUrl;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public PronosticoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PronosticoResponse obtenerPronostico(String ciudad) {
        String url = String.format("%s?q=%s&cnt=5&appid=%s&units=metric&lang=es", apiUrl, ciudad, apiKey);
        return restTemplate.getForObject(url, PronosticoResponse.class);
    }
}
