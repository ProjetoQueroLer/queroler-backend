CREATE TABLE IF NOT EXISTS tb_usuario_notificacao
(
    id             BIGSERIAL PRIMARY KEY,
    usuario_id     BIGINT NOT NULL,
    notificacao_id BIGINT NOT NULL,
    visualizada    BOOLEAN DEFAULT FALSE,
    data_leitura   TIMESTAMP,

    CONSTRAINT fk_usuario_notificacao_usuario
        FOREIGN KEY (usuario_id)
            REFERENCES tb_usuario (id),

    CONSTRAINT fk_usuario_notificacao_notificacao
        FOREIGN KEY (notificacao_id)
            REFERENCES tb_notificacao (id)
);