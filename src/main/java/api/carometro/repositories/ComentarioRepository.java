package api.carometro.repositories;

import api.carometro.enums.StatusComentario;
import api.carometro.models.Comentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    Page<Comentario> findAllByStatus(StatusComentario statusComentario, Pageable pageable);
}
