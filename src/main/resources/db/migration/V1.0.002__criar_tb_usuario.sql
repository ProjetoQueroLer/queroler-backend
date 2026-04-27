CREATE TABLE IF NOT EXISTS tb_usuario (
                            id BIGSERIAL PRIMARY KEY,
                            nome VARCHAR(80) NOT NULL,
                            email VARCHAR(150) NOT NULL UNIQUE,
                            cpf VARCHAR(14) NOT NULL UNIQUE,
                            data_nascimento DATE,
                            aceite_termos BOOLEAN NOT NULL,
                            cidade VARCHAR(80),
                            estado VARCHAR(100),
                            pais VARCHAR(100),
                            foto BYTEA,

                            user_id BIGINT UNIQUE,

                            CONSTRAINT fk_usuario_user
                                FOREIGN KEY (user_id)
                                    REFERENCES tb_user(id)
);