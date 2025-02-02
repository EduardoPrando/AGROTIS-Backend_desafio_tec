package com.agrotis.backendtest.service;

import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.repository.PropriedadeRepository;
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
    private PropriedadeRepository propriedadeRepository;

    @InjectMocks
    private PropriedadeService propriedadeService;

    private Propriedade propriedade;

    @BeforeEach
    void setUp() {
        propriedade = new Propriedade();
        propriedade.setId(1L);
        propriedade.setNome("Agrotis 1");
        propriedade.setCnpj("04.909.987.0001-89");
    }

    @Test
    @DisplayName("Deve salvar uma propriedade com sucesso")
    void testSalvarPropriedade() {
        when(propriedadeRepository.save(any(Propriedade.class))).thenReturn(propriedade);

        Propriedade resultado = propriedadeService.salvar(propriedade);
        assertNotNull(resultado, "A propriedade salva não deve ser nula");
        assertEquals("Agrotis 1", resultado.getNome(), "O nome da propriedade deve ser 'Agrotis 1'");
        verify(propriedadeRepository, times(1)).save(propriedade);
    }

    @Test
    @DisplayName("Deve editar uma propriedade atualizando os dados recebidos")
    void testEditarPropriedade() {
        when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));
        when(propriedadeRepository.save(any(Propriedade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Propriedade novaPropriedade = new Propriedade();
        novaPropriedade.setNome("Agrotis Atualizado");
        novaPropriedade.setCnpj("12.345.678.9012-34");

        Propriedade resultado = propriedadeService.editar(1L, novaPropriedade);

        assertNotNull(resultado, "A propriedade editada não deve ser nula");
        assertEquals(1L, resultado.getId(), "O id da propriedade deve ser 1");
        assertEquals("Agrotis Atualizado", resultado.getNome(), "O nome deve ser atualizado para 'Agrotis Atualizado'");
        assertEquals("12.345.678.9012-34", resultado.getCnpj(), "O CNPJ deve ser atualizado para o novo valor");
        verify(propriedadeRepository, times(1)).findById(1L);
        verify(propriedadeRepository, times(1)).save(novaPropriedade);
    }

    @Test
    @DisplayName("Deve listar todas as propriedades")
    void testListarPropriedades() {
        Propriedade propriedade2 = new Propriedade();
        propriedade2.setId(2L);
        propriedade2.setNome("Agrotis 2");
        propriedade2.setCnpj("04909987000188");

        List<Propriedade> lista = Arrays.asList(propriedade, propriedade2);
        when(propriedadeRepository.findAll()).thenReturn(lista);

        List<Propriedade> resultado = propriedadeService.listar();
        assertNotNull(resultado, "A lista de propriedades não deve ser nula");
        assertEquals(2, resultado.size(), "A lista deve conter 2 propriedades");
        verify(propriedadeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar propriedade por ID com sucesso")
    void testBuscarPropriedadePorId_Success() {
        when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));
        Propriedade resultado = propriedadeService.buscarPorId(1L);
        assertNotNull(resultado, "A propriedade encontrada não deve ser nula");
        assertEquals("Agrotis 1", resultado.getNome(), "O nome da propriedade deve ser 'Agrotis 1'");
        verify(propriedadeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar propriedade inexistente")
    void testBuscarPropriedadePorId_NotFound() {
        when(propriedadeRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> propriedadeService.buscarPorId(999L));
        assertEquals("Propriedade não encontrada!", ex.getMessage(), "A mensagem de erro deve ser 'Propriedade não encontrada!'");
        verify(propriedadeRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve deletar uma propriedade com sucesso")
    void testDeletarPropriedade() {
        when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));
        doNothing().when(propriedadeRepository).deleteById(1L);

        propriedadeService.deletar(1L);
        verify(propriedadeRepository, times(1)).findById(1L);
        verify(propriedadeRepository, times(1)).deleteById(1L);
    }
}