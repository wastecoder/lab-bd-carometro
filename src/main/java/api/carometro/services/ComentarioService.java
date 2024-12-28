package api.carometro.services;

import api.carometro.enums.StatusComentario;
import api.carometro.models.Comentario;
import api.carometro.repositories.ComentarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {
    @Autowired
    private final ComentarioRepository repository;

    public ComentarioService(ComentarioRepository repository) {
        this.repository = repository;
    }


    public Comentario buscarComentarioId(Long id) {
        return repository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Comentário com ID " + id + " não encontrado"));
    }

    public void aprovarComentarioId(Long id) {
        Comentario comentarioRetornando = buscarComentarioId(id);
        verificarStatusPendente(comentarioRetornando, "aprovados");

        comentarioRetornando.setStatus(StatusComentario.APROVADO);
        repository.save(comentarioRetornando);
    }

    public void rejeitarComentario(Long id) {
        Comentario comentarioRetornado = buscarComentarioId(id);
        verificarStatusPendente(comentarioRetornado, "rejeitados");

        comentarioRetornado.setStatus(StatusComentario.REJEITADO);
        repository.save(comentarioRetornado);
    }


    private void verificarStatusPendente(Comentario comentario, String acao) {
        if (comentario.getStatus() != StatusComentario.PENDENTE) {
            throw new IllegalStateException("Somente comentários pendentes podem ser " + acao);
        }
    }
}
