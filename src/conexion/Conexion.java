
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

    private Conexion() {
        // Evita crear objetos de esta clase.
    }

    public static Connection obtenerConexion(
            String usuario, String clave) throws SQLException {

        return DriverManager.getConnection(URL, usuario, clave);
    }
}
