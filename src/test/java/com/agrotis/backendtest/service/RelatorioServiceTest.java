package com.agrotis.backendtest.service;

import com.agrotis.backendtest.dto.FiltroLaboratorios;
import com.agrotis.backendtest.dto.LaboratorioResumoDTO;
import com.agrotis.backendtest.repository.RelatorioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RelatorioServiceTest {

    @Mock
    private RelatorioRepository repository;

    @InjectMocks
    private RelatorioService service;

    private FiltroLaboratorios filtroValido;
    private FiltroLaboratorios filtroDataInicialInvalida;
    private FiltroLaboratorios filtroDataFinalInvalida;

    @BeforeEach
    void setUp() {
        filtroValido = new FiltroLaboratorios();
        filtroValido.setQuantidadeMinima(1L);
        filtroValido.setDataInicialInicio(LocalDateTime.of(2025, 2, 1, 0, 0, 0));
        filtroValido.setDataInicialFim(LocalDateTime.of(2025, 2, 2, 0, 0, 0));
        filtroValido.setDataFinalInicio(LocalDateTime.of(2025, 2, 3, 0, 0, 0));
        filtroValido.setDataFinalFim(LocalDateTime.of(2025, 2, 4, 0, 0, 0));
        filtroValido.setObservacoes("BATATA");

        filtroDataInicialInvalida = new FiltroLaboratorios();
        filtroDataInicialInvalida.setQuantidadeMinima(1L);
        filtroDataInicialInvalida.setDataInicialInicio(LocalDateTime.of(2025, 2, 5, 0, 0, 0));
        filtroDataInicialInvalida.setDataInicialFim(LocalDateTime.of(2025, 2, 2, 0, 0, 0));

        filtroDataFinalInvalida = new FiltroLaboratorios();
        filtroDataFinalInvalida.setQuantidadeMinima(1L);
        filtroDataFinalInvalida.setDataFinalInicio(LocalDateTime.of(2025, 3, 5, 0, 0, 0));
        filtroDataFinalInvalida.setDataFinalFim(LocalDateTime.of(2025, 3, 2, 0, 0, 0));
    }

    @Test
    @DisplayName("buscar: Deve retornar resultados válidos quando os filtros estão corretos")
    void testBuscarRelatorioLaboratorios_ValidFilters() {
        LaboratorioResumoDTO resumo = new LaboratorioResumoDTO(1L, "LAB 1", 2L);
        when(repository.buscarLaboratorios(any(FiltroLaboratorios.class)))
                .thenReturn(List.of(resumo));

        List<LaboratorioResumoDTO> resultados = service.buscarRelatorioLaboratorios(filtroValido);

        assertNotNull(resultados, "A lista de resultados não deve ser nula");
        assertFalse(resultados.isEmpty(), "A lista de resultados não deve estar vazia");
        assertEquals(1, resultados.size(), "Deve retornar 1 resultado");
        LaboratorioResumoDTO resultado = resultados.get(0);
        assertEquals("LAB 1", resultado.getNomeLaboratorio(), "O nome do laboratório deve ser 'LAB 1'");
        assertEquals(2L, resultado.getQuantidadePessoas(), "A quantidade de pessoas deve ser 2");
    }

    @Test
    @DisplayName("buscar: Deve lançar exceção quando quantidadeMinima é inválida")
    void testBuscarRelatorioLaboratorios_InvalidQuantidadeMinima() {
        FiltroLaboratorios filtro = new FiltroLaboratorios();
        filtro.setQuantidadeMinima(0L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            service.buscarRelatorioLaboratorios(filtro)
        );
        assertEquals("Quantidade Minima deve ser >= 1", exception.getMessage());
    }

    @Test
    @DisplayName("buscar: Deve lançar exceção quando dataInicialInicio não for anterior a dataInicialFim")
    void testBuscarRelatorioLaboratorios_InvalidDataInicialRange() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.buscarRelatorioLaboratorios(filtroDataInicialInvalida)
        );
        assertTrue(exception.getMessage().contains("dataInicialInicio"),
                "A mensagem de erro deve indicar que dataInicialInicio é inválida");
    }

    @Test
    @DisplayName("buscar: Deve lançar exceção quando dataFinalInicio não for anterior a dataFinalFim")
    void testBuscarRelatorioLaboratorios_InvalidDataFinalRange() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            service.buscarRelatorioLaboratorios(filtroDataFinalInvalida)
        );
        assertTrue(exception.getMessage().contains("dataFinalInicio"),
                "A mensagem de erro deve indicar que dataFinalInicio é inválida");
    }
}