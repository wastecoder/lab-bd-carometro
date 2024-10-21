package api.carometro.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comentario")
@Data
public class Comentario {
    @Id
    @Column(name = "pk_comentario", nullable = false)
    private Long pk_comentario;

    @Column(name = "comentario_fatec", length = 500, nullable = true)
    private String comentarioFatec;

    @Column(name = "comentario_livre", length = 500, nullable = true)
    private String comentarioLivre;
}
