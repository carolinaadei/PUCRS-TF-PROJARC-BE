create table if not exists clientes(
  cpf varchar(15) not null primary key,
  nome varchar(100) not null,
  celular varchar(20) not null,
  endereco varchar(255) not null,
  email varchar(255) not null
);

create table if not exists ingredientes (
  id bigint primary key,
  descricao varchar(255) not null
);

create table if not exists itensEstoque(
    id bigint primary key,
    quantidade int,
    ingrediente_id bigint,
    foreign key (ingrediente_id) references ingredientes(id)
);

-- Tabela Receita
create table if not exists receitas (
  id bigint primary key,
  titulo varchar(255) not null
);

-- Tabela de relacionamento entre Receita e Ingrediente
create table if not exists receita_ingrediente (
  receita_id bigint not null,
  ingrediente_id bigint not null,
  primary key (receita_id, ingrediente_id),
  foreign key (receita_id) references receitas(id),
  foreign key (ingrediente_id) references ingredientes(id)
);

-- Tabela de Produtos
create table if not exists produtos (
  id bigint primary key,
  descricao varchar(255) not null,
  preco bigint,
  indicacao_chef boolean not null default false
);

-- Tabela de relacionamento entre Produto e Receita
create table if not exists produto_receita (
  produto_id bigint not null,
  receita_id bigint not null,
  primary key (produto_id,receita_id),
  foreign key (produto_id) references produtos(id),
  foreign key (receita_id) references receitas(id)
);

-- Tabela de Cardapios
create table if not exists cardapios (
  id bigint primary key,
  titulo varchar(255) not null
);

-- Tabela de relacionamento entre Cardapio e Produto
create table if not exists cardapio_produto (
  cardapio_id bigint not null,
  produto_id bigint not null,
  primary key (cardapio_id,produto_id),
  foreign key (cardapio_id) references cardapios(id),
  foreign key (produto_id) references produtos(id)
);

-- Tabela de Pedidos
create table if not exists pedidos (
  id bigserial primary key,
  cliente_cpf varchar(15) not null,
  status varchar(20) not null,
  valor double precision not null default 0,
  impostos double precision not null default 0,
  desconto double precision not null default 0,
  valor_cobrado double precision not null default 0,
  endereco_entrega varchar(255),
  data_hora_pagamento timestamp,
  cancelado_por varchar(100),
  data_hora_cancelamento timestamp,
  foreign key (cliente_cpf) references clientes(cpf)
);

-- Tabela de itens do pedido
create table if not exists itens_pedido (
  id bigserial primary key,
  pedido_id bigint not null,
  produto_id bigint not null,
  quantidade int not null,
  foreign key (pedido_id) references pedidos(id),
  foreign key (produto_id) references produtos(id)
);

create table if not exists pedido_status_historico (
  id bigserial primary key,
  pedido_id bigint not null,
  status varchar(30) not null,
  data_hora timestamp not null,
  responsavel varchar(50) not null,
  foreign key (pedido_id) references pedidos(id),
  unique (pedido_id, status)
);

create table if not exists usuarios (
  id bigint generated always as identity primary key,
  email varchar(255) unique not null,
  password varchar(255) not null,
  role varchar(50) not null
);
