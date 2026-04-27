CREATE TABLE IF NOT EXISTS tb_notificacao (
                                id BIGSERIAL PRIMARY KEY,
                                notificacao TEXT NOT NULL,
                                data_de_criacao TIMESTAMP NOT NULL
);