CREATE TABLE produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    nome VARCHAR(100) NOT NULL,

    preco DECIMAL(10, 2) NOT NULL,

    ativo BOOLEAN NOT NULL,

    criado_em TIMESTAMP NOT NULL,

    atualizado_em TIMESTAMP
);
