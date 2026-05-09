toggleSlots = (id, btn) => {
    const row = document.getElementById(id);
    const isOpen = row.style.display !== 'none';

    row.style.display = isOpen ? 'none' : 'table-row';

    btn.textContent= isOpen ? '▶' : '▾';
    btn.setAttribute('aria-expanded', String(!isOpen));
}