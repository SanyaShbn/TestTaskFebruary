function togglePasswordChangeForm() {
    const form = document.getElementById('passwordChangeForm');
    if (form.style.display === 'none' || form.style.display === '') {
        form.style.display = 'block';
    } else {
        form.style.display = 'none';
        document.getElementById('passwordInput').value = '';
    }
}

function openModal() {
    document.getElementById('myModal').style.display = "block";
}

function closeModal() {
    document.getElementById('myModal').style.display = "none";
}

function confirmPasswordChange() {
    closeModal();
    document.getElementById('passwordChangeForm').submit();
}
