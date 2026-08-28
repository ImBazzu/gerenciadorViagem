ALTER TABLE passageiros
    DROP FOREIGN KEY FK_PASSAGEIROS_ON_PACIENTE;

ALTER TABLE passageiros
    ADD pessoa_id BIGINT NULL;

ALTER TABLE passageiros
    MODIFY pessoa_id BIGINT NOT NULL;

ALTER TABLE passageiros
    ADD CONSTRAINT FK_PASSAGEIROS_ON_PESSOA FOREIGN KEY (pessoa_id) REFERENCES pacientes (id);

ALTER TABLE passageiros
    DROP COLUMN paciente_id;

ALTER TABLE passageiros
    ADD CONSTRAINT uk_passageiro_lista_paciente UNIQUE (lista_id);
ALTER TABLE passageiros
    DROP FOREIGN KEY FK_PASSAGEIROS_ON_PACIENTE;

ALTER TABLE passageiros
    ADD pessoa_id BIGINT NULL;

ALTER TABLE passageiros
    MODIFY pessoa_id BIGINT NOT NULL;

ALTER TABLE passageiros
    ADD CONSTRAINT FK_PASSAGEIROS_ON_PESSOA FOREIGN KEY (pessoa_id) REFERENCES pacientes (id);

ALTER TABLE passageiros
    DROP COLUMN paciente_id;

ALTER TABLE passageiros
    ADD CONSTRAINT uk_passageiro_lista_paciente UNIQUE (lista_id);