package com.quod.backendantifraude.controller;

import com.quod.backendantifraude.models.biometria;
import com.quod.backendantifraude.service.BiometriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/biometria")
public class BiometriaController {

    @Autowired
    private BiometriaService biometriaService;

    @PostMapping("/processar")
    public ResponseEntity<biometria> processarBiometria(@RequestBody biometria biometria) {
        biometria resultado = biometriaService.processarBiometria(biometria);
        return ResponseEntity.ok(resultado);
    }
}

