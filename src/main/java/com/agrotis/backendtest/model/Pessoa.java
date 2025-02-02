package com.agrotis.backendtest.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do cadastro é obrigatório")
    private String nome;

    @Column(name = "data_inicial")
    @NotNull(message = "A data inicial do cadastro é obrigatória")
    private LocalDateTime dataInicial;

    @Column(name = "data_final")
    @NotNull(message = "A data final do cadastro é obrigatória")
    private LocalDateTime dataFinal;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "propriedade_id")
    @NotNull(message = "Você deve selecionar uma propriedade!")
    private Propriedade propriedade;

    @ManyToOne
    @JoinColumn(name = "laboratorio_id")
    @NotNull(message = "Você deve selecionar um laboratório!")
    private Laboratorio laboratorio;

    public Pessoa() {

    }

    public Pessoa(String nome, LocalDateTime dataInicial, LocalDateTime dataFinal, String observacoes, Propriedade propriedade, Laboratorio laboratorio) {
        this.nome = nome;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.observacoes = observacoes;
        this.propriedade = propriedade;
        this.laboratorio = laboratorio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
