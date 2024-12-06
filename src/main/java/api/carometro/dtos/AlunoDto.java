package api.carometro.dtos;

import api.carometro.models.Aluno;
import api.carometro.models.Comentario;
import api.carometro.models.Turma;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AlunoDto {
    @NotEmpty(message = "Senha não pode ser vazia")
    @NotBlank(message = "Senha não deve ter apenas espaço")
    @Size(min = 5, max = 50, message = "Senha deve ter entre 5 e 100 caracteres")
    private String senha;

    @NotEmpty(message = "Nome não pode ser vazio")
    @NotBlank(message = "Nome não pode conter apenas espaço")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 a 100 caracteres")
    private String nome;

    @NotNull(message = "Data de nascimento não pode ser nula")
    @Past(message = "Data de nascimento deve estar no passado")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @NotNull(message = "Turma não pode ser nula")
    private Turma turma;


    @Size(max = 100, message = "Foto não pode ter mais de 100 caracteres")
    private String foto;

    @Size(max = 100, message = "URL do LinkedIn não pode ter mais de 100 caracteres")
    @Pattern(
            regexp = "(^$|^https:\\/\\/.*linkedin\\.com\\/.*$)",
            message = "URL do LinkedIn deve começar com 'https://linkedin.com/'"
    )
    private String urlLinkedin;

    @Size(max = 100, message = "URL do GitHub não pode ter mais de 100 caracteres")
    @Pattern(
            regexp = "(^$|^https:\\/\\/.*github\\.com\\/.*$)",
            message = "URL do GitHub deve começar com 'https://github.com/'"
    )
    private String urlGithub;

    @Size(max = 100, message = "URL do Lattes não pode ter mais de 100 caracteres")
    @Pattern(
            regexp = "(^$|^https:\\/\\/.*\\.com\\/.*$)",
            message = "URL do Lattes deve começar com 'https://'")
    private String urlLattes;

    private Comentario comentario;


    public Aluno dtoParaAluno() {
        Aluno aluno = new Aluno();

        aluno.setSenha(this.getSenha());
        aluno.setNome(this.getNome());
        aluno.setDataNascimento(this.getDataNascimento());
        aluno.setTurma(this.getTurma());

        aluno.setFoto(this.getFoto());
        aluno.setUrlLinkedin(this.getUrlLinkedin());
        aluno.setUrlGithub(this.getUrlGithub());
        aluno.setUrlLattes(this.getUrlLattes());
        aluno.setComentario(this.getComentario());

        return aluno;
    }
}
