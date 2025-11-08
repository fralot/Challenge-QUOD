package com.quod.backendantifraude.models;

import java.time.Instant;

public class Metadados {
    private String fabricante;
    private String modelo;
    private Instant dataCaptura;
    private Double latitude;
    private Double longitude;

    // Construtor
    public Metadados(String fabricante, String modelo, Instant dataCaptura, Double latitude, Double longitude) {
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.dataCaptura = dataCaptura;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters e Setters
    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Instant getDataCaptura() {
        return dataCaptura;
    }

    public void setDataCaptura(Instant dataCaptura) {
        this.dataCaptura = dataCaptura;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

