package api.carometro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String exibirFormularioLogin() {
        return "autenticacao/Login";
    }

    @GetMapping("/login-error")
    public ModelAndView exibirFormularioLoginComErro() {
        ModelAndView mv = new ModelAndView("autenticacao/Login");
        mv.addObject("loginErrado", true);
        return mv;
    }
}
