package api.carometro.repositories;

import api.carometro.models.Curso;
import api.carometro.models.Turma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    //Encontra todos os filhos (Turma) por Curso
    Page<Turma> findAllByCurso(Curso Curso, Pageable pageable);
}
