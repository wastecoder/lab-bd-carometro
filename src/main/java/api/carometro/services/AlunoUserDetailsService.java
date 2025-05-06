package api.carometro.services;

import api.carometro.models.Aluno;
import api.carometro.repositories.AlunoRepository;
import api.carometro.security.AlunoUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AlunoUserDetailsService implements UserDetailsService {

    private final AlunoRepository alunoRepository;

    public AlunoUserDetailsService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String ra) throws UsernameNotFoundException {
        Aluno aluno = alunoRepository
                .findByRa(ra)
                .orElseThrow(() -> new UsernameNotFoundException("Aluno não encontrado"));
        return new AlunoUserDetails(aluno);
    }
}
