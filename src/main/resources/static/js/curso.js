function deletarCurso(id, nome) {
    option = confirm("Deseja excluir o curso [" + nome + "]?");
    if (option) {
        alert("Curso excluído com sucesso!");
        window.location.replace("/cursos/deletar/" + id);
    }
}
