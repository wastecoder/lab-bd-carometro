package api.carometro.models;

import api.carometro.enums.TurnoTurma;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "turma",
        uniqueConstraints = @UniqueConstraint(name = "TurmaUnica",
                columnNames = {"anoSemestre", "turno", "fk_curso_turma"}))
@Data
public class Turma {
    @Id
    @Column(name = "pk_turma", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long pk_turma;

    //20241 = 1º semestre de 2024; 20242 = 2º semestre de 2024
    //ano = anoSemestre / 10 (divisão inteira)
    //semestre = anoSemestre % 10 (resto da divisão)
    //TODO: criar e testar uma nova anotação para validar a regra acima com ConstraintValidator
    @Column(name = "ano_semestre", length = 5, nullable = false)
    private int anoSemestre;

    @Column(name = "turno", length = 10, nullable = false)
    @Enumerated(EnumType.STRING) //Salva como texto no BD
    private TurnoTurma turno;


    @ManyToOne
    @JoinColumn(name = "fk_curso_turma", nullable = false)
    private Curso curso;

    public Turma(int anoSemestre, TurnoTurma turno, Curso curso) {
        this.anoSemestre = anoSemestre;
        this.turno = turno;
        this.curso = curso;
    }
}
