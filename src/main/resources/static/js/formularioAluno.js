const turmas = JSON.parse(turmasJsonGlobal); // Pega a variável global do HTML AlunoFormCadastrar

const cursoSelect = document.getElementById("curso");
const anoSelect = document.getElementById("ano");
const semestreSelect = document.getElementById("semestre");
const turnoSelect = document.getElementById("turno");

// Função para limpar o conteúdo de um dropdown
function limparDropdown(selectElement, placeholder) {
    selectElement.innerHTML = `<option value="" disabled selected>${placeholder}</option>`;
}

// Função para preencher um dropdown com itens fornecidos
function preencherDropdown(selectElement, items, placeholder) {
    limparDropdown(selectElement, placeholder);
    items.forEach((item) => {
        const option = document.createElement("option");
        option.value = item.value;
        option.textContent = item.text;
        selectElement.appendChild(option);
    });
    selectElement.disabled = items.length === 0; // Desabilitar se estiver vazio
}

// Função para filtrar turmas com base nos critérios fornecidos
function filtrarTurmas(curso, ano, semestre) {
    return turmas.filter(
        (t) =>
            (!curso || t.curso.pk_curso === parseInt(curso)) &&
            (!ano || t.ano === parseInt(ano)) &&
            (!semestre || t.semestre === semestre)
    );
}

// Preenche o dropdown de cursos com cursos únicos
const cursosUnicos = [
    ...new Map(turmas.map((t) => [t.curso.pk_curso, t.curso])).values(),
];
preencherDropdown(
    cursoSelect,
    cursosUnicos.map((c) => ({ value: c.pk_curso, text: c.nome })),
    "Selecione um Curso"
);

// Atualiza o dropdown de anos com base no curso selecionado
cursoSelect.addEventListener("change", () => {
    const cursoSelecionadoPk = cursoSelect.value; // Aqui pega diretamente a PK do curso
    const anos = [
        ...new Set(filtrarTurmas(cursoSelecionadoPk).map((t) => t.ano)),
    ];
    preencherDropdown(
        anoSelect,
        anos.map((ano) => ({ value: ano, text: ano })),
        "Selecione o Ano"
    );
    limparDropdown(semestreSelect, "Selecione o Semestre");
    limparDropdown(turnoSelect, "Selecione o Turno");
});

// Atualiza o dropdown de semestres com base no ano selecionado
anoSelect.addEventListener("change", () => {
    const cursoSelecionadoPk = cursoSelect.value;
    const anoSelecionado = anoSelect.value;
    const semestres = [
        ...new Set(
            filtrarTurmas(cursoSelecionadoPk, anoSelecionado).map(
                (t) => t.semestre
            )
        ),
    ];
    preencherDropdown(
        semestreSelect,
        semestres.map((semestre) => ({ value: semestre, text: semestre })),
        "Selecione o Semestre"
    );
    limparDropdown(turnoSelect, "Selecione o Turno");
});

// Atualiza o dropdown de turnos com base no semestre selecionado
semestreSelect.addEventListener("change", () => {
    const cursoSelecionadoPk = cursoSelect.value;
    const anoSelecionado = anoSelect.value;
    const semestreSelecionado = semestreSelect.value;
    const turnos = [
        ...new Set(
            filtrarTurmas(
                cursoSelecionadoPk,
                anoSelecionado,
                semestreSelecionado
            ).map((t) => t.turno)
        ),
    ];
    preencherDropdown(
        turnoSelect,
        turnos.map((turno) => ({ value: turno, text: turno })),
        "Selecione o Turno"
    );
});

// Função para atualizar o campo oculto 'pk_turma' com a chave da turma selecionada
function atualizarPkTurma(
    cursoSelecionadoPk,
    anoSelecionado,
    semestreSelecionado,
    turnoSelecionado
) {
    const turmaSelecionada = turmas.find(
        (t) =>
            t.curso.pk_curso === parseInt(cursoSelecionadoPk) &&
            t.ano === parseInt(anoSelecionado) &&
            t.semestre === semestreSelecionado &&
            t.turno === turnoSelecionado
    );

    if (turmaSelecionada) {
        console.log("Turma selecionada:", turmaSelecionada);
        document.getElementById("pk_turma").value = turmaSelecionada.pk_turma;
    } else {
        console.error(
            "Nenhuma turma encontrada para os critérios selecionados"
        );
        document.getElementById("pk_turma").value = ""; // Limpa o campo se não encontrar
        alert("Por favor, selecione uma turma válida.");
    }
}

// Atualiza o campo oculto 'pk_turma' quando o turno é selecionado
turnoSelect.addEventListener("change", () => {
    const cursoSelecionadoPk = cursoSelect.value;
    const anoSelecionado = anoSelect.value;
    const semestreSelecionado = semestreSelect.value;
    const turnoSelecionado = turnoSelect.value;

    atualizarPkTurma(
        cursoSelecionadoPk,
        anoSelecionado,
        semestreSelecionado,
        turnoSelecionado
    );
});
