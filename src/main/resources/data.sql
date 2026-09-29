-- Dados iniciais para o sistema Org-limp

-- Usuários (senha: 123456)
INSERT IGNORE INTO usuarios (nome, email, senha, perfil, ativo) VALUES
('Maria Gerente', 'gerente@empresa.com', '$2b$10$1hNhYJBr8BIout8r5wmgtOq6IOBefchV/QK9EeFRB71ImQ14Ohkie', 'gerente', true),
('João Supervisor', 'supervisor@empresa.com', '$2b$10$1hNhYJBr8BIout8r5wmgtOq6IOBefchV/QK9EeFRB71ImQ14Ohkie', 'supervisor', true),
('Ana Supervisora', 'ana.supervisor@empresa.com', '$2b$10$1hNhYJBr8BIout8r5wmgtOq6IOBefchV/QK9EeFRB71ImQ14Ohkie', 'supervisor', true);

-- Tarefas
INSERT IGNORE INTO tarefas (titulo, local, data, hora, prazo, supervisor_id, status, observacao, criado_em) VALUES
('Limpeza do salão principal', 'Salão de vendas', '2026-08-26', '09:00', '2026-08-26', 2, 'pendente', 'Priorizar corredores centrais.', '2026-08-26T08:00:00'),
('Higienização dos banheiros', 'Banheiros', '2026-08-26', '10:30', '2026-08-26', 2, 'iniciada', 'Repor materiais após a execução.', '2026-08-26T08:20:00'),
('Limpeza do depósito', 'Depósito', '2026-08-26', '14:00', '2026-08-27', 3, 'em_andamento', '', '2026-08-26T08:40:00'),
('Limpeza da entrada', 'Entrada principal', '2026-08-25', '08:00', '2026-08-25', 2, 'concluida', 'Atividade concluída.', '2026-08-25T08:00:00');

-- Notificações
INSERT IGNORE INTO notificacoes (destinatario_id, titulo, mensagem, tipo, lida, data) VALUES
(2, 'Nova tarefa atribuída', 'Você recebeu a tarefa "Limpeza do salão principal".', 'tarefa', false, '2026-08-26T08:00:00'),
(1, 'Tarefa atualizada', 'A tarefa "Higienização dos banheiros" foi iniciada.', 'status', false, '2026-08-26T10:00:00');

-- Configurações
INSERT IGNORE INTO configuracoes (empresa, email, whatsapp, notificar_email, notificar_whats_app) VALUES
('Minha Empresa', 'gestao@empresa.com', '', true, true);
