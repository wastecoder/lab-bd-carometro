package api.carometro.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "historicoprofissional")
@Data //Talvez dê erro por usar em conjunto com o Fetch
public class HistoricoProfissional {
    @Id
    @Column(name = "pk_historico", nullable = false)
    private int pk_historico;

    @Column(name = "ondeTrabalho", length = 100, nullable = true)
    private String ondeTrabalho;

    @Column(name = "oQueFaco", length = 100, nullable = true)
    private String oQueFaco;

    //Ver como fazer atributo derivado
    @Column(name = "quandoComecei", nullable = true)
    private Date quandoComecei;


    @OneToOne(targetEntity = Aluno.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_aluno_historico", nullable = false)
    private Aluno aluno;
}
