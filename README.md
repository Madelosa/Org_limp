# Org-limp - Sistema de Gestão de Limpeza

Sistema web para gestão de limpeza, desenvolvido com Spring Boot, Thymeleaf e MySQL.

## Requisitos

- Docker
- Docker Compose

## Configuração

1. Copie o arquivo de exemplo:
   ```bash
   cp .env.example .env
   ```

2. Ajuste as variáveis de ambiente no arquivo `.env` conforme necessário.

## Execução

Para iniciar a aplicação:

```bash
docker compose up --build
```

Para parar:

```bash
docker compose down
```

Para reconstruir após alterações no código:

```bash
docker compose up --build --force-recreate
```

## Acesso

- **URL:** http://localhost:8080
- **Usuário demo (Gerente):** gerente@empresa.com / 123456
- **Usuário demo (Supervisor):** supervisor@empresa.com / 123456

## Banco de Dados

- **Nome:** orglimp
- **Porta:** 3306
- **Usuário:** root
- **Senha:** root
- **Persistência:** Os dados são persistidos no volume Docker `mysql_data`

Os containers se comunicam através da rede interna `orglimp-network`. O host do banco é `mysql` (nome do serviço no Docker Compose).

## Estrutura do Projeto

```
src/main/java/com/tcc/orgLimp/
├── config/          # Configurações (Security, PasswordEncoder)
├── controller/      # Controllers MVC e REST
├── dto/             # Objetos de transferência de dados
├── entity/          # Entidades JPA
├── repository/      # Repositórios JPA
└── service/         # Serviços (lógica de negócio)

src/main/resources/
├── templates/       # Templates Thymeleaf
│   ├── fragments/   # Fragmentos reutilizáveis (header, sidebar)
│   └── pages/       # Páginas (gerente, supervisor, login)
├── static/          # Recursos estáticos (CSS, JS, imagens)
├── application.properties
└── data.sql         # Dados iniciais
```

## Funcionalidades

### Gerente
- Dashboard com indicadores
- Gestão de tarefas (CRUD)
- Plano semanal
- Relatórios
- Notificações
- Gestão de usuários
- Configurações
- Perfil

### Supervisor
- Dashboard com indicadores
- Minhas tarefas (atualização de status)
- Notificações
- Perfil

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL 8
- Docker
- Bootstrap 5
