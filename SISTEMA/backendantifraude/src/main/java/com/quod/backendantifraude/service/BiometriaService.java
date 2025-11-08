package com.quod.backendantifraude.service;

import com.quod.backendantifraude.models.Metadados;
import com.quod.backendantifraude.models.biometria;
import com.quod.backendantifraude.repository.BiometriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BiometriaService {

    @Autowired
    private BiometriaRepository repository;

    @Autowired
    private FraudeNotificacaoService fraudeNotificacaoService;

    public biometria processarBiometria(biometria biometria) {
        biometria.setTransacaoId(UUID.randomUUID().toString());
        biometria.setDataCaptura(Instant.now());

        String tipoFraude = null;

        if ("facial".equalsIgnoreCase(biometria.getTipoBiometria())) {
            tipoFraude = detectarFraudePorMetadados(biometria);

            if (tipoFraude == null) {
                tipoFraude = detectarFraudeVisual(biometria);
            }

        } else if ("digital".equalsIgnoreCase(biometria.getTipoBiometria())) {
            tipoFraude = detectarFraudePorMetadados(biometria);
        }

        if (tipoFraude != null) {
            biometria.setFraudeDetectada(true);
            biometria.setTipoFraude(tipoFraude);

            // Publica notificação de fraude
            fraudeNotificacaoService.notificarFraude(biometria);
        } else {
            // Publica notificação de sucesso
            fraudeNotificacaoService.notificarSucesso(biometria);
        }


        return repository.save(biometria);
    }

    private String detectarFraudePorMetadados(biometria biometria) {
        Metadados metadados = biometria.getMetadados();
        if (metadados == null) return "Metadados ausentes";
    
        Instant agora = Instant.now();
    
        // Validação da data de captura
        if (metadados.getDataCaptura() == null ||
            metadados.getDataCaptura().isAfter(agora.plusSeconds(60)) ||
            metadados.getDataCaptura().isBefore(agora.minusSeconds(60 * 60 * 24))) {
            return "Data de captura inconsistente";
        }
    
        // Validação de fabricante/modelo
        String fabricante = metadados.getFabricante() != null ? metadados.getFabricante().toLowerCase() : "";
        String modelo = metadados.getModelo() != null ? metadados.getModelo().toLowerCase() : "";
    
        if (fabricante.isEmpty() || modelo.isEmpty()) {
            return "Metadados do dispositivo ausentes";
        }
    
        if (fabricante.contains("generic") || modelo.contains("generic") ||
            fabricante.contains("unknown") || modelo.contains("unknown")) {
            return "Dispositivo suspeito - fabricante ou modelo genérico";
        }
    
        if (fabricante.contains("windows") || fabricante.contains("linux") ||
            modelo.contains("emulator") || modelo.contains("sdk")) {
            return "Dispositivo suspeito - possível emulador ou ambiente não móvel";
        }
    
        // Validação da localização
        if (metadados.getLatitude() == null || metadados.getLongitude() == null) {
            return "Localização não disponível";
        }
    
        if (metadados.getLatitude() == 0.0 && metadados.getLongitude() == 0.0) {
            return "Localização inválida - coordenadas zeradas";
        }
    
        return null;
    }    
    
    private String detectarFraudeVisual(biometria biometria) {
        int scoreRisco = 0;

        if (!biometria.isRostoDetectado()) {
            scoreRisco += 40;
        }

        if (biometria.getTamanhoImagemKB() != null && biometria.getTamanhoImagemKB() < 100) {
            scoreRisco += 20;
        }

        if (biometria.getBrilhoMedio() != null &&
                (biometria.getBrilhoMedio() < 10 || biometria.getBrilhoMedio() > 245)) {
            scoreRisco += 10;
        }

        if (biometria.getTipoArquivo() != null &&
                biometria.getTipoArquivo().equalsIgnoreCase("png")) {
            scoreRisco += 10;
        }

        if (biometria.getMetadados() != null &&
                biometria.getMetadados().getFabricante() != null &&
                biometria.getMetadados().getFabricante().toLowerCase().contains("windows")) {
            scoreRisco += 20;
        }

        if (scoreRisco >= 50) {
            return "Imagem suspeita - possível deepfake, máscara ou manipulação";
        }

        return null;
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int RAIO_TERRA = 6371;
        double latDist = Math.toRadians(lat2 - lat1);
        double lonDist = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDist / 2) * Math.sin(latDist / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDist / 2) * Math.sin(lonDist / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RAIO_TERRA * c;
    }

    @SuppressWarnings("unchecked")
    private Metadados mapParaMetadados(Map<String, Object> metadadosMap) {
        if (metadadosMap == null) return null;

        return new Metadados(
                metadadosMap.get("fabricante") != null ? metadadosMap.get("fabricante").toString() : null,
                metadadosMap.get("modelo") != null ? metadadosMap.get("modelo").toString() : null,
                metadadosMap.get("dataCaptura") != null ? Instant.parse(metadadosMap.get("dataCaptura").toString()) : null,
                metadadosMap.get("latitude") != null ? Double.parseDouble(metadadosMap.get("latitude").toString()) : null,
                metadadosMap.get("longitude") != null ? Double.parseDouble(metadadosMap.get("longitude").toString()) : null
        );
    }
}

