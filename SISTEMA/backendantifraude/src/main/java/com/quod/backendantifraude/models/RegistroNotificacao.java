package com.quod.backendantifraude.models;

import com.quod.backendantifraude.dto.DispositivoDTO;
import com.quod.backendantifraude.dto.MetadadosDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "notificacoes")
@Getter
@Setter
public class RegistroNotificacao {

    @Id
    private String id;

    private String transacaoId;
    private String tipoBiometria;
    private String tipoFraude;
    private Instant dataCaptura;
    private DispositivoDTO dispositivo;
    private List<String> canalNotificacao;
    private String notificadoPor;
    private MetadadosDTO metadados;
    private boolean fraudeDetectada;

}

