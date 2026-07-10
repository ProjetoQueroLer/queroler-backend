CREATE TABLE tb_metas_leitura (
                                  id BIGSERIAL PRIMARY KEY,

                                  ano INTEGER NOT NULL,
                                  meta_livros_ano INTEGER,
                                  meta_livros_mes INTEGER,
                                  meta_paginas_dia INTEGER,

                                  usuario_id BIGINT NOT NULL,

                                  CONSTRAINT fk_meta_leitura_usuario
                                      FOREIGN KEY (usuario_id)
                                          REFERENCES tb_usuario(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT uk_meta_leitura_usuario_ano
                                      UNIQUE (usuario_id, ano)
);

CREATE TABLE tb_livros_meta (
                                id BIGSERIAL PRIMARY KEY,

                                meta_leitura_id BIGINT NOT NULL,
                                livro_id BIGINT NOT NULL,

                                CONSTRAINT fk_livros_meta_meta
                                    FOREIGN KEY (meta_leitura_id)
                                        REFERENCES tb_metas_leitura(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_livros_meta_livro
                                    FOREIGN KEY (livro_id)
                                        REFERENCES tb_livros(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT uk_meta_livro
                                    UNIQUE (meta_leitura_id, livro_id)
);