USE org_limp;


-- =====================================================
-- 1. LISTAR USUÁRIOS
-- =====================================================

SELECT
    id,
    foto,
    email,
    notificacao_email,
    notificacao_whatsapp,
    notificacao_sistema,
    tipo
FROM usuario
ORDER BY email;

-- =====================================================
-- 3. LISTAR PLANOS
-- =====================================================

SELECT
    id,
    data_inicial,
    data_final
FROM plano
ORDER BY data_inicial DESC;


-- =====================================================
-- 4. LISTAR TAREFAS
-- =====================================================

SELECT
    id,
    titulo,
    local,
    descricao,
    status
FROM tarefa
ORDER BY titulo;


-- =====================================================
-- 5. LISTAR APENAS TAREFAS ATIVAS
-- =====================================================

SELECT
    id,
    titulo,
    local,
    descricao
FROM tarefa
WHERE status = 'ativa'
ORDER BY titulo;


-- =====================================================
-- 6. LISTAR TAREFAS INATIVAS
-- =====================================================

SELECT
    id,
    titulo,
    local,
    descricao
FROM tarefa
WHERE status = 'inativa'
ORDER BY titulo;


-- =====================================================
-- 7. LISTAR TAREFAS DE UM PLANO
-- =====================================================

SELECT
    pt.id AS plano_tarefa_id,
    p.id AS plano_id,
    t.id AS tarefa_id,
    t.titulo,
    t.local,
    pt.data_hora,
    pt.prazo,
    pt.status
FROM plano_tarefa pt
INNER JOIN plano p
    ON p.id = pt.plano_id
INNER JOIN tarefa t
    ON t.id = pt.tarefa_id
WHERE p.id = 1
ORDER BY pt.data_hora;


-- =====================================================
-- 8. LISTAR TODOS OS PLANOS COM SUAS TAREFAS
-- =====================================================

SELECT
    p.id AS plano_id,
    p.data_inicial,
    p.data_final,

    pt.id AS plano_tarefa_id,

    t.id AS tarefa_id,
    t.titulo,
    t.local,

    pt.data_hora,
    pt.prazo,
    pt.status AS status_execucao

FROM plano p

INNER JOIN plano_tarefa pt
    ON pt.plano_id = p.id

INNER JOIN tarefa t
    ON t.id = pt.tarefa_id

ORDER BY
    p.data_inicial,
    pt.data_hora;


-- =====================================================
-- 9. LISTAR PLANOS COM QUANTIDADE DE TAREFAS
-- =====================================================

SELECT
    p.id,
    p.data_inicial,
    p.data_final,
    COUNT(pt.id) AS quantidade_tarefas
FROM plano p
LEFT JOIN plano_tarefa pt
    ON pt.plano_id = p.id
GROUP BY
    p.id,
    p.data_inicial,
    p.data_final
ORDER BY p.data_inicial DESC;


-- =====================================================
-- 10. RESUMO DO STATUS DAS TAREFAS DE UM PLANO
-- =====================================================

SELECT
    pt.status,
    COUNT(*) AS quantidade
FROM plano_tarefa pt
WHERE pt.plano_id = 1
GROUP BY pt.status
ORDER BY
    FIELD(
        pt.status,
        'pendente',
        'iniciada',
        'finalizada'
    );


-- =====================================================
-- 11. LISTAR TAREFAS PENDENTES
-- =====================================================

SELECT
    pt.id AS plano_tarefa_id,
    p.id AS plano_id,
    t.titulo,
    t.local,
    pt.data_hora,
    pt.prazo,
    pt.status
FROM plano_tarefa pt
INNER JOIN plano p
    ON p.id = pt.plano_id
INNER JOIN tarefa t
    ON t.id = pt.tarefa_id
WHERE pt.status = 'pendente'
ORDER BY pt.data_hora;


-- =====================================================
-- 12. LISTAR TAREFAS EM EXECUÇÃO
-- =====================================================

SELECT
    pt.id AS plano_tarefa_id,
    p.id AS plano_id,
    t.titulo,
    t.local,
    pt.data_hora,
    pt.prazo,
    pt.status
FROM plano_tarefa pt
INNER JOIN plano p
    ON p.id = pt.plano_id
INNER JOIN tarefa t
    ON t.id = pt.tarefa_id
WHERE pt.status = 'iniciada'
ORDER BY pt.data_hora;


-- =====================================================
-- 13. LISTAR TAREFAS FINALIZADAS
-- =====================================================

SELECT
    pt.id AS plano_tarefa_id,
    p.id AS plano_id,
    t.titulo,
    t.local,
    pt.data_hora,
    pt.prazo,
    pt.status
FROM plano_tarefa pt
INNER JOIN plano p
    ON p.id = pt.plano_id
INNER JOIN tarefa t
    ON t.id = pt.tarefa_id
WHERE pt.status = 'finalizada'
ORDER BY pt.data_hora DESC;


-- =====================================================
-- 14. TAREFAS ATRASADAS
-- =====================================================

SELECT
    pt.id AS plano_tarefa_id,
    p.id AS plano_id,
    t.titulo,
    t.local,
    pt.data_hora,
    pt.prazo,
    pt.status
FROM plano_tarefa pt
INNER JOIN plano p
    ON p.id = pt.plano_id
INNER JOIN tarefa t
    ON t.id = pt.tarefa_id
WHERE
    pt.prazo < NOW()
    AND pt.status <> 'finalizada'
ORDER BY pt.prazo;


-- =====================================================
-- 15. DASHBOARD - RESUMO GERAL
-- =====================================================

SELECT
    COUNT(*) AS total_tarefas,
    SUM(pt.status = 'pendente') AS pendentes,
    SUM(pt.status = 'iniciada') AS iniciadas,
    SUM(pt.status = 'finalizada') AS finalizadas
FROM plano_tarefa pt;


-- =====================================================
-- 16. DASHBOARD - RESUMO POR PLANO
-- =====================================================

SELECT
    p.id AS plano_id,
    p.data_inicial,
    p.data_final,

    COUNT(pt.id) AS total_tarefas,

    SUM(pt.status = 'pendente') AS pendentes,
    SUM(pt.status = 'iniciada') AS iniciadas,
    SUM(pt.status = 'finalizada') AS finalizadas

FROM plano p

LEFT JOIN plano_tarefa pt
    ON pt.plano_id = p.id

GROUP BY
    p.id,
    p.data_inicial,
    p.data_final

ORDER BY p.data_inicial DESC;