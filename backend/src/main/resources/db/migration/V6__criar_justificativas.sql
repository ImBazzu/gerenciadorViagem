CREATE TABLE justificativas
(
    id BINARY(16) NOT NULL,
    descricacao VARCHAR(50) NOT NULL,
    ativo bool NOT NULL,
    CONSTRAINT pk_justificativas PRIMARY KEY (id)
);