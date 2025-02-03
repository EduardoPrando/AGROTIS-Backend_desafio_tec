package com.agrotis.backendtest.service;

import com.agrotis.backendtest.adapter.Adapter;
import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.repository.LaboratorioRepository;
import com.agrotis.backendtest.request.LaboratorioRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaboratorioService {

    public Adapter<Laboratorio, LaboratorioRequest> adapter;
    private final LaboratorioRepository repository;

    public LaboratorioService(LaboratorioRepository repository, Adapter<Laboratorio, LaboratorioRequest> adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }

    public Laboratorio salvar(LaboratorioRequest laboratorioRequest) {
        Laboratorio laboratorio = adapter.toEntity(laboratorioRequest);
        return repository.save(laboratorio);
    }

    public Laboratorio editar(Long id, LaboratorioRequest laboratorioRequest) {
        Laboratorio laboratorioExistente = buscarPorId(id);

        if (laboratorioRequest.getNome() != null) {
            laboratorioExistente.setNome(laboratorioRequest.getNome());
        }

        return repository.save(laboratorioExistente);
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
