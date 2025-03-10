package com.tiempo.clima.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Service
public class ContaminacionService {

    @Value("${api.pollution.url}")
    private String apiUrl;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ContaminacionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> obtenerCalidadAire(String ciudad) {
        String url = String.format("%s?q=%s&appid=%s", apiUrl, ciudad, apiKey);
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getBody();
    }
}
