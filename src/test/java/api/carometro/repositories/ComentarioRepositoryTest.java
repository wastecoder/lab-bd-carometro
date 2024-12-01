package api.carometro.repositories;

import api.carometro.enums.*;
import api.carometro.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ComentarioRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    private Aluno aluno1;
    private Aluno aluno2;

    @BeforeEach
    void setUp() {
        Comentario comentario1 = comentarioRepository.save(new Comentario("Fatec 111", "Livre 111"));
        Comentario comentario2 = comentarioRepository.save(new Comentario("Fatec 222", "Livre 222"));

        Curso cursoAds = cursoRepository.save(new Curso("Análise e Desenvolvimento de Sistemas",
                TipoCurso.TECNOLOGO, ModalidadeCurso.PRESENCIAL));
        Turma turmaNoturnaAds = turmaRepository.save(new Turma(2024, SemestreTurma.PRIMEIRO, TurnoTurma.NOTURNO, cursoAds));

        aluno1 = alunoRepository.save(new Aluno("1110482222011", "senha1", "nome1",
                LocalDate.of(2003, 11, 10), "foto1", "linkedin1",
                "github1", "lattes1", comentario1, turmaNoturnaAds));
        aluno2 = alunoRepository.save(new Aluno("1110482222022", "senha2", "nome2",
                LocalDate.of(2002, 6, 25), "foto2", "linkedin2",
                "github2", "lattes2", comentario2, turmaNoturnaAds));
    }

    @Test
    @DisplayName("findAllByStatus: retorna todos os comentários com status PENDENTE")
    void findAllByStatus() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Comentario> pagina = comentarioRepository.findAllByStatus(StatusComentario.PENDENTE, pageRequest);

        assertEquals(2, pagina.getTotalElements(),
                "O total de comentários PENDENTES deve ser 2");

        pagina.getContent().forEach(comentario ->
                assertEquals(StatusComentario.PENDENTE, comentario.getStatus(),
                        "Todos os comentários devem ter status PENDENTE"));

        assertEquals(aluno1.getComentario().getPk_comentario(), pagina.getContent().get(0).getPk_comentario());
        assertEquals(aluno2.getComentario().getPk_comentario(), pagina.getContent().get(1).getPk_comentario());
    }

    @Test
    @DisplayName("alterarStatusComentario: administrador aprovando o comentário do aluno 1")
    void alterarStatusComentario() {
        Comentario comentarioAprovado = aluno1.getComentario();
        assertEquals(StatusComentario.PENDENTE, comentarioAprovado.getStatus(),
                "O status inicial do comentário deve ser PENDENTE");

        comentarioAprovado.setStatus(StatusComentario.APROVADO);
        comentarioAprovado = comentarioRepository.save(comentarioAprovado);

        assertEquals(StatusComentario.APROVADO, comentarioAprovado.getStatus(),
                "O status do comentário deve ser atualizado para APROVADO pelo administrador");
    }

    @Test
    @DisplayName("validarComentarioExistente: impede fazer comentários (sobre a Fatec e o livre) idêntico a de outro aluno")
    void validarComentarioExistente() {
        Comentario comentario1Copiado = new Comentario("Fatec 111", "Livre 111");

        assertThrows(DataIntegrityViolationException.class, () -> {
            comentarioRepository.saveAndFlush(comentario1Copiado);
        });
    }

    @Test
    @DisplayName("validarComentarioDiferente: impede comentários com os atributos sobre a Fatec e o livre iguais")
    void validarComentarioDiferente() {
        Comentario comentario1Duplicado = new Comentario("Duplicado", "Duplicado");

        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            comentarioRepository.saveAndFlush(comentario1Duplicado);
        });

        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
    }
}