package api.carometro.controllers;

import api.carometro.dtos.HistoricoProfissionalDto;
import api.carometro.models.Aluno;
import api.carometro.models.HistoricoProfissional;
import api.carometro.services.AlunoService;
import api.carometro.services.HistoricoProfissionalService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/profissoes")
public class HistoricoProfissionalController {
    private final HistoricoProfissionalService profissaoService;
    private final AlunoService alunoService;

    public HistoricoProfissionalController(HistoricoProfissionalService profissaoService, AlunoService alunoService) {
        this.profissaoService = profissaoService;
        this.alunoService = alunoService;
    }


    @GetMapping
    public ModelAndView exibirPaginaInicial() {
        ModelAndView mv = new ModelAndView("/profissao/ProfissaoHome");
        mv.addObject("profissoes", profissaoService.todasProfissoes());
        return mv;
    }

    @GetMapping("/cadastrar/{ra}")
    public ModelAndView exibirFormularioCadastro(@PathVariable String ra) {
        Aluno alunoBuscado = alunoService.buscarAlunoRa(ra);
        if (alunoBuscado == null)
            return new ModelAndView("redirect:/profissoes");

        return criarViewParaFormulario("/profissao/ProfissaoCadastrar", new HistoricoProfissionalDto());
    }

    @PostMapping("/cadastrar/{ra}")
    @Transactional
    public ModelAndView salvarCadastroProfissao(@PathVariable String ra, @Valid HistoricoProfissionalDto requisicao, BindingResult resultadoValidacao) {
        Aluno alunoBuscado = alunoService.buscarAlunoRa(ra);
        if (alunoBuscado == null)
            return new ModelAndView("redirect:/profissoes");

        if (resultadoValidacao.hasErrors()) {
            return criarViewParaFormulario("/profissao/ProfissaoCadastrar", requisicao);
        }

        profissaoService.salvarProfissao(requisicao.dtoParaHistoricoProfissional(), alunoBuscado);
        return new ModelAndView("redirect:/alunos/perfil/" + ra);
    }

    @GetMapping("/editar/{id}")
    public ModelAndView exibirFormularioEdicao(@PathVariable Long id) {
        HistoricoProfissional profissaoBuscada = profissaoService.buscarProfissaoId(id);

        return criarViewParaFormulario("/profissao/ProfissaoEditar", profissaoBuscada);
    }

    @PutMapping("/editar/{id}")
    @Transactional
    public ModelAndView salvarEdicaoProfissao(@PathVariable Long id, @Valid HistoricoProfissionalDto requisicao, BindingResult resultadoValidacao) {
        if (resultadoValidacao.hasErrors()) {
            return criarViewParaFormulario("/profissao/ProfissaoEditar", requisicao);
        }

        HistoricoProfissional profissaoAntiga = profissaoService.buscarProfissaoId(id);
        profissaoService.atualizarProfissao(profissaoAntiga, requisicao.dtoParaHistoricoProfissional());
        return new ModelAndView("redirect:/alunos/perfil/" + profissaoAntiga.getAluno().getRa());
    }

    @DeleteMapping("/excluir/{id}")
    @Transactional
    public String excluirProfissao(@PathVariable Long id) {
        String raAluno = profissaoService.buscarProfissaoId(id).getAluno().getRa();
        profissaoService.deletarProfissaoId(id);
        return "redirect:/alunos/perfil/" + raAluno;
    }


    private ModelAndView criarViewParaFormulario(String view, Object profissao) {
        ModelAndView mv = new ModelAndView(view);
        mv.addObject("profissaoDto", profissao);
        return mv;
    }
}
