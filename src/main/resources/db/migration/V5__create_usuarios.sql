CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    nome VARCHAR(50) NOT NULL,
    email VARCHAR(120) NOT NULL,
    whatsapp VARCHAR(20) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    senha VARCHAR(255) NOT NULL,

    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP NULL,

    CONSTRAINT uk_usuarios_email UNIQUE (email),
    CONSTRAINT uk_usuarios_cpf UNIQUE (cpf)
);

CREATE INDEX idx_usuarios_email ON usuarios (email);
CREATE INDEX idx_usuarios_cpf ON usuarios (cpf);