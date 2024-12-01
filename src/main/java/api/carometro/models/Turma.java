package api.carometro.models;

import api.carometro.enums.SemestreTurma;
import api.carometro.enums.TurnoTurma;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "turma",
        uniqueConstraints = @UniqueConstraint(name = "TurmaUnica",
                columnNames = {"ano", "semestre", "turno", "fk_curso_turma"}))
@Data
@NoArgsConstructor
public class Turma {
    @Id
    @Column(name = "pk_turma", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long pk_turma;

    @Column(name = "ano", length = 4, nullable = false)
    private int ano;

    @Column(name = "semestre", length = 8, nullable = false)
    @Enumerated(EnumType.STRING)
    private SemestreTurma semestre;

    @Column(name = "turno", length = 10, nullable = false)
    @Enumerated(EnumType.STRING) //Salva como texto no BD
    private TurnoTurma turno;


    @ManyToOne
    @JoinColumn(name = "fk_curso_turma", nullable = false)
    private Curso curso;

    public Turma(int ano, SemestreTurma semestre, TurnoTurma turno, Curso curso) {
        this.ano = ano;
        this.semestre = semestre;
        this.turno = turno;
        this.curso = curso;
    }
}
