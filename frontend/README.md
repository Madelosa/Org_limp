# Frontend — Org_limp_Gestão de Limpeza v1

Tecnologias:

- HTML5 semântico
- CSS
- Bootstrap 5
- JavaScript
- LocalStorage para simulação do banco

Perfis:

### Gerente
- Dashboard
- Plano semanal
- Tarefas
- Relatórios
- Notificações
- Usuários
- Configurações

### Supervisor
- Dashboard
- Minhas tarefas
- Notificações
- Meu perfil

### Regra implementada no protótipo

Ao cadastrar uma tarefa em `gerente-tarefas.html`, o JavaScript:

1. grava a tarefa no LocalStorage;
2. identifica o supervisor responsável;
3. cria automaticamente uma notificação para o supervisor.

Quando o supervisor altera o status:

1. a tarefa é atualizada;
2. uma notificação de alteração é criada para o gerente.

Essa regra será mantida no backend Spring Boot na próxima versão.