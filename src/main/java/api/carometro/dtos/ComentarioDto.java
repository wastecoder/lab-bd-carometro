package api.carometro.dtos;

import api.carometro.models.Comentario;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ComentarioDto {
    @Size(max = 500, message = "Comentário sobre a Fatec deve ter no máximo 500 caracteres")
    private String comentarioFatec;

    @Size(max = 500, message = "Comentário livre deve ter no máximo 500 caracteres")
    private String comentarioLivre;

    public Comentario dtoParaComentario() {
        return new Comentario(this.comentarioFatec, this.comentarioLivre);
    }
}
