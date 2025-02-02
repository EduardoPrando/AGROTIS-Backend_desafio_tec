package com.agrotis.backendtest.request;

import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.model.Propriedade;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PessoaRequest {
    @NotBlank(message = "O nome do cadastro é obrigatório")
    private String nome;
    @NotNull(message = "A data inicial do cadastro é obrigatória")
    private LocalDateTime dataInicial;
    @NotNull(message = "A data final do cadastro é obrigatória")
    private LocalDateTime dataFinal;
    private String observacoes;
    @NotNull(message = "Você deve selecionar uma propriedade!")
    private Propriedade propriedade;
    @NotNull(message = "Você deve selecionar um laboratório!")
    private Laboratorio laboratorio;

    public PessoaRequest() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDateTime dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDateTime getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDateTime dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Propriedade getPropriedade() {
        return propriedade;
    }

    public void setPropriedade(Propriedade propriedade) {
        this.propriedade = propriedade;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }
}
