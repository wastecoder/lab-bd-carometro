package api.carometro.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "historico_profissional",
        uniqueConstraints = @UniqueConstraint(name = "HistoricoProfissionalUnico",
                columnNames = {"onde", "cargo", "inicio", "fim", "fk_aluno_historico"}))
@Data
public class HistoricoProfissional {
    @Id
    @Column(name = "pk_historico", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long pk_historico;

    @Column(name = "onde", length = 100, nullable = false)
    private String onde;

    @Column(name = "cargo", length = 100, nullable = false)
    private String cargo;

    @Column(name = "inicio", length = 10, nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd") //TODO: aparentemente isso é opcional para converter para o padrão brasileiro, remover depois se for desnecessário
    private LocalDate inicio;

    //TODO: como saber quando ainda está empregado? Talvez deixar nulo, o que precisaria ser tratado no HTML
    @Column(name = "fim", length = 10, nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fim;


    @ManyToOne
    @JoinColumn(name = "fk_aluno_historico", nullable = false)
    private Aluno aluno;


    @PrePersist
    @PreUpdate
    private void validarData() {
        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException("A data de FIM deve ser MAIOR do que a data de INÍCIO.");
        } else if (inicio.equals(fim)) {
            throw new IllegalArgumentException("A data de INÍCIO deve ser DIFERENTE do que a data de FIM.");
        }
    }

    public String calcularPeriodoEmpregado() {
        if (inicio == null || fim == null) {
            return "Período indefinido";
        }

        Period periodo = Period.between(inicio, fim);

        if (periodo.getYears() > 0) {
            return periodo.getYears() + " ano(s)";
        } else if (periodo.getMonths() > 0) {
            return periodo.getMonths() + " mes(es)";
        } else {
            return periodo.getDays() + " dia(s)";
        }
    }

    public HistoricoProfissional(String onde, String cargo, LocalDate inicio, LocalDate fim, Aluno aluno) {
        this.onde = onde;
        this.cargo = cargo;
        this.inicio = inicio;
        this.fim = fim;
        this.aluno = aluno;
    }
}
