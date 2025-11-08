package com.quod.backendantifraude.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class NotificacaoDTO {
    private String transacaoId;
    private String tipoBiometria;
    private String tipoFraude; // "deepfake", "dispositivo diferente", ou "nenhuma"
    private Instant dataCaptura;
    private DispositivoDTO dispositivo;
    private List<String> canalNotificacao;
    private String notificadoPor;
    private MetadadosDTO metadados;
    private boolean fraudeDetectada;
}

