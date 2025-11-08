package com.quod.backendantifraude.controller;

import com.quod.backendantifraude.dto.FaceVerificationRequest;
import com.quod.backendantifraude.dto.FaceVerificationResponse;
import com.quod.backendantifraude.models.Metadados;
import com.quod.backendantifraude.models.biometria;
import com.quod.backendantifraude.models.dispositivo;
import com.quod.backendantifraude.service.FaceVerificationService;
import com.quod.backendantifraude.service.FraudeNotificacaoService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/face")
public class FaceVerificationController {

    private final FaceVerificationService faceVerificationService;

    public FaceVerificationController(FaceVerificationService faceVerificationService) {
        this.faceVerificationService = faceVerificationService;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyFace(@RequestBody FaceVerificationRequest request) {
        try {
            FaceVerificationResponse response = faceVerificationService.verifyFace(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}




