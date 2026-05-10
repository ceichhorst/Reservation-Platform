toggleEditForm = (reservationId, btn) => {
    document.querySelectorAll('.edit-form-row').forEach(row => {
        row.style.display = 'none';
    });
    document.querySelectorAll('.edit-toggle-btn').forEach(b => {
        b.textContent = 'Edit';
    });

    const editRow = document.getElementById('edit-row-' + reservationId);

    if (btn === null) {
        return;
    }

    // Toggle open
    const isOpen = editRow.style.display === 'none';
    if (isOpen) {
        editRow.style.display = 'table-row';
        btn.textContent = 'Close';
        editRow.scrollIntoView({behavior: 'smooth', block: 'nearest'});
    }
}