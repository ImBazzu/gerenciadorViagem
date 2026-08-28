document.body.addEventListener('htmx:beforeSwap', function (evt) {
    if (evt.detail.xhr.status === 422) {
        evt.detail.shouldSwap = true;
        evt.detail.isError = false;
    }
});
function atualizarMenuAtivo() {
    const caminhoAtual = window.location.pathname;

    document.querySelectorAll('.menu-item[data-rota]').forEach(function (item) {
        const rota = item.getAttribute('data-rota');
        item.classList.toggle('active', caminhoAtual.startsWith(rota));
    });
}

// roda na carga inicial da página
atualizarMenuAtivo();

// roda toda vez que o HTMX atualiza a URL (navegação via hx-push-url)
document.body.addEventListener('htmx:pushedIntoHistory', atualizarMenuAtivo);

// cobre também o caso de voltar/avançar pelo navegador
window.addEventListener('popstate', atualizarMenuAtivo);