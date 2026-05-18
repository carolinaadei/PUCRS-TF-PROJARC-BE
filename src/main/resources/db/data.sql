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

-- Inserção dos itens de estoque
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (1, 30, 1) ON CONFLICT DO NOTHING;
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (2, 30, 2) ON CONFLICT DO NOTHING;
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (3, 30, 3) ON CONFLICT DO NOTHING;
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (4, 30, 4) ON CONFLICT DO NOTHING;
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (5, 30, 5) ON CONFLICT DO NOTHING;
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (6, 30, 6) ON CONFLICT DO NOTHING;
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (7, 30, 7) ON CONFLICT DO NOTHING;
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (8, 30, 8) ON CONFLICT DO NOTHING;
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (9, 30, 9) ON CONFLICT DO NOTHING;

-- Inserção das receitas
INSERT INTO receitas (id, titulo) VALUES (1, 'Pizza calabresa') ON CONFLICT DO NOTHING;
INSERT INTO receitas (id, titulo) VALUES (2, 'Pizza queijo e presunto') ON CONFLICT DO NOTHING;
INSERT INTO receitas (id, titulo) VALUES (3, 'Pizza margherita') ON CONFLICT DO NOTHING;

-- Associação dos ingredientes à receita Pizza calabresa
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 6) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 3) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 5) ON CONFLICT DO NOTHING;
-- Associação dos ingredientes à receita Pizza queijo e presunto
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 1) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 6) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 3) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 4) ON CONFLICT DO NOTHING;
-- Associação dos ingredientes à receita Pizza margherita
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 1) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 6) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 3) ON CONFLICT DO NOTHING;
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 8) ON CONFLICT DO NOTHING;

-- Inserção dos produtos
INSERT INTO produtos (id, descricao, preco) VALUES (1, 'Pizza calabresa', 5500) ON CONFLICT DO NOTHING;
INSERT INTO produtos (id, descricao, preco) VALUES (2, 'Pizza queijo e presunto', 6000) ON CONFLICT DO NOTHING;
INSERT INTO produtos (id, descricao, preco) VALUES (3, 'Pizza margherita', 4000) ON CONFLICT DO NOTHING;

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
