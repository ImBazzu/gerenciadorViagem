CREATE TABLE motoristas
(
    id BINARY(16) NOT NULL,
    nome VARCHAR(60) NOT NULL,
    cpf CHAR(11) NOT NULL,
    telefone CHAR(11) NOT NULL,
    tipo_motorista VARCHAR(20) NOT NULL,
    ativo bool not null,
    CONSTRAINT pk_motoristas PRIMARY KEY (id),
    CONSTRAINT uk_motorista_cpf UNIQUE (cpf)
);

