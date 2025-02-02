package com.agrotis.backendtest.service;

import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.repository.LaboratorioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LaboratorioServiceTest {

    @Mock
    private LaboratorioRepository repository;

    @InjectMocks
    private LaboratorioService service;

    private Laboratorio lab;

    @BeforeEach
    void setUp() {
        lab = new Laboratorio();
        lab.setId(1L);
        lab.setNome("Laboratorio 1");
    }

    @Test
    @DisplayName("Deve salvar um laboratório com sucesso")
    void testSalvar() {
        when(repository.save(any(Laboratorio.class))).thenReturn(lab);

        Laboratorio saved = service.salvar(lab);
        assertNotNull(saved, "O laboratório salvo não deve ser nulo");
        assertEquals("Laboratorio 1", saved.getNome(), "O nome do laboratório deve ser 'Laboratorio 1'");
        verify(repository, times(1)).save(lab);
    }

    @Test
    @DisplayName("Deve editar um laboratório atualizando os dados recebidos")
    void testEditarLaboratorio() {
        when(repository.findById(1L)).thenReturn(Optional.of(lab));
        when(repository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio novoLab = new Laboratorio();
        novoLab.setNome("Lab Atualizado");

        Laboratorio resultado = service.editar(1L, novoLab);
        assertNotNull(resultado, "O laboratório editado não deve ser nulo");
        assertEquals(1L, resultado.getId(), "O id do laboratório deve ser 1");
        assertEquals("Lab Atualizado", resultado.getNome(), "O nome deve ser atualizado para 'Lab Atualizado'");
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(novoLab);
    }

    @Test
    @DisplayName("Deve listar todos os laboratórios")
    void testListar() {
        List<Laboratorio> labs = Collections.singletonList(lab);
        when(repository.findAll()).thenReturn(labs);

        List<Laboratorio> result = service.listar();
        assertNotNull(result, "A lista de laboratórios não deve ser nula");
        assertFalse(result.isEmpty(), "A lista não deve estar vazia");
        assertEquals(1, result.size(), "A lista deve conter 1 laboratório");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar laboratório por ID com sucesso")
    void testBuscarPorId_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(lab));
        Laboratorio found = service.buscarPorId(1L);
        assertNotNull(found, "O laboratório encontrado não deve ser nulo");
        assertEquals("Laboratorio 1", found.getNome(), "O nome do laboratório deve ser 'Laboratorio 1'");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar laboratório inexistente")
    void testBuscarPorId_NotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.buscarPorId(2L),
                "Deveria lançar ResourceNotFoundException se o laboratório não for encontrado");
        assertEquals("Laboratorio não encontrado!", ex.getMessage(), "A mensagem de erro deve ser 'Laboratorio não encontrado!'");
        verify(repository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Deve deletar um laboratório com sucesso")
    void testDeletar() {
        when(repository.findById(1L)).thenReturn(Optional.of(lab));
        doNothing().when(repository).deleteById(1L);

        service.deletar(1L);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
