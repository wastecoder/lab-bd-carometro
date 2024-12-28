package api.carometro.controllers;

import api.carometro.dtos.ComentarioDto;
import api.carometro.models.Aluno;
import api.carometro.services.AlunoService;
import api.carometro.services.ComentarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {
    @Autowired
    private final AlunoService alunoService;
    private final ComentarioService comentarioService;

    public ComentarioController(AlunoService alunoService, ComentarioService comentarioService) {
        this.alunoService = alunoService;
        this.comentarioService = comentarioService;
    }


    @GetMapping
    public ModelAndView exibirPaginaInicial() {
        ModelAndView mv = new ModelAndView("/comentario/ComentarioHome");
        mv.addObject("alunos", alunoService.alunosSemComentario());
        return mv;
    }

    @GetMapping("/criar/{ra}")
    public ModelAndView exibirFormularioCadastro(@PathVariable String ra) {
        Aluno alunoBuscado = alunoService.buscarAlunoRa(ra);
        if (alunoBuscado == null || alunoBuscado.getComentario() != null)
            return new ModelAndView("redirect:/comentarios");

        return criarViewParaFormulario("/comentario/ComentarioCadastrar", new ComentarioDto());
    }

    @PostMapping("/criar/{ra}")
    @Transactional
    public ModelAndView salvarCadastroComentario(@Valid ComentarioDto requisicao, @PathVariable String ra, BindingResult resultadoValidacao) {
        Aluno alunoBuscado = alunoService.buscarAlunoRa(ra);
        if (alunoBuscado == null)
            return new ModelAndView("redirect:/comentarios");

        if (resultadoValidacao.hasErrors()) {
            return criarViewParaFormulario("/comentario/ComentarioCadastrar", requisicao);
        }

        //TODO: tratar DataIntegrityViolationException (UniqueConstraint) e IllegalArgumentException
        alunoBuscado.setComentario(requisicao.dtoParaComentario());
        alunoService.salvarAluno(alunoBuscado);

        return new ModelAndView("redirect:/comentarios");
    }

    @GetMapping("/editar/{ra}")
    public ModelAndView exibirFormularioEdicao(@PathVariable String ra) {
        Aluno alunoBuscado = alunoService.buscarAlunoRa(ra);
        if (alunoBuscado == null || alunoBuscado.getComentario() == null)
            return new ModelAndView("redirect:/comentarios");

        return criarViewParaFormulario("/comentario/ComentarioEditar", alunoBuscado.getComentario());
    }

    @PutMapping("/editar/{ra}")
    @Transactional
    public ModelAndView salvarEdicaoComentario(@PathVariable String ra, @Valid ComentarioDto requisicao, BindingResult resultadoValidacao) {
        Aluno alunoBuscado = alunoService.buscarAlunoRa(ra);
        if (alunoBuscado == null)
            return new ModelAndView("redirect:/comentarios");

        if (resultadoValidacao.hasErrors()) {
            return criarViewParaFormulario("/comentario/ComentarioEditar", requisicao);
        }

        //TODO: tratar DataIntegrityViolationException (UniqueConstraint) e IllegalArgumentException
        alunoBuscado.setComentario(requisicao.dtoParaComentario());
        alunoService.salvarAluno(alunoBuscado);

        return new ModelAndView("redirect:/comentarios");
    }

    @GetMapping("/pendentes")
    public ModelAndView exibirComentariosPendentes() {
        ModelAndView mv = new ModelAndView("/comentario/ComentariosPendentes");
        mv.addObject("alunos", alunoService.alunosComComentariosPendentes());
        return mv;
    }

    @PatchMapping("/pendentes/aprovar/{id}")
    @Transactional
    public String aprovarComentario(@PathVariable Long id) {
        comentarioService.aprovarComentarioId(id);
        return "redirect:/comentarios/pendentes";
    }

    @PatchMapping("/pendentes/rejeitar/{id}")
    @Transactional
    public String rejeitarComentario(@PathVariable Long id) {
        comentarioService.rejeitarComentario(id);
        return "redirect:/comentarios/pendentes";
    }


    private ModelAndView criarViewParaFormulario(String view, Object requisicao) {
        ModelAndView mv = new ModelAndView(view);
        mv.addObject("comentarioDto", requisicao);
        return mv;
    }
}
