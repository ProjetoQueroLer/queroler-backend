CREATE TABLE IF NOT EXISTS tb_documento (
                              id BIGSERIAL PRIMARY KEY,
                              titulo VARCHAR(100) NOT NULL,
                              tipo_documento VARCHAR(50) NOT NULL,
                              conteudo TEXT NOT NULL,
                              ultima_alteracao TIMESTAMP
);