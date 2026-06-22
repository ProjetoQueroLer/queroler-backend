ALTER TABLE tb_diario_leitura
    DROP CONSTRAINT fk_diario_usuario_livro;

ALTER TABLE tb_usuario_livro RENAME TO tb_leitura;

ALTER TABLE tb_leitura DROP CONSTRAINT pk_usuario_livro;

ALTER TABLE tb_leitura
    ADD COLUMN IF NOT EXISTS id BIGSERIAL PRIMARY KEY;

ALTER TABLE tb_diario_leitura
    ADD COLUMN IF NOT EXISTS leitura_id BIGINT;

UPDATE tb_diario_leitura d
SET leitura_id = l.id
FROM tb_leitura l
WHERE d.usuario_id = l.usuario_id AND d.livro_id = l.livro_id;

ALTER TABLE tb_diario_leitura
    ALTER COLUMN leitura_id SET NOT NULL;

ALTER TABLE tb_diario_leitura
    ADD CONSTRAINT fk_diario_leitura
        FOREIGN KEY (leitura_id)
            REFERENCES tb_leitura(id)
            ON DELETE CASCADE;

ALTER TABLE tb_diario_leitura
    DROP COLUMN usuario_id;

ALTER TABLE tb_diario_leitura
    DROP COLUMN livro_id;
