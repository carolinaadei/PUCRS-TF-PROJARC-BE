INSERT INTO ingredientes (id, descricao) VALUES (1, 'Disco de pizza')        ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (2, 'Porcao de tomate')       ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (3, 'Porcao de mussarela')    ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (4, 'Porcao de presunto')     ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (5, 'Porcao de calabresa')    ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (6, 'Molho de tomate (200ml)') ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (7, 'Porcao de azeitona')     ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (8, 'Porcao de oregano')      ON CONFLICT DO NOTHING;
INSERT INTO ingredientes (id, descricao) VALUES (9, 'Porcao de cebola')       ON CONFLICT DO NOTHING;

INSERT INTO itens_estoque (ingrediente_id, quantidade) VALUES (1, 30) ON CONFLICT (ingrediente_id) DO NOTHING;
INSERT INTO itens_estoque (ingrediente_id, quantidade) VALUES (2, 30) ON CONFLICT (ingrediente_id) DO NOTHING;
INSERT INTO itens_estoque (ingrediente_id, quantidade) VALUES (3, 30) ON CONFLICT (ingrediente_id) DO NOTHING;
INSERT INTO itens_estoque (ingrediente_id, quantidade) VALUES (4, 30) ON CONFLICT (ingrediente_id) DO NOTHING;
INSERT INTO itens_estoque (ingrediente_id, quantidade) VALUES (5, 30) ON CONFLICT (ingrediente_id) DO NOTHING;
INSERT INTO itens_estoque (ingrediente_id, quantidade) VALUES (6, 30) ON CONFLICT (ingrediente_id) DO NOTHING;
INSERT INTO itens_estoque (ingrediente_id, quantidade) VALUES (7, 30) ON CONFLICT (ingrediente_id) DO NOTHING;
INSERT INTO itens_estoque (ingrediente_id, quantidade) VALUES (8, 30) ON CONFLICT (ingrediente_id) DO NOTHING;
INSERT INTO itens_estoque (ingrediente_id, quantidade) VALUES (9, 30) ON CONFLICT (ingrediente_id) DO NOTHING;
