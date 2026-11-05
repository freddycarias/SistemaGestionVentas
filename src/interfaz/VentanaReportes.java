package interfaz;

import dao.ReporteDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class VentanaReportes extends JFrame {

    private final ReporteDAO reporteDAO;
    private final JTabbedPane pestañas;

    public VentanaReportes() {

        reporteDAO = new ReporteDAO();

        setTitle("Sistema de Gestión de Ventas - Reportes");
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pestañas = new JTabbedPane();

        construirInterfaz();
        cargarReportes();
    }

    private void construirInterfaz() {

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "MÓDULO DE REPORTES",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new java.awt.Font("Arial", java.awt.Font.BOLD, 22)
        );

        add(titulo, BorderLayout.NORTH);

        // Crear las siete pestañas.
        agregarPestana("Historial de ventas",
                new String[]{"ID Venta", "Fecha", "Cliente", "Empleado", "Total"});

        agregarPestana("Compras por cliente",
                new String[]{"ID Cliente", "Cliente", "Cantidad de Compras", "Total Comprado"});

        agregarPestana("Ventas por empleado",
                new String[]{"ID Empleado", "Empleado", "Ventas Realizadas", "Total Vendido"});

        agregarPestana("Productos más vendidos",
                new String[]{"ID Producto", "Producto", "Unidades Vendidas", "Ingresos"});

        agregarPestana("Ingresos por categoría",
                new String[]{"ID Categoría", "Categoría", "Unidades Vendidas", "Ingresos"});

        agregarPestana("Cliente por categoría",
                new String[]{"ID Cliente", "Cliente", "Categoría", "Compras", "Unidades", "Total"});

        agregarPestana("Clientes sobre promedio",
                new String[]{"ID Cliente", "Cliente", "Total Acumulado"});

        add(pestañas, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar reportes");
        JButton btnCerrar = new JButton("Cerrar");

        btnActualizar.addActionListener(e -> cargarReportes());
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarPestana(String nombre, String[] columnas) {

        DefaultTableModel modelo = new DefaultTableModel(
                columnas, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setRowHeight(24);

        pestañas.addTab(
                nombre,
                new JScrollPane(tabla)
        );
    }

    private void cargarReportes() {

        try {

            actualizarTabla(0, reporteDAO.reporteHistorialVentas());
            actualizarTabla(1, reporteDAO.reporteComprasCliente());
            actualizarTabla(2, reporteDAO.reporteVentasEmpleado());
            actualizarTabla(3, reporteDAO.reporteProductosMasVendidos());
            actualizarTabla(4, reporteDAO.reporteIngresosCategoria());
            actualizarTabla(5, reporteDAO.reporteClienteCategoria());
            actualizarTabla(6, reporteDAO.reporteClientesSobrePromedio());

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible cargar los reportes:\n" + ex.getMessage(),
                    "Error de reportes",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void actualizarTabla(
            int indice,
            DefaultTableModel modeloDatos) {

        JTable tabla = obtenerTabla(indice);

        tabla.setModel(modeloDatos);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setRowHeight(24);
    }

    private JTable obtenerTabla(int indice) {

        JScrollPane scroll = (JScrollPane) pestañas.getComponentAt(indice);

        return (JTable) scroll.getViewport().getView();
    }
}
