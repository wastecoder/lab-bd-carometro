package api.carometro.configs;

import api.carometro.enums.CargoAdm;
import api.carometro.models.Administrador;
import api.carometro.repositories.AdministradorRepository;
import api.carometro.services.AdminUserDetailsService;
import jakarta.servlet.RequestDispatcher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // TODOS: arquivos estáticos
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // TODOS: home, perfis e pesquisar alunos
                        .requestMatchers("/", "/alunos", "/alunos/perfil/**", "/alunos/pesquisar", "/error").permitAll()

                        // ALUNO: logout e o próprio perfil
                        .requestMatchers("/sair").hasAnyRole("ALUNO", "ADMIN")

                        // ADM: todas URLs
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login-error")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 403);
            request.getRequestDispatcher("/error").forward(request, response);
        };
    }

    @Bean
    public CommandLineRunner criarAdminInicial(AdministradorRepository repo, PasswordEncoder encoder) {
        return args -> repo
                .findByEmail("admin@fatec.sp.gov.br")
                .orElseGet(() -> repo.save(new Administrador(
                        "admin@fatec.sp.gov.br",
                        encoder.encode("123456"),
                        "Administrador Inicial",
                        CargoAdm.COORDENADOR
                )));
    }

    @Bean
    public UserDetailsService userDetailsService(AdministradorRepository repo) {
        return new AdminUserDetailsService(repo);
    }
}
