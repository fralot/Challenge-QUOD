package com.quod.backendantifraude.service;

import com.quod.backendantifraude.models.RegistroNotificacao;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class NotificacaoEmitterService {

    private final Sinks.Many<RegistroNotificacao> sink = Sinks.many().multicast().onBackpressureBuffer();

    public void emitir(RegistroNotificacao notificacao) {
        sink.tryEmitNext(notificacao);
    }

    public Flux<RegistroNotificacao> stream() {
        return sink.asFlux();
    }
}

