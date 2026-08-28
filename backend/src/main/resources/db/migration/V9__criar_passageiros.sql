CREATE TABLE passageiros
(
    id BINARY(16) NOT NULL,
    lista_id BINARY(16) NOT NULL,
    pessoa_id BINARY(16) NOT NULL,
    viagem_id BINARY(16) NULL,
    destino_id BINARY(16) NOT NULL,
    acompanhantes INT NOT NULL,
    hora_chegada  time NOT NULL,
    buscar BIT(1) NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT pk_passageiros PRIMARY KEY (id),
    CONSTRAINT uk_passageiro_lista_paciente UNIQUE (lista_id,pessoa_id),
    CONSTRAINT FK_PASSAGEIROS_ON_DESTINO FOREIGN KEY (destino_id) REFERENCES destinos(id),
    CONSTRAINT FK_PASSAGEIROS_ON_LISTA FOREIGN KEY (lista_id) REFERENCES listas (id),
    CONSTRAINT FK_PASSAGEIROS_ON_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoas (id),
    CONSTRAINT FK_PASSAGEIROS_ON_VIAGEM FOREIGN KEY (viagem_id) REFERENCES viagens (id)
);