drop database if exists tiendamia;
create database tiendamia;
use tiendamia;

create table rol (
    id int auto_increment primary key,
    nombre char(50) not null
);

-- Categoria: Relación Autorreferenciada
create table categoria (
    id int auto_increment primary key,
    nombre char(100) unique,
    slug char(100) unique,
    categoria_padre_id int null,
    foreign key (categoria_padre_id) references categoria(id) on delete set null
);

-- Tabla de Marcas del producto
create table marca (
    id int auto_increment primary key,
    nombre char(100) unique,
    slug char(100) unique,
    imagen_logo varchar(255) null,
    imagen_banner varchar(255) null,
    descripcion text null,
    destacada boolean default false,
    estado boolean default true,
    createAt timestamp default current_timestamp
);

-- Productos: Tabla principal
create table producto (
    id int auto_increment primary key,
    id_Categoria int,
    id_Marca int,
    nombre char(150) not null,
    slug char(150) unique,
    imagen_url varchar(255) not null,
    descripcion text,
    estado boolean default true,
    foreign key (id_Categoria) references categoria(id) on delete set null,
    foreign key (id_Marca) references marca(id) on delete set null
);

-- Tabla de Atributos (color, almacenamiento, etc)
create table atributo (
    id int auto_increment primary key,
    nombre char(50) not null
);

-- Tabla de Valores ('Deep Purple', '128GB', etc)
create table valores_atributo (
    id int auto_increment primary key,
    id_Atributo int,
    valor char(100) not null,
    foreign key (id_Atributo) references atributo(id) on delete cascade
);

-- Tabla Variaciones de Producto
create table variaciones_producto (
    id int auto_increment primary key,
    id_Producto int,
    codigo_inventario varchar(100) unique, 
    precio decimal(10, 2) not null,
    imagen_url varchar(255) not null,
    stock int default 0,
    foreign key (id_Producto) references producto(id) on delete cascade
);

-- Tabla intermedia: Variante con sus valores
create table variacion_valor (
    id_Variacion int,
    id_Valor_Atributo int,
    primary key (id_Variacion, id_Valor_Atributo),
    foreign key (id_Variacion) references variaciones_producto(id) on delete cascade,
    foreign key (id_Valor_Atributo) references valores_atributo(id) on delete cascade
);

-- Tabla Departamento
create table departamento (
    id int auto_increment primary key,
    nombre char(100) not null
);

-- Tabla Provincia
create table provincia (
    id int auto_increment primary key,
    id_Departamento int,
    nombre char(100) not null,
    foreign key (id_Departamento) references departamento(id) on delete cascade
);

-- Tabla Distrito
create table distrito (
    id int auto_increment primary key,
    id_Provincia int,
    nombre char(100) not null,
    foreign key (id_Provincia) references provincia(id) on delete cascade
);

-- Tabla usuarios
create table usuario(
	id int auto_increment primary key,
    id_Rol int,
    nombres char(100) not null,
    apellidos char(100) null,
    correo char(150) unique,
    telefono char(20) null,
    password varchar(255) null,
    id_Google varchar(255) null, -- Para login con Google
    estado boolean default true,
    createAt timestamp default current_timestamp,
    updateAt timestamp default current_timestamp on update current_timestamp,
    foreign key (id_Rol) references rol(id)
);

-- Tabla de Direcciones de Usuario
create table direcciones_usuario (
    id int auto_increment primary key,
    id_Usuario int,
    id_Distrito int not null,
    direccion varchar(255) not null,
    referencia varchar(255) not null,
    es_principal boolean default false,
    createAt timestamp default current_timestamp,
    updateAt timestamp default current_timestamp on update current_timestamp,
    foreign key (id_Usuario) references usuario(id) on delete cascade,
    foreign key (id_Distrito) references distrito(id) on delete restrict
);

-- Tabla marcas de tarjeta
create table marcas_tarjeta (
    id int auto_increment primary key,
    nombre char(50) unique,
    logo_url varchar(255) null,
    createAt timestamp default current_timestamp
);

-- Tabla de Metodos de Pago
create table metodos_pago (
    id int auto_increment primary key,
    id_Usuario int,
    pasarela char(50) not null,
    customer_token varchar(255) not null,
    ultimos_cuatro char(4) not null,
    id_Marca_Tarjeta int,
    createAt timestamp default current_timestamp,
    updateAt timestamp default current_timestamp on update current_timestamp,
    foreign key (id_Usuario) references usuario(id) on delete cascade,
    foreign key (id_Marca_Tarjeta) references marcas_tarjeta(id) on delete restrict
);

-- Tabla de carrito de compras
create table carrito (
    id int auto_increment primary key,
    id_Usuario int,
    tarifa decimal(10, 2),
    envio decimal(10, 2),
    total decimal(10, 2),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    foreign key (id_Usuario) references usuario(id) on delete cascade
);

-- Tabla de items del carrito
create table carrito_detalle (
    id int auto_increment primary key,
    id_Carrito int,
    id_Variacion int,
    cantidad int default 1,
    precio decimal(10, 2),
    foreign key (id_Carrito) references carrito(id) on delete cascade,
    foreign key (id_Variacion) references variaciones_producto(id) on delete cascade
);

-- Tabla Principal de Ordenes
create table orden (
    id int auto_increment primary key,
    id_Usuario int,
    id_Direccion int,
    total decimal(10, 2) not null,
    estado enum('pendiente', 'confirmada', 'enviada', 'entregada', 'cancelada') default 'pendiente',
    id_transaccion_pasarela varchar(255) null,
    createAt timestamp default current_timestamp,
    foreign key (id_Usuario) references usuario(id) on delete set null,
    foreign key (id_Direccion) references direcciones_usuario(id) on delete restrict
);

-- Tabla Detalle de la Orden
create table orden_detalle (
    id int auto_increment primary key,
    id_Orden int,
    id_Variacion int,
    cantidad int not null,
    precio_unitario decimal(10, 2) not null,
    foreign key (id_Orden) references orden(id) on delete cascade,
    foreign key (id_Variacion) references variaciones_producto(id) on delete set null
);

-- Tabla para tokens de recuperación de contraseña
create table tokens_recuperacion (
    id int auto_increment primary key,
    token char(36) not null unique,
    id_Usuario int not null,
    expiracion datetime not null,
    usado boolean default false,
    foreign key (id_Usuario) references usuario(id) on delete cascade
);

-- ─── DATOS INICIALES ─────────────────────────────────────────────────────────
insert into rol (nombre) values ('ADMIN'), ('USER');

-- 
