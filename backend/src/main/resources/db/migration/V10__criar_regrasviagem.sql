CREATE TABLE regra_viagem
(
    id BINARY(16) NOT NULL,
    capacidade_maxima INT NOT NULL,
    tempo_tolerancia BIGINT NOT NULL,
    CONSTRAINT pk_regra_viagem PRIMARY KEY (id)
);