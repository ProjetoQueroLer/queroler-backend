CREATE TABLE tb_livro_autor (
                                livro_id BIGINT NOT NULL,
                                autor_id BIGINT NOT NULL,

                                PRIMARY KEY (livro_id, autor_id),

                                CONSTRAINT fk_livro
                                    FOREIGN KEY (livro_id)
                                        REFERENCES tb_livros(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_autor
                                    FOREIGN KEY (autor_id)
                                        REFERENCES tb_autores(id)
                                        ON DELETE CASCADE
);