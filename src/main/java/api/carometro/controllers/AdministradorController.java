package api.carometro.controllers;

import api.carometro.dtos.AdministradorDto;
import api.carometro.enums.CargoAdm;
import api.carometro.models.Administrador;
import api.carometro.services.AdministradorService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/administradores")
public class AdministradorController {
    @Autowired
    private final AdministradorService admService;

    public AdministradorController(AdministradorService admService) {
        this.admService = admService;
    }


    @GetMapping
    public ModelAndView exibirPaginaInicial() {
        ModelAndView mv = new ModelAndView("administrador/AdministradorHome");
        mv.addObject("administradores", admService.todosAdministradores());
        return mv;
    }

    @GetMapping("/cadastrar")
    public ModelAndView exibirFormularioCadastro() {
        return criarViewParaFormulario("administrador/AdministradorCadastrar", new AdministradorDto());
    }

    @PostMapping("/cadastrar")
    @Transactional
    public ModelAndView salvarCadastroAdministrador(@Valid AdministradorDto requisicao, BindingResult resultadoValidacao) {
        if (resultadoValidacao.hasErrors()) {
            return criarViewParaFormulario("administrador/AdministradorCadastrar", requisicao);
        }

        admService.salvarAdministrador(requisicao.dtoParaAdministrador());
        return new ModelAndView("redirect:/administradores");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView exibirFormularioEdicao(@PathVariable Long id) {
        Administrador administradorBuscado = admService.buscarAdministradorId(id);
        if (administradorBuscado == null)
            return new ModelAndView("redirect:/administradores");

        return criarViewParaFormulario("administrador/AdministradorEditar", administradorBuscado);
    }

    @PutMapping("/editar/{id}")
    @Transactional
    public ModelAndView salvarEdicaoAdministrador(@PathVariable Long id, @Valid AdministradorDto requisicao, BindingResult resultadoValidacao) {
        if (resultadoValidacao.hasErrors()) {
            return criarViewParaFormulario("administrador/AdministradorEditar", requisicao);
        }

        Administrador administradorAntigo = admService.buscarAdministradorId(id);
        admService.atualizarAdministrador(administradorAntigo, requisicao.dtoParaAdministrador());
        return new ModelAndView("redirect:/administradores");
    }

    @DeleteMapping("/excluir/{id}")
    @Transactional
    public String excluirAdministrador(@PathVariable Long id) {
        admService.deletarAdministradorId(id);
        return "redirect:/administradores";
    }


    private ModelAndView criarViewParaFormulario(String view, Object requisicao) {
        ModelAndView mv = new ModelAndView(view);
        mv.addObject("cargos", CargoAdm.values());
        mv.addObject("admDto", requisicao);
        return mv;
    }
}
