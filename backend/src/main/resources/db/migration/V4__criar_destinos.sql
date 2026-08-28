CREATE TABLE destinos
(
    id BINARY(16) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cidade_id BINARY(16) NOT NULL,
    habilitado bool not null,
    deletado bool not null,
    CONSTRAINT pk_destinos PRIMARY KEY (id),
    CONSTRAINT uk_destinos_nome_cidade UNIQUE (nome, cidade_id),
    CONSTRAINT FK_DESTINOS_ON_CIDADE FOREIGN KEY (cidade_id) REFERENCES cidades(id)
);