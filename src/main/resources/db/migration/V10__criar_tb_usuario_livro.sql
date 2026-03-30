CREATE TABLE tb_usuario_livro (
                                  id BIGSERIAL PRIMARY KEY,

                                  status VARCHAR(50) NOT NULL,

                                  usuario_id BIGINT NOT NULL,
                                  livro_id BIGINT NOT NULL,

                                  CONSTRAINT fk_usuario
                                      FOREIGN KEY (usuario_id)
                                          REFERENCES tb_usuario(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_livro
                                      FOREIGN KEY (livro_id)
                                          REFERENCES tb_livros(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT uk_usuario_livro
                                      UNIQUE (usuario_id, livro_id)
);