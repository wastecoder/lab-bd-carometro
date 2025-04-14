package api.carometro.handlers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private static final Map<HttpStatus, String[]> ERROR_MESSAGES = Map.of(
            HttpStatus.NOT_FOUND, new String[]{"Página não encontrada", "A página que você está procurando não existe ou foi movida."},
            HttpStatus.FORBIDDEN, new String[]{"Acesso negado", "Você não tem permissão para acessar esta página."}
    );

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus status = (statusCode != null) ? HttpStatus.resolve(statusCode) : HttpStatus.INTERNAL_SERVER_ERROR;

        String[] info = ERROR_MESSAGES.getOrDefault(
                status,
                new String[]{"Erro inesperado", "Algo deu errado. Tente novamente mais tarde."}
        );

        model.addAttribute("titulo", status.value());
        model.addAttribute("descricao", info[0]);
        model.addAttribute("mensagem", info[1]);

        return "autenticacao/ErroGenerico";
    }

}
