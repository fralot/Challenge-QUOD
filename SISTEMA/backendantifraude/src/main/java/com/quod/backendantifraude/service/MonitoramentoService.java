package com.quod.backendantifraude.service;

import com.quod.backendantifraude.models.RegistroMonitoramento;
import com.quod.backendantifraude.models.biometria;
import com.quod.backendantifraude.repository.MonitoramentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MonitoramentoService {

    @Autowired
    private MonitoramentoRepository monitoramentoRepository;

    public void registrarFraude(biometria biometria) {
        RegistroMonitoramento registro = new RegistroMonitoramento();
        registro.setTransacaoId(biometria.getTransacaoId());
        registro.setTipoEvento("FRAUDE");
        registro.setDescricao(biometria.getTipoFraude());
        registro.setDataEvento(Instant.now());

        monitoramentoRepository.save(registro);
    }

    public void registrarSucesso(biometria biometria) {
        RegistroMonitoramento registro = new RegistroMonitoramento();
        registro.setTransacaoId(biometria.getTransacaoId());
        registro.setTipoEvento("SUCESSO");
        registro.setDescricao("Biometria processada com sucesso");
        registro.setDataEvento(Instant.now());

        monitoramentoRepository.save(registro);
    }
}

