package api.carometro.services;

import api.carometro.models.Administrador;
import api.carometro.repositories.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {
    @Autowired
    private final AdministradorRepository repository;

    public AdministradorService(AdministradorRepository repository) {
        this.repository = repository;
    }


    public void salvarAdministrador(Administrador administradorNovo) {
        repository.save(administradorNovo);
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
        antigo.setSenha(novo.getSenha());
        antigo.setNome(novo.getNome());
        antigo.setCargo(novo.getCargo());

        repository.save(antigo);
    }
}
