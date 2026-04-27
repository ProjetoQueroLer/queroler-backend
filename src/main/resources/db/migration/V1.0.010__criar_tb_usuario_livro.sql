CREATE TABLE IF NOT EXISTS tb_usuario_livro (
                                  usuario_id BIGINT NOT NULL,
                                  livro_id BIGINT NOT NULL,
                                  status VARCHAR(50) NOT NULL,

                                  CONSTRAINT pk_usuario_livro
                                      PRIMARY KEY (usuario_id, livro_id),

                                  CONSTRAINT fk_usuario_livro_usuario
                                      FOREIGN KEY (usuario_id)
                                          REFERENCES tb_usuario(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_usuario_livro_livro
                                      FOREIGN KEY (livro_id)
                                          REFERENCES tb_livros(id)
                                          ON DELETE CASCADE
);