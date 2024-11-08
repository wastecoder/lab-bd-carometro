package api.carometro.controllers;

import api.carometro.dtos.CursoDto;
import api.carometro.enums.ModalidadeCurso;
import api.carometro.enums.TipoCurso;
import api.carometro.models.Curso;
import api.carometro.services.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;


    @GetMapping
    public ModelAndView mudarPagina(@RequestParam(name = "pagina", defaultValue = "0") int paginaSelecionada) {
        Iterable<Curso> cursos = cursoService.todosCursos();

        ModelAndView mv = new ModelAndView("curso/CursoHome");
        mv.addObject("cursos", cursos);
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrarCurso(CursoDto requisicao) {
        return criarModelAndViewParaCadastro();
    }

    @PostMapping("/cadastrar")
    public ModelAndView salvarCadastroCurso(@Valid CursoDto requisicao, BindingResult resultadoValidacao, RedirectAttributes attributes) {
        if (resultadoValidacao.hasErrors()) {
            return criarModelAndViewParaCadastro();
        }

        try {
            Curso curso = requisicao.converterParaCurso();
            cursoService.salvarCurso(curso);
            return new ModelAndView("redirect:/cursos");
        } catch (DataIntegrityViolationException exception) {
            ModelAndView mv = criarModelAndViewParaCadastro();
            mv.addObject("erroUniqueConstraint", "Curso com mesmo nome, tipo e modalidade já cadastrado.");
            return mv;
        }
    }

    private ModelAndView criarModelAndViewParaCadastro() {
        ModelAndView mv = new ModelAndView("curso/CursoCadastrar");
        mv.addObject("tipos", TipoCurso.values());
        mv.addObject("modalidades", ModalidadeCurso.values());
        return mv;
    }
}
