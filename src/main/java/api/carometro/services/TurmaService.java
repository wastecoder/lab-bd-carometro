package api.carometro.services;

import api.carometro.models.Curso;
import api.carometro.models.Turma;
import api.carometro.repositories.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TurmaService {
    @Autowired
    private final TurmaRepository repository;
    private final CursoService cursoService;

    public TurmaService(TurmaRepository repository, CursoService cursoService) {
        this.repository = repository;
        this.cursoService = cursoService;
    }


    public void salvarTurma(Turma turmaNova) {
        repository.save(turmaNova);
    }

    public List<Turma> todasTurmas() {
        return repository.findAll();
    }

    public Turma buscarTurmaId(Long id) {
        Optional<Turma> optional = repository.findById(id);
        return optional.orElse(null);
    }

    public boolean deletarTurmaId(Long id) {
        Turma retorno = this.buscarTurmaId(id);

        if (retorno != null) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public void atualizarTurma(Turma antiga, Turma nova) {
        antiga.setAno(nova.getAno());
        antiga.setSemestre(nova.getSemestre());
        antiga.setTurno(nova.getTurno());
        antiga.setCurso(nova.getCurso());

        repository.save(antiga);
    }

    public List<Turma> buscarTurmasPorIdCursoAndAnoTurma(Long idCurso, Integer anoTurma) {
        Curso cursoBuscado = cursoService.cursoId(idCurso);
        if (cursoBuscado == null) return null;
        //Talvez validar o ano da turma com os mesmos valores do método anosPossiveis

        return repository.findAllByCursoAndAno(cursoBuscado, anoTurma);
    }


    public List<Integer> anosPossiveis() {
        final int MINIMO = 2015;
        final int MAXIMO = Year.now().getValue();

        return IntStream.rangeClosed(MINIMO, MAXIMO)
                .mapToObj(i -> MAXIMO - (i - MINIMO)) //Decrescente
                .collect(Collectors.toList());
    }
}
