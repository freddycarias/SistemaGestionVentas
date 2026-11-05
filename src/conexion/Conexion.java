
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
        "jdbc:mysql://localhost:3306/sistema_ventas"
        + "?useSSL=false"
        + "&serverTimezone=America/Guatemala"
        + "&allowPublicKeyRetrieval=true";

    private static String usuario;
    private static String clave;

    private Conexion() {
    }

    public static void configurar(String user, String password) {
        usuario = user;
        clave = password;
    }

    public static Connection obtenerConexion() throws SQLException {

        if (usuario == null || clave == null) {
            throw new SQLException(
                "La conexión no ha sido configurada."
            );
        }

        return DriverManager.getConnection(URL, usuario, clave);
    }

    public static void cerrarSesion() {
        usuario = null;
        clave = null;
    }
}
