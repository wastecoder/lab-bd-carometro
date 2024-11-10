package api.carometro.services;

import api.carometro.models.Curso;
import api.carometro.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;


    public void salvarCurso(Curso curso) {
        cursoRepository.save(curso);
    }

    public Curso cursoId(Long id) {
        Optional<Curso> optional = cursoRepository.findById(id);
        return optional.orElse(null);
    }

    public Iterable<Curso> todosCursos() {
        return cursoRepository.findAll();
    }

    public boolean deletarCurso(Long id) {
        Curso retorno = this.cursoId(id);

        if (retorno != null) {
            cursoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void atualizarCurso(Curso antigo, Curso novo) {
        antigo.setNome(novo.getNome());
        antigo.setTipo(novo.getTipo());
        antigo.setModalidade(novo.getModalidade());

        cursoRepository.save(antigo);
    }

    public Pageable definirPageRequest(int numeroPagina, String ordem) {
        final int REGISTROS_POR_PAGINA = 10;
        Sort ordenacao = Sort.by("pk_curso");
        ordenacao = ordem.equals("cresc") ? ordenacao.ascending() : ordenacao.descending();

        return PageRequest.of(--numeroPagina, REGISTROS_POR_PAGINA, ordenacao);
    }
}
