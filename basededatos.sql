drop database if exists hiraoka;
create database hiraoka;
use hiraoka;

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
    nombre char(150) not null,
    slug char(150) not null unique, -- La parte de la URL que identifica a un producto o categoria (productos/apple-iphone-14-pro-max)
    descripcion text,
    marca char(100),
    activo boolean default true,
    foreign key (categoria_id) references categorias(id) on delete set null
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