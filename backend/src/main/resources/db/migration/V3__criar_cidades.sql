CREATE TABLE cidades
(
    id BINARY(16) NOT NULL,
    nome VARCHAR(40) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    tempo_viagem BIGINT NOT NULL,
    arquivado bool not null,
    CONSTRAINT pk_cidades PRIMARY KEY (id),
    CONSTRAINT uc_cidades_nome_estado UNIQUE (nome,estado)
);
