document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("loginForm");
    if (!form) return;

    form.addEventListener("submit", e => {
        e.preventDefault();
        const email = document.getElementById("email").value.trim().toLowerCase();
        const senha = document.getElementById("senha").value;
        const mensagem = document.getElementById("loginMensagem");

        if (!email || !senha) {
            mensagem.textContent = "Informe e-mail e senha.";
            mensagem.className = "alert alert-danger";
            return;
        }

        // Demo: credenciais fictícias. Em produção, autenticação ocorrerá no backend.
        const contas = {
            "gerente@empresa.com": { senha: "123456", perfil: "gerente", nome: "Maria Moreno" },
            "supervisor@empresa.com": { senha: "123456", perfil: "supervisor", nome: "João Silva" }
        };

        const conta = contas[email];
        if (!conta || conta.senha !== senha) {
            mensagem.textContent = "E-mail ou senha inválidos.";
            mensagem.className = "alert alert-danger";
            return;
        }

        localStorage.setItem("gl_usuario", JSON.stringify(conta));
        window.location.href = conta.perfil === "gerente"
            ? "pages/gerente/dashboard-gerente.html"
            : "pages/supervisor/dashboard-supervisor.html";
    });
});
