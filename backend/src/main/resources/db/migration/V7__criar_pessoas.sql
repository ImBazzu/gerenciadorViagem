CREATE TABLE pessoas
(
    id BINARY(16) NOT NULL,
    nome VARCHAR(60) NOT NULL,
    cpf CHAR(11) NOT NULL,
    telefone CHAR(11) NOT NULL,
    endereco VARCHAR(255) NULL,
    observacao VARCHAR (255) NULL,
    ativo bool not null,
    CONSTRAINT pk_pacientes PRIMARY KEY (id),
    CONSTRAINT uk_paciente_cpf UNIQUE (cpf)
);