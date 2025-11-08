package com.quod.backendantifraude.repository;

import com.quod.backendantifraude.models.RegistroMonitoramento;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MonitoramentoRepository extends MongoRepository<RegistroMonitoramento, String> {
    // Você pode adicionar consultas customizadas aqui, se necessário
}

