package api.carometro.repositories;

import api.carometro.enums.ModalidadeCurso;
import api.carometro.enums.SemestreTurma;
import api.carometro.enums.TipoCurso;
import api.carometro.enums.TurnoTurma;
import api.carometro.models.Aluno;
import api.carometro.models.Curso;
import api.carometro.models.Turma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AlunoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    private Curso cursoAds;
    private Turma turmaMatutinaLgt;

    private Aluno pedroMendes;
    private Aluno anaMaria;
    private Aluno mariaLuiza;

    @BeforeEach
    void setUp() {
        cursoAds = cursoRepository.save(new Curso("Análise e Desenvolvimento de Sistemas",
                TipoCurso.TECNOLOGO, ModalidadeCurso.PRESENCIAL));
        Turma turmaNoturnaAds = turmaRepository.save(new Turma(2024, SemestreTurma.PRIMEIRO, TurnoTurma.NOTURNO, cursoAds));

        Curso cursoLgt = cursoRepository.save(new Curso("Logística", TipoCurso.POS_GRADUACAO, ModalidadeCurso.HIBRIDO));
        turmaMatutinaLgt = turmaRepository.save(new Turma(2024, SemestreTurma.PRIMEIRO, TurnoTurma.MATUTINO, cursoLgt));

        pedroMendes = alunoRepository.save(new Aluno("1110482222011", "senha1", "Pedro Mendes da Silva",
                LocalDate.of(2003, 11, 10), "foto1", null, null, null, null, turmaNoturnaAds));
        anaMaria = alunoRepository.save(new Aluno("1110482222022", "senha2", "Ana Maria Braga",
                LocalDate.of(1949, 4, 1), "foto2", null,null, null, null, turmaNoturnaAds));
        mariaLuiza = alunoRepository.save(new Aluno("1110482222033", "senha3", "Maria Luiza Mendes",
                LocalDate.of(2006, 6, 25), "foto3", null,null, null, null, turmaMatutinaLgt));
    }

    @Test
    @DisplayName("findById: procura um RA válido e outro inválido")
    void findById() {
        Aluno raValido = alunoRepository.findById("1110482222011").orElse(null);
        Optional<Aluno> raInvalido = alunoRepository.findById("1110482222000");

        assertNotNull(raValido, "O RA válido não deve retornar null");
        assertEquals(raValido, pedroMendes, "O RA válido deve retornar o aluno: " + pedroMendes.getNome());
        assertTrue(raInvalido.isEmpty(), "O RA inválido deve estar vazio");
    }

    @Test
    @DisplayName("findByNomeContaining: busca por alguma parte do nome em ordem alfabética")
    void findByNomeContainingOrderByNomeAsc() {
        final String NOME_PROCURADO = "Maria";
        Page<Aluno> pagina = alunoRepository
                .findByNomeContainingOrderByNomeAsc(NOME_PROCURADO, definirPageRequest());

        assertEquals(2, pagina.getTotalElements(), "A busca deve retornar 2 alunos com 'Maria' no nome");
        assertEquals(anaMaria, pagina.getContent().get(0), "O primeiro aluno deve ser Ana Maria");
        assertEquals(mariaLuiza, pagina.getContent().get(1), "O segundo aluno deve ser Maria Luiza");
    }

    @Test
    @DisplayName("findAllByTurmaCurso: busca alunos por curso")
    void findAllByTurmaCurso() {
        Page<Aluno> pagina = alunoRepository
                .findAllByTurmaCurso(cursoAds, definirPageRequest());

        assertEquals(2, pagina.getTotalElements(), "A busca por cursoAds deve retornar 2 alunos");
        assertEquals(anaMaria, pagina.getContent().get(0), "O primeiro aluno deve ser Ana Maria");
        assertEquals(pedroMendes, pagina.getContent().get(1), "O segundo aluno deve ser Pedro Mendes");
    }

    @Test
    @DisplayName("findAllByTurma: busca alunos por turma")
    void findAllByTurma() {
        Page<Aluno> pagina = alunoRepository
                .findAllByTurma(turmaMatutinaLgt, definirPageRequest());

        assertEquals(1, pagina.getTotalElements(), "A busca pela turmaMatutinaLgt deve retornar 1 aluno");
        assertEquals(mariaLuiza, pagina.getContent().get(0), "O único aluno deve ser Maria Luiza");
    }

    private PageRequest definirPageRequest() {
        final int PAGINA_ATUAL = 0;
        final int ITEMS_POR_PAGINA = 10;
        return PageRequest.of(PAGINA_ATUAL, ITEMS_POR_PAGINA, Sort.by("nome").ascending());
    }
}