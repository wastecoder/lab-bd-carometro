package api.carometro.models;

import api.carometro.enums.ModalidadeCurso;
import api.carometro.enums.TipoCurso;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curso",
        uniqueConstraints = @UniqueConstraint(name = "CursoUnico",
                columnNames = {"nome", "tipo", "modalidade"}))
@Data @NoArgsConstructor
public class Curso {
    @Id
    @Column(name = "pk_curso", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long pk_curso;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "tipo", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoCurso tipo;

    @Column(name = "modalidade", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private ModalidadeCurso modalidade;

    public Curso(String nome, TipoCurso tipo, ModalidadeCurso modalidade) {
        this.nome = nome;
        this.tipo = tipo;
        this.modalidade = modalidade;
    }
}
