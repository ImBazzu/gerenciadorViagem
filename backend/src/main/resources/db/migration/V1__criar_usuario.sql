CREATE TABLE usuarios(
    id BIGINT AUTO_INCREMENT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT uk_usuario_nome UNIQUE(nome),
    CONSTRAINT pk_usuarios PRIMARY KEY (id)
);


insert into usuarios(nome, senha, role)
values('admin',
     '$2a$12$dBJ/a7S/gN.EY9NR.gfwfeL3QX3RPVNfiWCFw64uH0QL0GMbHzQ5C',
     'ADMIN');