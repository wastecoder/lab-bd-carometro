package api.carometro.repositories;

import api.carometro.enums.StatusComentario;
import api.carometro.models.Aluno;
import api.carometro.models.Curso;
import api.carometro.models.Turma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, String> {
    Page<Aluno> findByNomeContainingOrderByNomeAsc(String nome, Pageable pageable);
    Page<Aluno> findAllByTurmaCurso(Curso curso, Pageable pageable);
    Page<Aluno> findAllByTurma(Turma turma, Pageable pageable);

    List<Aluno> findByComentarioIsNotNull();
    List<Aluno> findByComentarioStatus(StatusComentario status);
}
