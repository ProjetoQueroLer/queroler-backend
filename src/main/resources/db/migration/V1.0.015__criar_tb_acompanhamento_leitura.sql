CREATE TABLE tb_acompanhamento_leitura (
                                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                                           pagina_inicial INT NOT NULL,
                                           pagina_final INT NOT NULL,

                                           comentario TEXT,

                                           diario_leitura_id BIGINT NOT NULL,

                                           CONSTRAINT fk_acompanhamento_diario
                                               FOREIGN KEY (diario_leitura_id)
                                                   REFERENCES tb_diario_leitura(id)
                                                   ON DELETE CASCADE
);

CREATE INDEX idx_acompanhamento_diario
    ON tb_acompanhamento_leitura(diario_leitura_id);