CREATE TABLE viagens
(
    id BINARY(16) NOT NULL,
    motorista_id BINARY(16) NULL,
    hora TIME NOT NULL,
    data DATE NOT NULL,
    CONSTRAINT pk_viagens PRIMARY KEY (id),
    CONSTRAINT FK_VIAGENS_ON_MOTORISTA FOREIGN KEY (motorista_id) REFERENCES motoristas (id)
);