package api.carometro.repositories;

import api.carometro.enums.ModalidadeCurso;
import api.carometro.enums.TipoCurso;
import api.carometro.enums.TurnoTurma;
import api.carometro.models.Curso;
import api.carometro.models.Turma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TurmaRepositoryTest {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    private Curso cursoLgt;
    private Curso cursoAds;

    @BeforeEach
    void setUp() {
        inicializarCursos();
        inicializarTurmas();
    }

    private void inicializarCursos() {
        cursoLgt = cursoRepository.save(new Curso("Logística", TipoCurso.TECNOLOGO, ModalidadeCurso.EAD));
        cursoAds = cursoRepository.save(new Curso("Análise e Desenvolvimento de Sistemas", TipoCurso.GRADUACAO, ModalidadeCurso.PRESENCIAL));
    }

    private void inicializarTurmas() {
        turmaRepository.saveAll(Set.of(
                new Turma(20241, TurnoTurma.MATUTINO, cursoLgt),
                new Turma(20241, TurnoTurma.NOTURNO, cursoLgt),
                new Turma(20241, TurnoTurma.VESPERTINO, cursoAds),
                new Turma(20242, TurnoTurma.VESPERTINO, cursoAds)
        ));
    }

    @Test
    @DisplayName("findAllByCurso: verifica se retorna apenas o curso Logística")
    void findAllByCurso() {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("anoSemestre").ascending());
        Page<Turma> pagina = turmaRepository.findAllByCurso(cursoLgt, pageRequest);

        assertEquals(2, pagina.getTotalElements(), "O número total de turmas de Logística deve ser 2");

        validarCursoTurmas(pagina, "Logística");
    }

    private void validarCursoTurmas(Page<Turma> pagina, String nomeCursoEsperado) {
        pagina.getContent().forEach(turma ->
                assertEquals(nomeCursoEsperado, turma.getCurso().getNome(),
                        "Todas as turmas devem ser do curso " + nomeCursoEsperado)
        );
    }

    @Test
    @DisplayName("@UniqueConstraint: lança DataIntegrityViolationException ao salvar turma duplicada - anoSemestre, turno e curso iguais")
    void turmaDuplicadaComUniqueConstraint() {
        //Para teste, vou usar a Turma: (20241, TurnoTurma.VESPERTINO, cursoAds);
        //Ela já foi criada e salva pelo @Before - não preciso salvar de novo

        //Criando uma segunda turma com os mesmos valores para testar a violação de unicidade
        Turma turmaDuplicada = new Turma(20241, TurnoTurma.VESPERTINO, cursoAds);

        // Verifica se a exceção DataIntegrityViolationException é lançada ao salvar a segunda turma depois da primeira
        assertThrows(DataIntegrityViolationException.class, () -> {
            turmaRepository.saveAndFlush(turmaDuplicada);
        });
    }
}