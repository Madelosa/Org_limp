USE org_limp;


-- =====================================================
-- DADOS DE SIMULAÇÃO
-- =====================================================


-- =====================================================
-- USUÁRIOS
-- =====================================================

INSERT INTO usuario (
    foto,
    email,
    senha,
    notificacao_email,
    notificacao_whatsapp,
    notificacao_sistema,
    tipo
) VALUES
(
    'https://randomuser.me/api/portraits/men/75.jpg',
    'admin@orglimp.com.br',
    '$2b$12$hash_simulado_admin',
    TRUE,
    TRUE,
    TRUE,
    'admin'
),
(
    'https://randomuser.me/api/portraits/men/32.jpg',
    'carlos.silva@orglimp.com.br',
    '$2b$12$hash_simulado_carlos',
    TRUE,
    TRUE,
    TRUE,
    'gerente'
),
(
    'https://randomuser.me/api/portraits/women/44.jpg',
    'mariana.souza@orglimp.com.br',
    '$2b$12$hash_simulado_mariana',
    TRUE,
    TRUE,
    TRUE,
    'gerente'
),
(
    'https://randomuser.me/api/portraits/men/46.jpg',
    'joao.oliveira@orglimp.com.br',
    '$2b$12$hash_simulado_joao',
    TRUE,
    FALSE,
    TRUE,
    'supervisor'
),
(
    'https://randomuser.me/api/portraits/women/65.jpg',
    'ana.costa@orglimp.com.br',
    '$2b$12$hash_simulado_ana',
    FALSE,
    TRUE,
    TRUE,
    'supervisor'
);


-- =====================================================
-- PLANOS
-- =====================================================

INSERT INTO plano (
    data_inicial,
    data_final
) VALUES
(
    '2026-09-01',
    '2026-09-07'
),
(
    '2026-09-08',
    '2026-09-14'
),
(
    '2026-09-15',
    '2026-09-21'
),
(
    '2026-09-22',
    '2026-09-28'
),
(
    '2026-09-29',
    '2026-10-05'
);


-- =====================================================
-- TAREFAS
-- =====================================================

INSERT INTO tarefa (
    titulo,
    local,
    descricao,
    status
) VALUES
(
    'Limpeza dos banheiros',
    'Bloco Administrativo',
    'Realizar limpeza completa dos banheiros, incluindo vasos sanitários, pias, espelhos e reposição de materiais.',
    'ativa'
),
(
    'Limpeza dos corredores',
    'Bloco Administrativo',
    'Varrer, lavar e manter os corredores livres de resíduos e obstáculos.',
    'ativa'
),
(
    'Higienização da recepção',
    'Recepção Principal',
    'Realizar limpeza dos pisos, balcões, portas de vidro e área de atendimento.',
    'ativa'
),
(
    'Limpeza da área externa',
    'Área Externa',
    'Realizar varrição, recolhimento de resíduos e limpeza das áreas externas do estabelecimento.',
    'ativa'
),
(
    'Higienização do refeitório',
    'Refeitório',
    'Limpar mesas, cadeiras, pisos, bancadas e demais superfícies do refeitório.',
    'ativa'
),
(
    'Limpeza dos vestiários',
    'Vestiários',
    'Realizar higienização dos vestiários, chuveiros, armários e pisos.',
    'ativa'
),
(
    'Limpeza do estacionamento',
    'Estacionamento',
    'Realizar varrição e retirada de resíduos do estacionamento.',
    'ativa'
),
(
    'Limpeza das salas administrativas',
    'Setor Administrativo',
    'Realizar limpeza das salas, mesas, cadeiras, pisos e lixeiras.',
    'ativa'
),
(
    'Limpeza dos vidros',
    'Bloco Administrativo',
    'Realizar limpeza dos vidros internos e externos das áreas administrativas.',
    'inativa'
),
(
    'Higienização do depósito',
    'Depósito',
    'Realizar limpeza e organização do piso e das áreas de circulação do depósito.',
    'ativa'
);


-- =====================================================
-- PLANO_TAREFA
-- =====================================================

INSERT INTO plano_tarefa (
    plano_id,
    tarefa_id,
    data_hora,
    prazo,
    status
) VALUES

-- =====================================================
-- PLANO 1
-- =====================================================

(
    1,
    1,
    '2026-09-01 07:00:00',
    '2026-09-01 08:00:00',
    'finalizada'
),
(
    1,
    2,
    '2026-09-01 08:00:00',
    '2026-09-01 09:00:00',
    'finalizada'
),
(
    1,
    3,
    '2026-09-01 09:00:00',
    '2026-09-01 10:00:00',
    'finalizada'
),

-- =====================================================
-- PLANO 2
-- =====================================================

(
    2,
    4,
    '2026-09-08 07:00:00',
    '2026-09-08 09:00:00',
    'finalizada'
),
(
    2,
    5,
    '2026-09-08 09:00:00',
    '2026-09-08 10:30:00',
    'iniciada'
),
(
    2,
    6,
    '2026-09-08 10:30:00',
    '2026-09-08 12:00:00',
    'pendente'
),

-- =====================================================
-- PLANO 3
-- =====================================================

(
    3,
    1,
    '2026-09-15 07:00:00',
    '2026-09-15 08:00:00',
    'pendente'
),
(
    3,
    7,
    '2026-09-15 08:00:00',
    '2026-09-15 10:00:00',
    'pendente'
),
(
    3,
    8,
    '2026-09-15 10:00:00',
    '2026-09-15 12:00:00',
    'pendente'
),

-- =====================================================
-- PLANO 4
-- =====================================================

(
    4,
    2,
    '2026-09-22 07:00:00',
    '2026-09-22 08:00:00',
    'pendente'
),
(
    4,
    5,
    '2026-09-22 08:00:00',
    '2026-09-22 09:30:00',
    'pendente'
),
(
    4,
    10,
    '2026-09-22 09:30:00',
    '2026-09-22 11:00:00',
    'pendente'
),

-- =====================================================
-- PLANO 5
-- =====================================================

(
    5,
    1,
    '2026-09-29 07:00:00',
    '2026-09-29 08:00:00',
    'pendente'
),
(
    5,
    4,
    '2026-09-29 08:00:00',
    '2026-09-29 10:00:00',
    'pendente'
),
(
    5,
    6,
    '2026-09-29 10:00:00',
    '2026-09-29 11:30:00',
    'pendente'
);