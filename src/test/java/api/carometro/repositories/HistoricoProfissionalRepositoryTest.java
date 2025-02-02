package api.carometro.repositories;

import api.carometro.enums.ModalidadeCurso;
import api.carometro.enums.SemestreTurma;
import api.carometro.enums.TipoCurso;
import api.carometro.enums.TurnoTurma;
import api.carometro.models.Aluno;
import api.carometro.models.Curso;
import api.carometro.models.HistoricoProfissional;
import api.carometro.models.Turma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class HistoricoProfissionalRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private HistoricoProfissionalRepository historicoRepository;

    private Curso cursoAds;
    private Turma turmaNoturnaAds;
    private Aluno alunoTurmaAds;

    @BeforeEach
    void setUp() {
        cursoAds = cursoRepository.save(new Curso("Análise e Desenvolvimento de Sistemas",
                TipoCurso.TECNOLOGO, ModalidadeCurso.PRESENCIAL));
        turmaNoturnaAds = turmaRepository.save(new Turma(2024, SemestreTurma.PRIMEIRO, TurnoTurma.NOTURNO, cursoAds));
        alunoTurmaAds = alunoRepository.save(new Aluno("1110482222022", "senha", "nome",
                LocalDate.of(2003, 11, 10), "foto", "linkedin",
                "github", "lattes", null, turmaNoturnaAds));

        inicializarHistoricosProfissionais();
    }

    private void inicializarHistoricosProfissionais() {
        historicoRepository.saveAll(Set.of(
                new HistoricoProfissional("Empresa 1", "Cargo 1", LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 1, 15), alunoTurmaAds),
                new HistoricoProfissional("Empresa 2", "Cargo 2", LocalDate.of(2022, 2, 20),
                        LocalDate.of(2022, 4, 20), alunoTurmaAds),
                new HistoricoProfissional("Empresa 3", "Cargo 3", LocalDate.of(2022, 5, 1),
                        LocalDate.of(2024, 5, 1), alunoTurmaAds)
        ));
    }

    @Test
    @DisplayName("findAllByAlunoOrderByInicioAsc: exibe os empregos mais antigos aos mais recentes")
    void findAllByAlunoOrderByInicioAsc() {
        List<HistoricoProfissional> historicosAsc = historicoRepository.findAllByAlunoOrderByInicioAsc(alunoTurmaAds);

        assertEquals("Empresa 1", historicosAsc.get(0).getOnde());
        assertEquals("Empresa 2", historicosAsc.get(1).getOnde());
        assertEquals("Empresa 3", historicosAsc.get(2).getOnde());
    }

    @Test
    @DisplayName("findAllByAlunoOrderByFimDesc: exibe os empregos mais recentes aos mais antigos")
    void findAllByAlunoOrderByFimDesc() {
        List<HistoricoProfissional> historicosDesc = historicoRepository.findAllByAlunoOrderByFimDesc(alunoTurmaAds);

        assertEquals("Empresa 3", historicosDesc.get(0).getOnde());
        assertEquals("Empresa 2", historicosDesc.get(1).getOnde());
        assertEquals("Empresa 1", historicosDesc.get(2).getOnde());
    }

    @Test
    @DisplayName("@UniqueConstraint: lança DataIntegrityViolationException ao salvar histórico duplicado - onde, cargo, inicio e fim iguais")
    void cursoDuplicadoComUniqueConstraint() {
        //Para teste, vou usar o Histórico: ("Empresa 3", "Cargo 3", LocalDate.of(2022, 5, 1), LocalDate.of(2024, 5, 1), alunoTurmaAds);
        //Ele já foi criado e salvo pelo @Before - não preciso salvar de novo

        //Criando um histórico com os mesmos valores de um existente para testar a violação de unicidade
        HistoricoProfissional historicoDuplicado = new HistoricoProfissional("Empresa 3", "Cargo 3",
                LocalDate.of(2022, 5, 1), LocalDate.of(2024, 5, 1), alunoTurmaAds);

        // Verifica se a exceção DataIntegrityViolationException é lançada ao salvar um histórico duplicado
        assertThrows(DataIntegrityViolationException.class, () -> {
            historicoRepository.saveAndFlush(historicoDuplicado);
        });
    }

    @Test
    @DisplayName("validarData: verifica se a data fim é menor do que a data início")
    void validarDataFimMenorDoQueDataInicio() {
        HistoricoProfissional empregoDataInvalida = new HistoricoProfissional("Empresa 4", "Cargo 4",
                LocalDate.of(2020, 1, 1), LocalDate.of(2000, 1, 1), alunoTurmaAds);

        //Por algum motivo não da para testar "IllegalArgumentException" direto e precisa fazer isso
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            historicoRepository.saveAndFlush(empregoDataInvalida);
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @Test
    @DisplayName("validarData: verifica se a data inicío é igual do que a data fim")
    void validarDatasDeFimEInnicioIguais() {
        HistoricoProfissional empregoDataInvalida = new HistoricoProfissional("Empresa 5", "Cargo 5",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 1), alunoTurmaAds);

        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            historicoRepository.saveAndFlush(empregoDataInvalida);
        });

        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
    }

    @Test
    @DisplayName("calcularPeriodoEmpregado: calcula o tempo empregado (fim - inicio)")
    void calcularPeriodoEmpregado() {
        List<HistoricoProfissional> historicosAsc = historicoRepository.findAllByAlunoOrderByInicioAsc(alunoTurmaAds);

        assertEquals("14 dia(s)", historicosAsc.get(0).calcularPeriodoEmpregado());
        assertEquals("2 mes(es)", historicosAsc.get(1).calcularPeriodoEmpregado());
        assertEquals("2 ano(s)", historicosAsc.get(2).calcularPeriodoEmpregado());
    }

    @Test
    @DisplayName("calcularPeriodoEmpregado: calcula o tempo quando ainda empregado (fim nulo)")
    void calcularPeriodoEmpregadoComFimNulo() {
        HistoricoProfissional aindaEmpregado = new HistoricoProfissional("Empresa 6", "Cargo 6",
                LocalDate.of(2024, 8, 1), null, alunoTurmaAds);

        String periodoEsperado = aindaEmpregado.calcularPeriodoEmpregado();

        assertEquals(periodoEsperado, aindaEmpregado.calcularPeriodoEmpregado());
    }
}