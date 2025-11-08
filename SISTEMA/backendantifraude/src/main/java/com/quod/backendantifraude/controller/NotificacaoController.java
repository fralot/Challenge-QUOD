package com.quod.backendantifraude.controller;

import com.quod.backendantifraude.dto.NotificacaoDTO;
import com.quod.backendantifraude.models.RegistroNotificacao;
import com.quod.backendantifraude.repository.NotificacaoRepository;
import com.quod.backendantifraude.service.NotificacaoEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoController.class);

    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private NotificacaoEmitterService emitter;

    @PostMapping("/fraude")
    public ResponseEntity<Void> receberNotificacao(@RequestBody NotificacaoDTO notificacao) {
        logger.info("Notificação recebida: {}", notificacao);

        RegistroNotificacao registro = new RegistroNotificacao();
        registro.setTransacaoId(notificacao.getTransacaoId());
        registro.setTipoBiometria(notificacao.getTipoBiometria());
        registro.setTipoFraude(notificacao.getTipoFraude());
        registro.setDataCaptura(notificacao.getDataCaptura());
        registro.setDispositivo(notificacao.getDispositivo());
        registro.setCanalNotificacao(notificacao.getCanalNotificacao());
        registro.setNotificadoPor(notificacao.getNotificadoPor());
        registro.setMetadados(notificacao.getMetadados());
        registro.setFraudeDetectada(notificacao.isFraudeDetectada());

        repository.save(registro);

        return ResponseEntity.ok().build();
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<RegistroNotificacao> streamNotificacoes() {
        return emitter.stream();
    }

    @GetMapping("/historico")
    public List<RegistroNotificacao> listarTodas() {
        return repository.findAll();
    }

}
