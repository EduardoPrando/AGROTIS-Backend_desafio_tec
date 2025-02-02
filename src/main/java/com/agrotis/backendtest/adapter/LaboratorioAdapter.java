package com.agrotis.backendtest.adapter;

import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.request.LaboratorioRequest;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Component
public class LaboratorioAdapter implements Adapter<Laboratorio, LaboratorioRequest> {


    @Override
    public Laboratorio toEntity(LaboratorioRequest laboratorioRequest) {
        return new Laboratorio(laboratorioRequest.getNome());
    }
}
