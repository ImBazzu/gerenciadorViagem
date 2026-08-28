CREATE TABLE listas
(
    id BINARY(16) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NULL,
    data DATE NOT NULL,
    tipo_veiculo VARCHAR(20) NOT NULL,
    CONSTRAINT pk_listas PRIMARY KEY (id)
);