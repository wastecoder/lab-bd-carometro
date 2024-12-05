package api.carometro.controllers;

import api.carometro.dtos.AlunoCadastroDto;
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
            return criarViewParaFormulario("administrador/AdmEditarAluno", alunoBuscado);
        }
        return new ModelAndView("redirect:/alunos");
    }

    @PutMapping("editar/{ra}")
    @Transactional
    public ModelAndView salvarEdicaoAluno(@PathVariable String ra, @Valid AlunoCadastroDto requisicao, BindingResult resultadoValidacao) {
        if (resultadoValidacao.hasErrors()) {
            return criarViewParaFormulario("administrador/AdmCadastrarAluno", requisicao);
        }

        Aluno alunoAntigo = requisicao.dtoParaAluno();
        alunoService.atualizarAluno(alunoAntigo, requisicao.dtoParaAluno());
        return new ModelAndView("redirect:/alunos");
    }

    @GetMapping("/perfil/{id}")
    public ModelAndView exibirPerfilAluno() {
        return new ModelAndView("redirect:/alunos");
    }

    @DeleteMapping("/excluir/{ra}")
    @Transactional
    public String excluirAluno(@PathVariable String ra) {
        alunoService.deletarAlunoRa(ra);
        return "redirect:/alunos";
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
