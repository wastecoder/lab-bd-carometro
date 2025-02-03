package api.carometro.services;

import api.carometro.enums.StatusComentario;
import api.carometro.models.Aluno;
import api.carometro.repositories.AlunoRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    @Value("${pasta.fotos.perfil}")
    private String PASTA_FOTO_PERFIL;

    @Autowired
    private final AlunoRepository repository;
    private final HistoricoProfissionalService profissaoService;

    public AlunoService(AlunoRepository repository, HistoricoProfissionalService profissaoService) {
        this.repository = repository;
        this.profissaoService = profissaoService;
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
        if (retorno == null) return false;

        this.removerFotoAoRemoverAluno(retorno);
        profissaoService.deletarProfissoesPorAluno(retorno);
        repository.deleteById(ra);
        return true;
    }

    public void atualizarAluno(Aluno antigo, Aluno novo) {
        antigo.setSenha(novo.getSenha());
        antigo.setNome(novo.getNome());
        antigo.setDataNascimento(novo.getDataNascimento());
        antigo.setTurma(novo.getTurma());

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

    public void atualizarFotoPerfil(Aluno alunoAtualizado, MultipartFile arquivoRecebido) {
        if (arquivoRecebido == null || arquivoRecebido.isEmpty()) {
            return;
        }

        try {
            Path PASTA_DESTINO = Paths.get(PASTA_FOTO_PERFIL);
            if (!Files.exists(PASTA_DESTINO))
                Files.createDirectories(PASTA_DESTINO);

            String NOME_FOTO = alunoAtualizado.getRa();
            String caminhoFoto = NOME_FOTO + ".jpg";

            Path destino = PASTA_DESTINO.resolve(caminhoFoto);
            Files.write(destino, arquivoRecebido.getBytes());

            alunoAtualizado.setFoto(caminhoFoto); //Atualiza esse atributo apenas para sabermos que ele possui uma foto associada
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem do aluno.", e);
        }
    }

    public void removerFotoAoRemoverAluno(Aluno alunoRemovido) {
        if (alunoRemovido.getFoto() == null || alunoRemovido.getFoto().isEmpty()) {
            return;
        }

        String caminhoFoto = PASTA_FOTO_PERFIL + alunoRemovido.getRa() + ".jpg";

        File arquivoFoto = new File(caminhoFoto);
        if (arquivoFoto.exists()) arquivoFoto.delete();
    }

    public List<Aluno> alunosSemComentario() {
        return repository.findByComentarioIsNotNull();
    }

    public List<Aluno> alunosComComentariosPendentes() {
        return repository.findByComentarioStatus(StatusComentario.PENDENTE);
    }
}
