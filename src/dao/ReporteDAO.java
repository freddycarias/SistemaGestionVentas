package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class ReporteDAO {

    // Ejecuta una consulta y convierte el resultado en un modelo para JTable.
    private DefaultTableModel ejecutarConsulta(String sql)
            throws SQLException {

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        try (Connection con = Conexion.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int cantidadColumnas = meta.getColumnCount();

            // Crear encabezados automáticamente.
            for (int i = 1; i <= cantidadColumnas; i++) {
                modelo.addColumn(meta.getColumnLabel(i));
            }

            // Cargar filas.
            while (rs.next()) {
                Object[] fila = new Object[cantidadColumnas];

                for (int i = 1; i <= cantidadColumnas; i++) {
                    fila[i - 1] = rs.getObject(i);
                }

                modelo.addRow(fila);
            }
        }

        return modelo;
    }

    // 1. Historial de ventas y total por venta.
    public DefaultTableModel reporteHistorialVentas()
            throws SQLException {

        String sql = """
            SELECT
                v.id_venta AS 'ID Venta',
                v.fecha_venta AS 'Fecha',
                CONCAT(c.nombre, ' ', c.apellido) AS 'Cliente',
                CONCAT(e.nombre, ' ', e.apellido) AS 'Empleado',
                SUM(d.cantidad * d.precio_unitario) AS 'Total'
            FROM ventas v
            INNER JOIN clientes c
                ON v.id_cliente = c.id_cliente
            INNER JOIN empleados e
                ON v.id_empleado = e.id_empleado
            INNER JOIN detalle_venta d
                ON v.id_venta = d.id_venta
            GROUP BY
                v.id_venta, v.fecha_venta,
                c.nombre, c.apellido,
                e.nombre, e.apellido
            ORDER BY v.id_venta DESC
            """;

        return ejecutarConsulta(sql);
    }

    // 2. Compras realizadas por cliente.
    public DefaultTableModel reporteComprasCliente()
            throws SQLException {

        String sql = """
            SELECT
                c.id_cliente AS 'ID Cliente',
                CONCAT(c.nombre, ' ', c.apellido) AS 'Cliente',
                COUNT(DISTINCT v.id_venta) AS 'Cantidad de Compras',
                COALESCE(
                    SUM(d.cantidad * d.precio_unitario), 0
                ) AS 'Total Comprado'
            FROM clientes c
            LEFT JOIN ventas v
                ON c.id_cliente = v.id_cliente
            LEFT JOIN detalle_venta d
                ON v.id_venta = d.id_venta
            GROUP BY c.id_cliente, c.nombre, c.apellido
            ORDER BY `Total Comprado` DESC
            """;

        return ejecutarConsulta(sql);
    }

    // 3. Resumen de ventas por empleado.
    public DefaultTableModel reporteVentasEmpleado()
            throws SQLException {

        String sql = """
            SELECT
                e.id_empleado AS 'ID Empleado',
                CONCAT(e.nombre, ' ', e.apellido) AS 'Empleado',
                COUNT(DISTINCT v.id_venta) AS 'Ventas Realizadas',
                SUM(d.cantidad * d.precio_unitario) AS 'Total Vendido'
            FROM empleados e
            INNER JOIN ventas v
                ON e.id_empleado = v.id_empleado
            INNER JOIN detalle_venta d
                ON v.id_venta = d.id_venta
            GROUP BY
                e.id_empleado, e.nombre, e.apellido
            HAVING SUM(d.cantidad * d.precio_unitario) > 10000
            ORDER BY `Total Vendido` DESC
            """;

        return ejecutarConsulta(sql);
    }

    // 4. Productos más vendidos.
    public DefaultTableModel reporteProductosMasVendidos()
            throws SQLException {

        String sql = """
            SELECT
                p.id_producto AS 'ID Producto',
                p.nombre AS 'Producto',
                SUM(d.cantidad) AS 'Unidades Vendidas',
                SUM(d.cantidad * d.precio_unitario) AS 'Ingresos'
            FROM productos p
            INNER JOIN detalle_venta d
                ON p.id_producto = d.id_producto
            GROUP BY p.id_producto, p.nombre
            HAVING SUM(d.cantidad) > 10
            ORDER BY `Unidades Vendidas` DESC
            """;

        return ejecutarConsulta(sql);
    }

    // 5. Ingresos generados por categoría.
    public DefaultTableModel reporteIngresosCategoria()
            throws SQLException {

        String sql = """
            SELECT
                c.id_categoria AS 'ID Categoría',
                c.nombre AS 'Categoría',
                SUM(d.cantidad) AS 'Unidades Vendidas',
                SUM(d.cantidad * d.precio_unitario) AS 'Ingresos'
            FROM categorias c
            INNER JOIN productos p
                ON c.id_categoria = p.id_categoria
            INNER JOIN detalle_venta d
                ON p.id_producto = d.id_producto
            GROUP BY c.id_categoria, c.nombre
            HAVING SUM(d.cantidad * d.precio_unitario) > 5000
            ORDER BY `Ingresos` DESC
            """;

        return ejecutarConsulta(sql);
    }

    // 6. Compras de clientes agrupadas por categoría.
    public DefaultTableModel reporteClienteCategoria()
            throws SQLException {

        String sql = """
            SELECT
                c.id_cliente AS 'ID Cliente',
                CONCAT(c.nombre, ' ', c.apellido) AS 'Cliente',
                cat.nombre AS 'Categoría',
                COUNT(DISTINCT v.id_venta) AS 'Compras',
                SUM(d.cantidad) AS 'Unidades',
                SUM(d.cantidad * d.precio_unitario) AS 'Total'
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
                c.id_cliente, c.nombre, c.apellido,
                cat.id_categoria, cat.nombre
            HAVING SUM(d.cantidad * d.precio_unitario) > 2000
            ORDER BY `Total` DESC
            """;

        return ejecutarConsulta(sql);
    }

    // 7. Clientes cuyo acumulado supera el promedio
    // de los totales de las ventas individuales.
    public DefaultTableModel reporteClientesSobrePromedio()
            throws SQLException {

        String sql = """
            SELECT
                c.id_cliente AS 'ID Cliente',
                CONCAT(c.nombre, ' ', c.apellido) AS 'Cliente',
                SUM(d.cantidad * d.precio_unitario) AS 'Total Acumulado'
            FROM clientes c
            INNER JOIN ventas v
                ON c.id_cliente = v.id_cliente
            INNER JOIN detalle_venta d
                ON v.id_venta = d.id_venta
            GROUP BY c.id_cliente, c.nombre, c.apellido
            HAVING SUM(d.cantidad * d.precio_unitario) >
                (
                    SELECT AVG(total_venta)
                    FROM (
                        SELECT
                            SUM(d2.cantidad * d2.precio_unitario)
                                AS total_venta
                        FROM ventas v2
                        INNER JOIN detalle_venta d2
                            ON v2.id_venta = d2.id_venta
                        GROUP BY v2.id_venta
                    ) AS promedio_ventas
                )
            ORDER BY `Total Acumulado` DESC
            """;

        return ejecutarConsulta(sql);
    }
}
