# Org-limp - Sistema de Gestão de Limpeza

Sistema web para gestão de equipes de limpeza, desenvolvido com Spring Boot, Thymeleaf e MySQL.

## Requisitos

* Docker
* Docker Compose

## Configuração

O projeto utiliza variáveis de ambiente para configurar o banco de dados e a aplicação. As credenciais não ficam armazenadas diretamente no `docker-compose.yml`.

### 1. Criar o arquivo `.env`

Copie o arquivo de exemplo:

**Linux/macOS:**

```bash
cp .env.example .env
```

**Windows PowerShell:**

```powershell
Copy-Item .env.example .env
```

### 2. Configurar as variáveis

Abra o arquivo `.env` e defina os valores necessários:

```env
DB_HOST=mysql
DB_PORT=3306
DB_NAME=orglimp
DB_USER=root
DB_PASSWORD=DEFINA_SUA_SENHA
MYSQL_ROOT_PASSWORD=DEFINA_SUA_SENHA

APP_PORT=8080
```

> **Importante:** o arquivo `.env` contém informações de configuração local e não deve ser enviado ao GitHub. Ele já está incluído no `.gitignore` do projeto.

O arquivo `.env.example` pode ser utilizado como modelo para configurar o projeto em outro computador.

## Execução

Para iniciar a aplicação:

```bash
docker compose up --build
```

A aplicação será iniciada juntamente com o banco de dados MySQL.

Para parar os containers:

```bash
docker compose down
```

> **Atenção:** `docker compose down -v` também remove o volume `mysql_data`, apagando os dados persistidos do banco de dados. Utilize esse comando somente quando quiser recriar o banco do zero.

Para reconstruir após alterações no código:

```bash
docker compose up --build --force-recreate
```

## Acesso

* **URL:** http://localhost:8080
* **Usuário demo (Gerente):** [gerente@empresa.com](mailto:gerente@empresa.com) / 123456
* **Usuário demo (Supervisor):** [supervisor@empresa.com](mailto:supervisor@empresa.com) / 123456

> Os usuários acima são contas de demonstração da aplicação. Em um ambiente real, recomenda-se utilizar credenciais próprias e não utilizar senhas de demonstração.

## Banco de Dados

* **Nome:** orglimp
* **Porta no computador:** 3307
* **Porta dentro do container:** 3306
* **Usuário:** definido pela variável `DB_USER`
* **Senha:** definida pela variável `DB_PASSWORD`
* **Persistência:** os dados são armazenados no volume Docker `mysql_data`

Os containers se comunicam através da rede interna `orglimp-network`.

Dentro do Docker Compose, o host do banco é `mysql`, que corresponde ao nome do serviço do banco de dados.

A aplicação utiliza:

```text
Aplicação: localhost:8080
MySQL no host: localhost:3307
MySQL no container: mysql:3306
```

## Estrutura do Projeto

```text
src/main/java/com/tcc/orgLimp/

├── config/          # Configurações (Security, PasswordEncoder)
├── controller/      # Controllers MVC
├── dto/             # Objetos de transferência de dados
├── entity/          # Entidades JPA
├── repository/      # Repositórios JPA
└── service/         # Serviços (lógica de negócio)

src/main/resources/

├── templates/       # Templates Thymeleaf
│   ├── fragments/   # Fragmentos reutilizáveis (header, sidebar)
│   └── pages/       # Páginas (gerente, supervisor, login)
├── static/          # Recursos estáticos (CSS, JS, imagens)
└── application.properties
```

## Funcionalidades

### Gerente

* Dashboard com indicadores
* Gestão de tarefas (CRUD)
* Plano semanal
* Relatórios
* Notificações
* Gestão de usuários
* Configurações
* Perfil

### Supervisor

* Dashboard com indicadores
* Minhas tarefas
* Atualização de status das tarefas
* Notificações
* Perfil

## Tecnologias

* Java 21
* Spring Boot 4.1.1
* Spring Security
* Spring Data JPA
* Thymeleaf
* MySQL 8
* Docker
* Docker Compose
* Bootstrap 5
