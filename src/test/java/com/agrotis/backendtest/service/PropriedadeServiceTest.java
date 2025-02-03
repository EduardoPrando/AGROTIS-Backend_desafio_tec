package com.agrotis.backendtest.service;

import com.agrotis.backendtest.adapter.PropriedadeAdapter;
import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.repository.PropriedadeRepository;
import com.agrotis.backendtest.request.PropriedadeRequest;
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
class PropriedadeServiceTest {

    @Mock
    private PropriedadeRepository propriedadeRepository;

    @Mock
    private PropriedadeAdapter propriedadeAdapter;

    @InjectMocks
    private PropriedadeService propriedadeService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("salvar: Deve converter o request em entidade e salvar a Propriedade com sucesso")
    void deveCriarPropriedadeComSucesso() {
        PropriedadeRequest propriedadeRequest = new PropriedadeRequest();
        propriedadeRequest.setNome("Propriedade Teste");

        Propriedade propriedade = new Propriedade(1L, "Propriedade Teste");

        when(propriedadeAdapter.toEntity(propriedadeRequest)).thenReturn(propriedade);
        when(propriedadeRepository.save(any(Propriedade.class))).thenReturn(propriedade);

        Propriedade resultado = propriedadeService.salvar(propriedadeRequest);

        assertNotNull(resultado);
        assertEquals("Propriedade Teste", resultado.getNome());
        verify(propriedadeRepository, times(1)).save(any(Propriedade.class));
    }

    @Test
    @DisplayName("salvar: Deve retornar erro quando campos obrigatórios estão ausentes")
    void deveRetornarErroQuandoCamposObrigatoriosEstaoAusentes() {
        PropriedadeRequest propriedadeRequest = new PropriedadeRequest();

        Set<ConstraintViolation<PropriedadeRequest>> violations = validator.validate(propriedadeRequest);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<PropriedadeRequest> violation = violations.iterator().next();
        assertEquals("nome", violation.getPropertyPath().toString());
        assertEquals("O nome da propriedade é obrigatório", violation.getMessage());
    }

    @Test
    @DisplayName("buscarPorId: Deve retornar a Propriedade quando encontrada")
    void deveBuscarPropriedadePorIdComSucesso() {
        Propriedade propriedade = new Propriedade(1L, "Propriedade Teste");

        when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));

        Propriedade resultado = propriedadeService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(propriedadeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId: Deve retornar erro quando Propriedade não for encontrada")
    void deveRetornarErroAoBuscarPropriedadeInexistente() {
        when(propriedadeRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            propriedadeService.buscarPorId(999L);
        });

        assertEquals("Propriedade não encontrada!", exception.getMessage());
    }

    @Test
    @DisplayName("editar: Deve atualizar a Propriedade com sucesso")
    void deveEditarPropriedadeComSucesso() {
        PropriedadeRequest propriedadeRequest = new PropriedadeRequest();
        propriedadeRequest.setNome("Novo Nome");

        Propriedade propriedadeExistente = new Propriedade(1L, "Nome Antigo");

        when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedadeExistente));
        when(propriedadeRepository.save(any(Propriedade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Propriedade resultado = propriedadeService.editar(1L, propriedadeRequest);

        assertEquals("Novo Nome", resultado.getNome());
        verify(propriedadeRepository, times(1)).save(any(Propriedade.class));
    }


    @Test
    @DisplayName("deletar: Deve excluir a Propriedade com sucesso")
    void deveDeletarPropriedadeComSucesso() {
        Propriedade propriedade = new Propriedade(1L, "Propriedade Teste");

        when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));
        doNothing().when(propriedadeRepository).deleteById(1L);

        assertDoesNotThrow(() -> propriedadeService.deletar(1L));

        verify(propriedadeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deletar: Deve retornar erro quando Propriedade não for encontrada")
    void deveRetornarErroAoDeletarPropriedadeInexistente() {
        when(propriedadeRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            propriedadeService.deletar(999L);
        });

        assertEquals("Propriedade não encontrada!", exception.getMessage());
    }
}