package api.carometro.models;

import api.carometro.enums.CargoAdm;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "administrador")
@Data
@NoArgsConstructor
public class Administrador {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "senha", length = 100, nullable = false)
    private String senha;

    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Column(name = "cargo", length = 11, nullable = false)
    @Enumerated(EnumType.STRING)
    private CargoAdm cargo;
}
