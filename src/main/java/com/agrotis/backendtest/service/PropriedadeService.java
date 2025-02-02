package com.agrotis.backendtest.service;

import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.repository.PropriedadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropriedadeService {
    private final PropriedadeRepository repository;

    public PropriedadeService(PropriedadeRepository repository) {
        this.repository = repository;
    }

    public Propriedade salvar(Propriedade propriedade) {
        return repository.save(propriedade);
    }

    public Propriedade editar(Long id, Propriedade propriedade) {
        buscarPorId(id);
        propriedade.setId(id);
        return repository.save(propriedade);
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
