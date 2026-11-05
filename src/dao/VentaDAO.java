package dao;

import conexion.Conexion;
import modelo.Cliente;
import modelo.Empleado;
import modelo.Producto;
import modelo.DetalleVenta;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    // Consultar clientes
    public List<Cliente> listarClientes() throws SQLException {

        List<Cliente> lista = new ArrayList<>();

        String sql = """
            SELECT id_cliente, nombre, apellido, correo, telefono
            FROM clientes
            ORDER BY nombre, apellido
            """;

        try (Connection con = Conexion.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                ));
            }
        }

        return lista;
    }

    // Consultar empleados
    public List<Empleado> listarEmpleados() throws SQLException {

        List<Empleado> lista = new ArrayList<>();

        String sql = """
            SELECT id_empleado, nombre, apellido
            FROM empleados
            ORDER BY nombre, apellido
            """;

        try (Connection con = Conexion.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Empleado(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("apellido")
                ));
            }
        }

        return lista;
    }

    // Consultar productos
    public List<Producto> listarProductos() throws SQLException {

        List<Producto> lista = new ArrayList<>();

        String sql = """
            SELECT id_producto, nombre, precio_actual
            FROM productos
            ORDER BY nombre
            """;

        try (Connection con = Conexion.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getBigDecimal("precio_actual")
                ));
            }
        }

        return lista;
    }

    // Registrar venta completa mediante una transacción
    public int registrarVenta(
            int idCliente,
            int idEmpleado,
            List<DetalleVenta> detalles) throws SQLException {

        if (detalles == null || detalles.isEmpty()) {
            throw new SQLException(
                    "La venta debe contener al menos un producto."
            );
        }

        String sqlVenta = """
            INSERT INTO ventas (id_cliente, id_empleado)
            VALUES (?, ?)
            """;

        String sqlPrecio = """
            SELECT precio_actual
            FROM productos
            WHERE id_producto = ?
            FOR UPDATE
            """;

        String sqlDetalle = """
            INSERT INTO detalle_venta
                (id_venta, id_producto, cantidad, precio_unitario)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection con = Conexion.obtenerConexion()) {

            con.setAutoCommit(false);

            try {
                int idVenta;

                // Insertar encabezado de venta
                try (PreparedStatement ps = con.prepareStatement(
                        sqlVenta, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, idCliente);
                    ps.setInt(2, idEmpleado);

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "No se pudo obtener el ID de la venta."
                            );
                        }

                        idVenta = rs.getInt(1);
                    }
                }

                // Insertar los productos de la venta
                try (PreparedStatement psPrecio
                        = con.prepareStatement(sqlPrecio); PreparedStatement psDetalle
                        = con.prepareStatement(sqlDetalle)) {

                    for (DetalleVenta detalle : detalles) {

                        if (detalle.getCantidad() <= 0) {
                            throw new SQLException(
                                    "La cantidad debe ser mayor que cero."
                            );
                        }

                        int idProducto = detalle.getProducto()
                                .getIdProducto();

                        psPrecio.setInt(1, idProducto);

                        BigDecimal precio;

                        try (ResultSet rs = psPrecio.executeQuery()) {

                            if (!rs.next()) {
                                throw new SQLException(
                                        "El producto " + idProducto
                                        + " ya no existe."
                                );
                            }

                            precio = rs.getBigDecimal("precio_actual");
                        }

                        psDetalle.setInt(1, idVenta);
                        psDetalle.setInt(2, idProducto);
                        psDetalle.setInt(3, detalle.getCantidad());
                        psDetalle.setBigDecimal(4, precio);

                        psDetalle.executeUpdate();
                    }
                }

                // Confirmar toda la operación
                con.commit();

                return idVenta;

            } catch (SQLException | RuntimeException e) {

                try {
                    con.rollback();
                } catch (SQLException errorRollback) {
                    e.addSuppressed(errorRollback);
                }

                throw e;

            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
