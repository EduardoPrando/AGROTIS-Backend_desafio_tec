package com.agrotis.backendtest.repository;

import com.agrotis.backendtest.dto.FiltroLaboratorios;
import com.agrotis.backendtest.dto.LaboratorioResumoDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RelatorioRepository {
    @PersistenceContext
    private EntityManager em;

    public List<LaboratorioResumoDTO> buscarLaboratorios(FiltroLaboratorios filtro) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT new com.agrotis.backendtest.dto.LaboratorioResumoDTO( ");
        sb.append("lab.id, UPPER(lab.nome), COUNT(p)) ");
        sb.append("FROM Laboratorio lab ");
        sb.append("LEFT JOIN Pessoa p ON p.laboratorio.id = lab.id ");
        sb.append("WHERE 1=1 ");

        if (filtro.getDataInicialInicio() != null && filtro.getDataInicialFim() != null) {
            sb.append("AND p.dataInicial BETWEEN :dataInicialInicio AND :dataInicialFim ");
        }
        if (filtro.getDataFinalInicio() != null && filtro.getDataFinalFim() != null) {
            sb.append("AND p.dataFinal BETWEEN :dataFinalInicio AND :dataFinalFim ");
        }

        if (filtro.getObservacoes() != null && !filtro.getObservacoes().isEmpty()) {
            sb.append("AND UPPER(p.observacoes) LIKE :obs ");
        }

        sb.append("GROUP BY lab.id, lab.nome ");
        sb.append("HAVING COUNT(p) >= :quantidadeMinima ");
        sb.append("ORDER BY COUNT(p) DESC");

        if (filtro.getDataInicialInicio() != null && filtro.getDataInicialFim() != null) {
            sb.append(", MIN(p.dataInicial) ASC");
        }

        TypedQuery<LaboratorioResumoDTO> query = em.createQuery(sb.toString(), LaboratorioResumoDTO.class);

        query.setParameter("quantidadeMinima", filtro.getQuantidadeMinima());
        if (filtro.getDataInicialInicio() != null && filtro.getDataInicialFim() != null) {
            query.setParameter("dataInicialInicio", filtro.getDataInicialInicio());
            query.setParameter("dataInicialFim", filtro.getDataInicialFim());
        }
        if (filtro.getDataFinalInicio() != null && filtro.getDataFinalFim() != null) {
            query.setParameter("dataFinalInicio", filtro.getDataFinalInicio());
            query.setParameter("dataFinalFim", filtro.getDataFinalFim());
        }

        if (filtro.getObservacoes() != null && !filtro.getObservacoes().isEmpty()) {
            query.setParameter("obs", "%" + filtro.getObservacoes().toUpperCase() + "%");
        }

        return query.getResultList();
    }
}
