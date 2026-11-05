-- ============================================================
-- PROYECTO FINAL BASE DE DATOS I
-- SISTEMA DE GESTION DE VENTAS DE UNA TIENDA
-- Base de datos: sistema_ventas
-- Gestor: MySQL 8
-- ============================================================


-- ============================================================
-- 1. CREACION DE LA BASE DE DATOS
-- ============================================================

CREATE DATABASE IF NOT EXISTS sistema_ventas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sistema_ventas;


-- ============================================================
-- 2. TABLA CLIENTES
-- ============================================================

CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INT AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    apellido VARCHAR(60) NOT NULL,
    correo VARCHAR(120) UNIQUE,
    telefono VARCHAR(20),

    CONSTRAINT pk_clientes
        PRIMARY KEY (id_cliente)
) ENGINE=InnoDB;


-- ============================================================
-- 3. TABLA EMPLEADOS
-- ============================================================

CREATE TABLE IF NOT EXISTS empleados (
    id_empleado INT AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    apellido VARCHAR(60) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,

    CONSTRAINT pk_empleados
        PRIMARY KEY (id_empleado)
) ENGINE=InnoDB;


-- ============================================================
-- 4. TABLA CATEGORIAS
-- ============================================================

CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INT AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(200),

    CONSTRAINT pk_categorias
        PRIMARY KEY (id_categoria)
) ENGINE=InnoDB;


-- ============================================================
-- 5. TABLA PRODUCTOS
-- ============================================================

CREATE TABLE IF NOT EXISTS productos (
    id_producto INT AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    descripcion VARCHAR(200),
    precio_actual DECIMAL(10,2) NOT NULL,
    id_categoria INT NOT NULL,

    CONSTRAINT pk_productos
        PRIMARY KEY (id_producto),

    CONSTRAINT chk_productos_precio
        CHECK (precio_actual >= 0),

    CONSTRAINT fk_productos_categorias
        FOREIGN KEY (id_categoria)
        REFERENCES categorias(id_categoria)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;


-- ============================================================
-- 6. TABLA VENTAS
-- ============================================================

CREATE TABLE IF NOT EXISTS ventas (
    id_venta INT AUTO_INCREMENT,
    fecha_venta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_cliente INT NOT NULL,
    id_empleado INT NOT NULL,

    CONSTRAINT pk_ventas
        PRIMARY KEY (id_venta),

    CONSTRAINT fk_ventas_clientes
        FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_ventas_empleados
        FOREIGN KEY (id_empleado)
        REFERENCES empleados(id_empleado)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;


-- ============================================================
-- 7. TABLA DETALLE_VENTA
-- ============================================================

CREATE TABLE IF NOT EXISTS detalle_venta (
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,

    CONSTRAINT pk_detalle_venta
        PRIMARY KEY (id_venta, id_producto),

    CONSTRAINT chk_detalle_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT chk_detalle_precio
        CHECK (precio_unitario >= 0),

    CONSTRAINT fk_detalle_ventas
        FOREIGN KEY (id_venta)
        REFERENCES ventas(id_venta)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_detalle_productos
        FOREIGN KEY (id_producto)
        REFERENCES productos(id_producto)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;


-- ============================================================
-- 8. CREACION DE INDICES ADICIONALES
-- ============================================================

-- Indice 1: Facilita consultas y reportes por fecha de venta.

CREATE INDEX idx_ventas_fecha ON ventas(fecha_venta);


-- Indice 2: Facilita consultar las ventas de un cliente
-- ordenadas por fecha.

CREATE INDEX idx_ventas_cliente_fecha ON ventas(id_cliente, fecha_venta);


-- Indice 3: Facilita consultar las ventas registradas
-- por cada empleado.

CREATE INDEX idx_ventas_empleado_fecha ON ventas(id_empleado, fecha_venta);


-- Indice 4: Facilita localizar los detalles de venta
-- correspondientes a un producto.

CREATE INDEX idx_detalle_producto ON detalle_venta(id_producto);


-- ============================================================
-- 9. VERIFICACION DE LAS TABLAS CREADAS
-- ============================================================

SHOW TABLES;


-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================