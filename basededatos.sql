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

-- Tabla de imágenes del producto (opcional, para tener varias fotos por producto)
create table imagenes_producto (
    id int auto_increment primary key,
    producto_id int,
    url varchar(255) not null,
    orden int not null default 0, -- Para ordenar las imágenes
    foreign key (producto_id) references productos(id) on delete cascade
);

-- Tabla usuarios
create table usuarios(
	idusuario int auto_increment primary key,
    rol_id int,
    nombres char(100) not null,
    apellidos char(100) null,
    correo char(150) not null unique,
    telefono char(20) null,
    password char(255) null,
    google_id char(255) null, -- Para login con Google
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
    updateAt timestamp default current_timestamp on update current_timestamp,
    foreign key (usuario_id) references usuarios(id) on delete cascade
);

-- Table marcas de tarjeta
CREATE TABLE marcas_tarjeta (
    id int auto_increment primary key,
    nombre char(50) not null unique, -- 'Visa', 'Mastercard', 'American Express'
    logo_url char(255) null 
);

-- Tabla de Metodos de Pago (solo tokens, no tarjetas)
create table metodos_pago(
	id int auto_increment primary key,
    idusuario int,
    pasarela char(50) not null, -- pasarela de pago que usaremos 'stripe' o 'mercado pago'
    customer_token char(255) not null, -- token seguro que nos dara la pasarela
    ultimos_cuatro char(4) not null, -- Solo para mostrar en la vista "Visa terminado en XXXX"
    idtarjeta int,
    createAt timestamp default current_timestamp, 
    foreign key (idusuario) references usuarios(idusuario) on delete cascade,
    foreign key (idtarjeta) references marcas_tarjeta(id) on delete restrict
);


-- Tabla de carrito de compras
create table carrito (
    idcarrito int auto_increment primary key,
    idusuario int references usuarios(idusuario) on delete cascade,
    tarifa decimal(10, 2) not null default 0.00,
    envio decimal(10, 2) not null default 0.00,
    total decimal(10, 2) not null default 0.00,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
);

-- Tabla de items del carrito
create table carrito_detalle (
    id int auto_increment primary key,
    idcarrito int references carrito(idcarrito) on delete cascade,
    variacion_id int references variaciones_producto(id) on delete cascade,
    cantidad int not null default 1,
    precio decimal(10, 2) not null
);

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

