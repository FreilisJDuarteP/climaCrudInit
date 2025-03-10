package com.tiempo.clima.service;

import com.tiempo.clima.dto.PronosticoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class PronosticoService {

    private final WebClient webClient;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public PronosticoService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Cacheable(value = "pronostico", key = "#ciudad")
    public Flux<PronosticoDTO> obtenerPronostico(String ciudad, String units, String lang) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/forecast")
                        .queryParam("q", ciudad)
                        .queryParam("units", units)
                        .queryParam("lang", lang)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToFlux(PronosticoDTO.class);
    }
}
