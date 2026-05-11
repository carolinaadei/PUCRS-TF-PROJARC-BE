-- ============================================================
--  Dados Iniciais - Tele Pizza PUCRS
-- ============================================================

-- Cardápio corrente (id=1, corrente=true)
INSERT INTO cardapios (id, descricao, corrente) VALUES
    (1, 'Cardápio Outubro 2025', TRUE),
    (2, 'Cardápio Promoção Verão', FALSE);

-- Ingredientes
INSERT INTO ingredientes (id, nome, unidade) VALUES
    (1,  'Massa de pizza 30cm',  'un'),
    (2,  'Molho de tomate',      'ml'),
    (3,  'Mussarela',            'g'),
    (4,  'Presunto',             'g'),
    (5,  'Calabresa fatiada',    'g'),
    (6,  'Cebola',               'g'),
    (7,  'Milho',                'g'),
    (8,  'Frango desfiado',      'g'),
    (9,  'Cheddar',              'g'),
    (10, 'Bacon',                'g'),
    (11, 'Refrigerante 350ml',   'un'),
    (12, 'Suco natural 300ml',   'un');

-- Itens de estoque (100 unidades de cada para testes)
INSERT INTO itens_estoque (ingrediente_id, quantidade) VALUES
    (1, 100), (2, 10000), (3, 10000), (4, 5000),
    (5, 5000), (6, 3000), (7, 3000), (8, 5000),
    (9, 3000), (10, 3000), (11, 200), (12, 200);

-- Itens do cardápio 1
INSERT INTO itens_cardapio (id, cardapio_id, descricao, preco_unit, disponivel) VALUES
    (1, 1, 'Pizza Margherita (30cm) - Molho, mussarela e manjericão',         42.90, TRUE),
    (2, 1, 'Pizza Calabresa (30cm) - Molho, mussarela e calabresa',           44.90, TRUE),
    (3, 1, 'Pizza Frango com Catupiry (30cm) - Frango, catupiry e milho',     46.90, TRUE),
    (4, 1, 'Pizza Quatro Queijos (30cm) - Mussarela, cheddar, gorgonzola e parmesão', 49.90, TRUE),
    (5, 1, 'Pizza Especial Bacon (30cm) - Mussarela, bacon e cebola caramelada', 52.90, TRUE),
    (6, 1, 'Refrigerante Lata 350ml',                                          6.90, TRUE),
    (7, 1, 'Suco Natural 300ml',                                               8.90, TRUE);

-- Receitas (ingredientes necessários por item)
-- Pizza Margherita
INSERT INTO receitas (item_cardapio_id, ingrediente_id, quantidade) VALUES
    (1, 1,  1),    -- 1 massa
    (1, 2, 80),    -- 80ml molho
    (1, 3, 150);   -- 150g mussarela

-- Pizza Calabresa
INSERT INTO receitas (item_cardapio_id, ingrediente_id, quantidade) VALUES
    (2, 1,  1),
    (2, 2, 80),
    (2, 3, 120),
    (2, 5, 100),   -- 100g calabresa
    (2, 6,  30);   -- 30g cebola

-- Pizza Frango com Catupiry
INSERT INTO receitas (item_cardapio_id, ingrediente_id, quantidade) VALUES
    (3, 1,  1),
    (3, 2, 80),
    (3, 3, 100),
    (3, 8, 120),   -- frango
    (3, 7,  50);   -- milho

-- Pizza Quatro Queijos
INSERT INTO receitas (item_cardapio_id, ingrediente_id, quantidade) VALUES
    (4, 1,  1),
    (4, 2, 80),
    (4, 3, 100),
    (4, 9,  60);   -- cheddar

-- Pizza Especial Bacon
INSERT INTO receitas (item_cardapio_id, ingrediente_id, quantidade) VALUES
    (5, 1,  1),
    (5, 2, 80),
    (5, 3, 120),
    (5, 10, 80),   -- bacon
    (5, 6,  40);   -- cebola

-- Refrigerante
INSERT INTO receitas (item_cardapio_id, ingrediente_id, quantidade) VALUES
    (6, 11, 1);

-- Suco
INSERT INTO receitas (item_cardapio_id, ingrediente_id, quantidade) VALUES
    (7, 12, 1);

-- Cliente de teste (senha: "senha123" em BCrypt)
INSERT INTO clientes (id, nome, cpf, celular, endereco, email, senha) VALUES
    (1, 'João Silva', '123.456.789-00', '(51) 99999-1234',
     'Rua das Flores, 100, Porto Alegre/RS', 'joao@email.com',
     '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTpyYMSzKey');
