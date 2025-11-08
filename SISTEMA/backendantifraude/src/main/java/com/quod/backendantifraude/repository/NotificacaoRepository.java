package com.quod.backendantifraude.repository;

import com.quod.backendantifraude.models.RegistroNotificacao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificacaoRepository extends MongoRepository<RegistroNotificacao, String> {
}

