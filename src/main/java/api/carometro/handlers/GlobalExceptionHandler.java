package api.carometro.handlers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Ocorre quando há uma violação de integridade de dados no banco de dados.
     * <p>
     * Exemplos:
     * - Tentativa de inserir um registro duplicado (ex: mesmo nome de curso já existente).
     * - Tentativa de inserir um valor nulo em um campo que é obrigatório (ex: nome do curso nulo).
     */
    @ExceptionHandler({DataIntegrityViolationException.class, UnexpectedRollbackException.class})
    public String handleDatabaseExceptions(Exception ex, Model model) {
        model.addAttribute("titulo", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("descricao", "Violação de integridade de dados");
        model.addAttribute("mensagem", "Não foi possível salvar. Verifique se há duplicatas ou se preencheu todos os campos obrigatórios.");
        return "autenticacao/ErroGenerico";
    }

    /**
     * Ocorre quando o usuário acessa uma página que não existe.
     * OBS: daria para deixar igual o handleGenericError() abaixo, mas eu mandei
     * para o HttpErrorController para evitar código repetido e centralizar erros 403, 404 e 500.
     * <p>
     * Exemplo:
     * - Acessar uma URL inválida (ex: /pagina-inexistente).
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNotFound(HttpServletRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 404);
        return "forward:/error";
    }

    /**
     * Ocorre quando o usuário tenta acessar algo que ele não tem permissão.
     * O AccessDeniedHandler, tratado no SecurityConfig, é lançado em requisições
     * bloqueadas pelo método securityFilterChain().
     * A exceção AuthorizationDeniedException é lançada depois disso, pelo @PreAuthorize.
     * <p>
     * Exemplo:
     * - Usuário logado como "111111" tenta excluir o aluno "222222".
     *   A regra @PreAuthorize("#ra == authentication.name or hasRole('ADMIN')") impede isso,
     *   e lança AuthorizationDeniedException.
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public String handleAccessDenied(HttpServletRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 403);
        return "forward:/error";
    }

    /**
     * Ocorre para erros genéricos não tratados especificamente.
     * <p>
     * Exemplo:
     * - NullPointerException ao acessar um objeto que não foi instanciado.
     * - RuntimeException inesperada durante a execução.
     */
    @ExceptionHandler(Exception.class)
    public String handleGenericError(Exception ex, Model model) {
        model.addAttribute("titulo", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("descricao", "Erro inesperado");
        model.addAttribute("mensagem", "Ocorreu um erro interno. Tente novamente mais tarde.");
        return "autenticacao/ErroGenerico";
    }

}
