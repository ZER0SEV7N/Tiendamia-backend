-- Tabla Wishlist (Lista de Favoritos)

CREATE TABLE IF NOT EXISTS wishlist (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    id_Usuario   INT NOT NULL,
    id_Producto  INT NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Un usuario no puede agregar el mismo producto dos veces
    UNIQUE KEY uq_usuario_producto (id_Usuario, id_Producto),

    FOREIGN KEY (id_Usuario)  REFERENCES usuario(id)  ON DELETE CASCADE,
    FOREIGN KEY (id_Producto) REFERENCES producto(id) ON DELETE CASCADE
);
