ALTER TABLE tb_user
    ADD COLUMN senha_trocada BOOLEAN;

UPDATE tb_user
SET senha_trocada = TRUE;

UPDATE tb_user
SET senha_trocada = FALSE
WHERE perfil IN ('ADMINISTRADOR', 'MODERADOR');

ALTER TABLE tb_user
    ALTER COLUMN senha_trocada SET NOT NULL;

ALTER TABLE tb_user
    ALTER COLUMN senha_trocada SET DEFAULT FALSE;