
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        String usuario = JOptionPane.showInputDialog(
            null,
            "Ingrese el usuario de MySQL:",
            "Conexión a la base de datos",
            JOptionPane.QUESTION_MESSAGE
        );

        if (usuario == null || usuario.trim().isEmpty()) {
            return;
        }

        JPasswordField campoClave = new JPasswordField();

        int resultado = JOptionPane.showConfirmDialog(
            null,
            campoClave,
            "Ingrese la contraseña de MySQL:",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        String clave = new String(campoClave.getPassword());

        try (Connection conexion =
                Conexion.obtenerConexion(usuario, clave)) {

            JOptionPane.showMessageDialog(
                null,
                "Conexión exitosa con sistema_ventas.",
                "Conexión correcta",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                null,
                "No fue posible conectar con MySQL.\n"
                + e.getMessage(),
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

