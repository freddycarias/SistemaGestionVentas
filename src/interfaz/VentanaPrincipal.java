
package interfaz;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {

        setTitle("Sistema de Gestión de Ventas");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Encabezado
        JLabel titulo = new JLabel(
            "SISTEMA DE GESTIÓN DE VENTAS",
            SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(
            30, 10, 20, 10
        ));

        add(titulo, BorderLayout.NORTH);

        // Panel de opciones
        JPanel panelOpciones = new JPanel(
            new GridLayout(3, 1, 15, 15)
        );

        panelOpciones.setBorder(
            BorderFactory.createEmptyBorder(30, 150, 30, 150)
        );

        JButton btnClientes = new JButton("GESTIÓN DE CLIENTES");
        JButton btnVentas = new JButton("REGISTRO DE VENTAS");
        JButton btnReportes = new JButton("REPORTES");

        btnClientes.setFont(new Font("Arial", Font.BOLD, 16));
        btnVentas.setFont(new Font("Arial", Font.BOLD, 16));
        btnReportes.setFont(new Font("Arial", Font.BOLD, 16));

        panelOpciones.add(btnClientes);
        panelOpciones.add(btnVentas);
        panelOpciones.add(btnReportes);

        add(panelOpciones, BorderLayout.CENTER);

        // Acciones provisionales
        btnClientes.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "Módulo de clientes en desarrollo."
            )
        );

        btnVentas.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "Módulo de ventas en desarrollo."
            )
        );

        btnReportes.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "Módulo de reportes en desarrollo."
            )
        );

        // Pie de ventana
        JLabel pie = new JLabel(
            "Base de datos: sistema_ventas",
            SwingConstants.CENTER
        );

        pie.setBorder(BorderFactory.createEmptyBorder(
            10, 10, 20, 10
        ));

        add(pie, BorderLayout.SOUTH);

        // Cierre de sesión
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(
                    java.awt.event.WindowEvent e) {

                int opcion = JOptionPane.showConfirmDialog(
                    VentanaPrincipal.this,
                    "¿Desea cerrar el sistema?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    conexion.Conexion.cerrarSesion();
                    dispose();
                    System.exit(0);
                }
            }
        });
    }
}
