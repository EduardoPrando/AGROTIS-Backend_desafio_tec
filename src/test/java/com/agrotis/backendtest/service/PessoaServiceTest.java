package com.agrotis.backendtest.service;

import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.model.Pessoa;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.repository.LaboratorioRepository;
import com.agrotis.backendtest.repository.PessoaRepository;
import com.agrotis.backendtest.repository.PropriedadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @Mock
    private PropriedadeRepository propriedadeRepository;

    @InjectMocks
    private PessoaService pessoaService;

    private Pessoa pessoa;
    private Propriedade propriedade;
    private Laboratorio laboratorio;

    @BeforeEach
    void setUp() {
        propriedade = new Propriedade();
        propriedade.setId(1L);
        propriedade.setNome("Agrotis 1");
        propriedade.setCnpj("04.909.987.0001-89");

        laboratorio = new Laboratorio();
        laboratorio.setId(1L);
        laboratorio.setNome("Agro Skynet");

        pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Operario 1");
        pessoa.setDataInicial(LocalDateTime.now());
        pessoa.setDataFinal(LocalDateTime.now().plusDays(5));
        pessoa.setObservacoes("Observação de teste");
        pessoa.setPropriedade(propriedade);
        pessoa.setLaboratorio(laboratorio);
    }

    @Test
    @DisplayName("Deve salvar uma Pessoa com sucesso")
    void testSalvarPessoaSuccess() {
        when(propriedadeRepository.findById(propriedade.getId())).thenReturn(Optional.of(propriedade));
        when(laboratorioRepository.findById(laboratorio.getId())).thenReturn(Optional.of(laboratorio));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa result = pessoaService.salvar(pessoa);

        assertNotNull(result, "A pessoa salva não deve ser nula");
        assertEquals("Operario 1", result.getNome(), "O nome da pessoa deve ser 'Operario 1'");
        verify(propriedadeRepository, times(1)).findById(propriedade.getId());
        verify(laboratorioRepository, times(1)).findById(laboratorio.getId());
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao salvar se Propriedade não for encontrada")
    void testSalvarPessoaPropriedadeNotFound() {
        when(propriedadeRepository.findById(propriedade.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> pessoaService.salvar(pessoa));
        assertEquals("Propriedade com ID " + propriedade.getId() + " não encontrada", ex.getMessage());
        verify(propriedadeRepository, times(1)).findById(propriedade.getId());
        verify(laboratorioRepository, never()).findById(anyLong());
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao salvar se Laboratorio não for encontrado")
    void testSalvarPessoaLaboratorioNotFound() {
        when(propriedadeRepository.findById(propriedade.getId())).thenReturn(Optional.of(propriedade));
        when(laboratorioRepository.findById(laboratorio.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> pessoaService.salvar(pessoa));
        assertEquals("Laboratorio com ID " + laboratorio.getId() + " não encontrado", ex.getMessage());
        verify(propriedadeRepository, times(1)).findById(propriedade.getId());
        verify(laboratorioRepository, times(1)).findById(laboratorio.getId());
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("Deve editar uma Pessoa atualizando os dados recebidos")
    void testEditarPessoa_Success() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome("Operario Atualizado");
        novaPessoa.setDataInicial(pessoa.getDataInicial().plusHours(1));
        novaPessoa.setDataFinal(pessoa.getDataFinal().plusHours(1));
        novaPessoa.setObservacoes("Observação atualizada");
        novaPessoa.setPropriedade(propriedade);
        novaPessoa.setLaboratorio(laboratorio);

        Pessoa resultado = pessoaService.editar(1L, novaPessoa);
        assertNotNull(resultado, "A pessoa editada não deve ser nula");
        assertEquals(1L, resultado.getId(), "O id da pessoa deve ser 1");
        assertEquals("Operario Atualizado", resultado.getNome(), "O nome deve ser atualizado para 'Operario Atualizado'");
        assertEquals("Observação atualizada", resultado.getObservacoes(), "As observações devem ser atualizadas");
        verify(pessoaRepository, times(1)).findById(1L);
        verify(pessoaRepository, times(1)).save(novaPessoa);
    }

    @Test
    @DisplayName("Deve listar todas as Pessoas")
    void testListarPessoas() {
        Pessoa p1 = pessoa;
        Pessoa p2 = new Pessoa();
        p2.setId(2L);
        p2.setNome("Operario 2");
        p2.setDataInicial(pessoa.getDataInicial());
        p2.setDataFinal(pessoa.getDataFinal());
        p2.setObservacoes("Outra observação");
        p2.setPropriedade(propriedade);
        p2.setLaboratorio(laboratorio);

        List<Pessoa> pessoas = Arrays.asList(p1, p2);
        when(pessoaRepository.findAll()).thenReturn(pessoas);

        List<Pessoa> resultados = pessoaService.listar();
        assertNotNull(resultados, "A lista de pessoas não deve ser nula");
        assertEquals(2, resultados.size(), "Deve retornar 2 pessoas");
        verify(pessoaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar Pessoa por ID com sucesso")
    void testBuscarPessoaPorId_Success() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        Pessoa encontrada = pessoaService.buscarPorId(1L);
        assertNotNull(encontrada, "A pessoa encontrada não deve ser nula");
        assertEquals("Operario 1", encontrada.getNome(), "O nome da pessoa deve ser 'Operario 1'");
        verify(pessoaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar Pessoa inexistente")
    void testBuscarPessoaPorId_NotFound() {
        when(pessoaRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> pessoaService.buscarPorId(999L));
        assertEquals("Pessoa não encontrada!", ex.getMessage(), "A mensagem de erro deve ser 'Pessoa não encontrada!'");
        verify(pessoaRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve deletar Pessoa com sucesso")
    void testDeletarPessoa() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        doNothing().when(pessoaRepository).deleteById(1L);

        pessoaService.deletar(1L);

        verify(pessoaRepository, times(1)).findById(1L);
        verify(pessoaRepository, times(1)).deleteById(1L);
    }
}