package com.agrotis.backendtest.service;

import com.agrotis.backendtest.adapter.Adapter;
import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.repository.PropriedadeRepository;
import com.agrotis.backendtest.request.PropriedadeRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropriedadeService {

    Adapter<Propriedade, PropriedadeRequest> adapter;
    private final PropriedadeRepository repository;

    public PropriedadeService(PropriedadeRepository repository, Adapter<Propriedade, PropriedadeRequest> adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }

    public Propriedade salvar(PropriedadeRequest propriedadeRequest) {
        Propriedade propriedade = adapter.toEntity(propriedadeRequest);
        return repository.save(propriedade);
    }

    public Propriedade editar(Long id, PropriedadeRequest propriedadeRequest) {
        Propriedade propriedadeExistente = buscarPorId(id);

        if (propriedadeRequest.getNome() != null) {
            propriedadeExistente.setNome(propriedadeRequest.getNome());
        }

        return repository.save(propriedadeExistente);
    }


    public List<Propriedade> listar() {
        return repository.findAll();
    }

    public Propriedade buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propriedade não encontrada!"));
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
