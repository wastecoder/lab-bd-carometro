package api.carometro.services;

import api.carometro.models.Aluno;
import api.carometro.repositories.AlunoRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    @Autowired
    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }


    public void salvarAluno(Aluno alunoNovo) {
        repository.save(alunoNovo);
    }

    public List<Aluno> todosAlunos() {
        return repository.findAll();
    }

    public Aluno buscarAlunoRa(String ra) {
        Optional<Aluno> optional = repository.findById(ra);
        return optional.orElse(null);
    }

    public boolean deletarAlunoRa(String ra) {
        Aluno retorno = this.buscarAlunoRa(ra);

        if (retorno != null) {
            repository.deleteById(ra);
            return true;
        }
        return false;
    }

    public void atualizarAluno(Aluno antigo, Aluno novo) {
        antigo.setSenha(novo.getSenha());
        antigo.setNome(novo.getNome());
        antigo.setDataNascimento(novo.getDataNascimento());
        antigo.setTurma(novo.getTurma());

        antigo.setFoto(novo.getFoto());
        antigo.setUrlLinkedin(novo.getUrlLinkedin());
        antigo.setUrlGithub(novo.getUrlGithub());
        antigo.setUrlLattes(novo.getUrlLattes());
        antigo.setComentario(novo.getComentario());

        repository.save(antigo);
    }


    public boolean raExistente(String ra) {
        return this.buscarAlunoRa(ra) != null;
    }

    public String converterParaJson(Aluno aluno) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Ignorar campos nulos
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS); // Evitar erros em objetos vazios
        objectMapper.findAndRegisterModules(); // Suporte para tipos como LocalDate
        try {
            return objectMapper.writeValueAsString(aluno);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter aluno para JSON", e);
        }
    }
}
