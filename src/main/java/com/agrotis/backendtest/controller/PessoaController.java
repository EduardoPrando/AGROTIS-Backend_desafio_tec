package com.agrotis.backendtest.controller;

import com.agrotis.backendtest.handlers.CustomSuccessResponse;
import com.agrotis.backendtest.model.Pessoa;
import com.agrotis.backendtest.service.PessoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    private final PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Validated Pessoa pessoa) {
        return CustomSuccessResponse.created(service.salvar(pessoa),"Cadastro realizado com sucesso!");
    }

    @GetMapping()
    public ResponseEntity<?> buscar() {
        return CustomSuccessResponse.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return CustomSuccessResponse.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        return CustomSuccessResponse.ok(service.editar(id, pessoa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.deletar(id);
        return CustomSuccessResponse.noContent();
    }
}
