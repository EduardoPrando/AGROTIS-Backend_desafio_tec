package com.agrotis.backendtest.service;

import com.agrotis.backendtest.adapter.Adapter;
import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.model.Pessoa;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.repository.LaboratorioRepository;
import com.agrotis.backendtest.repository.PessoaRepository;
import com.agrotis.backendtest.repository.PropriedadeRepository;
import com.agrotis.backendtest.request.PessoaRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {

    public Adapter<Pessoa, PessoaRequest> adapter;
    private final PessoaRepository repository;
    private final LaboratorioRepository laboratorioRepository;
    private final PropriedadeRepository propriedadeRepository;


    public PessoaService(PessoaRepository repository, LaboratorioRepository laboratorioRepository, PropriedadeRepository propriedadeRepository,
                         Adapter<Pessoa, PessoaRequest> adapter) {
        this.repository = repository;
        this.laboratorioRepository = laboratorioRepository;
        this.propriedadeRepository = propriedadeRepository;
        this.adapter = adapter;
    }

    public Pessoa salvar(PessoaRequest pessoaRequest) {
        Pessoa pessoa = adapter.toEntity(pessoaRequest);

        if (pessoa.getPropriedade() != null) {
            Long propriedadeId = pessoa.getPropriedade().getId();
            if (propriedadeId != null) {
                Propriedade propriedade = propriedadeRepository
                        .findById(propriedadeId)
                        .orElseThrow(() -> new ResourceNotFoundException("Propriedade com ID " + propriedadeId + " não encontrada"));
                pessoa.setPropriedade(propriedade);
            }
        }

        if (pessoa.getLaboratorio() != null) {
            Long laboratorioId = pessoa.getLaboratorio().getId();
            if (laboratorioId != null) {
                Laboratorio laboratorio = laboratorioRepository
                        .findById(laboratorioId)
                        .orElseThrow(() -> new ResourceNotFoundException("Laboratorio com ID " + laboratorioId + " não encontrado"));
                pessoa.setLaboratorio(laboratorio);
            }
        }

        return repository.save(pessoa);
    }


    public Pessoa editar(Long id, PessoaRequest pessoaRequest) {
        Pessoa pessoaExistente = buscarPorId(id);

        pessoaExistente.setNome(pessoaRequest.getNome());
        pessoaExistente.setDataInicial(pessoaRequest.getDataInicial());
        pessoaExistente.setDataFinal(pessoaRequest.getDataFinal());
        pessoaExistente.setObservacoes(pessoaRequest.getObservacoes());

        if (pessoaRequest.getPropriedade() != null) {
            pessoaExistente.setPropriedade(propriedadeRepository.findById(pessoaRequest.getPropriedade().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Propriedade não encontrada!")));
        }

        if (pessoaRequest.getLaboratorio() != null) {
            pessoaExistente.setLaboratorio(laboratorioRepository.findById(pessoaRequest.getLaboratorio().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Laboratório não encontrado!")));
        }

        return repository.save(pessoaExistente);
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
