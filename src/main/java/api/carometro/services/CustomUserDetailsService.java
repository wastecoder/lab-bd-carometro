package api.carometro.services;

import api.carometro.models.Administrador;
import api.carometro.models.Aluno;
import api.carometro.repositories.AdministradorRepository;
import api.carometro.repositories.AlunoRepository;
import api.carometro.security.AdminUserDetails;
import api.carometro.security.AlunoUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    private final AdministradorRepository adminRepository;
    private final AlunoRepository alunoRepository;

    public CustomUserDetailsService(AdministradorRepository adminRepository, AlunoRepository alunoRepository) {
        this.adminRepository = adminRepository;
        this.alunoRepository = alunoRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String login) {
        Administrador adm = adminRepository
                .findByEmail(login)
                .orElse(null);
        if (adm != null) return new AdminUserDetails(adm);

        Aluno aluno = alunoRepository
                .findByRa(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return new AlunoUserDetails(aluno);
    }
}
