CREATE TABLE posicao (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

CREATE TABLE nacionalidade (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

CREATE TABLE tecnico (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

CREATE TABLE time(
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cidade VARCHAR(50),
	tecnico_id INT REFERENCES tecnico(id) ON DELETE SET NULL
);

CREATE TABLE jogador (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    idade INT NOT NULL CHECK (idade > 0),
    time_id INT REFERENCES time(id) ON DELETE SET NULL,
    nacionalidade_id INT REFERENCES nacionalidade(id) ON DELETE SET NULL,
    posicao_id INT REFERENCES posicao(id) ON DELETE SET NULL,
    tecnico INT REFERENCES time(id) ON DELETE SET NULL
);

CREATE OR REPLACE VIEW vw_jogadores_detalhado AS
SELECT 
    jogador.id AS jogador_id,
    jogador.nome AS jogador_nome,
    jogador.idade,
    time.nome AS time_nome,
    posicao.nome AS posicao_nome,
    nacionalidade.nome AS nacionalidade,
    tecnico.nome AS tecnico_nome
FROM 
    jogador 
JOIN time ON jogador.time_id = time.id
JOIN posicao ON jogador.posicao_id = posicao.id
JOIN nacionalidade  ON jogador.nacionalidade_id = nacionalidade.id
JOIN tecnico ON jogador.tecnico = tecnico.id;


CREATE OR REPLACE VIEW vw_times AS 
SELECT 
	time.id AS time_id, 
	time.nome AS time_nome, 
	time.cidade AS time_cidade, 
	tecnico.nome AS tecnico_nome 
FROM 
	time 
JOIN tecnico ON time.tecnico_id = tecnico.id;


SELECT * FROM vw_jogadores_detalhado;


