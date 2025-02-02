package com.agrotis.backendtest.service;

import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.repository.PropriedadeRepository;
import com.agrotis.backendtest.request.PropriedadeRequest;
import com.agrotis.backendtest.adapter.Adapter;
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
public class PropriedadeServiceTest {

    @Mock
    private PropriedadeRepository repository;

    @Mock
    private Adapter<Propriedade, PropriedadeRequest> adapter;

    @InjectMocks
    private PropriedadeService service;

    private PropriedadeRequest propriedadeRequest;
    private Propriedade propriedade;

    @BeforeEach
    void setUp() {
        propriedadeRequest = new PropriedadeRequest();
        propriedadeRequest.setNome("Agrotis 1");
        propriedadeRequest.setCnpj("04909987000189");

        propriedade = new Propriedade();
        propriedade.setId(1L);
        propriedade.setNome("Agrotis 1");
        propriedade.setCnpj("04909987000189");
    }

    @Test
    @DisplayName("salvar: Deve converter o request em entidade e salvar a Propriedade com sucesso")
    void testSalvarPropriedade_Success() {
        when(adapter.toEntity(propriedadeRequest)).thenReturn(propriedade);
        when(repository.save(any(Propriedade.class))).thenReturn(propriedade);

        Propriedade resultado = service.salvar(propriedadeRequest);

        assertNotNull(resultado, "A propriedade salva não deve ser nula");
        assertEquals("Agrotis 1", resultado.getNome(), "O nome deve ser 'Agrotis 1'");
        verify(adapter, times(1)).toEntity(propriedadeRequest);
        verify(repository, times(1)).save(propriedade);
    }

    @Test
    @DisplayName("editar: Deve editar uma Propriedade definindo o ID e atualizando os dados")
    void testEditarPropriedade() {
        when(repository.findById(1L)).thenReturn(Optional.of(propriedade));
        when(repository.save(any(Propriedade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Propriedade propriedadeAtualizada = new Propriedade();
        propriedadeAtualizada.setNome("Agrotis Atualizado");
        propriedadeAtualizada.setCnpj("12345678901234");

        Propriedade resultado = service.editar(1L, propriedadeAtualizada);

        assertNotNull(resultado, "A propriedade editada não deve ser nula");
        assertEquals(1L, resultado.getId(), "O ID deve ser 1");
        assertEquals("Agrotis Atualizado", resultado.getNome(), "O nome deve ser atualizado para 'Agrotis Atualizado'");
        assertEquals("12345678901234", resultado.getCnpj(), "O CNPJ deve ser atualizado para o novo valor");
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(propriedadeAtualizada);
    }

    @Test
    @DisplayName("listar: Deve retornar a lista de Propriedades")
    void testListarPropriedades() {
        Propriedade propriedade2 = new Propriedade();
        propriedade2.setId(2L);
        propriedade2.setNome("Agrotis 2");
        propriedade2.setCnpj("04909987000188");

        List<Propriedade> lista = Arrays.asList(propriedade, propriedade2);
        when(repository.findAll()).thenReturn(lista);

        List<Propriedade> resultado = service.listar();
        assertNotNull(resultado, "A lista de propriedades não deve ser nula");
        assertEquals(2, resultado.size(), "A lista deve conter 2 propriedades");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarPorId: Deve retornar a Propriedade quando encontrada")
    void testBuscarPropriedadePorId_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(propriedade));

        Propriedade resultado = service.buscarPorId(1L);
        assertNotNull(resultado, "A propriedade encontrada não deve ser nula");
        assertEquals("Agrotis 1", resultado.getNome(), "O nome deve ser 'Agrotis 1'");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId: Deve lançar ResourceNotFoundException quando a Propriedade não for encontrada")
    void testBuscarPropriedadePorId_NotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.buscarPorId(999L);
        });
        assertEquals("Propriedade não encontrada!", ex.getMessage(), "A mensagem de erro deve ser 'Propriedade não encontrada!'");
        verify(repository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("deletar: Deve deletar a Propriedade quando encontrada")
    void testDeletarPropriedade() {
        when(repository.findById(1L)).thenReturn(Optional.of(propriedade));
        doNothing().when(repository).deleteById(1L);

        service.deletar(1L);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
