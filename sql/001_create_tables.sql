-- =====================================================
-- BANCO DE DADOS: org_limp
-- =====================================================

CREATE DATABASE IF NOT EXISTS org_limp
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE org_limp;


-- =====================================================
-- TABELA: usuario
-- =====================================================

CREATE TABLE usuario (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    foto VARCHAR(255) NULL,
    email VARCHAR(150) NOT NULL,
    senha VARCHAR(255) NOT NULL,

    notificacao_email BOOLEAN NOT NULL DEFAULT TRUE,
    notificacao_whatsapp BOOLEAN NOT NULL DEFAULT FALSE,
    notificacao_sistema BOOLEAN NOT NULL DEFAULT TRUE,

    tipo ENUM(
        'admin',
        'gerente',
        'supervisor'
    ) NOT NULL DEFAULT 'supervisor',

    PRIMARY KEY (id),

    UNIQUE KEY uk_usuario_email (email)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;


-- =====================================================
-- TABELA: plano
-- =====================================================

CREATE TABLE plano (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    data_inicial DATE NOT NULL,
    data_final DATE NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT chk_plano_periodo
        CHECK (data_final >= data_inicial)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;


-- =====================================================
-- TABELA: tarefa
-- =====================================================

CREATE TABLE tarefa (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    titulo VARCHAR(200) NOT NULL,
    local VARCHAR(200) NULL,
    descricao TEXT NULL,

    status ENUM(
        'ativa',
        'inativa'
    ) NOT NULL DEFAULT 'ativa',

    PRIMARY KEY (id),

    INDEX idx_tarefa_status (status)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;


-- =====================================================
-- TABELA: plano_tarefa
-- =====================================================

CREATE TABLE plano_tarefa (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    plano_id BIGINT UNSIGNED NOT NULL,
    tarefa_id BIGINT UNSIGNED NOT NULL,

    data_hora DATETIME NOT NULL,
    prazo DATETIME NULL,

    status ENUM(
        'pendente',
        'iniciada',
        'finalizada'
    ) NOT NULL DEFAULT 'pendente',

    PRIMARY KEY (id),

    CONSTRAINT fk_plano_tarefa_plano
        FOREIGN KEY (plano_id)
        REFERENCES plano (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_plano_tarefa_tarefa
        FOREIGN KEY (tarefa_id)
        REFERENCES tarefa (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    INDEX idx_plano_tarefa_plano (plano_id),
    INDEX idx_plano_tarefa_tarefa (tarefa_id),
    INDEX idx_plano_tarefa_status (status),
    INDEX idx_plano_tarefa_data_hora (data_hora)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;