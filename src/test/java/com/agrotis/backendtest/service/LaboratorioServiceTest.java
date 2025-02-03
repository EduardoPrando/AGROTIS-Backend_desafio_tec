package com.agrotis.backendtest.service;

import com.agrotis.backendtest.adapter.LaboratorioAdapter;
import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.repository.LaboratorioRepository;
import com.agrotis.backendtest.request.LaboratorioRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LaboratorioServiceTest {

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @Mock
    private LaboratorioAdapter laboratorioAdapter;

    @InjectMocks
    private LaboratorioService laboratorioService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("salvar: Deve converter o request em entidade e salvar o Laboratório com sucesso")
    void deveCriarLaboratorioComSucesso() {
        LaboratorioRequest laboratorioRequest = new LaboratorioRequest();
        laboratorioRequest.setNome("Laboratório Teste");

        Laboratorio laboratorio = new Laboratorio(1L, "Laboratório Teste");

        when(laboratorioAdapter.toEntity(laboratorioRequest)).thenReturn(laboratorio);
        when(laboratorioRepository.save(any(Laboratorio.class))).thenReturn(laboratorio);

        Laboratorio resultado = laboratorioService.salvar(laboratorioRequest);

        assertNotNull(resultado);
        assertEquals("Laboratório Teste", resultado.getNome());
        verify(laboratorioRepository, times(1)).save(any(Laboratorio.class));
    }

    @Test
    @DisplayName("salvar: Deve retornar erro quando campos obrigatórios estão ausentes")
    void deveRetornarErroQuandoCamposObrigatoriosEstaoAusentes() {
        LaboratorioRequest laboratorioRequest = new LaboratorioRequest();

        Set<ConstraintViolation<LaboratorioRequest>> violations = validator.validate(laboratorioRequest);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<LaboratorioRequest> violation = violations.iterator().next();
        assertEquals("nome", violation.getPropertyPath().toString());
        assertEquals("O nome do laboratório é obrigatório", violation.getMessage());
    }

    @Test
    @DisplayName("buscarPorId: Deve retornar o Laboratório quando encontrado")
    void deveBuscarLaboratorioPorIdComSucesso() {
        Laboratorio laboratorio = new Laboratorio(1L, "Laboratório Teste");

        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorio));

        Laboratorio resultado = laboratorioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(laboratorioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId: Deve retornar erro quando Laboratório não for encontrado")
    void deveRetornarErroAoBuscarLaboratorioInexistente() {
        when(laboratorioRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            laboratorioService.buscarPorId(999L);
        });

        assertEquals("Laboratorio não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("editar: Deve atualizar o Laboratório com sucesso")
    void deveEditarLaboratorioComSucesso() {
        LaboratorioRequest laboratorioRequest = new LaboratorioRequest();
        laboratorioRequest.setNome("Novo Nome");

        Laboratorio laboratorioExistente = new Laboratorio(1L, "Nome Antigo");

        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorioExistente));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio resultado = laboratorioService.editar(1L, laboratorioRequest);

        assertEquals("Novo Nome", resultado.getNome());
        verify(laboratorioRepository, times(1)).save(any(Laboratorio.class));
    }


    @Test
    @DisplayName("deletar: Deve excluir o Laboratório com sucesso")
    void deveDeletarLaboratorioComSucesso() {
        Laboratorio laboratorio = new Laboratorio(1L, "Laboratório Teste");

        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorio));
        doNothing().when(laboratorioRepository).deleteById(1L);

        assertDoesNotThrow(() -> laboratorioService.deletar(1L));

        verify(laboratorioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deletar: Deve retornar erro quando Laboratório não for encontrado")
    void deveRetornarErroAoDeletarLaboratorioInexistente() {
        when(laboratorioRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            laboratorioService.deletar(999L);
        });

        assertEquals("Laboratorio não encontrado!", exception.getMessage());
    }
}