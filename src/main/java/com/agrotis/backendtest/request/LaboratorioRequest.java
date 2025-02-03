package com.agrotis.backendtest.request;

import javax.validation.constraints.NotBlank;


public class LaboratorioRequest {
    @NotBlank(message = "O nome do laboratório é obrigatório")
    private String nome;
    public LaboratorioRequest() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
