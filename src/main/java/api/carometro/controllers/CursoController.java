package api.carometro.controllers;

import api.carometro.dtos.CursoDto;
import api.carometro.enums.ModalidadeCurso;
import api.carometro.enums.TipoCurso;
import api.carometro.models.Curso;
import api.carometro.services.CursoService;
import jakarta.transaction.Transactional;
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
        return criarModelAndViewParaFormulario("curso/CursoCadastrar");
    }

    @PostMapping("/cadastrar")
    @Transactional
    public ModelAndView salvarCadastroCurso(@Valid CursoDto requisicao, BindingResult resultadoValidacao, RedirectAttributes attributes) {
        if (resultadoValidacao.hasErrors()) {
            return criarModelAndViewParaFormulario("curso/CursoCadastrar");
        }

        try {
            Curso curso = requisicao.converterParaCurso();
            cursoService.salvarCurso(curso);
            return new ModelAndView("redirect:/cursos");
        } catch (DataIntegrityViolationException exception) {
            return lidarComDataIntegrityViolation("curso/CursoCadastrar");
        }
    }

    @GetMapping("/editar/{codigo}")
    public ModelAndView editarCurso(@PathVariable("codigo") Long codigo) {
        Curso cursoBuscado = cursoService.cursoId(codigo);

        if (cursoBuscado != null) {
            ModelAndView mv = criarModelAndViewParaFormulario("curso/CursoEditar");
            mv.addObject("cursoDto", cursoBuscado);
            return mv;
        } else {
            return new ModelAndView("redirect:/cursos");
        }
    }

    @PutMapping("/editar/{codigo}")
    @Transactional
    public ModelAndView salvarEdicaoCurso(@PathVariable("codigo") Long codigo, @Valid CursoDto requisicao, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mv = criarModelAndViewParaFormulario("curso/CursoEditar");
            mv.addObject("cursoDto", requisicao);
            return mv;
        }

        Curso cursoAntigo = cursoService.cursoId(codigo);
        try {
            cursoService.atualizarCurso(cursoAntigo, requisicao.converterParaCurso());
            return new ModelAndView("redirect:/cursos");
        } catch (DataIntegrityViolationException exception) {
            return lidarComDataIntegrityViolation("curso/CursoEditar");
        }
    }


    private ModelAndView criarModelAndViewParaFormulario(String arquivo) {
        ModelAndView mv = new ModelAndView(arquivo);
        mv.addObject("tipos", TipoCurso.values());
        mv.addObject("modalidades", ModalidadeCurso.values());
        return mv;
    }

    private ModelAndView lidarComDataIntegrityViolation(String arquivo) {
        ModelAndView mv = criarModelAndViewParaFormulario(arquivo);
        mv.addObject("erroUniqueConstraint", "Curso com mesmo nome, tipo e modalidade já cadastrado.");
        return mv;
    }
}
