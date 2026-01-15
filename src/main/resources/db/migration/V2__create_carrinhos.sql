CREATE TABLE carrinhos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  criado_em TIMESTAMP NOT NULL,
  atualizado_em TIMESTAMP
);

CREATE TABLE itens_carrinho (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  carrinho_id BIGINT NOT NULL,
  produto_id BIGINT NOT NULL,
  nome_produto VARCHAR(100) NOT NULL,
  preco_unitario DECIMAL(10,2) NOT NULL,
  quantidade INT NOT NULL,

  CONSTRAINT fk_itens_carrinho_carrinho
    FOREIGN KEY (carrinho_id) REFERENCES carrinhos(id)
    ON DELETE CASCADE
);

CREATE INDEX idx_itens_carrinho_carrinho_id ON itens_carrinho (carrinho_id);
CREATE INDEX idx_itens_carrinho_produto_id ON itens_carrinho (produto_id);
