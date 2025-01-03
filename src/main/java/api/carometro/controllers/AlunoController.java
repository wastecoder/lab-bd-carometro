package api.carometro.controllers;

import api.carometro.dtos.AlunoCadastroDto;
import api.carometro.dtos.AlunoDto;
import api.carometro.models.Aluno;
import api.carometro.services.AlunoService;
import api.carometro.services.TurmaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/alunos")
public class AlunoController {
    @Autowired
    private final AlunoService alunoService;
    private final TurmaService turmaService;

    public AlunoController(AlunoService alunoService, TurmaService turmaService) {
        this.alunoService = alunoService;
        this.turmaService = turmaService;
    }


    @GetMapping
    public ModelAndView exibirPaginaInicial() {
        ModelAndView mv = new ModelAndView("/aluno/AlunoHome");
        mv.addObject("alunos", alunoService.todosAlunos());
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView exibirFormularioCadastro() {
        return criarViewParaFormulario("administrador/AdmCadastrarAluno", new AlunoCadastroDto());
    }

    @PostMapping("/cadastrar")
    @Transactional
    public ModelAndView salvarCadastroAluno(@Valid AlunoCadastroDto requisicao, BindingResult resultadoValidacao) {
        if (alunoService.raExistente(requisicao.getRa())) {
            ModelAndView mv = criarViewParaFormulario("administrador/AdmCadastrarAluno", requisicao);
            mv.addObject("raExistente", "Aluno com o mesmo RA já cadastrado.");
            return mv;
        }

        if (resultadoValidacao.hasErrors()) {
            return criarViewParaFormulario("administrador/AdmCadastrarAluno", requisicao);
        }

        Aluno alunoNovo = requisicao.dtoParaAluno();
        alunoService.salvarAluno(alunoNovo);
        return new ModelAndView("redirect:/alunos");
    }

    @GetMapping("/editar/{ra}")
    public ModelAndView exibirFormularioEdicao(@PathVariable String ra) {
        Aluno alunoBuscado = alunoService.buscarAlunoRa(ra);

        if (alunoBuscado != null) {
            ModelAndView mv = criarViewParaFormulario("administrador/AdmEditarAluno", alunoBuscado);
            mv.addObject("alunoJson", alunoService.converterParaJson(alunoBuscado));

            return mv;
        }
        return new ModelAndView("redirect:/alunos");
    }

    @PutMapping("/editar/{ra}")
    @Transactional
    public ModelAndView salvarEdicaoAluno(
            @PathVariable String ra,
            @RequestPart(value = "foto", required = false) MultipartFile file,
            @Valid AlunoDto requisicao,
            BindingResult resultadoValidacao) {
        if (resultadoValidacao.hasErrors()) {
            ModelAndView mv = criarViewParaFormulario("administrador/AdmEditarAluno", requisicao);
            mv.addObject("alunoJson", alunoService.converterParaJson(requisicao.dtoParaAluno()));

            return mv;
        }

        Aluno alunoAntigo = alunoService.buscarAlunoRa(ra);
        alunoService.atualizarFotoPerfil(alunoAntigo, file);
        alunoService.atualizarAluno(alunoAntigo, requisicao.dtoParaAluno());
        return new ModelAndView("redirect:/alunos/perfil/" + ra);
    }

    @DeleteMapping("/excluir/{ra}")
    @Transactional
    public String excluirAluno(@PathVariable String ra) {
        alunoService.deletarAlunoRa(ra);
        return "redirect:/alunos";
    }

    @GetMapping("/perfil/{ra}")
    public ModelAndView exibirPerfilAluno(@PathVariable String ra) {
        Aluno alunoBuscado = alunoService.buscarAlunoRa(ra);

        if (alunoBuscado != null) {
            ModelAndView mv = new ModelAndView("aluno/AlunoExibirPerfil");
            mv.addObject("aluno", alunoBuscado);

            return mv;
        }
        return new ModelAndView("redirect:/alunos");
    }


    private ModelAndView criarViewParaFormulario(String view, Object requisicao) {
        ModelAndView mv = new ModelAndView(view);
        mv.addObject("alunoDto", requisicao);

        ObjectMapper mapper = new ObjectMapper();
        String turmasCadastradasComoJson;
        try {
            turmasCadastradasComoJson = mapper.writeValueAsString(turmaService.todasTurmas());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao serializar turmas para JSON", e);
        }
        mv.addObject("turmasJson", turmasCadastradasComoJson);

        return mv;
    }
}
