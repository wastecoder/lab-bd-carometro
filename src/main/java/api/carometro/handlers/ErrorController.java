package api.carometro.handlers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = (statusCode != null) ? statusCode : 500;

        String descricao;
        String mensagem;

        switch (status) {
            case 404 -> {
                descricao = "Página não encontrada";
                mensagem = "A página que você está procurando não existe ou foi movida.";
            }
            case 403 -> {
                descricao = "Acesso negado";
                mensagem = "Você não tem permissão para acessar esta página.";
            }
            default -> {
                descricao = "Erro inesperado";
                mensagem = "Algo deu errado. Tente novamente mais tarde.";
            }
        }

        model.addAttribute("titulo", status);
        model.addAttribute("descricao", descricao);
        model.addAttribute("mensagem", mensagem);

        return "autenticacao/ErroGenerico";
    }

}
