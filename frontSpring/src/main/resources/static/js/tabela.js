function closeAllDropdowns(except) {
    document.querySelectorAll('.dropdown-menu.show').forEach(menu => {
        if (menu !== except) menu.classList.remove('show');
    });
}

function positionDropdown(trigger, menu) {
    const rect = trigger.getBoundingClientRect();
    const menuHeight = menu.offsetHeight;
    const menuWidth = menu.offsetWidth;
    const spaceBelow = window.innerHeight - rect.bottom;

    // alinha a borda direita do menu com a borda direita do botão
    let left = rect.right - menuWidth;
    if (left < 8) left = 8;

    // abre pra baixo; se não tiver espaço, abre pra cima
    let top;
    if (spaceBelow >= menuHeight + 8) {
        top = rect.bottom + 6;
    } else {
        top = rect.top - menuHeight - 6;
    }

    menu.style.top = `${top}px`;
    menu.style.left = `${left}px`;
}

document.addEventListener('click', function (e) {
    const trigger = e.target.closest('.btn-icon');
    const menu = trigger ? trigger.nextElementSibling : null;

    if (menu && menu.classList.contains('dropdown-menu')) {
        const isOpen = menu.classList.contains('show');
        closeAllDropdowns(isOpen ? null : menu);

        if (!isOpen) {
            menu.classList.add('show');
            positionDropdown(trigger, menu);
        } else {
            menu.classList.remove('show');
        }
    } else {
        closeAllDropdowns(null);
    }
});

// fecha o menu ao rolar ou redimensionar — evita ele "flutuar" na posição errada
window.addEventListener('scroll', () => closeAllDropdowns(null), true);
window.addEventListener('resize', () => closeAllDropdowns(null));


document.body.addEventListener("passengerUpdated", () => {
    document.getElementById("passageiro-form").innerHTML = "";
});
