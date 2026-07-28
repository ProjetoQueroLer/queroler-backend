INSERT INTO
    tb_usuario (nome, email, cpf, aceite_termos, user_id)
SELECT
    'Administador',
    'administrador@queroler.com',
    '000.000.000-00',
    true,
    id
FROM
    tb_user
WHERE
    login = 'administrador@queroler.com'
    AND NOT EXISTS (
        SELECT
            1
        FROM
            tb_usuario
        WHERE
            user_id = (
                SELECT
                    id
                FROM
                    tb_user
                WHERE
                    login = 'administrador@queroler.com'
            )
    );

INSERT INTO
    tb_usuario (nome, email, cpf, aceite_termos, user_id)
SELECT
    'Moderador',
    'moderador@queroler.com',
    '111.111.111-11',
    true,
    id
FROM
    tb_user
WHERE
    login = 'moderador@queroler.com'
    AND NOT EXISTS (
        SELECT
            1
        FROM
            tb_usuario
        WHERE
            user_id = (
                SELECT
                    id
                FROM
                    tb_user
                WHERE
                    login = 'moderador@queroler.com'
            )
    );