package api.carometro.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "curso")
@Data
public class Curso {
    @Id
    @Column(name = "pk_curso", nullable = false)
    private int pk_curso;

    @Column(name = "nome", nullable = false)
    private String nome;

    //O que é o ano na imagem inicial?
//    @Column(name = "anoInicio", nullable = false)
//    private Date ano;


    @OneToOne(targetEntity = Aluno.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_aluno_curso", nullable = false)
    private Aluno aluno;
}
