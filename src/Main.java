
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

import conexion.Conexion;
import interfaz.VentanaPrincipal;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> iniciarSistema());
    }

    private static void iniciarSistema() {

        String usuario = JOptionPane.showInputDialog(
            null,
            "Ingrese el usuario de MySQL:",
            "Inicio de sesión",
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

        try {
            Conexion.configurar(usuario.trim(), clave);

            try (Connection conexion = Conexion.obtenerConexion()) {

                if (!conexion.isValid(5)) {
                    throw new SQLException(
                        "La conexión con MySQL no es válida."
                    );
                }
            }

            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);

        } catch (SQLException e) {

            Conexion.cerrarSesion();

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
