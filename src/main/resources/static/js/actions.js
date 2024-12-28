function excluirRegistro(nome, endpoint) {
    document.getElementById("modalName").innerHTML = nome;
    document.getElementById("formDeletar").setAttribute("action", endpoint);
}

function configurarModal(acao, nome, endpoint) {
    const modal = document.getElementById("modalGenerico");
    const modalIcon = document.getElementById("modalIcon");
    const modalTextAction = document.getElementById("modalTextAction");
    const modalName = document.getElementById("modalName");
    const modalForm = document.getElementById("modalForm");

    if (acao === "aprovar") {
        modal.className = "modal fade aprovar";
        modalIcon.className = "fa-solid fa-check";
        modalTextAction.innerText = "Deseja aprovar o comentário de:";
    } else if (acao === "rejeitar") {
        modal.className = "modal fade rejeitar";
        modalIcon.className = "fa-solid fa-x";
        modalTextAction.innerText = "Deseja rejeitar o comentário de:";
    }

    modalName.innerText = nome;
    modalForm.setAttribute("action", endpoint);
}
