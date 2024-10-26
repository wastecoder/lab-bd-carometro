package api.carometro.repositories;

import api.carometro.enums.ModalidadeCurso;
import api.carometro.enums.TipoCurso;
import api.carometro.models.Curso;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @BeforeEach
    void setUp() {
        inserirDadosIniciais();
    }

    private void inserirDadosIniciais() {
        cursoRepository.saveAll(List.of(
                new Curso("Logística", TipoCurso.TECNOLOGO, ModalidadeCurso.EAD),
                new Curso("Análise e Desenvolvimento de Sistemas", TipoCurso.GRADUACAO, ModalidadeCurso.PRESENCIAL),
                new Curso("Gestão Empresarial", TipoCurso.POS_GRADUACAO, ModalidadeCurso.HIBRIDO)
        ));
    }

    @Test
    @DisplayName("orderByNomeAsc: verifica se os 3 primeiros estão em ordem crescente")
    void findAllByOrderByNomeAsc() {
        Page<Curso> cursos = cursoRepository.findAllByOrderByNomeAsc(definirPageRequest(Sort.Direction.ASC));

        assertEquals(3, cursos.getTotalElements());
        assertEquals("Análise e Desenvolvimento de Sistemas", cursos.getContent().get(0).getNome());
        assertEquals("Gestão Empresarial", cursos.getContent().get(1).getNome());
        assertEquals("Logística", cursos.getContent().get(2).getNome());
    }

    @Test
    @DisplayName("orderByNomeDesc: verifica se os 3 primeiros estão em ordem decrescente")
    void findAllByOrderByNomeDesc() {
        Page<Curso> cursos = cursoRepository.findAllByOrderByNomeDesc(definirPageRequest(Sort.Direction.DESC));

        assertEquals(3, cursos.getTotalElements());
        assertEquals("Logística", cursos.getContent().get(0).getNome());
        assertEquals("Gestão Empresarial", cursos.getContent().get(1).getNome());
        assertEquals("Análise e Desenvolvimento de Sistemas", cursos.getContent().get(2).getNome());
    }

    private PageRequest definirPageRequest(Sort.Direction direction) {
        final int PAGINA_ATUAL = 0;
        final int ITEMS_POR_PAGINA = 10;
        return PageRequest.of(PAGINA_ATUAL, ITEMS_POR_PAGINA, Sort.by(direction, "nome"));
    }

    @Test
    @DisplayName("@UniqueConstraint: lança DataIntegrityViolationException ao salvar curso duplicado - nome, tipo e modalidade iguais")
    void cursoDuplicadoComUniqueConstraint() {
        //Para teste, vou usar o Curso: ("Logística", TipoCurso.TECNOLOGO, ModalidadeCurso.EAD);
        //Ele já foi criado e salvo pelo @Before - não preciso salvar de novo

        //Criando um curso com os mesmos valores de um existente para testar a violação de unicidade
        Curso cursoDuplicado = new Curso("Logística", TipoCurso.TECNOLOGO, ModalidadeCurso.EAD);

        // Verifica se a exceção DataIntegrityViolationException é lançada ao salvar um curso duplicado
        assertThrows(DataIntegrityViolationException.class, () -> {
            cursoRepository.saveAndFlush(cursoDuplicado);
        });
    }
}