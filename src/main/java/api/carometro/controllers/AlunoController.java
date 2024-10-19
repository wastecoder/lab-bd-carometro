package api.carometro.controllers;

import api.carometro.repositories.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/aluno")
public class AlunoController {
    @Autowired
    AlunoRepository alunoRepository;

    @GetMapping("/login")
    public ModelAndView exibirPaginaCadastro() {
        ModelAndView paginaCadastro = new ModelAndView("alunoCadastro");
        return paginaCadastro;
    }
}
