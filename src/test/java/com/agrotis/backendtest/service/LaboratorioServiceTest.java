package com.agrotis.backendtest.service;

import com.agrotis.backendtest.adapter.Adapter;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LaboratorioServiceTest {

    @Mock
    private LaboratorioRepository repository;

    @Mock
    private Adapter<Laboratorio, LaboratorioRequest> adapter;

    @InjectMocks
    private LaboratorioService service;

    private LaboratorioRequest laboratorioRequest;
    private Laboratorio laboratorio;

    @BeforeEach
    void setUp() {
        laboratorioRequest = new LaboratorioRequest();
        laboratorioRequest.setNome("Lab Teste");

        laboratorio = new Laboratorio("Lab Teste");
        laboratorio.setId(1L);
    }

    @Test
    @DisplayName("salvar: Deve converter o request em entidade e salvar o laboratório com sucesso")
    void testSalvarLaboratorio_Success() {
        when(adapter.toEntity(laboratorioRequest)).thenReturn(laboratorio);
        when(repository.save(any(Laboratorio.class))).thenReturn(laboratorio);

        Laboratorio resultado = service.salvar(laboratorioRequest);

        assertNotNull(resultado, "O laboratório salvo não deve ser nulo");
        assertEquals("Lab Teste", resultado.getNome(), "O nome deve ser 'Lab Teste'");
        verify(adapter, times(1)).toEntity(laboratorioRequest);
        verify(repository, times(1)).save(laboratorio);
    }

    @Test
    @DisplayName("editar: Deve editar o laboratório definindo o ID e atualizando os dados")
    void testEditarLaboratorio() {
        when(repository.findById(1L)).thenReturn(Optional.of(laboratorio));
        when(repository.save(any(Laboratorio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laboratorio novoLab = new Laboratorio("Lab Atualizado");
        Laboratorio resultado = service.editar(1L, novoLab);

        assertNotNull(resultado, "O laboratório editado não deve ser nulo");
        assertEquals(1L, resultado.getId(), "O ID deve ser 1");
        assertEquals("Lab Atualizado", resultado.getNome(), "O nome deve ser atualizado para 'Lab Atualizado'");
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(novoLab);
    }

    @Test
    @DisplayName("listar: Deve retornar a lista de laboratórios")
    void testListarLaboratorios() {
        Laboratorio lab2 = new Laboratorio("Lab Beta");
        lab2.setId(2L);

        List<Laboratorio> labs = Arrays.asList(laboratorio, lab2);
        when(repository.findAll()).thenReturn(labs);

        List<Laboratorio> resultado = service.listar();
        assertNotNull(resultado, "A lista de laboratórios não deve ser nula");
        assertEquals(2, resultado.size(), "Deve retornar 2 laboratórios");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarPorId: Deve retornar laboratório por ID com sucesso")
    void testBuscarLaboratorioPorId_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(laboratorio));

        Laboratorio resultado = service.buscarPorId(1L);
        assertNotNull(resultado, "O laboratório encontrado não deve ser nulo");
        assertEquals("Lab Teste", resultado.getNome(), "O nome deve ser 'Lab Teste'");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId: Deve lançar ResourceNotFoundException quando laboratório não for encontrado")
    void testBuscarLaboratorioPorId_NotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(999L));
        assertEquals("Laboratorio não encontrado!", ex.getMessage(), "A mensagem de erro deve ser 'Laboratorio não encontrado!'");
        verify(repository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("deletar: Deve deletar o laboratório quando encontrado")
    void testDeletarLaboratorio() {
        when(repository.findById(1L)).thenReturn(Optional.of(laboratorio));
        doNothing().when(repository).deleteById(1L);

        service.deletar(1L);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
