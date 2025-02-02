document.addEventListener("DOMContentLoaded", function () {
    const fimInput = document.querySelector('[name="fim"]');
    const checkbox = document.getElementById("aindaEmpregadoCheckbox");

    function toggleFimInput() {
        if (checkbox.checked) {
            fimInput.value = ""; // Limpa o campo
            fimInput.setAttribute("disabled", "true"); // Desativa o campo
        } else {
            fimInput.removeAttribute("disabled"); // Ativa o campo
        }
    }

    checkbox.addEventListener("change", toggleFimInput);

    // Verifica se já existe um valor em "fim" ao carregar a página
    if (!fimInput.value) {
        checkbox.checked = true;
        toggleFimInput();
    }
});
