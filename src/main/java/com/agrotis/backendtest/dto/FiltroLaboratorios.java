package com.agrotis.backendtest.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

public class FiltroLaboratorios {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataInicialInicio;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataInicialFim;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataFinalInicio;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataFinalFim;
    private String observacoes;

    private Long quantidadeMinima;

    public LocalDateTime getDataInicialInicio() {
        return dataInicialInicio;
    }

    public void setDataInicialInicio(LocalDateTime dataInicialInicio) {
        this.dataInicialInicio = dataInicialInicio;
    }

    public LocalDateTime getDataInicialFim() {
        return dataInicialFim;
    }

    public void setDataInicialFim(LocalDateTime dataInicialFim) {
        this.dataInicialFim = dataInicialFim;
    }

    public LocalDateTime getDataFinalInicio() {
        return dataFinalInicio;
    }

    public void setDataFinalInicio(LocalDateTime dataFinalInicio) {
        this.dataFinalInicio = dataFinalInicio;
    }

    public LocalDateTime getDataFinalFim() {
        return dataFinalFim;
    }

    public void setDataFinalFim(LocalDateTime dataFinalFim) {
        this.dataFinalFim = dataFinalFim;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(Long quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }
}
