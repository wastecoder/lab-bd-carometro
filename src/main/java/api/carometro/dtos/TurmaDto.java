package api.carometro.dtos;

import api.carometro.enums.SemestreTurma;
import api.carometro.enums.TurnoTurma;
import api.carometro.models.Curso;
import api.carometro.models.Turma;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TurmaDto {
    @Positive(message = "Ano deve ser positivo")
    @Min(value = 2009, message = "Ano deve ser de 2009 ou mais recente")
    @Max(value = 2030, message = "Ano deve ser de 2030 ou mais antigo") //Daria para validar melhor com @PrePersist/Update, pegando ano atual
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
