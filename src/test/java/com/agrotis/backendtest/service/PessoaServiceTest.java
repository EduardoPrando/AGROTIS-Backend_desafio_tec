package com.agrotis.backendtest.service;

import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.model.Pessoa;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.repository.LaboratorioRepository;
import com.agrotis.backendtest.repository.PessoaRepository;
import com.agrotis.backendtest.repository.PropriedadeRepository;
import com.agrotis.backendtest.request.PessoaRequest;
import com.agrotis.backendtest.adapter.Adapter;
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

    @Mock
    private Adapter<Pessoa, PessoaRequest> adapter;

    @InjectMocks
    private PessoaService pessoaService;

    private PessoaRequest pessoaRequest;
    private Pessoa pessoa;
    private Propriedade propriedade;
    private Laboratorio laboratorio;

    @BeforeEach
    void setUp() {
        propriedade = new Propriedade();
        propriedade.setId(1L);
        propriedade.setNome("Agrotis 1");
        propriedade.setCnpj("04909987000189");

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

        pessoaRequest = new PessoaRequest();
        pessoaRequest.setNome("Operario 1");
        pessoaRequest.setDataInicial(LocalDateTime.now());
        pessoaRequest.setDataFinal(LocalDateTime.now().plusDays(5));
        pessoaRequest.setObservacoes("Observação de teste");
        pessoaRequest.setPropriedade(propriedade);
        pessoaRequest.setLaboratorio(laboratorio);
    }

    @Test
    @DisplayName("salvar: Deve converter o request em entidade e salvar a Pessoa com sucesso")
    void testSalvarPessoa_Success() {
        when(adapter.toEntity(pessoaRequest)).thenReturn(pessoa);
        when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorio));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa resultado = pessoaService.salvar(pessoaRequest);

        assertNotNull(resultado, "A Pessoa salva não deve ser nula");
        assertEquals("Operario 1", resultado.getNome(), "O nome deve ser 'Operario 1'");
        verify(adapter, times(1)).toEntity(pessoaRequest);
        verify(propriedadeRepository, times(1)).findById(1L);
        verify(laboratorioRepository, times(1)).findById(1L);
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    @DisplayName("salvar: Deve lançar ResourceNotFoundException se a Propriedade não for encontrada")
    void testSalvarPessoa_PropriedadeNotFound() {
        when(adapter.toEntity(pessoaRequest)).thenReturn(pessoa);
        when(propriedadeRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.salvar(pessoaRequest);
        });
        assertEquals("Propriedade com ID 1 não encontrada", ex.getMessage());
        verify(propriedadeRepository, times(1)).findById(1L);
        verify(laboratorioRepository, never()).findById(anyLong());
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("salvar: Deve lançar ResourceNotFoundException se o Laboratorio não for encontrado")
    void testSalvarPessoa_LaboratorioNotFound() {
        when(adapter.toEntity(pessoaRequest)).thenReturn(pessoa);
        when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.salvar(pessoaRequest);
        });
        assertEquals("Laboratorio com ID 1 não encontrado", ex.getMessage());
        verify(propriedadeRepository, times(1)).findById(1L);
        verify(laboratorioRepository, times(1)).findById(1L);
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("editar: Deve editar uma Pessoa definindo o ID e salvando os dados atualizados")
    void testEditarPessoa() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PessoaRequest requestAtualizado = new PessoaRequest();
        requestAtualizado.setNome("Operario Atualizado");
        requestAtualizado.setDataInicial(pessoa.getDataInicial().plusHours(1));
        requestAtualizado.setDataFinal(pessoa.getDataFinal().plusHours(1));
        requestAtualizado.setObservacoes("Observação atualizada");
        requestAtualizado.setPropriedade(propriedade);
        requestAtualizado.setLaboratorio(laboratorio);

        Pessoa pessoaAtualizada = new Pessoa();
        pessoaAtualizada.setNome("Operario Atualizado");
        pessoaAtualizada.setDataInicial(requestAtualizado.getDataInicial());
        pessoaAtualizada.setDataFinal(requestAtualizado.getDataFinal());
        pessoaAtualizada.setObservacoes("Observação atualizada");
        pessoaAtualizada.setPropriedade(propriedade);
        pessoaAtualizada.setLaboratorio(laboratorio);
        when(adapter.toEntity(requestAtualizado)).thenReturn(pessoaAtualizada);

        Pessoa resultado = pessoaService.editar(1L, requestAtualizado);

        assertNotNull(resultado, "A Pessoa editada não deve ser nula");
        assertEquals(1L, resultado.getId(), "O ID deve ser 1");
        assertEquals("Operario Atualizado", resultado.getNome(), "O nome deve ser atualizado para 'Operario Atualizado'");
        assertEquals("Observação atualizada", resultado.getObservacoes(), "As observações devem ser atualizadas");
        verify(pessoaRepository, times(1)).findById(1L);
        verify(adapter, times(1)).toEntity(requestAtualizado);
        verify(pessoaRepository, times(1)).save(pessoaAtualizada);
    }

    @Test
    @DisplayName("listar: Deve retornar a lista de Pessoas")
    void testListarPessoas() {
        Pessoa p1 = pessoa;
        Pessoa p2 = new Pessoa();
        p2.setId(2L);
        p2.setNome("Operario 2");
        p2.setDataInicial(LocalDateTime.now());
        p2.setDataFinal(LocalDateTime.now().plusDays(3));
        p2.setObservacoes("Outra observação");
        p2.setPropriedade(propriedade);
        p2.setLaboratorio(laboratorio);

        List<Pessoa> lista = Arrays.asList(p1, p2);
        when(pessoaRepository.findAll()).thenReturn(lista);

        List<Pessoa> resultado = pessoaService.listar();
        assertNotNull(resultado, "A lista de pessoas não deve ser nula");
        assertEquals(2, resultado.size(), "Deve retornar 2 pessoas");
        verify(pessoaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarPorId: Deve retornar Pessoa por ID com sucesso")
    void testBuscarPessoaPorId_Success() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        Pessoa resultado = pessoaService.buscarPorId(1L);
        assertNotNull(resultado, "A Pessoa encontrada não deve ser nula");
        assertEquals("Operario 1", resultado.getNome(), "O nome deve ser 'Operario 1'");
        verify(pessoaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId: Deve lançar ResourceNotFoundException quando Pessoa não for encontrada")
    void testBuscarPessoaPorId_NotFound() {
        when(pessoaRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.buscarPorId(999L);
        });
        assertEquals("Pessoa não encontrada!", ex.getMessage(), "A mensagem de erro deve ser 'Pessoa não encontrada!'");
        verify(pessoaRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("deletar: Deve deletar Pessoa com sucesso")
    void testDeletarPessoa() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        doNothing().when(pessoaRepository).deleteById(1L);

        pessoaService.deletar(1L);
        verify(pessoaRepository, times(1)).findById(1L);
        verify(pessoaRepository, times(1)).deleteById(1L);
    }
}
