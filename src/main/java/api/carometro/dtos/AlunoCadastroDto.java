package api.carometro.dtos;

import api.carometro.models.Aluno;
import api.carometro.models.Turma;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AlunoCadastroDto {
    @NotEmpty(message = "RA não pode ser vazio")
    @NotBlank(message = "RA não pode conter apenas espaço")
    @Size(min = 3, max = 100, message = "RA deve ter entre 3 a 13 caracteres")
    private String ra;

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
    private LocalDate dataNascimento;

    @NotNull(message = "Turma não pode ser nula")
    private Turma turma;

    public Aluno dtoParaAluno() {
        Aluno aluno = new Aluno();
        aluno.setRa(this.getRa());
        aluno.setSenha(this.getSenha());
        aluno.setNome(this.getNome());
        aluno.setDataNascimento(this.getDataNascimento());
        aluno.setTurma(this.getTurma());
        return aluno;
    }
}
