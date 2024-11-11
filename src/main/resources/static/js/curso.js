function deletarCurso(id, nome) {
    document.getElementById('modalName').innerHTML = nome;
    document.getElementById('modalConfirm').setAttribute("href", "/cursos/deletar/" + id);
}
