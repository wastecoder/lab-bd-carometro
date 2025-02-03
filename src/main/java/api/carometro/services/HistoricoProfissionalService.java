package api.carometro.services;

import api.carometro.models.Aluno;
import api.carometro.models.HistoricoProfissional;
import api.carometro.repositories.HistoricoProfissionalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoProfissionalService {
    @Autowired
    private final HistoricoProfissionalRepository repository;

    public HistoricoProfissionalService(HistoricoProfissionalRepository repository) {
        this.repository = repository;
    }


    public void salvarProfissao(HistoricoProfissional profissaoNova, Aluno alunoProfissao) {
        if (profissaoNova == null || alunoProfissao == null) {
            throw new IllegalArgumentException("Dados inválidos: profissão ou aluno não podem ser nulos");
        }
        profissaoNova.setAluno(alunoProfissao);
        repository.save(profissaoNova);
    }

    public List<HistoricoProfissional> todasProfissoes() {
        return repository.findAll();
    }

    public HistoricoProfissional buscarProfissaoId(Long id) {
        return repository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Histórico profissional com ID " + id + " não encontrado")
                );
    }

    public void deletarProfissaoId(Long id) {
        repository.deleteById(id);
    }

    public void atualizarProfissao(HistoricoProfissional antigo, HistoricoProfissional novo) {
        antigo.setOnde(novo.getOnde());
        antigo.setCargo(novo.getCargo());
        antigo.setInicio(novo.getInicio());
        antigo.setFim(novo.getFim());

        repository.save(antigo);
    }

    public List<HistoricoProfissional> buscarProfissoesPorAluno(Aluno aluno) {
        return repository.findAllByAlunoOrderByInicioAsc(aluno);
    }

    public void deletarProfissoesPorAluno(Aluno aluno) {
        repository.deleteByAluno(aluno);
    }
}
