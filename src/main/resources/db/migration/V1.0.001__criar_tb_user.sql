CREATE TABLE IF NOT EXISTS tb_user (
                         id BIGSERIAL PRIMARY KEY,
                         login VARCHAR(255) NOT NULL UNIQUE ,
                         senha VARCHAR(255) NOT NULL,
                         perfil VARCHAR(50) NOT NULL
);