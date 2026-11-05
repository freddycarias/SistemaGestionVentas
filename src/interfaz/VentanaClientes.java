
package interfaz;

import dao.ClienteDAO;
import modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class VentanaClientes extends JFrame {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCorreo;
    private JTextField txtTelefono;

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    private int idClienteSeleccionado = -1;

    public VentanaClientes() {

        setTitle("Gestión de Clientes");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        crearInterfaz();
        cargarClientes();
    }

    private void crearInterfaz() {

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
            "GESTIÓN DE CLIENTES",
            SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        add(titulo, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(0, 1, 5, 5));
        panelFormulario.setBorder(
            BorderFactory.createTitledBorder("Datos del cliente")
        );
        panelFormulario.setPreferredSize(new Dimension(260, 0));

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtCorreo = new JTextField();
        txtTelefono = new JTextField();

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Apellido:"));
        panelFormulario.add(txtApellido);

        panelFormulario.add(new JLabel("Correo electrónico:"));
        panelFormulario.add(txtCorreo);

        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);

        JButton btnRegistrar = new JButton("Registrar cliente");
        JButton btnActualizar = new JButton("Actualizar cliente");
        JButton btnLimpiar = new JButton("Limpiar formulario");

        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnActualizar);
        panelFormulario.add(btnLimpiar);

        add(panelFormulario, BorderLayout.WEST);

        // Tabla
        modeloTabla = new DefaultTableModel(
            new Object[]{
                "ID", "Nombre", "Apellido", "Correo", "Teléfono"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        tablaClientes.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(tablaClientes);
        scroll.setBorder(
            BorderFactory.createTitledBorder("Clientes registrados")
        );

        add(scroll, BorderLayout.CENTER);

        // Botón inferior
        JButton btnVolver = new JButton("Volver al menú principal");

        JPanel panelInferior = new JPanel();
        panelInferior.add(btnVolver);

        add(panelInferior, BorderLayout.SOUTH);

        // Eventos
        btnRegistrar.addActionListener(e -> registrarCliente());

        btnActualizar.addActionListener(e -> actualizarCliente());

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        btnVolver.addActionListener(e -> dispose());

        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarCliente();
            }
        });
    }

    private void cargarClientes() {

        try {
            modeloTabla.setRowCount(0);

            List<Cliente> clientes = clienteDAO.listar();

            for (Cliente cliente : clientes) {
                modeloTabla.addRow(new Object[]{
                    cliente.getIdCliente(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getCorreo(),
                    cliente.getTelefono()
                });
            }

        } catch (SQLException e) {

            mostrarError("No se pudieron cargar los clientes.", e);
        }
    }

    private Cliente obtenerDatosFormulario() {

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "El nombre y el apellido son obligatorios.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        if (nombre.length() > 60 || apellido.length() > 60) {
            JOptionPane.showMessageDialog(
                this,
                "El nombre y el apellido no pueden superar 60 caracteres.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        if (correo.length() > 120) {
            JOptionPane.showMessageDialog(
                this,
                "El correo no puede superar 120 caracteres.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        if (!correo.isEmpty()
                && !correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            JOptionPane.showMessageDialog(
                this,
                "Ingrese un correo electrónico válido.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        if (telefono.length() > 20) {
            JOptionPane.showMessageDialog(
                this,
                "El teléfono no puede superar 20 caracteres.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        return new Cliente(
            idClienteSeleccionado,
            nombre,
            apellido,
            correo,
            telefono
        );
    }

    private void registrarCliente() {

        Cliente cliente = obtenerDatosFormulario();

        if (cliente == null) {
            return;
        }

        try {
            if (clienteDAO.insertar(cliente)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Cliente registrado correctamente."
                );

                cargarClientes();
                limpiarFormulario();
            }

        } catch (SQLException e) {
            mostrarError(
                "No se pudo registrar el cliente. "
                + "Verifique que el correo no esté registrado.",
                e
            );
        }
    }

    private void actualizarCliente() {

        if (idClienteSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un cliente de la tabla para actualizarlo.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Cliente cliente = obtenerDatosFormulario();

        if (cliente == null) {
            return;
        }

        try {
            if (clienteDAO.actualizar(cliente)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Cliente actualizado correctamente."
                );

                cargarClientes();
                limpiarFormulario();
            }

        } catch (SQLException e) {
            mostrarError(
                "No se pudo actualizar el cliente. "
                + "Verifique que el correo no esté registrado.",
                e
            );
        }
    }

    private void seleccionarCliente() {

        int filaVista = tablaClientes.getSelectedRow();

        if (filaVista == -1) {
            return;
        }

        int filaModelo = tablaClientes.convertRowIndexToModel(filaVista);

        idClienteSeleccionado = Integer.parseInt(
            modeloTabla.getValueAt(filaModelo, 0).toString()
        );

        txtNombre.setText(
            modeloTabla.getValueAt(filaModelo, 1).toString()
        );

        txtApellido.setText(
            modeloTabla.getValueAt(filaModelo, 2).toString()
        );

        Object correo = modeloTabla.getValueAt(filaModelo, 3);
        txtCorreo.setText(correo == null ? "" : correo.toString());

        Object telefono = modeloTabla.getValueAt(filaModelo, 4);
        txtTelefono.setText(telefono == null ? "" : telefono.toString());
    }

    private void limpiarFormulario() {

        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");

        idClienteSeleccionado = -1;
        tablaClientes.clearSelection();
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
