package com.quod.backendantifraude.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "monitoramento")
@Getter
@Setter
public class RegistroMonitoramento {

    @Id
    private String id;

    private String transacaoId;
    private String tipoEvento; // Ex: "FRAUDE", "SUCESSO"
    private String descricao;
    private Instant dataEvento;

}

