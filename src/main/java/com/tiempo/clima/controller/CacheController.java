package com.tiempo.clima.controller;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
@CrossOrigin
public class CacheController {

    private final CacheManager cacheManager;

    public CacheController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/limpiar")
    public String limpiarCache() {
        cacheManager.getCacheNames().forEach(cache -> cacheManager.getCache(cache).clear());
        return "Caché limpiada";
    }
}
