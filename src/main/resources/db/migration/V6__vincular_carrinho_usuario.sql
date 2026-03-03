ALTER TABLE carrinhos
ADD COLUMN usuario_id BIGINT NOT NULL;

ALTER TABLE carrinhos
ADD CONSTRAINT fk_carrinho_usuario
FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
ON DELETE CASCADE;

CREATE UNIQUE INDEX uk_carrinho_usuario
ON carrinhos(usuario_id);