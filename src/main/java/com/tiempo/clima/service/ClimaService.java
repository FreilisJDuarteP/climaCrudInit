package com.tiempo.clima.service;

import com.tiempo.clima.dto.ClimaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ClimaService {

    private final WebClient webClient;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public ClimaService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Cacheable(value = "clima", key = "#ciudad")
    public Mono<ClimaDTO> obtenerClimaActual(String ciudad, String units, String lang) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/weather")
                        .queryParam("q", ciudad)
                        .queryParam("units", units)
                        .queryParam("lang", lang)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(ClimaDTO.class);
    }
}
