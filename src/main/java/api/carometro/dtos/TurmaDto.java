package api.carometro.dtos;

import api.carometro.enums.SemestreTurma;
import api.carometro.enums.TurnoTurma;
import api.carometro.models.Curso;
import api.carometro.models.Turma;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TurmaDto {
    @Positive(message = "Ano deve ser positivo")
    @Max(value = 2099) //Daria para validar melhor com @PrePersist/Update
    private Integer ano;

    @NotNull(message = "Turno selecionado inválido")
    private SemestreTurma semestre;

    @NotNull(message = "Turno selecionado inválido")
    private TurnoTurma turno;

    @NotNull(message = "Curso selecionado inválido")
    private Curso curso;

    public Turma dtoParaTurma() {
        Turma turma = new Turma();
        turma.setAno(this.getAno());
        turma.setSemestre(this.getSemestre());
        turma.setTurno(this.getTurno());
        turma.setCurso(this.getCurso());
        return turma;
    }
}
