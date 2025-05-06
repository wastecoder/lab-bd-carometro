package api.carometro.models;

import jakarta.persistence.*;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity(name = "aluno")
@Table(name = "aluno")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aluno {
    @Id
    @Column(name = "aluno_ra", length = 13, nullable = false)
    private String ra;

    @Column(name = "senha", length = 100, nullable = false)
    private String senha;


    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "data_nascimento", length = 10, nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @Column(name = "foto", length = 100)
    private String foto;

    @Column(name = "urlLinkedin", length = 100)
    private String urlLinkedin;

    @Column(name = "urlGithub", length = 100)
    private String urlGithub;

    @Column(name = "urlLattes", length = 100)
    private String urlLattes;


    // orphanRemoval = ao remover o aluno, remove o comentário junto
    // CascadeType.ALL = ao manipular o aluno, manipula (CRUD) o comentário junto
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_comentario_aluno", nullable = true) //True porque pode não ter comentário: Aluno 1 --- 0..1
    private Comentario comentario;

    @ManyToOne
    @JoinColumn(name = "fk_turma_aluno", nullable = false)
    private Turma turma;
}
