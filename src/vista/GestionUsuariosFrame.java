package vista;

import controlador.UsuarioDAO;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionUsuariosFrame extends JFrame {
    private UsuarioDAO usuarioDAO;
    private DefaultTableModel modelUsuarios;
    private JTable tblUsuarios;
    private JTextField txtUsername, txtPassword;
    private JComboBox<String> cmbRol;
    private JButton btnAgregar, btnEditar, btnEliminar;
    private List<Usuario> usuarios;
    
    public GestionUsuariosFrame() {
        initComponents();
        usuarioDAO = new UsuarioDAO();
        cargarUsuarios();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Gestion de Usuarios - Administrador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        
        
        JPanel panelFormulario = new JPanel(new GridLayout(2, 3, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Agregar/Editar Usuario"));
        
        panelFormulario.add(new JLabel("Usuario:"));
        txtUsername = new JTextField();
        panelFormulario.add(txtUsername);
        
        panelFormulario.add(new JLabel("Contraseña:"));
        txtPassword = new JTextField();
        panelFormulario.add(txtPassword);
        
        panelFormulario.add(new JLabel("Rol:"));
        cmbRol = new JComboBox<>(new String[]{"mesero", "chef", "administrador"});
        panelFormulario.add(cmbRol);
        
        
        JPanel panelBotonesForm = new JPanel(new FlowLayout());
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEditar.setEnabled(false);
        
        panelBotonesForm.add(btnAgregar);
        panelBotonesForm.add(btnEditar);
        
        
        modelUsuarios = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Usuario", "Rol"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblUsuarios = new JTable(modelUsuarios);
        JScrollPane scrollPane = new JScrollPane(tblUsuarios);
        
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnEliminar = new JButton("Eliminar Usuario");
        btnEliminar.setEnabled(false);
        
        panelBotones.add(btnEliminar);
        
        
        setLayout(new BorderLayout(10, 10));
        add(panelFormulario, BorderLayout.NORTH);
        add(panelBotonesForm, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        
        btnAgregar.addActionListener(e -> agregarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        
        tblUsuarios.getSelectionModel().addListSelectionListener(e -> {
            boolean haySeleccion = tblUsuarios.getSelectedRow() >= 0;
            btnEditar.setEnabled(haySeleccion);
            btnEliminar.setEnabled(haySeleccion);
            
            if (haySeleccion) {
                cargarDatosFormulario();
            }
        });
    }
    
    private void cargarUsuarios() {
        modelUsuarios.setRowCount(0);
        usuarios = usuarioDAO.obtenerTodosUsuarios();
        
        for (Usuario usuario : usuarios) {
            modelUsuarios.addRow(new Object[]{
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getRol()
            });
        }
    }
    
    private void cargarDatosFormulario() {
        int selectedRow = tblUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            int idUsuario = (int) modelUsuarios.getValueAt(selectedRow, 0);
            for (Usuario usuario : usuarios) {
                if (usuario.getIdUsuario() == idUsuario) {
                    txtUsername.setText(usuario.getUsername());
                    txtPassword.setText(usuario.getPassword());
                    cmbRol.setSelectedItem(usuario.getRol());
                    break;
                }
            }
        }
    }
    
    private void agregarUsuario() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String rol = (String) cmbRol.getSelectedItem();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }
        
        Usuario nuevoUsuario = new Usuario(0, username, password, rol);
        
        if (usuarioDAO.agregarUsuario(nuevoUsuario)) {
            JOptionPane.showMessageDialog(this, "Usuario agregado exitosamente");
            limpiarFormulario();
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar usuario");
        }
    }
    
    private void editarUsuario() {
        int selectedRow = tblUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            int idUsuario = (int) modelUsuarios.getValueAt(selectedRow, 0);
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String rol = (String) cmbRol.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }
            
            Usuario usuario = new Usuario(idUsuario, username, password, rol);
            
            if (usuarioDAO.actualizarUsuario(usuario)) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente");
                limpiarFormulario();
                cargarUsuarios();
            }
        }
    }
    
    private void eliminarUsuario() {
        int selectedRow = tblUsuarios.getSelectedRow();
        if (selectedRow >= 0) {
            int idUsuario = (int) modelUsuarios.getValueAt(selectedRow, 0);
            String username = (String) modelUsuarios.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Esta seguro de eliminar el usuario: " + username + "?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (usuarioDAO.eliminarUsuario(idUsuario)) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado");
                    cargarUsuarios();
                }
            }
        }
    }
    
    private void limpiarFormulario() {
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRol.setSelectedIndex(0);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}