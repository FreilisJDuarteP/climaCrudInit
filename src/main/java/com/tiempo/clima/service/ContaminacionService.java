package com.tiempo.clima.service;

import com.tiempo.clima.dto.ContaminacionResponse;
import com.tiempo.clima.entity.Usuario;
import com.tiempo.clima.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private final ConsultaService consultaService;
    private final UsuarioService usuarioService; // ✅ Inyectar UsuarioService

    public ContaminacionService(
            RestTemplate restTemplate,
            ConsultaService consultaService,
            UsuarioService usuarioService) {
        this.restTemplate = restTemplate;
        this.consultaService = consultaService;
        this.usuarioService = usuarioService;
    }

    @Cacheable(value = "contaminacion", key = "#ciudad")
    public ContaminacionResponse obtenerCalidadAire(String ciudad, String nombreUsuario) {
        // ✅ 1. Obtener el objeto Usuario desde el nombre
        Usuario usuario = usuarioService.getByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 🔎 2. Obtener coordenadas geográficas de la ciudad
        String geoUrl = String.format("%s?q=%s&limit=1&appid=%s", geoApiUrl, ciudad, apiKey);
        List<Map<String, Object>> geoResponse = restTemplate.getForObject(geoUrl, List.class);

        if (geoResponse == null || geoResponse.isEmpty()) {
            throw new RuntimeException("Ciudad no encontrada");
        }

        Map<String, Object> location = geoResponse.get(0);
        double lat = (double) location.get("lat");
        double lon = (double) location.get("lon");

        // 🌍 3. Obtener calidad del aire usando coordenadas
        String pollutionUrl = String.format("%s?lat=%f&lon=%f&appid=%s", pollutionApiUrl, lat, lon, apiKey);
        Map<String, Object> pollutionResponse = restTemplate.getForObject(pollutionUrl, Map.class);

        if (pollutionResponse == null || pollutionResponse.get("list") == null) {
            throw new RuntimeException("No se pudo obtener la calidad del aire");
        }

        List<Map<String, Object>> list = (List<Map<String, Object>>) pollutionResponse.get("list");
        Map<String, Object> main = (Map<String, Object>) list.get(0).get("main");
        int aqi = (int) main.get("aqi");

        // ✅ 4. Registrar la consulta mediante ConsultaService
        String resultado = traducirAqi(aqi);
        consultaService.registrarConsulta(usuario, ciudad, resultado);

        return new ContaminacionResponse(
                ciudad,
                resultado
        );
    }

    private String traducirAqi(int aqi) {
        return switch (aqi) {
            case 1 -> "Bueno";
            case 2 -> "Moderado";
            case 3 -> "Dañino para grupos sensibles";
            case 4 -> "Dañino para la salud";
            case 5 -> "Muy dañino para la salud";
            default -> "Desconocido";
        };
    }
}
