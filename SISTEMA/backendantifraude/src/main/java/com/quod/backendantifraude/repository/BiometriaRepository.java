package com.quod.backendantifraude.repository;

import com.quod.backendantifraude.models.biometria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BiometriaRepository extends MongoRepository<biometria, String> {
    // Buscar as últimas 5 biometrias ordenadas pela data de captura em ordem decrescente
    List<biometria> findTop5ByOrderByDataCapturaDesc();
}
