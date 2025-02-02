package com.agrotis.backendtest.request;

import com.agrotis.backendtest.adapter.Adapter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class LaboratorioRequest {
    private String nome;

    public LaboratorioRequest() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
