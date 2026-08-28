CREATE TABLE conjuntos_cidades
(
    id              BINARY(16)   NOT NULL,
    regra_viagem_id BINARY(16)   NOT NULL,
    nome            VARCHAR(100) NOT NULL,
    CONSTRAINT pk_conjuntos_cidades PRIMARY KEY (id),
    CONSTRAINT FK_CONJUNTOS_CIDADES_ON_REGRA_VIAGEM FOREIGN KEY (regra_viagem_id) REFERENCES regra_viagem (id)
);

CREATE TABLE conjunto_cidade
(
    cidade_id   BINARY(16) NOT NULL,
    conjunto_id BINARY(16) NOT NULL,
    CONSTRAINT pk_conjunto_cidade PRIMARY KEY (cidade_id, conjunto_id),
    CONSTRAINT FK_CONJCIDADE_ON_CIDADE FOREIGN KEY (cidade_id) REFERENCES cidades (id),
    CONSTRAINT FK_CONJCIDADE_ON_CONJUNTO FOREIGN KEY (conjunto_id) REFERENCES conjuntos_cidades (id)
);