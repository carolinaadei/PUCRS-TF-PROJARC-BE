-- ============================================================
--  Schema - Tele Pizza PUCRS
--  Cobre: Cliente, Pedido, ItemCardapio, Ingrediente
--  + tabelas de suporte (cardapio, receita, estoque)
-- ============================================================

DROP TABLE IF EXISTS itens_pedido;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS itens_estoque;
DROP TABLE IF EXISTS receitas;
DROP TABLE IF EXISTS itens_cardapio;
DROP TABLE IF EXISTS cardapios;
DROP TABLE IF EXISTS ingredientes;

-- ----------------------------------------------------------
-- Cardápio (catálogo do restaurante)
-- ----------------------------------------------------------
CREATE TABLE cardapios (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao   VARCHAR(120) NOT NULL,
    corrente    BOOLEAN      NOT NULL DEFAULT FALSE
);

-- ----------------------------------------------------------
-- Ingrediente (matéria-prima do estoque)
-- ----------------------------------------------------------
CREATE TABLE ingredientes (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome    VARCHAR(100) NOT NULL UNIQUE,
    unidade VARCHAR(20)  NOT NULL           -- ex: "g", "ml", "un"
);

-- ----------------------------------------------------------
-- Item de cardápio (produto que o cliente pode pedir)
-- ----------------------------------------------------------
CREATE TABLE itens_cardapio (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    cardapio_id  BIGINT         NOT NULL,
    descricao    VARCHAR(200)   NOT NULL,
    preco_unit   NUMERIC(10,2)  NOT NULL,
    disponivel   BOOLEAN        NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_ic_cardapio FOREIGN KEY (cardapio_id) REFERENCES cardapios(id)
);

-- ----------------------------------------------------------
-- Receita: quantos ingredientes cada item de cardápio usa
-- ----------------------------------------------------------
CREATE TABLE receitas (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_cardapio_id BIGINT         NOT NULL,
    ingrediente_id   BIGINT         NOT NULL,
    quantidade       NUMERIC(10,3)  NOT NULL,
    CONSTRAINT fk_rec_item   FOREIGN KEY (item_cardapio_id) REFERENCES itens_cardapio(id),
    CONSTRAINT fk_rec_ingred FOREIGN KEY (ingrediente_id)   REFERENCES ingredientes(id)
);

-- ----------------------------------------------------------
-- Estoque: quantidade disponível de cada ingrediente
-- (organizado em porções conforme o enunciado)
-- ----------------------------------------------------------
CREATE TABLE itens_estoque (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    ingrediente_id BIGINT         NOT NULL UNIQUE,
    quantidade     NUMERIC(10,3)  NOT NULL DEFAULT 0,
    CONSTRAINT fk_est_ingred FOREIGN KEY (ingrediente_id) REFERENCES ingredientes(id)
);

-- ----------------------------------------------------------
-- Cliente
-- ----------------------------------------------------------
CREATE TABLE clientes (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome     VARCHAR(150)  NOT NULL,
    cpf      VARCHAR(14)   NOT NULL UNIQUE,
    celular  VARCHAR(20),
    endereco VARCHAR(300),
    email    VARCHAR(150)  NOT NULL UNIQUE,
    senha    VARCHAR(255)  NOT NULL     -- hash BCrypt
);

-- ----------------------------------------------------------
-- Pedido
-- ----------------------------------------------------------
CREATE TABLE pedidos (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id          BIGINT         NOT NULL,
    status              VARCHAR(20)    NOT NULL DEFAULT 'NOVO',
    -- status possíveis: NOVO | APROVADO | CANCELADO | PAGO |
    --                   AGUARDANDO | PREPARACAO | PRONTO |
    --                   TRANSPORTE | ENTREGUE
    endereco_entrega    VARCHAR(300)   NOT NULL,
    custo_itens         NUMERIC(10,2),
    desconto            NUMERIC(10,2),
    imposto             NUMERIC(10,2),
    custo_final         NUMERIC(10,2),
    criado_em           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ped_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- ----------------------------------------------------------
-- Itens do Pedido
-- ----------------------------------------------------------
CREATE TABLE itens_pedido (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id        BIGINT         NOT NULL,
    item_cardapio_id BIGINT         NOT NULL,
    quantidade       INT            NOT NULL CHECK (quantidade > 0),
    preco_unit       NUMERIC(10,2)  NOT NULL,  -- snapshot do preço no momento do pedido
    CONSTRAINT fk_ip_pedido FOREIGN KEY (pedido_id)        REFERENCES pedidos(id),
    CONSTRAINT fk_ip_item   FOREIGN KEY (item_cardapio_id) REFERENCES itens_cardapio(id)
);
