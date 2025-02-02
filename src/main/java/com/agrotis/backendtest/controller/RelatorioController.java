package com.agrotis.backendtest.controller;

import com.agrotis.backendtest.dto.FiltroLaboratorios;
import com.agrotis.backendtest.handlers.CustomSuccessResponse;
import com.agrotis.backendtest.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/laboratorios")
    public ResponseEntity<?> listarLaboratorios(@ModelAttribute FiltroLaboratorios filtro) {
        return CustomSuccessResponse.ok(relatorioService.buscarRelatorioLaboratorios(filtro));
    }
}