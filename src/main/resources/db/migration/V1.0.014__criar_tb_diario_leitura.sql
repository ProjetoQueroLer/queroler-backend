CREATE TABLE IF NOT EXISTS tb_diario_leitura (
                                   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                                   usuario_id BIGINT NOT NULL,
                                   livro_id BIGINT NOT NULL,

                                   inicio_da_leitura TIMESTAMP,
                                   termino_da_leitura TIMESTAMP,
                                   paginas_lidas INT,

                                   nota INT,
                                   titulo_da_resenha VARCHAR(255),

                                   resenha TEXT,

                                   CONSTRAINT fk_diario_usuario_livro
                                       FOREIGN KEY (usuario_id, livro_id)
                                           REFERENCES tb_usuario_livro(usuario_id, livro_id)
                                           ON DELETE CASCADE
);