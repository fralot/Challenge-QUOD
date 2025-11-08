package com.quod.backendantifraude.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaceVerificationResponse {
    private boolean verified;
    private double distance;
}

