package api.carometro.services;

import api.carometro.models.Administrador;
import api.carometro.repositories.AdministradorRepository;
import api.carometro.security.AdminUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AdminUserDetailsService implements UserDetailsService {

    private final AdministradorRepository administradorRepository;

    public AdminUserDetailsService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Administrador administrador = administradorRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador não encontrado"));
        return new AdminUserDetails(administrador);
    }
}
