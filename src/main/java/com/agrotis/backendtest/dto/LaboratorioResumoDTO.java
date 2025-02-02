package com.agrotis.backendtest.dto;

public class LaboratorioResumoDTO {
    private Long codigoLaboratorio;
    private String nomeLaboratorio;
    private Long quantidadePessoas;

    public LaboratorioResumoDTO(Long codigoLaboratorio, String nomeLaboratorio, Long quantidadePessoas) {
        this.codigoLaboratorio = codigoLaboratorio;
        this.nomeLaboratorio = nomeLaboratorio;
        this.quantidadePessoas = quantidadePessoas;
    }

    public Long getCodigoLaboratorio() {
        return codigoLaboratorio;
    }

    public void setCodigoLaboratorio(Long codigoLaboratorio) {
        this.codigoLaboratorio = codigoLaboratorio;
    }

    public String getNomeLaboratorio() {
        return nomeLaboratorio;
    }

    public void setNomeLaboratorio(String nomeLaboratorio) {
        this.nomeLaboratorio = nomeLaboratorio;
    }

    public Long getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public void setQuantidadePessoas(Long quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }
}
