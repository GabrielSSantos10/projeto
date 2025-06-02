window.onload = function () {
    const userId = localStorage.getItem('usuarioEditarId');

    if (!userId) {
        alert("Usuário inválido.");
        window.location.href = "/site_admin/usuario_list";
        return;
    }

    fetch(`/usuario/${userId}`)
        .then(response => response.json())
        .then(usuario => {
            document.getElementById('nome').value = usuario.nome;
            document.getElementById('cpf').value = usuario.cpf;
            document.getElementById('username').value = usuario.username;
            document.getElementById('email').value = usuario.email;
            document.getElementById('perfil').value = usuario.perfil;
        })
        .catch(error => {
            console.error("Erro ao carregar usuário:", error);
            alert("Erro ao carregar os dados do usuário.");
        });

    const form = document.getElementById('editarUsuarioForm');
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const usuarioAtualizado = {
            id: userId,
            nome: document.getElementById('nome').value,
            cpf: document.getElementById('cpf').value,
            email: document.getElementById('email').value,
            perfil: document.getElementById('perfil').value
        };

        fetch('/site_admin/atualizar_usuario', {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(usuarioAtualizado)
        })
            .then(response => {
                if (response.ok) {
                    alert("Usuário atualizado com sucesso!");
                    localStorage.removeItem('usuarioEditarId');
                    window.location.href = "/site_admin/usuario_page";
                } else {
                    alert("Erro ao atualizar o usuário.");
                }
            })
            .catch(error => {
                console.error("Erro na atualização:", error);
            });
    });
};
