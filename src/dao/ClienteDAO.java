
package dao;

import conexion.Conexion;
import modelo.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Consultar todos los clientes.
    public List<Cliente> listar() throws SQLException {

        List<Cliente> clientes = new ArrayList<>();

        String sql = """
            SELECT id_cliente, nombre, apellido, correo, telefono
            FROM clientes
            ORDER BY id_cliente
            """;

        try (Connection cn = Conexion.obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("correo"),
                    rs.getString("telefono")
                );

                clientes.add(cliente);
            }
        }

        return clientes;
    }

    // Registrar un cliente nuevo.
    public boolean insertar(Cliente cliente) throws SQLException {

        String sql = """
            INSERT INTO clientes (nombre, apellido, correo, telefono)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection cn = Conexion.obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());

            return ps.executeUpdate() > 0;
        }
    }

    // Actualizar un cliente existente.
    public boolean actualizar(Cliente cliente) throws SQLException {

        String sql = """
            UPDATE clientes
            SET nombre = ?, apellido = ?, correo = ?, telefono = ?
            WHERE id_cliente = ?
            """;

        try (Connection cn = Conexion.obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setInt(5, cliente.getIdCliente());

            return ps.executeUpdate() > 0;
        }
    }
}
