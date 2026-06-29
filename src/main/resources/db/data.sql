-- Inserção dos clientes
INSERT INTO clientes (cpf, nome, celular, endereco, email) VALUES ('9001', 'Huguinho Pato', '51985744566', 'Rua das Flores, 100', 'huguinho.pato@email.com') ON CONFLICT DO NOTHING;
INSERT INTO clientes (cpf, nome, celular, endereco, email) VALUES ('9002', 'Luizinho Pato', '5199172079', 'Av. Central, 200', 'zezinho.pato@email.com') ON CONFLICT DO NOTHING;

-- Inserção dos ingredientes
INSERT INTO ingredientes (id, descricao) VALUES (1, 'Disco de pizza') ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (2, 'Porcao de tomate') ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (3, 'Porcao de mussarela') ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (4, 'Porcao de presunto') ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (5, 'Porcao de calabresa') ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (6, 'Molho de tomate (200ml)') ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (7, 'Porcao de azeitona') ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (8, 'Porcao de oregano') ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (9, 'Porcao de cebola') ON CONFLICT DO NOTHING;

-- As quantidades reais em estoque (itens_estoque) vivem apenas no banco do
-- estoque-service; o monólito só mantém o catálogo de ingredientes e receitas.

-- Inserção das receitas
INSERT INTO receitas (id, titulo) VALUES (1, 'Pizza calabresa') ON CONFLICT DO NOTHING;
INSERT INTO receitas (id, titulo) VALUES (2, 'Pizza queijo e presunto') ON CONFLICT DO NOTHING;
INSERT INTO receitas (id, titulo) VALUES (3, 'Pizza margherita') ON CONFLICT DO NOTHING;

-- Associação dos ingredientes à receita Pizza calabresa (com porção necessária por unidade)
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (1, 1, 1) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (1, 6, 1) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (1, 3, 2) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (1, 5, 2) ON CONFLICT DO NOTHING;
-- Associação dos ingredientes à receita Pizza queijo e presunto
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (2, 1, 1) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (2, 6, 1) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (2, 3, 2) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (2, 4, 2) ON CONFLICT DO NOTHING;
-- Associação dos ingredientes à receita Pizza margherita
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (3, 1, 1) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (3, 6, 1) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (3, 3, 2) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade) VALUES (3, 8, 1) ON CONFLICT DO NOTHING;

-- Inserção dos produtos
INSERT INTO produtos (id, descricao, preco, indicacao_chef, disponivel) VALUES (1, 'Pizza calabresa', 5500, false, true) ON CONFLICT DO NOTHING;
INSERT INTO produtos (id, descricao, preco, indicacao_chef, disponivel) VALUES (2, 'Pizza queijo e presunto', 6000, true, true) ON CONFLICT DO NOTHING;
INSERT INTO produtos (id, descricao, preco, indicacao_chef, disponivel) VALUES (3, 'Pizza margherita', 4000, false, true) ON CONFLICT DO NOTHING;

-- Associação dos produtos com as receitas
INSERT INTO produto_receita (produto_id, receita_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO produto_receita (produto_id, receita_id) VALUES (2, 2) ON CONFLICT DO NOTHING;
INSERT INTO produto_receita (produto_id, receita_id) VALUES (3, 3) ON CONFLICT DO NOTHING;

-- Inserção dos cardápios
INSERT INTO cardapios (id, titulo) VALUES (1, 'Cardapio de Agosto') ON CONFLICT DO NOTHING;
INSERT INTO cardapios (id, titulo) VALUES (2, 'Cardapio de Setembro') ON CONFLICT DO NOTHING;

-- Associação dos cardápios com os produtos
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (1, 2) ON CONFLICT DO NOTHING;
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (1, 3) ON CONFLICT DO NOTHING;
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (2, 1) ON CONFLICT DO NOTHING;
INSERT INTO cardapio_produto (cardapio_id, produto_id) VALUES (2, 3) ON CONFLICT DO NOTHING;

-- Pedido aprovado (pode ser cancelado)
INSERT INTO pedidos (id, cliente_cpf, status, valor, impostos, desconto, valor_cobrado)
VALUES (1, '9001', 'APROVADO', 5500, 550, 0, 6050) ON CONFLICT DO NOTHING;

INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade)
VALUES (1, 1, 1, 1) ON CONFLICT DO NOTHING;

-- Pedido já pago (não pode ser cancelado)
INSERT INTO pedidos (id, cliente_cpf, status, valor, impostos, desconto, valor_cobrado, data_hora_pagamento)
VALUES (2, '9002', 'PAGO', 6000, 600, 0, 6600, '2026-05-01 14:30:00') ON CONFLICT DO NOTHING;

INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade)
VALUES (2, 2, 2, 1) ON CONFLICT DO NOTHING;

-- Pedido entregue para teste do UC8
INSERT INTO pedidos (id, cliente_cpf, status, valor, impostos, desconto, valor_cobrado, data_hora_pagamento)
VALUES (3, '9001', 'ENTREGUE', 5500, 550, 0, 6050, '2026-05-01 14:30:00') ON CONFLICT DO NOTHING;

INSERT INTO itens_pedido (id, pedido_id, produto_id, quantidade)
VALUES (3, 3, 1, 1) ON CONFLICT DO NOTHING;

-- Histórico de status do pedido 1 (APROVADO)
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (1, 'NOVO',     '2026-05-14 10:00:00', 'cliente') ON CONFLICT DO NOTHING;
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (1, 'APROVADO', '2026-05-14 10:05:00', 'sistema') ON CONFLICT DO NOTHING;

-- Histórico de status do pedido 2 (PAGO)
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (2, 'NOVO',     '2026-05-01 14:00:00', 'cliente') ON CONFLICT DO NOTHING;
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (2, 'APROVADO', '2026-05-01 14:10:00', 'sistema') ON CONFLICT DO NOTHING;
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (2, 'PAGO',     '2026-05-01 14:30:00', 'cliente') ON CONFLICT DO NOTHING;

-- Histórico de status do pedido 3 (ENTREGUE)
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (3, 'NOVO',       '2026-05-01 13:00:00', 'cliente') ON CONFLICT DO NOTHING;
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (3, 'APROVADO',   '2026-05-01 13:05:00', 'sistema') ON CONFLICT DO NOTHING;
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (3, 'PAGO',       '2026-05-01 13:30:00', 'cliente') ON CONFLICT DO NOTHING;
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (3, 'AGUARDANDO', '2026-05-01 13:31:00', 'cozinha') ON CONFLICT DO NOTHING;
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (3, 'PREPARACAO', '2026-05-01 13:45:00', 'cozinha') ON CONFLICT DO NOTHING;
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (3, 'PRONTO',     '2026-05-01 14:10:00', 'cozinha') ON CONFLICT DO NOTHING;
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (3, 'TRANSPORTE', '2026-05-01 14:15:00', 'entregador') ON CONFLICT DO NOTHING;
INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) VALUES (3, 'ENTREGUE',   '2026-05-01 14:30:00', 'entregador') ON CONFLICT DO NOTHING;