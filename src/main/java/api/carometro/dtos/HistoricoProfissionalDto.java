package api.carometro.dtos;

import api.carometro.models.HistoricoProfissional;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class HistoricoProfissionalDto {
    @NotEmpty(message = "Nome da empresa não pode ser vazio")
    @NotBlank(message = "Nome da empresa não pode conter apenas espaço")
    @Size(min = 3, max = 100, message = "Nome da empresa deve ter entre 3 a 100 caracteres")
    private String onde;

    @NotEmpty(message = "Cargo não pode ser vazio")
    @NotBlank(message = "Cargo não pode conter apenas espaço")
    @Size(min = 3, max = 100, message = "Cargo deve ter entre 3 a 100 caracteres")
    private String cargo;

    @NotNull(message = "Data de início não pode ser nula")
    @Past(message = "Data de início deve estar no passado")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate inicio;

    @NotNull(message = "Data do fim não pode ser nula")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fim;

    public HistoricoProfissional dtoParaHistoricoProfissional() {
        HistoricoProfissional historicoProfissional = new HistoricoProfissional();
        historicoProfissional.setOnde(this.getOnde());
        historicoProfissional.setCargo(this.getCargo());
        historicoProfissional.setInicio(this.getInicio());
        historicoProfissional.setFim(this.getFim());
        return historicoProfissional;
    }
}
