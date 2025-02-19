document.addEventListener("DOMContentLoaded", function () {
    const raInput = document.getElementById("raInput");
    const nomeInput = document.getElementById("nomeInput");

    function toggleDisable(input, otherInput) {
        otherInput.disabled = input.value.trim().length > 0;
    }

    raInput.addEventListener("input", () => toggleDisable(raInput, nomeInput));
    nomeInput.addEventListener("input", () => toggleDisable(nomeInput, raInput));
});
