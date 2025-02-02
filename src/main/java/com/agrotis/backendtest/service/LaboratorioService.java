package com.agrotis.backendtest.service;

import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.repository.LaboratorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaboratorioService {

    private final LaboratorioRepository repository;

    public LaboratorioService(LaboratorioRepository repository) {
        this.repository = repository;
    }

    public Laboratorio salvar(Laboratorio laboratorio) {
        return repository.save(laboratorio);
    }

    public Laboratorio editar(Long id, Laboratorio laboratorio) {
        buscarPorId(id);
        laboratorio.setId(id);
        return repository.save(laboratorio);
    }

    public List<Laboratorio> listar() {
        return repository.findAll();
    }

    public Laboratorio buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratorio não encontrado!"));
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
