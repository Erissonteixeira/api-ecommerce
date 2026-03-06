ALTER TABLE pedidos
ADD COLUMN usuario_id BIGINT NOT NULL;

ALTER TABLE pedidos
ADD CONSTRAINT fk_pedidos_usuario
FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
ON DELETE CASCADE;

CREATE INDEX idx_pedidos_usuario_id
ON pedidos(usuario_id);