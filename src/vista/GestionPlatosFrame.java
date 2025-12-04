package vista;

import controlador.PlatoDAO;
import modelo.Plato;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionPlatosFrame extends JFrame {
    private PlatoDAO platoDAO;
    private DefaultTableModel modelPlatos;
    private JTable tblPlatos;
    private JTextField txtNombre, txtPrecio;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizar;
    private List<Plato> platos;
    
    public GestionPlatosFrame() {
        initComponents();
        platoDAO = new PlatoDAO();
        cargarPlatos();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Gestion de Platos - Administrador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        
        
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Agregar/Editar Plato"));
        
        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);
        
        panelFormulario.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelFormulario.add(txtPrecio);
        
        
        JPanel panelBotonesForm = new JPanel(new FlowLayout());
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEditar.setEnabled(false);
        btnActualizar = new JButton("Actualizar Lista");
        
        panelBotonesForm.add(btnAgregar);
        panelBotonesForm.add(btnEditar);
        panelBotonesForm.add(btnActualizar);
        
        
        modelPlatos = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nombre", "Precio", "Estado"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPlatos = new JTable(modelPlatos);
        JScrollPane scrollPane = new JScrollPane(tblPlatos);
        
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnEliminar = new JButton("Eliminar Plato");
        btnEliminar.setEnabled(false);
        
        panelBotones.add(btnEliminar);
        
        
        setLayout(new BorderLayout(10, 10));
        add(panelFormulario, BorderLayout.NORTH);
        add(panelBotonesForm, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        
        btnAgregar.addActionListener(e -> agregarPlato());
        btnEditar.addActionListener(e -> editarPlato());
        btnEliminar.addActionListener(e -> eliminarPlato());
        btnActualizar.addActionListener(e -> cargarPlatos());
        
        tblPlatos.getSelectionModel().addListSelectionListener(e -> {
            boolean haySeleccion = tblPlatos.getSelectedRow() >= 0;
            btnEditar.setEnabled(haySeleccion);
            btnEliminar.setEnabled(haySeleccion);
        });
    }
    
    private void cargarPlatos() {
        modelPlatos.setRowCount(0);
        platos = platoDAO.obtenerTodosPlatos();
        
        for (Plato plato : platos) {
            modelPlatos.addRow(new Object[]{
                plato.getIdPlato(),
                plato.getNombre(),
                String.format("S/%.2f", plato.getPrecio()),
                "Disponible"
            });
        }
    }
    
    private void agregarPlato() {
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        
        if (nombre.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }
        
        try {
            double precio = Double.parseDouble(precioStr);
            Plato nuevoPlato = new Plato(0, nombre, precio, "General");
            
            if (platoDAO.agregarPlato(nuevoPlato)) {
                JOptionPane.showMessageDialog(this, "Plato agregado exitosamente");
                limpiarFormulario();
                cargarPlatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar platillo");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio debe ser un numero valido");
        }
    }
    
    private void editarPlato() {
        int selectedRow = tblPlatos.getSelectedRow();
        if (selectedRow >= 0) {
            int idPlato = (int) modelPlatos.getValueAt(selectedRow, 0);
            String nombre = txtNombre.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            
            if (nombre.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }
            
            try {
                double precio = Double.parseDouble(precioStr);
                Plato platillo = new Plato(idPlato, nombre, precio, "General");
                
                if (platoDAO.actualizarPlato(platillo)) {
                    JOptionPane.showMessageDialog(this, "Plato actualizado exitosamente");
                    limpiarFormulario();
                    cargarPlatos();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Precio debe ser un numero valido");
            }
        }
    }
    
    private void eliminarPlato() {
        int selectedRow = tblPlatos.getSelectedRow();
        if (selectedRow >= 0) {
            int idPlato = (int) modelPlatos.getValueAt(selectedRow, 0);
            String nombrePlato = (String) modelPlatos.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Esta seguro de eliminar el platillo: " + nombrePlato + "?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (platoDAO.eliminarPlato(idPlato)) {
                    JOptionPane.showMessageDialog(this, "Plato eliminado");
                    cargarPlatos();
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