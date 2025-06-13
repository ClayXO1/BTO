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

CREATE TABLE partida(
	id SERIAL PRIMARY KEY,
	time_casa INT REFERENCES time(id) ON DELETE SET NULL,
	time_visitante INT REFERENCES time(id) ON DELETE SET NULL,
	partida_data DATE,
	partida_hora TIME,
	partida_local INT REFERENCES time(id) ON DELETE SET NULL
);

CREATE OR REPLACE PROCEDURE inserir_jogador(
    IN nome VARCHAR(50),
    IN idade INT,
    IN time_id INT,
    IN nacionalidade_id INT,
    IN posicao_id INT,
    IN tecnico INT,
    OUT resultado TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    tecnico_fk INT;
BEGIN
    SELECT tecnico_id INTO tecnico_fk FROM time WHERE id = tecnico;

    IF tecnico_fk IS NULL THEN
        resultado := 'Erro: Técnico não encontrado.';
        RETURN;
    END IF;

    INSERT INTO jogador (nome, idade, time_id, nacionalidade_id, posicao_id, tecnico)
    VALUES (nome, idade, time_id, nacionalidade_id, posicao_id, tecnico_fk);

    resultado := 'Jogador inserido com sucesso!';
END;
$$;

CREATE OR REPLACE PROCEDURE marcar_partida(
	IN time_casa INT,
	IN time_visitante INT,
	IN partida_data DATE,
	IN partida_hora TIME,
	IN partida_local INT,
	OUT resultado TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
	partida_local_fk INT;
BEGIN
	SELECT partida_local INTO partida_local_fk FROM time WHERE id = partida_local;

	IF partida_local_fk IS NULL THEN
		resultado := 'Error: local não encontrado';
	END IF;

	INSERT INTO partida (time_casa, time_visitante, partida_data, partida_hora, partida_local)
	VALUES (time_casa, time_visitante, partida_data, partida_hora, partida_local_fk);

	resultado := 'Partida marcada com sucesso!';
END;
$$;

CREATE OR REPLACE VIEW vw_times AS 
SELECT 
	time.id AS time_id, 
	time.nome AS time_nome, 
	time.cidade AS time_cidade, 
	tecnico.nome AS tecnico_nome 
FROM 
	time 
JOIN tecnico ON time.tecnico_id = tecnico.id;

CREATE OR REPLACE VIEW vw_jogadores AS
SELECT 
    j.id AS jogador_id,
    j.nome AS jogador_nome,
    j.idade,
    time.nome AS time_nome,
    posicao.nome AS posicao_nome,
    nacionalidade.nome AS nacionalidade,
    tecnico.nome AS tecnico_nome
FROM 
    jogador j
JOIN time ON j.time_id = time.id
JOIN posicao ON j.posicao_id = posicao.id
JOIN nacionalidade  ON j.nacionalidade_id = nacionalidade.id
JOIN tecnico ON j.tecnico = tecnico.id;

CREATE OR REPLACE VIEW vw_partidas AS
SELECT
    p.id AS partida_id,
    tc.nome AS time_casa,
    tv.nome AS time_visitante,
    p.partida_data,
    p.partida_hora,
    tl.cidade AS partida_local
FROM
    partida p
LEFT JOIN time tc ON p.time_casa = tc.id
LEFT JOIN time tv ON p.time_visitante = tv.id
LEFT JOIN time tl ON p.partida_local = tl.id;
	

SELECT * FROM vw_jogadores;

SELECT * FROM vw_times;

SELECT * FROM vw_partidas;


