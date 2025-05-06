package api.carometro.controllers;

import api.carometro.dtos.AlunoCadastroDto;
import api.carometro.dtos.AlunoDto;
import api.carometro.models.Aluno;
import api.carometro.models.HistoricoProfissional;
import api.carometro.services.AlunoService;
import api.carometro.services.HistoricoProfissionalService;
import api.carometro.services.TurmaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/alunos")
public class AlunoController {
    @Autowired
    private final AlunoService alunoService;
    private final TurmaService turmaService;
    private final HistoricoProfissionalService profissaoService;

    public AlunoController(AlunoService alunoService, TurmaService turmaService, HistoricoProfissionalService profissaoService) {
        this.alunoService = alunoService;
        this.turmaService = turmaService;
        this.profissaoService = profissaoService;
    }


    @GetMapping
    public ModelAndView exibirPaginaInicial() {
        ModelAndView mv = new ModelAndView("/aluno/AlunoExibirHome");
        mv.addObject("alunos", alunoService.todosAlunosOrdenadosPorCurso());
        return mv;
    }

    @GetMapping("/tabela")
    public ModelAndView exibirTabelaAlunos() {
        ModelAndView mv = new ModelAndView("/aluno/AlunoExibirTabela");
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

    @PreAuthorize("#ra == authentication.name or hasRole('ADMIN')")
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

    @PreAuthorize("#ra == authentication.name or hasRole('ADMIN')")
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

    @PreAuthorize("#ra == authentication.name or hasRole('ADMIN')")
    @DeleteMapping("/excluir/{ra}")
    @Transactional
    public String excluirAluno(@PathVariable String ra, HttpServletRequest request, HttpServletResponse response) {
        alunoService.deletarAlunoRa(ra);

        if (ra.equals(request.getUserPrincipal().getName())) {
            new SecurityContextLogoutHandler().logout(request, response, null);
            return "redirect:/login?logout";
        }

        return "redirect:/alunos";
    }

    @GetMapping("/perfil/{ra}")
    public ModelAndView exibirPerfilAluno(
            @PathVariable String ra,
            @RequestParam(required = false) Long idProfissao) {
        Aluno alunoBuscado = alunoService.buscarAlunoRa(ra);
        if (alunoBuscado == null) {
            return new ModelAndView("redirect:/alunos");
        }

        ModelAndView mv = new ModelAndView("aluno/AlunoExibirPerfil");
        mv.addObject("aluno", alunoBuscado);

        //Busca todas profissões para montar o select
        List<HistoricoProfissional> retornoProfissoes = profissaoService.buscarProfissoesPorAluno(alunoBuscado);
        mv.addObject("profissoes", retornoProfissoes);

        HistoricoProfissional profissaoSelecionada = null;
        if (idProfissao != null) {
            profissaoSelecionada = retornoProfissoes.stream()
                    .filter(prof -> prof.getPk_historico().equals(idProfissao))
                    .findFirst()
                    .orElse(null);

            if (profissaoSelecionada == null) {
                mv.addObject("mensagemErro",
                        "A profissão selecionada não é válida ou não pertence a este aluno.");
            }
        }

        //Seleciona a primeira profissão se nenhuma for selecionada
        if (profissaoSelecionada == null && !retornoProfissoes.isEmpty()) {
            profissaoSelecionada = retornoProfissoes.get(0);
        }
        mv.addObject("profissaoSelecionada", profissaoSelecionada);

        if (profissaoSelecionada != null)
            mv.addObject("periodoEmpregado", profissaoSelecionada.calcularPeriodoEmpregado());

        return mv;
    }

    @GetMapping("/pesquisar")
    public ModelAndView pesquisarAluno(
            @RequestParam(required = false, defaultValue = "") String ra,
            @RequestParam(required = false, defaultValue = "") String nome,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho
    ) {
        ModelAndView mv = new ModelAndView("/aluno/AlunoPesquisar");

        PageRequest pageRequest = alunoService.definirPageRequest(pagina, tamanho);
        Page<Aluno> alunos;

        if (!nome.trim().isEmpty()) {
            alunos = alunoService.buscarAlunoPorParteNome(nome, pageRequest);
        } else if (!ra.trim().isEmpty()) {
            Aluno aluno = alunoService.buscarAlunoRa(ra);

            // Se aluno for encontrado, cria uma Page com ele; senão, retorna Page vazia
            alunos = (aluno != null) ? new PageImpl<>(List.of(aluno)) : Page.empty();
        } else {
            alunos = alunoService.todosAlunos(pageRequest);
        }

        mv.addObject("alunos", alunos);
        mv.addObject("alunoDto", new AlunoCadastroDto());

        return mv;
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

    private PageRequest definirPageRequest() {
        final int PAGINA_ATUAL = 0;
        final int ITEMS_POR_PAGINA = 10;
        return PageRequest.of(PAGINA_ATUAL, ITEMS_POR_PAGINA, Sort.by("nome").ascending());
    }
}
