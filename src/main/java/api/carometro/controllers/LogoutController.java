package api.carometro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/sair")
    public String exibirFormularioLogout() {
        return "autenticacao/Logout";
    }

}
