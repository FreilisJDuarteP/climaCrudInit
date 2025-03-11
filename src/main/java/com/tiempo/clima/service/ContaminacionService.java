package com.tiempo.clima.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

@Service
public class ContaminacionService {

    @Value("${api.geocoding.url}")
    private String geoApiUrl;

    @Value("${api.pollution.url}")
    private String pollutionApiUrl;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ContaminacionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
@Cacheable(value = "contaminacion", key = "#ciudad")
    public Map<String, Object> obtenerCalidadAire(String ciudad) {

        String geoUrl = String.format("%s?q=%s&limit=1&appid=%s", geoApiUrl, ciudad, apiKey);
        ResponseEntity<List> geoResponse = restTemplate.getForEntity(geoUrl, List.class);

        if (geoResponse.getBody() == null || geoResponse.getBody().isEmpty()) {
            throw new RuntimeException("Ciudad no encontrada");
        }

        Map<String, Object> location = (Map<String, Object>) geoResponse.getBody().get(0);
        double lat = (double) location.get("lat");
        double lon = (double) location.get("lon");


        String pollutionUrl = String.format("%s?lat=%f&lon=%f&appid=%s", pollutionApiUrl, lat, lon, apiKey);
        ResponseEntity<Map> pollutionResponse = restTemplate.getForEntity(pollutionUrl, Map.class);

        return pollutionResponse.getBody();
    }
}
