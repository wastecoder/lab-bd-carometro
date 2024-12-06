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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cursos")
public class CursoController {
    @Autowired
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }


    @GetMapping
    public ModelAndView mudarPagina(@RequestParam(name = "pagina", defaultValue = "1") int paginaSelecionada) {
        Page<Curso> paginas = cursoService.definirPagePorPk(paginaSelecionada);

        int totalPaginas = paginas.getTotalPages();
        int inicioIntervalo, finalIntervalo;

        if (totalPaginas <= 5) {
            inicioIntervalo = 1;
            finalIntervalo = totalPaginas;
        } else if (paginaSelecionada <= 3) {
            inicioIntervalo = 1;
            finalIntervalo = 5;
        } else if (paginaSelecionada < totalPaginas - 2) {
            inicioIntervalo = paginaSelecionada - 2;
            finalIntervalo = paginaSelecionada + 2;
        } else {
            inicioIntervalo = totalPaginas - 4;
            finalIntervalo = totalPaginas;
        }

        String legenda = "Exibindo " + paginas.getNumberOfElements()
                + " de " + paginas.getTotalElements() + " registros";

        ModelAndView mv = new ModelAndView("curso/CursoHome");
        mv.addObject("paginaAtual", paginaSelecionada);
        mv.addObject("totalPaginas", totalPaginas);
        mv.addObject("inicio", inicioIntervalo);
        mv.addObject("fim", finalIntervalo);
        mv.addObject("legenda", legenda);
        mv.addObject("cursos", paginas);
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

    @DeleteMapping("/deletar/{codigo}")
    @Transactional
    public String deletarCurso(@PathVariable("codigo") Long codigo) {
        cursoService.deletarCurso(codigo);
        return "redirect:/cursos";
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
