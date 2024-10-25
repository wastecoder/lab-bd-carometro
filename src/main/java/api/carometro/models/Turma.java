package api.carometro.models;

import api.carometro.enums.TurnoTurma;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "turma")
@Data
public class Turma {
    @Id
    @Column(name = "pk_turma", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long pk_turma;

    //20241 = 1º semestre de 2024; 20242 = 2º semestre de 2024
    //ano = anoSemestre / 10 (divisão inteira)
    //semestre = anoSemestre % 10 (resto da divisão)
    @Column(name = "ano_semestre", length = 4, nullable = false)
    private int anoSemestre;

    @Column(name = "turno", length = 10, nullable = false)
    @Enumerated(EnumType.STRING) //Salva como texto no BD
    private TurnoTurma turno;


    @ManyToOne
    @JoinColumn(name = "fk_curso_turma", nullable = false)
    private Curso curso;
}
