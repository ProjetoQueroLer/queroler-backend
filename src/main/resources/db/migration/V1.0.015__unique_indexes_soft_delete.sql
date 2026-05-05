-- Permite re-cadastro após soft-delete: unicidade só entre registros ativos (excluido = false)

-- tb_user.login: usado como "user" (login) e, no cadastro, recebe o e-mail
ALTER TABLE tb_user
    DROP CONSTRAINT IF EXISTS tb_user_login_key;

CREATE UNIQUE INDEX IF NOT EXISTS uq_tb_user_login_active
    ON tb_user (upper(login))
    WHERE NOT excluido;

-- tb_usuario.email / cpf
ALTER TABLE tb_usuario
    DROP CONSTRAINT IF EXISTS tb_usuario_email_key;

ALTER TABLE tb_usuario
    DROP CONSTRAINT IF EXISTS tb_usuario_cpf_key;

CREATE UNIQUE INDEX IF NOT EXISTS uq_tb_usuario_email_active
    ON tb_usuario (lower(email))
    WHERE NOT excluido;

CREATE UNIQUE INDEX IF NOT EXISTS uq_tb_usuario_cpf_active
    ON tb_usuario (cpf)
    WHERE NOT excluido;
