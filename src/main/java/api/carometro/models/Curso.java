package api.carometro.models;

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
    private String tipo; //técnico, graduação, pós-graduação etc

    @Column(name = "modalidade", length = 10, nullable = false)
//    @Enumerated(EnumType.STRING) //TODO: ver como usar
    private String modalidade; //presencial, EAD ou hibrído
}
