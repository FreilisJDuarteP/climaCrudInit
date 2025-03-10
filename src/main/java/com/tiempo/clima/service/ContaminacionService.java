package com.tiempo.clima.service;

import com.tiempo.clima.dto.ContaminacionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ContaminacionService {

    private final WebClient webClient;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public ContaminacionService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Cacheable(value = "contaminacion", key = "#ciudad")
    public Mono<ContaminacionDTO> obtenerCalidadAire(String ciudad) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/air_pollution")
                        .queryParam("q", ciudad)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(ContaminacionDTO.class);
    }
}
