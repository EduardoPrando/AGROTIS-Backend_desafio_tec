package com.agrotis.backendtest.controller;

import com.agrotis.backendtest.handlers.CustomSuccessResponse;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.service.LaboratorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/laboratorio")
public class LaboratorioController {
    private final LaboratorioService service;

    public LaboratorioController(LaboratorioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Validated Laboratorio laboratorio) {
        return CustomSuccessResponse.created(service.salvar(laboratorio),"Cadastro realizado com sucesso!");
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
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Laboratorio laboratorio) {
        return CustomSuccessResponse.ok(service.editar(id, laboratorio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.deletar(id);
        return CustomSuccessResponse.noContent();
    }
}
