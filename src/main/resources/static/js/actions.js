function excluirRegistro(nome, endpoint) {
    document.getElementById("modalName").innerHTML = nome;
    document.getElementById("formDeletar").setAttribute("action", endpoint);
}
