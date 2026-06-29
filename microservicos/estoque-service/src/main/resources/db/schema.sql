CREATE TABLE IF NOT EXISTS ingredientes (
    id   BIGINT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS itens_estoque (
    id             BIGSERIAL PRIMARY KEY,
    ingrediente_id BIGINT  NOT NULL UNIQUE,
    quantidade     INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (ingrediente_id) REFERENCES ingredientes (id)
);
