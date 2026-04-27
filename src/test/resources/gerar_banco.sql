DELETE FROM tb_usuario_livro;
DELETE FROM tb_livro_autor;
DELETE FROM tb_usuario_notificacao;
DELETE FROM tb_notificacao;
DELETE FROM tb_documento;
DELETE FROM tb_usuario;
DELETE FROM tb_livros;
DELETE FROM tb_autores;
DELETE FROM tb_user;

INSERT INTO tb_user (id, login, senha, perfil)
VALUES
    (1, 'admin', '$2a$10$bvURj2qlBUU5CRTm8pBOLeQau1nYClU.5MB7H/uEWNfKxFfnpkryy', 'ADMINISTRADOR'),
    (2, 'leitor', '$2a$10$bvURj2qlBUU5CRTm8pBOLeQau1nYClU.5MB7H/uEWNfKxFfnpkryy', 'LEITOR');

INSERT INTO tb_usuario (
    id, nome, email, cpf, data_nascimento, aceite_termos, cidade, estado, pais, foto, user_id
)
VALUES
    (1, 'Administrador', 'admin@email.com', '64343764052', '1990-01-01', TRUE, 'Campinas', 'SP', 'Brasil', NULL, 1),
    (2, 'leitor', 'leitor@email.com', '01803138009', '1990-01-01', TRUE, 'Campinas', 'SP', 'Brasil', NULL, 2);

INSERT INTO tb_notificacao (id, data_de_criacao, notificacao)
VALUES
    (1, CURRENT_TIMESTAMP, 'Bem-vindo ao sistema'),
    (2, CURRENT_TIMESTAMP, 'Alterado o termo gerais');

INSERT INTO tb_usuario_notificacao (id, usuario_id, notificacao_id, visualizada, data_leitura)
VALUES
    (1, 2, 1, FALSE, NULL),
    (2, 2, 2, TRUE, NULL);

INSERT INTO tb_documento (id, titulo, tipo_documento, conteudo, ultima_alteracao)
VALUES
    (1, 'Termos Gerais de Uso', 'TERMOS_GERAIS_DE_USO', 'Texto de teste para os termos de uso...', CURRENT_TIMESTAMP);

SELECT setval(pg_get_serial_sequence('tb_user', 'id'), COALESCE((SELECT MAX(id) FROM tb_user), 1), true);
SELECT setval(pg_get_serial_sequence('tb_usuario', 'id'), COALESCE((SELECT MAX(id) FROM tb_usuario), 1), true);
SELECT setval(pg_get_serial_sequence('tb_notificacao', 'id'), COALESCE((SELECT MAX(id) FROM tb_notificacao), 1), true);
SELECT setval(pg_get_serial_sequence('tb_usuario_notificacao', 'id'), COALESCE((SELECT MAX(id) FROM tb_usuario_notificacao), 1), true);
SELECT setval(pg_get_serial_sequence('tb_documento', 'id'), COALESCE((SELECT MAX(id) FROM tb_documento), 1), true);
