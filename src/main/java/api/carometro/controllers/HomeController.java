package api.carometro.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    /*
    TODO: diferenciar se é ADM ou aluno
    ADM: redirecionar para /cursos
    Alunos: exibir N alunos, com suas fotos e cursos
     */
    @GetMapping
    public String exibirPaginaInicial() {
        return "redirect:/cursos";
    }
}
