package api.carometro.services;

import api.carometro.models.Administrador;
import api.carometro.repositories.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {
    @Autowired
    private final AdministradorRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AdministradorService(AdministradorRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    public void salvarAdministrador(Administrador admNovo) {
        admNovo.setSenha(passwordEncoder.encode(admNovo.getSenha()));
        repository.save(admNovo);
    }

    public List<Administrador> todosAdministradores() {
        return repository.findAll();
    }

    public Administrador buscarAdministradorId(Long id) {
        Optional<Administrador> optional = repository.findById(id);
        return optional.orElse(null);
    }

    public boolean deletarAdministradorId(Long id) {
        Administrador retorno = this.buscarAdministradorId(id);

        if (retorno != null) {
            repository.deleteById(id);
        }
        return false;
    }

    public void atualizarAdministrador(Administrador antigo, Administrador novo) {
        antigo.setEmail(novo.getEmail());
        antigo.setNome(novo.getNome());
        antigo.setCargo(novo.getCargo());

        String novaSenha = novo.getSenha();
        if (novaSenha != null && !novaSenha.startsWith("$2")) { //Para evitar criptografar 2x se não alterar a senha
            novaSenha = passwordEncoder.encode(novaSenha);
        }
        antigo.setSenha(novaSenha);

        repository.save(antigo);
    }
}
