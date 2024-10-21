package api.carometro.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "historico_profissional")
@Data //Talvez dê erro por usar em conjunto com o Fetch
public class HistoricoProfissional {
    @Id
    @Column(name = "pk_historico", nullable = false)
    private Long pk_historico;

    @Column(name = "onde", length = 100, nullable = false)
    private String onde;

    @Column(name = "cargo", length = 100, nullable = false)
    private String cargo;

    @Column(name = "inicio", length = 10, nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd") //TODO: aparentemente isso é opcional para converter para o padrão brasileiro, remover depois se for desnecessário
    private LocalDate inicio;

    @Column(name = "fim", length = 10, nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fim;


    @ManyToOne
    @JoinColumn(name = "fk_aluno_historico", nullable = false)
    private Aluno aluno;
}
