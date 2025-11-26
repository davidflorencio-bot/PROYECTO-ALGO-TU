package vista;

import controlador.PlatilloDAO;
import modelo.Plato;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionPlatillosFrame extends JFrame {
    private PlatilloDAO platilloDAO;
    private DefaultTableModel modelPlatillos;
    private JTable tblPlatillos;
    private JTextField txtNombre, txtPrecio;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizar;
    private List<Plato> platillos;
    
    public GestionPlatillosFrame() {
        initComponents();
        platilloDAO = new PlatilloDAO();
        cargarPlatillos();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Gestión de Platillos - Administrador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        
        // Panel superior - Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Agregar/Editar Platillo"));
        
        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);
        
        panelFormulario.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelFormulario.add(txtPrecio);
        
        // Panel botones formulario
        JPanel panelBotonesForm = new JPanel(new FlowLayout());
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEditar.setEnabled(false);
        btnActualizar = new JButton("Actualizar Lista");
        
        panelBotonesForm.add(btnAgregar);
        panelBotonesForm.add(btnEditar);
        panelBotonesForm.add(btnActualizar);
        
        // Tabla de platillos
        modelPlatillos = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nombre", "Precio", "Estado"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPlatillos = new JTable(modelPlatillos);
        JScrollPane scrollPane = new JScrollPane(tblPlatillos);
        
        // Panel inferior - Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnEliminar = new JButton("Eliminar Platillo");
        btnEliminar.setEnabled(false);
        
        panelBotones.add(btnEliminar);
        
        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(panelFormulario, BorderLayout.NORTH);
        add(panelBotonesForm, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Event Listeners
        btnAgregar.addActionListener(e -> agregarPlatillo());
        btnEditar.addActionListener(e -> editarPlatillo());
        btnEliminar.addActionListener(e -> eliminarPlatillo());
        btnActualizar.addActionListener(e -> cargarPlatillos());
        
        tblPlatillos.getSelectionModel().addListSelectionListener(e -> {
            boolean haySeleccion = tblPlatillos.getSelectedRow() >= 0;
            btnEditar.setEnabled(haySeleccion);
            btnEliminar.setEnabled(haySeleccion);
        });
    }
    
    private void cargarPlatillos() {
        modelPlatillos.setRowCount(0);
        platillos = platilloDAO.obtenerTodosPlatillos();
        
        for (Plato plato : platillos) {
            modelPlatillos.addRow(new Object[]{
                plato.getIdPlato(),
                plato.getNombre(),
                String.format("S/%.2f", plato.getPrecio()),
                "Disponible"
            });
        }
    }
    
    private void agregarPlatillo() {
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        
        if (nombre.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }
        
        try {
            double precio = Double.parseDouble(precioStr);
            Plato nuevoPlato = new Plato(0, nombre, precio, "General");
            
            if (platilloDAO.agregarPlatillo(nuevoPlato)) {
                JOptionPane.showMessageDialog(this, "Platillo agregado exitosamente");
                limpiarFormulario();
                cargarPlatillos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar platillo");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio debe ser un número válido");
        }
    }
    
    private void editarPlatillo() {
        int selectedRow = tblPlatillos.getSelectedRow();
        if (selectedRow >= 0) {
            int idPlatillo = (int) modelPlatillos.getValueAt(selectedRow, 0);
            String nombre = txtNombre.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            
            if (nombre.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }
            
            try {
                double precio = Double.parseDouble(precioStr);
                Plato platillo = new Plato(idPlatillo, nombre, precio, "General");
                
                if (platilloDAO.actualizarPlatillo(platillo)) {
                    JOptionPane.showMessageDialog(this, "Platillo actualizado exitosamente");
                    limpiarFormulario();
                    cargarPlatillos();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Precio debe ser un número válido");
            }
        }
    }
    
    private void eliminarPlatillo() {
        int selectedRow = tblPlatillos.getSelectedRow();
        if (selectedRow >= 0) {
            int idPlatillo = (int) modelPlatillos.getValueAt(selectedRow, 0);
            String nombrePlatillo = (String) modelPlatillos.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el platillo: " + nombrePlatillo + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (platilloDAO.eliminarPlatillo(idPlatillo)) {
                    JOptionPane.showMessageDialog(this, "Platillo eliminado");
                    cargarPlatillos();
                }
            }
        }
    }
    
    private void limpiarFormulario() {
        txtNombre.setText("");
        txtPrecio.setText("");
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}