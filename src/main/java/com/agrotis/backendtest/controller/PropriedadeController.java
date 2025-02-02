package com.agrotis.backendtest.controller;

import com.agrotis.backendtest.handlers.CustomSuccessResponse;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.request.PropriedadeRequest;
import com.agrotis.backendtest.service.PropriedadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/propriedade")
public class PropriedadeController {
    private final PropriedadeService service;

    public PropriedadeController(PropriedadeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Validated PropriedadeRequest propriedade) {
        return CustomSuccessResponse.created(service.salvar(propriedade), "Cadastro realizado com sucesso!");
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
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Propriedade propriedade) {
        return CustomSuccessResponse.ok(service.editar(id, propriedade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? > deletar(@PathVariable Long id) {
        service.deletar(id);
        return CustomSuccessResponse.noContent();
    }
}
