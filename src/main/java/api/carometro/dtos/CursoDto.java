package api.carometro.dtos;

import api.carometro.enums.ModalidadeCurso;
import api.carometro.enums.TipoCurso;
import api.carometro.models.Curso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class CursoDto {
    @NotEmpty(message = "Nome não pode ser vazio")
    @NotBlank(message = "Nome não pode conter apenas espaço")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 a 100 caracteres")
    private String nome;

    @NotNull(message = "Tipo não pode ser nulo")
    private TipoCurso tipo;

    @NotNull(message = "Modalidade não pode ser nula")
    private ModalidadeCurso modalidade;

    public Curso converterParaCurso() {
        return new Curso(this.nome, this.tipo, this.modalidade);
    }
}
