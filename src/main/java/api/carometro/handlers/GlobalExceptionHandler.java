package api.carometro.handlers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
