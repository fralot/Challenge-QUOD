package com.quod.backendantifraude.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "biometrias")
public class biometria {

    @Id
    private String id;
    private String transacaoId;
    private String tipoBiometria; // facial ou digital
    private Instant dataCaptura;
    private dispositivo dispositivo;
    private Metadados metadados;
    private boolean fraudeDetectada;
    private String tipoFraude; // deepfake, máscara, foto de foto, etc.

    private boolean rostoDetectado;
    private Long tamanhoImagemKB;
    private Integer brilhoMedio; // 0 (preto) até 255 (branco)
    private String tipoArquivo; // ex: jpg, png


    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransacaoId() {
        return transacaoId;
    }

    public void setTransacaoId(String transacaoId) {
        this.transacaoId = transacaoId;
    }

    public String getTipoBiometria() {
        return tipoBiometria;
    }

    public void setTipoBiometria(String tipoBiometria) {
        this.tipoBiometria = tipoBiometria;
    }

    public Instant getDataCaptura() {
        return dataCaptura;
    }

    public void setDataCaptura(Instant dataCaptura) {
        this.dataCaptura = dataCaptura;
    }

    public dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Metadados getMetadados() {
        return metadados;
    }

    public void setMetadados(Metadados metadados) {
        this.metadados = metadados;
    }

    public boolean isFraudeDetectada() {
        return fraudeDetectada;
    }

    public void setFraudeDetectada(boolean fraudeDetectada) {
        this.fraudeDetectada = fraudeDetectada;
    }

    public String getTipoFraude() {
        return tipoFraude;
    }

    public void setTipoFraude(String tipoFraude) {
        this.tipoFraude = tipoFraude;
    }

    public boolean isRostoDetectado() {
        return rostoDetectado;
    }

    public void setRostoDetectado(boolean rostoDetectado) {
        this.rostoDetectado = rostoDetectado;
    }

    public Long getTamanhoImagemKB() {
        return tamanhoImagemKB;
    }

    public void setTamanhoImagemKB(Long tamanhoImagemKB) {
        this.tamanhoImagemKB = tamanhoImagemKB;
    }

    public Integer getBrilhoMedio() {
        return brilhoMedio;
    }

    public void setBrilhoMedio(Integer brilhoMedio) {
        this.brilhoMedio = brilhoMedio;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }
}

