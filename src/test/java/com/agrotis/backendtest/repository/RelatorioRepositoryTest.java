package com.agrotis.backendtest.repository;

import com.agrotis.backendtest.dto.FiltroLaboratorios;
import com.agrotis.backendtest.dto.LaboratorioResumoDTO;
import com.agrotis.backendtest.model.Laboratorio;
import com.agrotis.backendtest.model.Pessoa;
import com.agrotis.backendtest.model.Propriedade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@Import(RelatorioRepository.class) // Importa explicitamente o seu repositório
public class RelatorioRepositoryTest {

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve retornar resultados ordenados por quantidade de pessoas (DESC) e, se informado, por MIN(dataInicial) ASC")
    void testBuscarLaboratoriosComFiltrosEOrdenacao() {
        Propriedade pro = new Propriedade();
        pro.setNome("Propriedade 1");
        pro.setCnpj("12.345.678-9999-00");
        entityManager.persist(pro);

        Laboratorio lab1 = new Laboratorio();
        lab1.setNome("Lab Alpha");
        entityManager.persist(lab1);

        Laboratorio lab2 = new Laboratorio();
        lab2.setNome("Lab Beta");
        entityManager.persist(lab2);

        Pessoa p1 = new Pessoa();
        p1.setNome("Pessoa A1");
        p1.setDataInicial(LocalDateTime.of(2025, 2, 1, 8, 0));
        p1.setDataFinal(LocalDateTime.of(2025, 2, 5, 10, 0));
        p1.setObservacoes("Observação qualquer");
        p1.setLaboratorio(lab1);
        p1.setPropriedade(pro);
        entityManager.persist(p1);

        Pessoa p2 = new Pessoa();
        p2.setNome("Pessoa A2");
        p2.setDataInicial(LocalDateTime.of(2025, 2, 3, 9, 0));
        p2.setDataFinal(LocalDateTime.of(2025, 2, 6, 10, 0));
        p2.setObservacoes("Observação qualquer");
        p2.setLaboratorio(lab1);
        p2.setPropriedade(pro);
        entityManager.persist(p2);

        Pessoa p3 = new Pessoa();
        p3.setNome("Pessoa A3");
        p3.setDataInicial(LocalDateTime.of(2025, 2, 2, 10, 0));
        p3.setDataFinal(LocalDateTime.of(2025, 2, 7, 10, 0));
        p3.setObservacoes("Observação qualquer");
        p3.setLaboratorio(lab1);
        p3.setPropriedade(pro);
        entityManager.persist(p3);

        // Persiste pessoas associadas a lab2: 2 pessoas
        Pessoa p4 = new Pessoa();
        p4.setNome("Pessoa B1");
        p4.setDataInicial(LocalDateTime.of(2025, 2, 5, 8, 0));
        p4.setDataFinal(LocalDateTime.of(2025, 2, 8, 10, 0));
        p4.setObservacoes("Observação qualquer");
        p4.setLaboratorio(lab2);
        p4.setPropriedade(pro);
        entityManager.persist(p4);

        Pessoa p5 = new Pessoa();
        p5.setNome("Pessoa B2");
        p5.setDataInicial(LocalDateTime.of(2025, 2, 4, 9, 0));
        p5.setDataFinal(LocalDateTime.of(2025, 2, 9, 10, 0));
        p5.setObservacoes("Observação qualquer");
        p5.setLaboratorio(lab2);
        p5.setPropriedade(pro);
        entityManager.persist(p5);

        entityManager.flush();

        // Configura o filtro
        FiltroLaboratorios filtro = new FiltroLaboratorios();
        filtro.setQuantidadeMinima(1L);
        filtro.setDataInicialInicio(LocalDateTime.of(2025, 2, 1, 0, 0));
        filtro.setDataInicialFim(LocalDateTime.of(2025, 2, 6, 0, 0));
        // Sem filtro de dataFinal nem observações neste teste

        List<LaboratorioResumoDTO> resultados = relatorioRepository.buscarLaboratorios(filtro);

        assertNotNull(resultados, "A lista de resultados não deve ser nula");
        assertEquals(2, resultados.size(), "Deve haver 2 laboratórios retornados");

        // Como a ordenação é: COUNT(p) DESC, e se dataInicial estiver filtrada, MIN(p.dataInicial) ASC,
        // lab1 (3 pessoas, MIN(p.dataInicial)=2025-02-01 08:00) deve vir antes de lab2 (2 pessoas, MIN(p.dataInicial)=2025-02-04 09:00).
        LaboratorioResumoDTO primeiro = resultados.get(0);
        LaboratorioResumoDTO segundo = resultados.get(1);

        assertEquals(lab1.getId(), primeiro.getCodigoLaboratorio(), "Lab1 deve estar na primeira posição por ter mais pessoas");
        assertEquals(3L, primeiro.getQuantidadePessoas(), "Lab1 deve ter 3 pessoas");
        assertEquals(lab2.getId(), segundo.getCodigoLaboratorio(), "Lab2 deve estar na segunda posição");
        assertEquals(2L, segundo.getQuantidadePessoas(), "Lab2 deve ter 2 pessoas");
    }

    @Test
    @DisplayName("Deve aplicar corretamente o filtro de observações (case-insensitive)")
    void testBuscarLaboratorios_FiltroObservacoes() {
        Propriedade pro = new Propriedade();
        pro.setNome("Propriedade 1");
        pro.setCnpj("12.345.678-9999-00");
        entityManager.persist(pro);

        Laboratorio lab = new Laboratorio();
        lab.setNome("Lab Observacao");
        entityManager.persist(lab);

        Pessoa p1 = new Pessoa();
        p1.setNome("Pessoa Obs 1");
        p1.setDataInicial(LocalDateTime.of(2025, 3, 1, 9, 0));
        p1.setDataFinal(LocalDateTime.of(2025, 3, 5, 9, 0));
        p1.setObservacoes("Come BATATA frita");
        p1.setLaboratorio(lab);
        p1.setPropriedade(pro);
        entityManager.persist(p1);

        Pessoa p2 = new Pessoa();
        p2.setNome("Pessoa Obs 2");
        p2.setDataInicial(LocalDateTime.of(2025, 3, 2, 9, 0));
        p2.setDataFinal(LocalDateTime.of(2025, 3, 6, 9, 0));
        p2.setObservacoes("Gosta de arroz");
        p2.setLaboratorio(lab);
        p2.setPropriedade(pro);
        entityManager.persist(p2);

        entityManager.flush();

        FiltroLaboratorios filtro = new FiltroLaboratorios();
        filtro.setQuantidadeMinima(1L);
        filtro.setObservacoes("batata");

        List<LaboratorioResumoDTO> resultados = relatorioRepository.buscarLaboratorios(filtro);
        assertNotNull(resultados, "A lista de resultados não deve ser nula");
        assertEquals(1, resultados.size(), "Deve retornar 1 laboratório");
        assertEquals("LAB OBSERVACAO", resultados.get(0).getNomeLaboratorio(), "O nome do laboratório deve estar em maiúsculas");
    }
}