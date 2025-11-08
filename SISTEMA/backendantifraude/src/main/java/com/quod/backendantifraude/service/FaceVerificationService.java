package com.quod.backendantifraude.service;

import com.quod.backendantifraude.dto.FaceVerificationRequest;
import com.quod.backendantifraude.dto.FaceVerificationResponse;
import com.quod.backendantifraude.models.Metadados;
import com.quod.backendantifraude.models.biometria;
import com.quod.backendantifraude.models.dispositivo;
import com.quod.backendantifraude.repository.BiometriaRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.UUID;

@Service
public class FaceVerificationService {

    private final RestTemplate restTemplate;
    private final FraudeNotificacaoService notificacaoService;
    private final BiometriaRepository biometriaRepository;

    private static final String PYTHON_URL = "http://face-verification:5001/compare";

    public FaceVerificationService(RestTemplateBuilder builder,
                                   FraudeNotificacaoService notificacaoService,
                                   BiometriaRepository biometriaRepository) {
        this.restTemplate = builder.build();
        this.notificacaoService = notificacaoService;
        this.biometriaRepository = biometriaRepository;
    }

    public FaceVerificationResponse verifyFace(FaceVerificationRequest request) {
        ResponseEntity<FaceVerificationResponse> response = restTemplate.postForEntity(
                PYTHON_URL, request, FaceVerificationResponse.class
        );

        FaceVerificationResponse resultado = response.getBody();
        biometria biometria = construirBiometria(request, resultado);

        if (resultado != null && resultado.isVerified()) {
            notificacaoService.notificarSucesso(biometria);
        } else {
            biometria.setTipoFraude("face-nao-bateu");
            notificacaoService.notificarFraude(biometria);
        }
        biometriaRepository.save(biometria);
        return resultado;
    }

    private biometria construirBiometria(FaceVerificationRequest request, FaceVerificationResponse resultado) {
        biometria biometria = new biometria();
        biometria.setTransacaoId(UUID.randomUUID().toString());
        biometria.setTipoBiometria("facial");
        biometria.setDataCaptura(Instant.now());
        biometria.setFraudeDetectada(!resultado.isVerified());

        Metadados metadados = request.getMetadados(); // Supondo que agora o request inclua esses dados
        biometria.setMetadados(metadados);

        dispositivo dispositivo = new dispositivo();
        dispositivo.setFabricante(metadados.getFabricante());
        dispositivo.setModelo(metadados.getModelo());
        biometria.setDispositivo(dispositivo);

        biometria.setRostoDetectado(true); // Idealmente vindo do ML Kit ou do próprio request
        biometria.setTamanhoImagemKB(150L); // Pode ser passado pelo app também
        biometria.setBrilhoMedio(130); // Idealmente calculado
        biometria.setTipoArquivo("jpg");

        return biometria;
    }
}

