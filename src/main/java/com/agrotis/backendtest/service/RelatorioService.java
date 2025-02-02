package com.agrotis.backendtest.service;

import com.agrotis.backendtest.dto.FiltroLaboratorios;
import com.agrotis.backendtest.dto.LaboratorioResumoDTO;
import com.agrotis.backendtest.repository.RelatorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {
    private final RelatorioRepository repository;

    public RelatorioService(RelatorioRepository repository) {
        this.repository = repository;
    }

    public List<LaboratorioResumoDTO> buscarRelatorioLaboratorios(FiltroLaboratorios filtro) {
        boolean temDataInicial = filtro.getDataInicialInicio() != null && filtro.getDataInicialFim() != null;
        boolean temDataFinal = filtro.getDataFinalInicio() != null && filtro.getDataFinalFim() != null;

        if (filtro.getQuantidadeMinima() == null || filtro.getQuantidadeMinima() < 1) {
            throw new IllegalArgumentException("Quantidade Minima deve ser >= 1");
        }

        if (temDataInicial && !filtro.getDataInicialInicio().isBefore(filtro.getDataInicialFim())) {
            throw new IllegalArgumentException("A dataInicialInicio deve ser menor que a dataInicialFim.");
        }

        if (temDataFinal && !filtro.getDataFinalInicio().isBefore(filtro.getDataFinalFim())) {
            throw new IllegalArgumentException("A dataFinalInicio deve ser menor que a dataFinalFim.");
        }
        return repository.buscarLaboratorios(filtro);
    }
}
