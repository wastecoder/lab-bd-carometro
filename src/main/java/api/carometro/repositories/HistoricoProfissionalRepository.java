package api.carometro.repositories;

import api.carometro.models.Aluno;
import api.carometro.models.HistoricoProfissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoProfissionalRepository extends JpaRepository<HistoricoProfissional, Long> {
    //Para mostrar a ordem que o aluno trabalhou - inicio >>> fim
    List<HistoricoProfissional> findAllByAlunoOrderByInicioAsc(Aluno aluno);

    //Para mostrar os empregos mais recentes do aluno - fim >>> inicio
    List<HistoricoProfissional> findAllByAlunoOrderByFimDesc(Aluno aluno);

    void deleteByAluno(Aluno aluno);

    List<HistoricoProfissional> findByAluno_NomeContainingIgnoreCaseOrderByInicioAsc(String nome);
    List<HistoricoProfissional> findByAluno_RaOrderByInicioAsc(String ra);
}
