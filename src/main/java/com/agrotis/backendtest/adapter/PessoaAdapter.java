package com.agrotis.backendtest.adapter;

import com.agrotis.backendtest.model.Pessoa;
import com.agrotis.backendtest.request.PessoaRequest;
import org.springframework.stereotype.Component;

@Component
public class PessoaAdapter implements Adapter<Pessoa, PessoaRequest> {

    @Override
    public Pessoa toEntity(PessoaRequest pessoaRequest) {
        return new Pessoa(
                pessoaRequest.getNome(),
                pessoaRequest.getDataInicial(),
                pessoaRequest.getDataFinal(),
                pessoaRequest.getObservacoes(),
                pessoaRequest.getPropriedade(),
                pessoaRequest.getLaboratorio()
        );
    }
}
