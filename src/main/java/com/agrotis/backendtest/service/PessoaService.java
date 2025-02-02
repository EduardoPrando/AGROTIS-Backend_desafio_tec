package com.agrotis.backendtest.service;

import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.model.Pessoa;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.repository.LaboratorioRepository;
import com.agrotis.backendtest.repository.PessoaRepository;
import com.agrotis.backendtest.repository.PropriedadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {
    private final PessoaRepository repository;
    private final LaboratorioRepository laboratorioRepository;
    private final PropriedadeRepository propriedadeRepository;


    public PessoaService(PessoaRepository repository, LaboratorioRepository laboratorioRepository, PropriedadeRepository propriedadeRepository) {
        this.repository = repository;
        this.laboratorioRepository = laboratorioRepository;
        this.propriedadeRepository = propriedadeRepository;
    }

    public Pessoa salvar(Pessoa pessoa) {
        if (pessoa.getPropriedade() != null && pessoa.getPropriedade().getId() != null) {
            Propriedade propriedade = propriedadeRepository
                    .findById(pessoa.getPropriedade().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Propriedade com ID " + pessoa.getPropriedade().getId() + " não encontrada"));
            pessoa.setPropriedade(propriedade);
        }
        if (pessoa.getLaboratorio() != null && pessoa.getLaboratorio().getId() != null) {
            Laboratorio laboratorio = laboratorioRepository
                    .findById(pessoa.getLaboratorio().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Laboratorio com ID " + pessoa.getLaboratorio().getId() + " não encontrado"));
            pessoa.setLaboratorio(laboratorio);
        }
        return repository.save(pessoa);
    }

    public Pessoa editar(Long id, Pessoa pessoa) {
        buscarPorId(id);
        pessoa.setId(id);
        return repository.save(pessoa);
    }

    public List<Pessoa> listar() {
        return repository.findAll();
    }

    public Pessoa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada!"));
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

}
