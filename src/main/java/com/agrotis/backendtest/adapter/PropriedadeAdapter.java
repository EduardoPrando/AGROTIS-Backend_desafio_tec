package com.agrotis.backendtest.adapter;

import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.request.PropriedadeRequest;
import org.springframework.stereotype.Component;

@Component
public class PropriedadeAdapter implements Adapter<Propriedade, PropriedadeRequest> {

    @Override
    public Propriedade toEntity(PropriedadeRequest propriedadeRequest) {
        return new Propriedade(
                propriedadeRequest.getNome(),
                propriedadeRequest.getCnpj()
        );
    }
}
