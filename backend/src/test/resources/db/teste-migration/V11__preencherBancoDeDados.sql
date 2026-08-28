
INSERT INTO regra_viagem (
    id,
    capacidade_maxima,
    tempo_tolerancia
)
VALUES (
           UUID_TO_BIN(UUID()),
           4,
           1
       );

INSERT INTO cidades (id, nome, tempo_viagem) VALUES
                                                 (UUID_TO_BIN(UUID()), 'ARACAJU', 60),
                                                 (UUID_TO_BIN(UUID()), 'ITABAIANA', 45),
                                                 (UUID_TO_BIN(UUID()), 'LAGARTO', 30),
                                                 (UUID_TO_BIN(UUID()), 'ESTANCIA', 50);

INSERT INTO destinos (id, nome, cidade_id)
SELECT UUID_TO_BIN(UUID()), 'HOSPITAL DE CIRURGIA', id
FROM cidades
WHERE nome = 'ARACAJU';

INSERT INTO destinos (id, nome, cidade_id)
SELECT UUID_TO_BIN(UUID()), 'HOSPITAL UNIVERSITARIO', id
FROM cidades
WHERE nome = 'ARACAJU';

INSERT INTO destinos (id, nome, cidade_id)
SELECT UUID_TO_BIN(UUID()), 'CLINICA SÃO LUCAS', id
FROM cidades
WHERE nome = 'ITABAIANA';

INSERT INTO destinos (id, nome, cidade_id)
SELECT UUID_TO_BIN(UUID()), 'HOSPITAL REGIONAL', id
FROM cidades
WHERE nome = 'LAGARTO';

INSERT INTO destinos (id, nome, cidade_id)
SELECT UUID_TO_BIN(UUID()), 'CLINICA SANTA MARIA', id
FROM cidades
WHERE nome = 'ESTANCIA';

INSERT INTO conjuntos_cidades (
    id,
    regra_viagem_id,
    nome
)
SELECT
    UUID_TO_BIN(UUID()),
    id,
    'GRANDE ARACAJU'
FROM regra_viagem
LIMIT 1;

INSERT INTO conjuntos_cidades (
    id,
    regra_viagem_id,
    nome
)
SELECT
    UUID_TO_BIN(UUID()),
    id,
    'INTERIOR'
FROM regra_viagem
LIMIT 1;


INSERT INTO conjunto_cidade (cidade_id, conjunto_id)
SELECT c.id, cc.id
FROM cidades c
         JOIN conjuntos_cidades cc
WHERE c.nome = 'ARACAJU'
  AND cc.nome = 'GRANDE ARACAJU';

INSERT INTO conjunto_cidade (cidade_id, conjunto_id)
SELECT c.id, cc.id
FROM cidades c
         JOIN conjuntos_cidades cc
WHERE c.nome = 'ESTANCIA'
  AND cc.nome = 'GRANDE ARACAJU';

INSERT INTO conjunto_cidade (cidade_id, conjunto_id)
SELECT c.id, cc.id
FROM cidades c
         JOIN conjuntos_cidades cc
WHERE c.nome = 'LAGARTO'
  AND cc.nome = 'INTERIOR';

INSERT INTO conjunto_cidade (cidade_id, conjunto_id)
SELECT c.id, cc.id
FROM cidades c
         JOIN conjuntos_cidades cc
WHERE c.nome = 'ITABAIANA'
  AND cc.nome = 'INTERIOR';


INSERT INTO motoristas (
    id,
    nome,
    cpf,
    telefone,
    tipo_motorista
)
VALUES
    (
        UUID_TO_BIN(UUID()),
        'JOSÉ SANTOS',
        '24067388098',
        '79999999991',
        'FIXO_ONIBUS'
    ),
    (
        UUID_TO_BIN(UUID()),
        'CARLOS OLIVEIRA',
        '22222222222',
        '79999999992',
        'FIXO_CARRO'
    ),    (
        UUID_TO_BIN(UUID()),
        'JULIETA MENDONÇA',
        '44444444444',
        '79999999995',
        'FIXO_ONIBUS'
    ),
    (
        UUID_TO_BIN(UUID()),
        'FRANCISCO ALCANTRA',
        '33333333333',
        '79999999994',
        'TERCEIRIZADO'
    );


INSERT INTO pessoas (
    id,
    nome,
    cpf,
    telefone,
    endereco,
    tipo_pessoa
)
VALUES
    (UUID_TO_BIN(UUID()), 'Maria Silva', '33333333333', '79999999993', 'Centro', 'COMUM'),
    (UUID_TO_BIN(UUID()), 'João Souza', '44444444444', '79999999994', 'Centro', 'SOMENTE_CARRO'),
    (UUID_TO_BIN(UUID()), 'Ana Santos', '24067388098', '79999999995', 'Centro', 'CARRO_OU_VAN'),
    (UUID_TO_BIN(UUID()), 'Pedro Lima', '66666666666', '79999999996', 'Centro', 'COMUM'),
    (UUID_TO_BIN(UUID()), 'Lucas Ferreira', '77777777777', '79999999997', 'Centro', 'SOMENTE_CARRO'),
    (UUID_TO_BIN(UUID()), 'Julia Alves', '88888888888', '79999999998', 'Centro', 'CARRO_OU_VAN'),
    (UUID_TO_BIN(UUID()), 'Ricardo Costa', '99999999999', '79999999999', 'Centro', 'COMUM'),
    (UUID_TO_BIN(UUID()), 'Fernanda Rocha', '10101010101', '79999999910', 'Centro', 'COMUM');


INSERT INTO listas (
    id,
    titulo,
    descricao,
    data,
    tipo_veiculo
)
VALUES
    (
        UUID_TO_BIN(UUID()),
        'VIAGENS SEGUNDA',
        'ListaDoDia da segunda-feira',
        '2026-06-15',
        'CARRO'
    ),
    (
        UUID_TO_BIN(UUID()),
        'VIAGENS TERCA',
        'ListaDoDia da terça-feira',
        '2026-06-16',
        'VAN'
    ),    (
        UUID_TO_BIN(UUID()),
        'VIAGENS QUARTA',
        'ListaDoDia da quarta-feira',
        '2026-06-17',
        'ONIBUS'
    );

INSERT INTO viagens (
    id,
    motorista_id,
    hora,
    data
)
SELECT
    UUID_TO_BIN(UUID()),
    id,
    '05:30:00',
    '2026-06-15'
FROM motoristas
WHERE nome = 'José Santos';

INSERT INTO viagens (
    id,
    motorista_id,
    hora,
    data
)
SELECT
    UUID_TO_BIN(UUID()),
    id,
    '06:00:00',
    '2026-06-15'
FROM motoristas
WHERE nome = 'Carlos Oliveira';

INSERT INTO viagens (
    id,
    motorista_id,
    hora,
    data
)
SELECT
    UUID_TO_BIN(UUID()),
    id,
    '07:00:00',
    '2026-06-16'
FROM motoristas
WHERE nome = 'José Santos';

-- ================================================================
-- PASSAGEIROS
-- ListaDoDia CARRO (Segunda, 2026-06-15)
-- ----------------------------------------------------------------
-- Cenário ①: Maria + Ana → mesmo conjunto GRANDE ARACAJU,
--             destinos diferentes — devem agrupar no mesmo carro
-- Cenário ②: João (SOMENTE_CARRO) → mesmo conjunto, testa
--             restrição de tipo_pessoa
-- ================================================================
INSERT INTO passageiros (
    id, lista_id, pessoa_id, viagem_id, destino_id,
    acompanhantes, hora_chegada, buscar
)

-- Maria Silva → Hospital de Cirurgia (Aracaju)
SELECT
    UUID_TO_BIN(UUID()),
    l.id,
    p.id,
    NULL,
    d.id,
    0,
    '05:00:00',
    TRUE
FROM listas l
         JOIN pessoas p  ON p.nome = 'Maria Silva'
         JOIN destinos d ON d.nome = 'HOSPITAL DE CIRURGIA'
WHERE l.titulo = 'VIAGENS SEGUNDA'

UNION ALL

-- Ana Santos → Hospital Universitário (Aracaju) · tipo CARRO_OU_VAN
SELECT
    UUID_TO_BIN(UUID()),
    l.id,
    p.id,
    NULL,
    d.id,
    0,
    '05:02:00',
    FALSE
FROM listas l
         JOIN pessoas p  ON p.nome = 'Ana Santos'
         JOIN destinos d ON d.nome = 'HOSPITAL UNIVERSITARIO'
WHERE l.titulo = 'VIAGENS SEGUNDA'

UNION ALL

-- João Souza → Clínica Santa Maria (Estância) · SOMENTE_CARRO
-- Estância está no conjunto GRANDE ARACAJU → agrupa com Maria/Ana
-- Testa: tipo_pessoa SOMENTE_CARRO não embarca em VAN/ônibus
SELECT
    UUID_TO_BIN(UUID()),
    l.id,
    p.id,
    NULL,
    d.id,
    1,        -- 1 acompanhante → ocupa 2 vagas
    '05:03:00',
    TRUE
FROM listas l
         JOIN pessoas p  ON p.nome = 'João Souza'
         JOIN destinos d ON d.nome = 'CLINICA SANTA MARIA'
WHERE l.titulo = 'VIAGENS SEGUNDA';


-- ================================================================
-- PASSAGEIROS
-- ListaDoDia VAN (Terça, 2026-06-16)
-- ----------------------------------------------------------------
-- Cenário ③: Pedro + Lucas → mesma cidade (Lagarto/INTERIOR)
-- Cenário ③: Julia       → Itabaiana, mesmo conjunto INTERIOR
--            → os três devem agrupar juntos
-- Cenário ④: Ricardo     → Aracaju, mas listaDoDia é VAN e Aracaju
--            aparece em GRANDE ARACAJU; testa agrupamento
--            cruzado ou isolamento dependendo da regra vigente
-- ================================================================
INSERT INTO passageiros (
    id, lista_id, pessoa_id, viagem_id, destino_id,
    acompanhantes, hora_chegada, buscar
)

-- Pedro Lima → Hospital Regional (Lagarto)
SELECT
    UUID_TO_BIN(UUID()),
    l.id,
    p.id,
    NULL,
    d.id,
    0,
    '06:30:00',
    TRUE
FROM listas l
         JOIN pessoas p  ON p.nome = 'Pedro Lima'
         JOIN destinos d ON d.nome = 'HOSPITAL REGIONAL'
WHERE l.titulo = 'Viagens Terca'

UNION ALL

-- Lucas Ferreira → Hospital Regional (Lagarto) · SOMENTE_CARRO
-- Mesma cidade e destino que Pedro → agrupamento por cidade exata
SELECT
    UUID_TO_BIN(UUID()),
    l.id,
    p.id,
    NULL,
    d.id,
    0,
    '06:31:00',
    FALSE
FROM listas l
         JOIN pessoas p  ON p.nome = 'Lucas Ferreira'
         JOIN destinos d ON d.nome = 'HOSPITAL REGIONAL'
WHERE l.titulo = 'Viagens Terca'

UNION ALL

-- Julia Alves → Clínica São Lucas (Itabaiana) · CARRO_OU_VAN
-- Itabaiana está no conjunto INTERIOR junto com Lagarto
-- → deve agrupar com Pedro e Lucas
SELECT
    UUID_TO_BIN(UUID()),
    l.id,
    p.id,
    NULL,
    d.id,
    0,
    '06:29:00',
    TRUE
FROM listas l
         JOIN pessoas p  ON p.nome = 'Julia Alves'
         JOIN destinos d ON d.nome = 'CLINICA SAO LUCAS'
WHERE l.titulo = 'Viagens Terca'

UNION ALL

-- Ricardo Costa → Hospital de Cirurgia (Aracaju)
-- Aracaju está em GRANDE ARACAJU, não em INTERIOR
-- → cidade sem conjunto compatível nesta listaDoDia; fica em viagem isolada
SELECT
    UUID_TO_BIN(UUID()),
    l.id,
    p.id,
    NULL,
    d.id,
    0,
    '06:00:00',
    FALSE
FROM listas l
         JOIN pessoas p  ON p.nome = 'Ricardo Costa'
         JOIN destinos d ON d.nome = 'HOSPITAL DE CIRURGIA'
WHERE l.titulo = 'Viagens Terca';