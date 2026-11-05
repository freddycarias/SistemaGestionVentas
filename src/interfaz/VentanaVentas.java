package interfaz;

import dao.VentaDAO;
import modelo.Cliente;
import modelo.Empleado;
import modelo.Producto;
import modelo.DetalleVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentanaVentas extends JFrame {

    private final VentaDAO ventaDAO = new VentaDAO();

    private JComboBox<Cliente> cmbClientes;
    private JComboBox<Empleado> cmbEmpleados;
    private JComboBox<Producto> cmbProductos;

    private JSpinner spnCantidad;

    private JTable tablaDetalle;
    private DefaultTableModel modeloTabla;

    private JLabel lblTotal;

    private final List<DetalleVenta> detalles = new ArrayList<>();

    public VentanaVentas() {

        setTitle("Registro de Ventas");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearInterfaz();
        cargarDatos();
    }

    private void crearInterfaz() {

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "REGISTRO DE VENTAS",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        add(titulo, BorderLayout.NORTH);

        // Panel superior: cliente y empleado
        JPanel panelDatos = new JPanel(new GridLayout(2, 2, 10, 10));

        panelDatos.setBorder(
                BorderFactory.createTitledBorder("Datos de la venta")
        );

        cmbClientes = new JComboBox<>();
        cmbEmpleados = new JComboBox<>();

        panelDatos.add(new JLabel("Cliente:"));
        panelDatos.add(cmbClientes);

        panelDatos.add(new JLabel("Empleado:"));
        panelDatos.add(cmbEmpleados);

        // Panel para agregar productos
        JPanel panelProducto = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelProducto.setBorder(
                BorderFactory.createTitledBorder("Agregar productos")
        );

        cmbProductos = new JComboBox<>();

        spnCantidad = new JSpinner(
                new SpinnerNumberModel(1, 1, 10000, 1)
        );

        JButton btnAgregar = new JButton("Agregar producto");
        JButton btnEliminar = new JButton("Eliminar producto");

        panelProducto.add(new JLabel("Producto:"));
        panelProducto.add(cmbProductos);

        panelProducto.add(new JLabel("Cantidad:"));
        panelProducto.add(spnCantidad);

        panelProducto.add(btnAgregar);
        panelProducto.add(btnEliminar);

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.add(panelDatos, BorderLayout.NORTH);
        panelSuperior.add(panelProducto, BorderLayout.CENTER);

        // Tabla de productos
        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID producto",
                    "Producto",
                    "Precio unitario",
                    "Cantidad",
                    "Subtotal"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDetalle = new JTable(modeloTabla);
        tablaDetalle.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scroll = new JScrollPane(tablaDetalle);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Detalle de la venta")
        );

        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.add(panelSuperior, BorderLayout.NORTH);
        panelCentro.add(scroll, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout());

        lblTotal = new JLabel("TOTAL: Q0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.add(lblTotal);

        JButton btnRegistrar = new JButton("REGISTRAR VENTA");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnVolver = new JButton("Volver");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnVolver);

        panelInferior.add(panelTotal, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        // Eventos
        btnAgregar.addActionListener(e -> agregarProducto());

        btnEliminar.addActionListener(e -> eliminarProducto());

        btnRegistrar.addActionListener(e -> registrarVenta());

        btnLimpiar.addActionListener(e -> limpiarVenta());

        btnVolver.addActionListener(e -> dispose());
    }

    private void cargarDatos() {

        try {
            cmbClientes.removeAllItems();
            cmbEmpleados.removeAllItems();
            cmbProductos.removeAllItems();

            for (Cliente cliente : ventaDAO.listarClientes()) {
                cmbClientes.addItem(cliente);
            }

            for (Empleado empleado : ventaDAO.listarEmpleados()) {
                cmbEmpleados.addItem(empleado);
            }

            for (Producto producto : ventaDAO.listarProductos()) {
                cmbProductos.addItem(producto);
            }

        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los datos.", e);
        }
    }

    private void agregarProducto() {

        Producto producto = (Producto) cmbProductos.getSelectedItem();

        if (producto == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay productos disponibles."
            );
            return;
        }

        int cantidad = (Integer) spnCantidad.getValue();

        // Evitar productos repetidos en el detalle
        for (DetalleVenta detalle : detalles) {

            if (detalle.getProducto().getIdProducto()
                    == producto.getIdProducto()) {

                detalle.setCantidad(detalle.getCantidad() + cantidad);

                actualizarTabla();
                return;
            }
        }

        detalles.add(new DetalleVenta(producto, cantidad));

        actualizarTabla();
    }

    private void eliminarProducto() {

        int fila = tablaDetalle.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto de la tabla."
            );
            return;
        }

        detalles.remove(fila);

        actualizarTabla();
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVenta detalle : detalles) {

            Producto producto = detalle.getProducto();

            BigDecimal precio = producto.getPrecioActual();
            BigDecimal subtotal = detalle.getSubtotal();

            modeloTabla.addRow(new Object[]{
                producto.getIdProducto(),
                producto.getNombre(),
                "Q" + precio,
                detalle.getCantidad(),
                "Q" + subtotal
            });

            total = total.add(subtotal);
        }

        lblTotal.setText("TOTAL: Q" + total);
    }

    private void registrarVenta() {

        Cliente cliente = (Cliente) cmbClientes.getSelectedItem();
        Empleado empleado = (Empleado) cmbEmpleados.getSelectedItem();

        if (cliente == null || empleado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un cliente y un empleado."
            );
            return;
        }

        if (detalles.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe agregar al menos un producto."
            );
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea confirmar el registro de esta venta?",
                "Confirmar venta",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int idVenta = ventaDAO.registrarVenta(
                    cliente.getIdCliente(),
                    empleado.getIdEmpleado(),
                    detalles
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Venta registrada correctamente.\n"
                    + "Número de venta: " + idVenta,
                    "Venta exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarVenta();

        } catch (SQLException e) {

            mostrarError(
                    "No fue posible registrar la venta. "
                    + "La operación fue cancelada.",
                    e
            );
        }
    }

    private void limpiarVenta() {

        detalles.clear();
        actualizarTabla();

        spnCantidad.setValue(1);
        tablaDetalle.clearSelection();
    }

    private void mostrarError(String mensaje, SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                mensaje + "\nDetalle: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
