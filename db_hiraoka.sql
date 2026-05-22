drop database if exists tiendamia;
create database tiendamia;
use tiendamia;

-- MÓDULO 1: USUARIOS, ROLES Y SEGURIDAD
-- 

-- Tabla roles: Administrador y Cliente
create table roles (
	id int auto_increment primary key,
    nombre char(50) not null
);

-- Tabla de Usuarios del sistema
create table usuarios(
	id int auto_increment primary key,
    rol_id int,
    nombres char(100) not null,
    apellidos char(100) not null,
    correo char(150) not null unique,
    telefono char(20) null,
    password char(255) not null,
    activo boolean default true,
    createAt timestamp default current_timestamp,
    updateAt timestamp default current_timestamp on update current_timestamp,
    foreign key (rol_id) references roles(id)
);

-- Tabla de Direcciones de Usuario
create table direcciones_usuario (
	id int auto_increment primary key,
    usuario_id int,
    direccion char(255) not null,
    distrito char(100) not null,
    provincia char(100) not null,
    departamento char(100) not null,
    referencia char(255) not null,
    es_principal boolean default false, -- True si es la direccion principal del usuario
    createAt timestamp default current_timestamp, 
    foreign key (usuario_id) references usuarios(id) on delete cascade
);

-- Table marcas de tarjeta
CREATE TABLE marcas_tarjeta (
    id int auto_increment primary key,
    nombre char(50) not null unique, -- 'Visa', 'Mastercard', 'American Express'
    logo_url char(255) null 
);

-- Tabla de Metodos de Pago (solo tokens, no tarjetas)
create table metodos_pago_usuario(
	id int auto_increment primary key,
    usuario_id int,
    pasarela char(50) not null, -- pasarela de pago que usaremos 'stripe' o 'mercado pago'
    customer_token char(255) not null, -- token seguro que nos dara la pasarela
    ultimos_cuatro char(4) not null, -- Solo para mostrar en la vista "Visa terminado en XXXX"
    id_marca_tarjeta int,
    createAt timestamp default current_timestamp, 
    foreign key (usuario_id) references usuarios(id) on delete cascade,
    foreign key (id_marca_tarjeta) references marcas_tarjeta(id) on delete restrict
);

-- MÓDULO 2: CATÁLOGO DE PRODUCTOS Y FILTROS
--

-- Categoria: Relación Autorreferenciada
create table categorias (
    id int auto_increment primary key,
    nombre char(100) not null,
    slug char(100) not null unique, 
    categoria_padre_id int null, -- Si es NULL, es una categoría principal
    foreign key (categoria_padre_id) references categorias(id) on delete set null
);

-- Tabla de Marcas del producto
CREATE TABLE marcas (
    id int auto_increment primary key,
    nombre char(100) not null unique,  
    slug char(100) not null unique, -- Para rutas amigables (ej: /marcas/apple)
    imagen_logo char(255) null, -- URL del logo cuadrado para las tarjetas de producto
    imagen_banner char(255) null, -- URL del banner horizontal para cuando entras a la página de la marca
    descripcion text null,                  
    destacada boolean default false, -- Marcas Destacadas 
    activo boolean default true,           
    creado_en timestamp default current_timestamp
);

-- Productos: Tabla principal, aqui guardamos los datos que nunca cambian del producto
create table productos (
    id int auto_increment primary key,
    categoria_id int,
    marca_id int,
    nombre char(150) not null,
    slug char(150) not null unique, -- La parte de la URL que identifica a un producto o categoria (productos/apple-iphone-14-pro-max)
    descripcion text,
    activo boolean default true,
    foreign key (categoria_id) references categorias(id) on delete set null,
    foreign key (marca_id) references marcas(id) on delete set null
);

-- Tabla de Atributos (color, almacenamiento, etc)
create table atributos (
    id int auto_increment primary key,
    nombre char(50) not null 
);

-- Tabla de Valores (Las opciones: 'Deep Purple', '128GB', 'AT&T')
create table valores_atributos (
    id int auto_increment primary key,
    atributo_id int,
    valor char(100) not null,
    foreign key (atributo_id) references atributos(id) on delete cascade
);

-- Tabla Variaciones de Producto: Aquí es donde vive el precio real y el stock de cada combinación
-- Nota: Esto se le pasara al detalle de carrito de compras
create table variaciones_producto (
    id int auto_increment primary key,
    producto_id int,
    codigo_inventario char(100) not null unique, 
    precio decimal(10, 2) not null,
    stock int not null default 0,
    imagen_url varchar(255) null, -- Para cambiar la foto según el color elegido
    foreign key (producto_id) references productos(id) on delete cascade
);

-- Tabla intermedia: Variante con sus valores
create table variacion_valores (
    variacion_id int,
    valor_atributo_id int,
    primary key (variacion_id, valor_atributo_id),
    foreign key (variacion_id) references variaciones_producto(id) on delete cascade,
    foreign key (valor_atributo_id) references valores_atributos(id) on delete cascade
);

-- Tabla para el Carrito de Compras
create table carrito_items(
	id int auto_increment primary key,
    usuario_id int,
    variacion_id int,
    cantidad int not null default 1,
    createAt timestamp default current_timestamp, 
    updateAt timestamp default current_timestamp on update current_timestamp,
    foreign key (usuario_id) references usuarios(id) on delete cascade,
	foreign key (variacion_id) references variaciones_producto(id) on delete cascade,
    unique(usuario_id, variacion_id)
);

-- MÓDULO 3: TRANSACCIONAL Y CHECKOUT
--

-- Tabla Principal de Ordenes (El encabezado del comprobante)
create table ordenes(
	id int auto_increment primary key,
    usuario_id int,
    direccion_id int,
    total decimal(10,2) not null,
    estado char(50) default 'pendiente', -- ('pendiente','pagado','enviado','cancelado')
    id_transaccion_pasarela char(255) null, -- ID que te devuelve MercadoPago cuando el cobro fue exitoso
    createAt timestamp default current_timestamp, 
    foreign key (usuario_id) references usuarios(id) on delete set null,
    foreign key (direccion_id) references direcciones_usuario(id) on delete restrict
);

-- Tabla Intermedia: Detalle de la Orden (Los productos comprados)
create table orden_detalles(
	id int auto_increment primary key,
    orden_id int,
    variacion_id int,
    cantidad int not null,
    precio_unitario decimal(10, 2) not null,
    foreign key (orden_id) references ordenes(id) on delete cascade,
    foreign key (variacion_id) references variaciones_producto(id) on delete set null
);





