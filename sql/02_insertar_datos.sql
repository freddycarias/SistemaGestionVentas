-- ============================================================
-- PROYECTO FINAL BASE DE DATOS I
-- SISTEMA DE GESTION DE VENTAS DE UNA TIENDA
-- Archivo: 02_insertar_datos.sql
-- Base de datos: sistema_ventas
-- ============================================================

USE sistema_ventas;


-- ============================================================
-- 1. INSERTAR CLIENTES
-- ============================================================

INSERT INTO clientes
(id_cliente, nombre, apellido, correo, telefono)
VALUES
(1, 'Ana', 'Perez', 'ana.perez@gmail.com', '55510001'),
(2, 'Carlos', 'Diaz', 'carlos.diaz@gmail.com', '55510002'),
(3, 'Maria', 'Lopez', 'maria.lopez@gmail.com', '55510003'),
(4, 'Jose', 'Garcia', 'jose.garcia@gmail.com', '55510004'),
(5, 'Sofia', 'Martinez', 'sofia.martinez@gmail.com', '55510005'),
(6, 'Diego', 'Ramirez', 'diego.ramirez@gmail.com', '55510006');


-- ============================================================
-- 2. INSERTAR EMPLEADOS
-- ============================================================

INSERT INTO empleados
(id_empleado, nombre, apellido, correo)
VALUES
(1, 'Luis', 'Gomez', 'luis.gomez@tienda.com'),
(2, 'Marta', 'Lopez', 'marta.lopez@tienda.com'),
(3, 'Pedro', 'Castillo', 'pedro.castillo@tienda.com'),
(4, 'Laura', 'Morales', 'laura.morales@tienda.com');


-- ============================================================
-- 3. INSERTAR CATEGORIAS
-- ============================================================

INSERT INTO categorias
(id_categoria, nombre, descripcion)
VALUES
(1, 'Perifericos', 'Dispositivos externos para computadoras'),
(2, 'Pantallas', 'Monitores y equipos de proyeccion'),
(3, 'Computo', 'Computadoras y dispositivos de procesamiento'),
(4, 'Almacenamiento', 'Unidades para guardar informacion'),
(5, 'Accesorios', 'Accesorios y complementos tecnologicos');


-- ============================================================
-- 4. INSERTAR PRODUCTOS
-- ============================================================

INSERT INTO productos
(id_producto, nombre, descripcion, precio_actual, id_categoria)
VALUES
(1, 'Teclado mecanico', 'Teclado mecanico USB', 150.00, 1),
(2, 'Mouse inalambrico', 'Mouse optico inalambrico', 75.00, 1),
(3, 'Audifonos', 'Audifonos con microfono', 250.00, 1),

(4, 'Monitor 24 pulgadas', 'Monitor LED Full HD', 1200.00, 2),
(5, 'Proyector HD', 'Proyector multimedia HD', 4500.00, 2),
(6, 'Monitor 27 pulgadas', 'Monitor LED de 27 pulgadas', 1800.00, 2),

(7, 'Laptop empresarial', 'Laptop para trabajo y oficina', 8500.00, 3),
(8, 'Computadora de escritorio', 'Equipo de escritorio completo', 7000.00, 3),
(9, 'Tablet', 'Tablet de uso general', 2500.00, 3),

(10, 'Disco SSD 1TB', 'Unidad de estado solido', 800.00, 4),
(11, 'Disco duro 1TB', 'Disco duro externo', 500.00, 4),
(12, 'Memoria USB 64GB', 'Memoria USB de 64 GB', 80.00, 4),

(13, 'Router WiFi', 'Router inalambrico', 600.00, 5),
(14, 'Bocina Bluetooth', 'Bocina portatil Bluetooth', 400.00, 5),
(15, 'Microfono USB', 'Microfono para computadora', 300.00, 5);


-- ============================================================
-- 5. INSERTAR VENTAS
-- ============================================================

INSERT INTO ventas
(id_venta, fecha_venta, id_cliente, id_empleado)
VALUES
(1, '2026-09-10 09:30:00', 1, 1),
(2, '2026-09-11 10:15:00', 2, 2),
(3, '2026-09-12 11:00:00', 1, 2),
(4, '2026-09-13 12:20:00', 3, 3),
(5, '2026-09-14 14:10:00', 4, 1),
(6, '2026-09-15 15:30:00', 2, 1),
(7, '2026-09-16 09:45:00', 5, 4),
(8, '2026-09-17 10:30:00', 1, 1),
(9, '2026-09-18 11:45:00', 6, 2),
(10, '2026-09-19 13:00:00', 3, 3),
(11, '2026-09-20 14:30:00', 4, 1),
(12, '2026-09-21 16:00:00', 2, 2);


-- ============================================================
-- 6. INSERTAR DETALLES DE VENTAS
-- ============================================================

INSERT INTO detalle_venta
(id_venta, id_producto, cantidad, precio_unitario)
VALUES

-- VENTA 1: ANA PEREZ
(1, 7, 1, 8500.00),
(1, 4, 2, 1200.00),
(1, 1, 2, 150.00),

-- VENTA 2: CARLOS DIAZ
(2, 5, 1, 4500.00),
(2, 8, 1, 7000.00),
(2, 2, 2, 75.00),

-- VENTA 3: ANA PEREZ
(3, 4, 2, 1200.00),
(3, 10, 2, 800.00),
(3, 12, 5, 80.00),

-- VENTA 4: MARIA LOPEZ
(4, 7, 1, 8500.00),
(4, 3, 2, 350.00),

-- VENTA 5: JOSE GARCIA
(5, 8, 2, 7000.00),
(5, 1, 3, 150.00),

-- VENTA 6: CARLOS DIAZ
(6, 5, 1, 4500.00),
(6, 4, 2, 1200.00),
(6, 14, 2, 400.00),

-- VENTA 7: SOFIA MARTINEZ
(7, 9, 2, 2500.00),
(7, 12, 10, 80.00),
(7, 2, 5, 75.00),

-- VENTA 8: ANA PEREZ
(8, 7, 1, 8500.00),
(8, 10, 3, 800.00),

-- VENTA 9: DIEGO RAMIREZ
(9, 13, 3, 600.00),
(9, 11, 4, 500.00),
(9, 12, 10, 80.00),

-- VENTA 10: MARIA LOPEZ
(10, 4, 3, 1200.00),
(10, 5, 1, 4500.00),
(10, 1, 4, 150.00),

-- VENTA 11: JOSE GARCIA
(11, 8, 1, 7000.00),
(11, 7, 1, 8500.00),

-- VENTA 12: CARLOS DIAZ
(12, 7, 1, 8500.00),
(12, 4, 1, 1200.00),
(12, 1, 2, 150.00);


-- ============================================================
-- 7. VERIFICAR CANTIDAD DE REGISTROS INSERTADOS
-- ============================================================

SELECT 'Clientes' AS tabla, COUNT(*) AS registros
FROM clientes

UNION ALL

SELECT 'Empleados', COUNT(*)
FROM empleados

UNION ALL

SELECT 'Categorias', COUNT(*)
FROM categorias

UNION ALL

SELECT 'Productos', COUNT(*)
FROM productos

UNION ALL

SELECT 'Ventas', COUNT(*)
FROM ventas

UNION ALL

SELECT 'Detalle de ventas', COUNT(*)
FROM detalle_venta;


-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================