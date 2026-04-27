CREATE TABLE IF NOT EXISTS tb_livros (
                           id BIGSERIAL PRIMARY KEY,
                           titulo VARCHAR(255) NOT NULL,
                           isbn VARCHAR(13) NOT NULL UNIQUE,
                           editora VARCHAR(255) NOT NULL,
                           ano_de_publicacao VARCHAR(4) NOT NULL,
                           numero_de_paginas INTEGER NOT NULL,
                           idioma VARCHAR(20) NOT NULL,
                           sinopse TEXT NOT NULL,
                           capa BYTEA
);