package api.carometro.repositories;

import api.carometro.enums.StatusComentario;
import api.carometro.models.Aluno;
import api.carometro.models.Curso;
import api.carometro.models.Turma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, String> {
    Page<Aluno> findByNomeContainingOrderByNomeAsc(String nome, Pageable pageable);
    Page<Aluno> findAllByTurmaCurso(Curso curso, Pageable pageable);
    Page<Aluno> findAllByTurma(Turma turma, Pageable pageable);

    List<Aluno> findByComentarioIsNotNull();
}
