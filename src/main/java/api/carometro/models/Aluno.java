package api.carometro.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;

@Entity(name = "aluno")
@Table(name = "aluno")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "ra")
public class Aluno {
    @Id
    @Column(name = "aluno_ra", length = 13, nullable = false)
    private String ra;

    @Column(name = "senha", length = 100, nullable = false)
    private String senha;


    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "foto", length = 100)
    private String foto;

    @Column(name = "urlLinkedin", length = 100)
    private String urlLinkedin;

    @Column(name = "urlGithub", length = 100)
    private String urlGithub;

    @Column(name = "urlLattes", length = 100)
    private String urlLattes;
}
