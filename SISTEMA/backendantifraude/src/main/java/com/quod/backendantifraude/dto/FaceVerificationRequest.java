package com.quod.backendantifraude.dto;

import com.quod.backendantifraude.models.Metadados;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaceVerificationRequest {
    private String selfie;
    private String document;
    private Metadados metadados;
}

