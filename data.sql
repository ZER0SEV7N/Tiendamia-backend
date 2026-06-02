-- =========================================================================
-- NIVEL 1: Categoría Padre Principal
-- =========================================================================
insert categoria (id, nombre, slug, categoria_padre_id) values 
(1, 'Tecnología', 'tecnologia', null);

-- =========================================================================
-- NIVEL 2: Categorías Hijas (Secciones principales del menú)
-- =========================================================================
insert categoria (id, nombre, slug, categoria_padre_id) values 
(2, 'Celulares', 'celulares', 1),
(3, 'Smartwatches', 'smartwatches', 1),
(4, 'Cómputo', 'computo', 1),
(5, 'Mundo Gamer', 'mundo-gamer', 1),
(6, 'Audio', 'audio', 1),
(7, 'Videojuegos', 'videojuegos', 1);

-- =========================================================================
-- NIVEL 3: Categorías Nietas (Subcategorías finales o marcas integradas)
-- =========================================================================
-- Nietas de Celulares (ID: 2)
insert categoria (id, nombre, slug, categoria_padre_id) values 
(8, 'Smartphones Apple', 'smartphones-apple', 2),
(9, 'Smartphones Samsung', 'smartphones-samsung', 2),
(10, 'Smartphones Xiaomi', 'smartphones-xiaomi', 2);

-- Nietas de Smartwatches (ID: 3)
insert categoria (id, nombre, slug, categoria_padre_id) values 
(11, 'Relojes Apple', 'relojes-apple', 3),
(12, 'Relojes Huawei', 'relojes-huawei', 3);

-- Nietas de Cómputo (ID: 4)
insert categoria (id, nombre, slug, categoria_padre_id) values 
(13, 'Laptops', 'laptops', 4),
(14, 'Tablets', 'tablets', 4);

-- Nietas de Mundo Gamer (ID: 5)
insert categoria (id, nombre, slug, categoria_padre_id) values 
(15, 'Laptops Gamer', 'laptops-gamer', 5),
(16, 'Monitores Gamer', 'monitores-gamer', 5);

-- Nietas de Audio (ID: 6)
insert categoria (id, nombre, slug, categoria_padre_id) values 
(17, 'Audífonos', 'audifonos', 6),
(18, 'Parlantes', 'parlantes', 6);

-- Nietas de Videojuegos (ID: 7)
insert categoria (id, nombre, slug, categoria_padre_id) values 
(19, 'Consolas', 'consolas', 7),
(20, 'Juegos', 'juegos', 7);

-- Marcas
insert marca (id, nombre, slug, destacada) values 
(1, 'Apple', 'apple', true),
(2, 'Samsung', 'samsung', true),
(3, 'Xiaomi', 'xiaomi', false),
(4, 'Huawei', 'huawei', false),
(5, 'HP', 'hp', false),
(6, 'Lenovo', 'lenovo', false),
(7, 'Sony', 'sony', true),
(8, 'Nintendo', 'nintendo', true);

-- Productos
insert producto (id, id_Categoria, id_Marca, nombre, slug, imagen_url, descripcion) values 
(1, 8, 1, 'iPhone 15 Pro Max', 'iphone-15-pro-max', 'https://images.local/iphone15.png', 'Smartphone Apple con acabado de titanio.'),
(2, 9, 2, 'Galaxy S24 Ultra', 'galaxy-s24-ultra', 'https://images.local/s24ultra.png', 'Smartphone Samsung con Inteligencia Artificial integrada.'),
(3, 11, 1, 'Apple Watch Series 9', 'apple-watch-series-9', 'https://images.local/aw9.png', 'Reloj inteligente Apple con sensor de temperatura.'),
(4, 13, 6, 'Laptop Lenovo IdeaPad Slim 3', 'laptop-lenovo-ideapad-slim-3', 'https://images.local/ideapad3.png', 'Laptop ideal para estudios y oficina.'),
(5, 15, 5, 'Laptop Gamer HP Victus 16', 'laptop-gamer-hp-victus-16', 'https://images.local/victus16.png', 'Laptop gamer con tarjeta gráfica RTX.'),
(6, 19, 7, 'PlayStation 5 Slim', 'playstation-5-slim', 'https://images.local/ps5slim.png', 'Consola de videojuegos de última generación.');

-- Definición de Atributos globales
insert atributo (id, nombre) values 
(1, 'Color'),
(2, 'Almacenamiento'),
(3, 'Memoria RAM');

-- Valores para Color (ID: 1)
insert valores_atributo (id, id_Atributo, valor) values 
(1, 1, 'Titanio Negro'),
(2, 1, 'Titanio Natural'),
(3, 1, 'Gris Oscuro'),
(4, 1, 'Blanco');

-- Valores para Almacenamiento (ID: 2)
insert valores_atributo (id, id_Atributo, valor) values 
(5, 2, '128GB'),
(6, 2, '256GB'),
(7, 2, '512GB'),
(8, 2, '1TB');

-- Valores para Memoria RAM (ID: 3)
insert valores_atributo (id, id_Atributo, valor) values 
(9, 3, '8GB'),
(10, 3, '12GB'),
(11, 3, '16GB');

-- =========================================================================
-- Variaciones de Productos
-- =========================================================================
-- Variaciones para el iPhone 15 Pro Max (id_Producto = 1)
insert variaciones_producto (id, id_Producto, codigo_inventario, precio, imagen_url, stock) values 
(1, 1, 'IPH15PM-256-TITNAT', 4999.00, 'https://images.local/iphone15-nat.png', 15),
(2, 1, 'IPH15PM-512-TITBLK', 5799.00, 'https://images.local/iphone15-blk.png', 8);

-- Variación para el Galaxy S24 Ultra (id_Producto = 2)
insert variaciones_producto (id, id_Producto, codigo_inventario, precio, imagen_url, stock) values 
(3, 2, 'S24U-512-GRY', 4599.00, 'https://images.local/s24u-gry.png', 20);

-- Variación para la Laptop HP Victus (id_Producto = 5)
insert variaciones_producto (id, id_Producto, codigo_inventario, precio, imagen_url, stock) values 
(4, 5, 'HP-VICTUS-16RAM-512SSD', 3499.00, 'https://images.local/victus-base.png', 5);


-- =========================================================================
-- Mapeo Intermedio: Uniendo Variación con sus correspondientes valores
-- =========================================================================
-- Variación 1 (iPhone Titanio Natural de 256GB): Color = Titanio Natural (2), Almacenamiento = 256GB (6)
insert variacion_valor (id_Variacion, id_Valor_Atributo) values 
(1, 2), 
(1, 6);

-- Variación 2 (iPhone Titanio Negro de 512GB): Color = Titanio Negro (1), Almacenamiento = 512GB (7)
insert variacion_valor (id_Variacion, id_Valor_Atributo) values 
(2, 1), 
(2, 7);

-- Variación 3 (S24 Ultra Gris de 512GB): Color = Gris Oscuro (3), Almacenamiento = 512GB (7)
insert variacion_valor (id_Variacion, id_Valor_Atributo) values 
(3, 3), 
(3, 7);

-- Variación 4 (Laptop HP con 16GB de RAM y 512GB SSD): Almacenamiento = 512GB (7), RAM = 16GB (11)
insert variacion_valor (id_Variacion, id_Valor_Atributo) values 
(4, 7), 
(4, 11);



-- =========================================================================
-- DIRECCIONES
-- =========================================================================


-- 1. Insertar Departamento (ID: 1)
INSERT departamento (id, nombre) VALUES (1, 'Lima');

-- 2. Insertar Provincia (ID: 1) amarrada a Lima
INSERT provincia (id, id_Departamento, nombre) VALUES (1, 1, 'Lima');

-- 3. Insertar Distritos amarrados a la Provincia de Lima (Aquí creamos el ID 1)
INSERT distrito (id, id_Provincia, nombre) VALUES 
(1, 1, 'San Juan de Lurigancho'),
(2, 1, 'Miraflores'),
(3, 1, 'Santiago de Surco'),
(4, 1, 'Los Olivos');