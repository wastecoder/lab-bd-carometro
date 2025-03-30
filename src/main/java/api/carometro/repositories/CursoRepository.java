package api.carometro.repositories;

import api.carometro.models.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    Page<Curso> findAllByOrderByNomeAsc(Pageable pageable);
    Page<Curso> findAllByOrderByNomeDesc(Pageable pageable);
}
