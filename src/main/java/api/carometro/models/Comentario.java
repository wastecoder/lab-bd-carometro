package api.carometro.models;

import api.carometro.enums.StatusComentario;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comentario",
        uniqueConstraints = @UniqueConstraint(name = "ComentariosUnicos",
                columnNames = {"comentario_fatec", "comentario_livre"}))
@Data
public class Comentario {
    @Id
    @Column(name = "pk_comentario", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long pk_comentario;

    @Column(name = "comentario_fatec", length = 500, nullable = true)
    private String comentarioFatec;

    @Column(name = "comentario_livre", length = 500, nullable = true)
    private String comentarioLivre;

    @Column(name = "status", length = 9, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusComentario status;


    @PrePersist
    @PreUpdate
    private void validarComentariosDiferentes() {
        if (comentarioFatec != null && comentarioFatec.equals(comentarioLivre)) {
            throw new IllegalArgumentException("comentarioFatec e comentarioLivre não podem ser iguais.");
        }
    }


    public Comentario(String comentarioFatec, String comentarioLivre) {
        this.comentarioFatec = comentarioFatec;
        this.comentarioLivre = comentarioLivre;
        this.status = StatusComentario.PENDENTE;
    }
}
