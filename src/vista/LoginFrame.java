package vista;

import controlador.UsuarioDAO;
import modelo.Usuario;
import javax.swing.*;

public class LoginFrame extends javax.swing.JFrame {
    private UsuarioDAO usuarioDAO;
    
    public LoginFrame() {
        initComponents();
        usuarioDAO = new UsuarioDAO();
        setLocationRelativeTo(null);
        System.out.println("LoginFrame iniciado"); // Debug
    }
    
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - Sistema Restaurante");
        
        jLabel1.setText("Usuario:");
        jLabel2.setText("Contraseña:");
        
        btnLogin.setText("Ingresar");
        btnLogin.addActionListener(evt -> btnLoginActionPerformed());
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtUsername)
                    .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(95, Short.MAX_VALUE)
                .addComponent(btnLogin)
                .addGap(95, 95, 95)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnLogin)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }
    
    private void btnLoginActionPerformed() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        System.out.println("Intentando login con: " + username); // Debug
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña requeridos");
            return;
        }
        
        Usuario usuario = usuarioDAO.validarUsuario(username, password);
        
        if (usuario != null) {
            System.out.println("Login exitoso. Usuario: " + usuario.getUsername() + ", Rol: " + usuario.getRol()); // Debug
            DatosCompartidos.usuarioLogueado = usuario;
            abrirVentanaPorRol(usuario.getRol());
            this.dispose();
        } else {
            System.out.println("Login fallido"); // Debug
            JOptionPane.showMessageDialog(this, "Credenciales inválidas");
        }
    }
    
    private void abrirVentanaPorRol(String rol) {
        System.out.println("Abriendo ventana para rol: " + rol); // Debug
        
        try {
            switch (rol.toLowerCase()) {
                case "mesero":
                System.out.println("Intentando abrir PedidoFrame...");
                PedidoFrame pedidoFrame = new PedidoFrame();
                pedidoFrame.setVisible(true);
                System.out.println("PedidoFrame abierto");
                break;
                
            case "chef":
                System.out.println("Intentando abrir PedidosPendientesFrame...");
                PedidosPendientesFrame chefFrame = new PedidosPendientesFrame();
                chefFrame.setVisible(true);
                System.out.println("PedidosPendientesFrame abierto");
                break;
                
            case "administrador":  // CORREGIDO: "admin" en lugar de "administrador"
                System.out.println("Intentando abrir MenuPrincipalFrame...");
                MenuPrincipalFrame adminFrame = new MenuPrincipalFrame();
                adminFrame.setVisible(true);
                System.out.println("MenuPrincipalFrame abierto");
                break;
                    
                default:
                    System.out.println("Rol no reconocido: " + rol);
                    JOptionPane.showMessageDialog(this, "Rol no configurado: " + rol);
            }
        } catch (Exception e) {
            System.err.println("Error al abrir ventana: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al abrir la interfaz: " + e.getMessage());
        }
    }
    
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
}