drop database if exists tiendamia;
create database tiendamia;
use tiendamia;

create table roles (
    id int auto_increment primary key,
    nombre char(50) not null
);

-- Categoria: Relación Autorreferenciada
create table categorias (
    id int auto_increment primary key,
    nombre char(100) not null,
    slug char(100) not null unique,
    categoria_padre_id int null,
    foreign key (categoria_padre_id) references categorias(id) on delete set null
);

-- Tabla de Marcas del producto
create table marcas (
    id int auto_increment primary key,
    nombre char(100) not null unique,
    slug char(100) not null unique,
    imagen_logo char(255) null,
    imagen_banner char(255) null,
    descripcion text null,
    destacada boolean default false,
    activo boolean default true,
    createAt timestamp default current_timestamp
);

-- Productos: Tabla principal
create table productos (
    id int auto_increment primary key,
    categoria_id int,
    marca_id int,
    nombre char(150) not null,
    slug char(150) not null unique,
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

-- Tabla de Valores ('Deep Purple', '128GB', etc)
create table valores_atributos (
    id int auto_increment primary key,
    atributo_id int,
    valor char(100) not null,
    foreign key (atributo_id) references atributos(id) on delete cascade
);

-- Tabla Variaciones de Producto
create table variaciones_producto (
    id int auto_increment primary key,
    producto_id int,
    codigo_inventario char(100) not null unique,
    precio decimal(10, 2) not null,
    stock int not null default 0,
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

-- Tabla de imágenes del producto
create table imagenes_producto (
    id int auto_increment primary key,
    producto_id int,
    url varchar(255) not null,
    orden int not null default 0,
    foreign key (producto_id) references productos(id) on delete cascade
);

-- Tabla usuarios
create table usuarios (
    id int auto_increment primary key,
    rol_id int,
    nombres char(100) not null,
    apellidos char(100) null,
    correo char(150) not null unique,
    telefono char(20) null,
    password char(255) null,
    google_id char(255) null,
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
    es_principal boolean default false,
    createAt timestamp default current_timestamp,
    updateAt timestamp default current_timestamp on update current_timestamp,
    foreign key (usuario_id) references usuarios(id) on delete cascade
);

-- Tabla marcas de tarjeta
create table marcas_tarjeta (
    id int auto_increment primary key,
    nombre char(50) not null unique,
    logo_url char(255) null
);

-- Tabla de Metodos de Pago
create table metodos_pago (
    id int auto_increment primary key,
    usuario_id int,
    pasarela char(50) not null,
    customer_token char(255) not null,
    ultimos_cuatro char(4) not null,
    marca_tarjeta_id int,
    createAt timestamp default current_timestamp,
    foreign key (usuario_id) references usuarios(id) on delete cascade,
    foreign key (marca_tarjeta_id) references marcas_tarjeta(id) on delete restrict
);

-- Tabla de carrito de compras
create table carrito (
    id int auto_increment primary key,
    usuario_id int,
    tarifa decimal(10, 2) not null default 0.00,
    envio decimal(10, 2) not null default 0.00,
    total decimal(10, 2) not null default 0.00,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    foreign key (usuario_id) references usuarios(id) on delete cascade
);

-- Tabla de items del carrito
create table carrito_detalle (
    id int auto_increment primary key,
    carrito_id int,
    variacion_id int,
    cantidad int not null default 1,
    precio decimal(10, 2) not null,
    foreign key (carrito_id) references carrito(id) on delete cascade,
    foreign key (variacion_id) references variaciones_producto(id) on delete cascade
);

-- Tabla Principal de Ordenes
create table ordenes (
    id int auto_increment primary key,
    usuario_id int,
    direccion_id int,
    total decimal(10, 2) not null,
    estado char(50) default 'pendiente',
    id_transaccion_pasarela char(255) null,
    createAt timestamp default current_timestamp,
    foreign key (usuario_id) references usuarios(id) on delete set null,
    foreign key (direccion_id) references direcciones_usuario(id) on delete restrict
);

-- Tabla Detalle de la Orden
create table orden_detalles (
    id int auto_increment primary key,
    orden_id int,
    variacion_id int,
    cantidad int not null,
    precio_unitario decimal(10, 2) not null,
    foreign key (orden_id) references ordenes(id) on delete cascade,
    foreign key (variacion_id) references variaciones_producto(id) on delete set null
);

-- Tabla para tokens de recuperación de contraseña
create table tokens_recuperacion (
    id int auto_increment primary key,
    token char(36) not null unique,
    usuario_id int not null,
    expiracion datetime not null,
    usado boolean default false,
    foreign key (usuario_id) references usuarios(id) on delete cascade
);

-- ─── DATOS INICIALES ─────────────────────────────────────────────────────────
insert into roles (nombre) values ('ADMIN'), ('USER');
