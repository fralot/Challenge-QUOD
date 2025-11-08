package com.quod.backendantifraude.service;

import com.quod.backendantifraude.dto.DispositivoDTO;
import com.quod.backendantifraude.dto.MetadadosDTO;
import com.quod.backendantifraude.dto.NotificacaoDTO;
import com.quod.backendantifraude.models.Metadados;
import com.quod.backendantifraude.models.biometria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FraudeNotificacaoService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String URL_NOTIFICACAO = "http://localhost:8080/api/notificacoes/fraude";

    public void notificarFraude(biometria biometria) {
        NotificacaoDTO dto = construirDto(biometria, true);
        restTemplate.postForEntity(URL_NOTIFICACAO, dto, Void.class);
    }

    public void notificarSucesso(biometria biometria) {
        NotificacaoDTO dto = construirDto(biometria, false);
        restTemplate.postForEntity(URL_NOTIFICACAO, dto, Void.class);
    }

    private NotificacaoDTO construirDto(biometria biometria, boolean fraude) {
        Metadados metadados = biometria.getMetadados();

        DispositivoDTO dispositivo = new DispositivoDTO();
        dispositivo.setFabricante(metadados.getFabricante());
        dispositivo.setModelo(metadados.getModelo());
        dispositivo.setSistemaOperacional("Android 13"); // ou capturado real

        MetadadosDTO meta = new MetadadosDTO();
        meta.setLatitude(metadados.getLatitude());
        meta.setLongitude(metadados.getLongitude());
        meta.setIpOrigem("192.168.1.10"); // opcionalmente extraído

        NotificacaoDTO dto = new NotificacaoDTO();
        dto.setTransacaoId(biometria.getTransacaoId());
        dto.setTipoBiometria(biometria.getTipoBiometria());
        dto.setTipoFraude(fraude ? biometria.getTipoFraude() : "nenhuma");
        dto.setDataCaptura(biometria.getDataCaptura());
        dto.setDispositivo(dispositivo);
        dto.setMetadados(meta);
        dto.setCanalNotificacao(List.of("sms", "email"));
        dto.setNotificadoPor("sistema-de-monitoramento");
        dto.setFraudeDetectada(fraude);

        return dto;
    }
}





