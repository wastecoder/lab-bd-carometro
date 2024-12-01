package api.carometro.controllers;

import api.carometro.dtos.TurmaDto;
import api.carometro.enums.SemestreTurma;
import api.carometro.enums.TurnoTurma;
import api.carometro.models.Turma;
import api.carometro.services.CursoService;
import api.carometro.services.TurmaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/turmas")
public class TurmaController {
    @Autowired
    private final TurmaService turmaService;
    private final CursoService cursoService;

    public TurmaController(TurmaService turmaService, CursoService cursoService) {
        this.turmaService = turmaService;
        this.cursoService = cursoService;
    }


    @GetMapping
    public ModelAndView exibirPaginaInicial() {
        ModelAndView mv = new ModelAndView("/turma/TurmaHome");
        mv.addObject("turmas", turmaService.todasTurmas());
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView exibirFormularioCadastro(TurmaDto requisicao) {
        return criarViewParaFormulario("/turma/TurmaCadastrar");
    }

    @PostMapping("/cadastrar")
    //Se colocar @Transaction vai dar "AssertionFailure: null id" no Try Catch
    public ModelAndView salvarCadastroTurma(@Valid TurmaDto requisicao, BindingResult resultadoValidacao) {
        if (resultadoValidacao.hasErrors()) {
            return criarViewParaFormulario("/turma/TurmaCadastrar");
        }

        try {
            turmaService.salvarTurma(requisicao.dtoParaTurma());
            return new ModelAndView("redirect:/turmas");
        } catch (DataIntegrityViolationException exception) {
            ModelAndView mv = criarViewParaFormulario("/turma/TurmaCadastrar");
            mv.addObject("erroUniqueConstraint", "Turma com mesmo ano, semestre, turno e curso já cadastrada.");
            return mv;
        }
    }

    @GetMapping("/editar/{id}")
    public ModelAndView exibirFormularioEdicao(@PathVariable Long id) {
        Turma turmaBuscada = turmaService.buscarTurmaId(id);

        System.out.println(">>>>>>>>>");
        System.out.println(turmaBuscada);
        System.out.println(">>>>>>>>>");

        if (turmaBuscada != null) {
            ModelAndView mv = criarViewParaFormulario("/turma/TurmaEditar");
//            mv.addObject("turmaDto", new TurmaDto(2020, SemestreTurma.PRIMEIRO, TurnoTurma.NOTURNO, turmaBuscada.getCurso()));
            mv.addObject("turmaDto", turmaBuscada);
            return mv;
        }
        return new ModelAndView("redirect:/cursos");
    }

    @PutMapping("/editar/{id}")
    //@Transactional ignora a exceção DataIntegrity por ela ser checked
    public ModelAndView salvarEdicaoTurma(@PathVariable Long id, @Valid TurmaDto requisicao, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mv = criarViewParaFormulario("/turma/TurmaEditar");
            mv.addObject("turmaDto", requisicao);
        }

        Turma turmaAntiga = turmaService.buscarTurmaId(id);
        try {
            turmaService.atualizarTurma(turmaAntiga, requisicao.dtoParaTurma());
            return new ModelAndView("redirect:/turmas");
        } catch (DataIntegrityViolationException exception) {
            ModelAndView mv = criarViewParaFormulario("/turma/TurmaEditar");
            mv.addObject("erroUniqueConstraint", "Turma com mesmo ano, semestre, turno e curso já cadastrada.");
            return mv;
        }
    }

    @DeleteMapping("/excluir/{id}")
    @Transactional
    public String excluirTurma(@PathVariable Long id) {
        turmaService.deletarTurmaId(id);
        return "redirect:/turmas";
    }


    private ModelAndView criarViewParaFormulario(String view) {
        ModelAndView mv = new ModelAndView(view);
        mv.addObject("anos", turmaService.anosPossiveis());
        mv.addObject("semestres", SemestreTurma.values());
        mv.addObject("turnos", TurnoTurma.values());
        mv.addObject("cursos", cursoService.todosCursos());
        return mv;
    }
}
