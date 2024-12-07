package api.carometro.dtos;

import api.carometro.enums.CargoAdm;
import api.carometro.models.Administrador;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdministradorDto {
    @Email(message = "E-mail deve ter um formato válido")
    @NotEmpty(message = "E-mail não pode ser vazio")
    @NotBlank(message = "E-mail não deve ter apenas espaço")
    @Size(min = 5, max = 50, message = "E-mail deve ter entre 5 a 50 caracteres")
    private String email;

    @NotEmpty(message = "Senha não pode ser vazia")
    @NotBlank(message = "Senha não deve ter apenas espaço")
    @Size(min = 5, max = 100, message = "Senha deve ter entre 5 e 100 caracteres")
    private String senha;

    @NotEmpty(message = "Nome não pode ser vazio")
    @NotBlank(message = "Nome não pode conter apenas espaço")
    @Size(min = 3, max = 50, message = "Nome deve ter entre 3 a 50 caracteres")
    private String nome;

    @NotNull(message = "Cargo selecionado inválido")
    private CargoAdm cargo;

    public Administrador dtoParaAdministrador() {
        Administrador administrador = new Administrador();
        administrador.setEmail(this.getEmail());
        administrador.setSenha(this.getSenha());
        administrador.setNome(this.getNome());
        administrador.setCargo(this.getCargo());
        return administrador;
    }
}
