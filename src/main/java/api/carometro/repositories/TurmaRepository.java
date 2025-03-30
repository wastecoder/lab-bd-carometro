package api.carometro.repositories;

import api.carometro.models.Curso;
import api.carometro.models.Turma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    //Encontra todos os filhos (Turma) por Curso
    Page<Turma> findAllByCurso(Curso Curso, Pageable pageable);

    List<Turma> findAllByCursoAndAno(Curso curso, Integer ano);

    List<Turma> findAllByCurso(Curso curso);
}
