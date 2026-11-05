
USE sistema_ventas;

-- =====================================================
-- 5.1 HISTORIAL DE VENTAS
-- Muestra cada venta, cliente, empleado e importe total.
-- Ordena de mayor a menor importe.
-- =====================================================

SELECT
    v.id_venta,
    v.fecha_venta,
    CONCAT(c.nombre, ' ', c.apellido) AS cliente,
    CONCAT(e.nombre, ' ', e.apellido) AS empleado,
    SUM(d.cantidad * d.precio_unitario) AS total_venta
FROM ventas v
INNER JOIN clientes c
    ON v.id_cliente = c.id_cliente
INNER JOIN empleados e
    ON v.id_empleado = e.id_empleado
INNER JOIN detalle_venta d
    ON v.id_venta = d.id_venta
GROUP BY
    v.id_venta,
    v.fecha_venta,
    c.id_cliente,
    c.nombre,
    c.apellido,
    e.id_empleado,
    e.nombre,
    e.apellido
ORDER BY total_venta DESC;


-- =====================================================
-- 5.2 COMPRAS POR CLIENTE
-- Cuenta las ventas y suma el importe comprado
-- por cada cliente.
-- =====================================================

SELECT
    c.id_cliente,
    CONCAT(c.nombre, ' ', c.apellido) AS cliente,
    COUNT(DISTINCT v.id_venta) AS cantidad_ventas,
    SUM(d.cantidad * d.precio_unitario) AS total_compras
FROM clientes c
INNER JOIN ventas v
    ON c.id_cliente = v.id_cliente
INNER JOIN detalle_venta d
    ON v.id_venta = d.id_venta
GROUP BY
    c.id_cliente,
    c.nombre,
    c.apellido
ORDER BY total_compras DESC;


-- =====================================================
-- 5.3 RESUMEN DE VENTAS POR EMPLEADO
-- Muestra empleados con ventas acumuladas mayores
-- a Q10,000.
-- =====================================================

SELECT
    e.id_empleado,
    CONCAT(e.nombre, ' ', e.apellido) AS empleado,
    COUNT(DISTINCT v.id_venta) AS cantidad_ventas,
    SUM(d.cantidad * d.precio_unitario) AS total_vendido
FROM empleados e
INNER JOIN ventas v
    ON e.id_empleado = v.id_empleado
INNER JOIN detalle_venta d
    ON v.id_venta = d.id_venta
GROUP BY
    e.id_empleado,
    e.nombre,
    e.apellido
HAVING SUM(d.cantidad * d.precio_unitario) > 10000
ORDER BY total_vendido DESC;


-- =====================================================
-- 5.4 PRODUCTOS MAS VENDIDOS
-- Muestra productos con más de 10 unidades vendidas.
-- Incluye categoría e ingresos generados.
-- =====================================================

SELECT
    p.id_producto,
    p.nombre AS producto,
    c.nombre AS categoria,
    SUM(d.cantidad) AS unidades_vendidas,
    SUM(d.cantidad * d.precio_unitario) AS ingresos
FROM productos p
INNER JOIN categorias c
    ON p.id_categoria = c.id_categoria
INNER JOIN detalle_venta d
    ON p.id_producto = d.id_producto
GROUP BY
    p.id_producto,
    p.nombre,
    c.id_categoria,
    c.nombre
HAVING SUM(d.cantidad) > 10
ORDER BY unidades_vendidas DESC;


-- =====================================================
-- 5.5 INGRESOS POR CATEGORIA
-- Muestra categorías con ingresos superiores a Q5,000.
-- Incluye productos diferentes y unidades vendidas.
-- =====================================================

SELECT
    c.id_categoria,
    c.nombre AS categoria,
    COUNT(DISTINCT p.id_producto) AS productos_vendidos,
    SUM(d.cantidad) AS unidades_vendidas,
    SUM(d.cantidad * d.precio_unitario) AS ingresos
FROM categorias c
INNER JOIN productos p
    ON c.id_categoria = p.id_categoria
INNER JOIN detalle_venta d
    ON p.id_producto = d.id_producto
GROUP BY
    c.id_categoria,
    c.nombre
HAVING SUM(d.cantidad * d.precio_unitario) > 5000
ORDER BY ingresos DESC;


-- =====================================================
-- 5.6 REPORTE DE CLIENTES Y CATEGORIAS
-- Muestra compras de cada cliente por categoría.
-- Solo presenta acumulados superiores a Q2,000.
-- =====================================================

SELECT
    c.id_cliente,
    CONCAT(c.nombre, ' ', c.apellido) AS cliente,
    cat.nombre AS categoria,
    COUNT(DISTINCT v.id_venta) AS cantidad_ventas,
    SUM(d.cantidad) AS unidades_compradas,
    SUM(d.cantidad * d.precio_unitario) AS total_comprado
FROM clientes c
INNER JOIN ventas v
    ON c.id_cliente = v.id_cliente
INNER JOIN detalle_venta d
    ON v.id_venta = d.id_venta
INNER JOIN productos p
    ON d.id_producto = p.id_producto
INNER JOIN categorias cat
    ON p.id_categoria = cat.id_categoria
GROUP BY
    c.id_cliente,
    c.nombre,
    c.apellido,
    cat.id_categoria,
    cat.nombre
HAVING SUM(d.cantidad * d.precio_unitario) > 2000
ORDER BY total_comprado DESC;


-- =====================================================
-- 5.7 CLIENTES QUE SUPERAN EL PROMEDIO DE VENTAS
-- Compara el total acumulado de cada cliente contra
-- el promedio del importe de las ventas individuales.
-- =====================================================

SELECT
    c.id_cliente,
    CONCAT(c.nombre, ' ', c.apellido) AS cliente,
    COUNT(DISTINCT v.id_venta) AS cantidad_ventas,
    SUM(d.cantidad * d.precio_unitario) AS total_compras
FROM clientes c
INNER JOIN ventas v
    ON c.id_cliente = v.id_cliente
INNER JOIN detalle_venta d
    ON v.id_venta = d.id_venta
GROUP BY
    c.id_cliente,
    c.nombre,
    c.apellido
HAVING SUM(d.cantidad * d.precio_unitario) >
(
    SELECT AVG(t.total_venta)
    FROM (
        SELECT
            v2.id_venta,
            SUM(d2.cantidad * d2.precio_unitario) AS total_venta
        FROM ventas v2
        INNER JOIN detalle_venta d2
            ON v2.id_venta = d2.id_venta
        GROUP BY v2.id_venta
    ) AS t
)
ORDER BY total_compras DESC;
