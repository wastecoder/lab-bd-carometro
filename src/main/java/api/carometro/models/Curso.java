package api.carometro.models;

import api.carometro.enums.ModalidadeCurso;
import api.carometro.enums.TipoCurso;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "curso")
@Data
public class Curso {
    @Id
    @Column(name = "pk_curso", nullable = false)
    private Long pk_curso;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "tipo", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoCurso tipo;

    @Column(name = "modalidade", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private ModalidadeCurso modalidade;
}
