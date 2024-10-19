package api.carometro.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comentario")
@Data
public class Comentario {
    @Id
    @Column(name = "pk_comentario", nullable = false)
    private int pk_comentario;

    @Column(name = "comentarioFatec", nullable = true)
    private String comentarioFatec;

    @Column(name = "comentarioLivre", nullable = true)
    private String comentarioLivre;


    @OneToOne(targetEntity = Aluno.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_aluno_comentario")
    private Aluno aluno;
}
