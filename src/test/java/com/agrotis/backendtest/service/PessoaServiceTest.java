package com.agrotis.backendtest.service;

import com.agrotis.backendtest.adapter.PessoaAdapter;
import com.agrotis.backendtest.handlers.ResourceNotFoundException;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.model.Pessoa;
import com.agrotis.backendtest.model.Propriedade;
import com.agrotis.backendtest.repository.LaboratorioRepository;
import com.agrotis.backendtest.repository.PessoaRepository;
import com.agrotis.backendtest.repository.PropriedadeRepository;
import com.agrotis.backendtest.request.PessoaRequest;
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
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @Mock
    private PropriedadeRepository propriedadeRepository;

    @Mock
    private PessoaAdapter pessoaAdapter;

    @InjectMocks
    private PessoaService pessoaService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("salvar: Deve converter o request em entidade e salvar a Pessoa com sucesso")
    void deveCriarPessoaComSucesso() {
        PessoaRequest pessoaRequest = new PessoaRequest();
        pessoaRequest.setNome("João da Silva");
        pessoaRequest.setDataInicial(LocalDateTime.now());
        pessoaRequest.setDataFinal(LocalDateTime.now().plusDays(1));
        pessoaRequest.setObservacoes("Teste de observação");
        pessoaRequest.setPropriedade(new Propriedade(1L, "Propriedade Teste"));
        pessoaRequest.setLaboratorio(new Laboratorio(1L, "Laboratório Teste"));

        Pessoa pessoa = new Pessoa("João da Silva", pessoaRequest.getDataInicial(), pessoaRequest.getDataFinal(),
                pessoaRequest.getObservacoes(), pessoaRequest.getPropriedade(), pessoaRequest.getLaboratorio());

        when(pessoaAdapter.toEntity(pessoaRequest)).thenReturn(pessoa);
        when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(pessoaRequest.getPropriedade()));
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(pessoaRequest.getLaboratorio()));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa resultado = pessoaService.salvar(pessoaRequest);

        assertNotNull(resultado);
        assertEquals("João da Silva", resultado.getNome());
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("salvar: Deve retornar erro quando campos obrigatórios estão ausentes")
    void deveRetornarErroQuandoCamposObrigatoriosEstaoAusentes() {
        PessoaRequest pessoaRequest = new PessoaRequest();

        Set<ConstraintViolation<PessoaRequest>> violations = validator.validate(pessoaRequest);

        assertFalse(violations.isEmpty());
        assertEquals(5, violations.size());

        Map<String, String> errorMessages = new HashMap<>();
        for (ConstraintViolation<PessoaRequest> violation : violations) {
            errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        assertTrue(errorMessages.containsKey("nome"));
        assertEquals("O nome do cadastro é obrigatório", errorMessages.get("nome"));
        assertTrue(errorMessages.containsKey("dataInicial"));
        assertEquals("A data inicial do cadastro é obrigatória", errorMessages.get("dataInicial"));
        assertTrue(errorMessages.containsKey("dataFinal"));
        assertEquals("A data final do cadastro é obrigatória", errorMessages.get("dataFinal"));
        assertTrue(errorMessages.containsKey("propriedade"));
        assertEquals("Você deve selecionar uma propriedade!", errorMessages.get("propriedade"));
    }

    @Test
    @DisplayName("salvar: Deve retornar erro quando a propriedade não existe")
    void deveRetornarErroQuandoPropriedadeNaoExiste() {
        PessoaRequest pessoaRequest = new PessoaRequest();
        pessoaRequest.setNome("Teste");
        pessoaRequest.setDataInicial(LocalDateTime.now());
        pessoaRequest.setDataFinal(LocalDateTime.now().plusDays(1));
        pessoaRequest.setObservacoes("Teste de erro");
        pessoaRequest.setPropriedade(new Propriedade(99L, "Propriedade Inexistente")); // Setando um ID inválido

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaRequest.getNome());
        pessoa.setDataInicial(pessoaRequest.getDataInicial());
        pessoa.setDataFinal(pessoaRequest.getDataFinal());
        pessoa.setObservacoes(pessoaRequest.getObservacoes());
        pessoa.setPropriedade(pessoaRequest.getPropriedade());

        when(pessoaAdapter.toEntity(pessoaRequest)).thenReturn(pessoa);
        when(propriedadeRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.salvar(pessoaRequest);
        });

        assertEquals("Propriedade com ID 99 não encontrada", exception.getMessage());
    }


    @Test
    @DisplayName("salvar: Deve retornar erro quando o laboratório não existe")
    void deveRetornarErroQuandoLaboratorioNaoExiste() {
        PessoaRequest pessoaRequest = new PessoaRequest();
        pessoaRequest.setNome("Teste");
        pessoaRequest.setDataInicial(LocalDateTime.now());
        pessoaRequest.setDataFinal(LocalDateTime.now().plusDays(1));
        pessoaRequest.setObservacoes("Teste de erro");
        pessoaRequest.setLaboratorio(new Laboratorio(99L, "Laboratório Inexistente"));

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaRequest.getNome());
        pessoa.setDataInicial(pessoaRequest.getDataInicial());
        pessoa.setDataFinal(pessoaRequest.getDataFinal());
        pessoa.setObservacoes(pessoaRequest.getObservacoes());
        pessoa.setLaboratorio(pessoaRequest.getLaboratorio());

        when(pessoaAdapter.toEntity(pessoaRequest)).thenReturn(pessoa);
        when(laboratorioRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.salvar(pessoaRequest);
        });

        assertEquals("Laboratorio com ID 99 não encontrado", exception.getMessage());
    }


    @Test
    @DisplayName("buscarPorId: Deve retornar a Pessoa quando encontrada")
    void deveBuscarPessoaPorIdComSucesso() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Teste");

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        Pessoa resultado = pessoaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pessoaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId: Deve retornar erro quando Pessoa não for encontrada")
    void deveRetornarErroAoBuscarPessoaInexistente() {
        when(pessoaRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.buscarPorId(999L);
        });

        assertEquals("Pessoa não encontrada!", exception.getMessage());
    }

    @Test
    @DisplayName("editar: Deve atualizar a Pessoa sem modificar Propriedade e Laboratório se não forem informados")
    void deveEditarPessoaSemModificarPropriedadeOuLaboratorioSeNaoForemInformados() {
        PessoaRequest pessoaRequest = new PessoaRequest();
        pessoaRequest.setNome("Novo Nome");
        pessoaRequest.setDataInicial(LocalDateTime.now());
        pessoaRequest.setDataFinal(LocalDateTime.now().plusDays(1));
        pessoaRequest.setObservacoes("Nova observação");
        pessoaRequest.setPropriedade(null);
        pessoaRequest.setLaboratorio(null);

        Propriedade propriedadeExistente = new Propriedade(1L, "Propriedade Original");
        Laboratorio laboratorioExistente = new Laboratorio(2L, "Laboratório Original");

        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(1L);
        pessoaExistente.setNome("Nome Antigo");
        pessoaExistente.setDataInicial(LocalDateTime.now().minusDays(2));
        pessoaExistente.setDataFinal(LocalDateTime.now().minusDays(1));
        pessoaExistente.setObservacoes("Observação Antiga");
        pessoaExistente.setPropriedade(propriedadeExistente);
        pessoaExistente.setLaboratorio(laboratorioExistente);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pessoa resultado = pessoaService.editar(1L, pessoaRequest);

        assertEquals("Novo Nome", resultado.getNome());
        assertEquals("Nova observação", resultado.getObservacoes());
        assertEquals(propriedadeExistente, resultado.getPropriedade());
        assertEquals(laboratorioExistente, resultado.getLaboratorio());

        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }


    @Test
    @DisplayName("deletar: Deve excluir a Pessoa com sucesso")
    void deveDeletarPessoaComSucesso() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        doNothing().when(pessoaRepository).deleteById(1L);

        assertDoesNotThrow(() -> pessoaService.deletar(1L));

        verify(pessoaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deletar: Deve retornar erro quando Pessoa não for encontrada")
    void deveRetornarErroAoDeletarPessoaInexistente() {
        when(pessoaRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.deletar(999L);
        });

        assertEquals("Pessoa não encontrada!", exception.getMessage());
    }
}