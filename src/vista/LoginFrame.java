package vista;

import controlador.UsuarioDAO;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private UsuarioDAO usuarioDAO;
    
    
    private final Color COLOR_ACENTO = new Color(184, 29, 19);
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    
    public LoginFrame() {
        initComponents();
        usuarioDAO = new UsuarioDAO();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Sistema Restaurante - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
        
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        
       
        JPanel header = crearHeader("INICIAR SESION");
        panelPrincipal.add(header, BorderLayout.NORTH);
        
        
        JPanel panelContenido = crearPanelContenido();
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        
        
        JPanel footer = crearFooter();
        panelPrincipal.add(footer, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JPanel crearHeader(String titulo) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_ACENTO);
        header.setPreferredSize(new Dimension(1000, 100));
        
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.CENTER);
        
        return header;
    }
    
    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        
        JLabel lblBienvenida = new JLabel("Bienvenido al Sistema", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBienvenida.setForeground(COLOR_TEXTO);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblBienvenida, gbc);
        
        
        gbc.gridy = 1;
        panel.add(Box.createVerticalStrut(30), gbc);
        
        
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(COLOR_TEXTO);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(lblUsuario, gbc);
        
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtUsername, gbc);
        
        
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(COLOR_TEXTO);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblPassword, gbc);
        
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(txtPassword, gbc);
        
        
        btnLogin = crearBotonLogin("INGRESAR AL SISTEMA", 300);
        btnLogin.addActionListener(e -> btnLoginActionPerformed());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        panel.add(btnLogin, gbc);
        
        return panel;
    }
    
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(COLOR_FONDO);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblFooter = new JLabel("Sistema de Gestion de Restaurante - 2025");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(COLOR_TEXTO);
        footer.add(lblFooter);
        
        return footer;
    }
    
    private JButton crearBotonLogin(String texto, int ancho) {
        JButton boton = new JButton(texto);
        boton.setBackground(Color.WHITE);           
        boton.setForeground(COLOR_ACENTO);          
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACENTO, 2), 
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        boton.setPreferredSize(new Dimension(ancho, 60));
        
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_ACENTO);           
                boton.setForeground(Color.WHITE);            
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACENTO.darker(), 3),
                    BorderFactory.createEmptyBorder(15, 30, 15, 30)
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.WHITE);            
                boton.setForeground(COLOR_ACENTO);           
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACENTO, 2),
                    BorderFactory.createEmptyBorder(15, 30, 15, 30)
                ));
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
       
            public void mousePressed(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_ACENTO.darker());
                boton.setForeground(Color.WHITE);
            }
            
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_ACENTO);
                boton.setForeground(Color.WHITE);
            }
        });
        
        return boton;
    }
    
    private void btnLoginActionPerformed() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Usuario y contraseña requeridos", 
                "Error de Validacion", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        
        btnLogin.setText("VERIFICANDO...");
        btnLogin.setEnabled(false);
        
        
        Timer timer = new Timer(500, e -> {
            Usuario usuario = usuarioDAO.validarUsuario(username, password);
            
            if (usuario != null) {
                DatosCompartidos.usuarioLogueado = usuario;
                abrirVentanaPorRol(usuario.getRol());
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Credenciales invalidas", 
                    "Error de Autenticacion", 
                    JOptionPane.ERROR_MESSAGE);
                btnLogin.setText("INGRESAR AL SISTEMA");
                btnLogin.setEnabled(true);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void abrirVentanaPorRol(String rol) {
        try {
            switch (rol.toLowerCase()) {
                case "mesero":
                    new PedidoFrame().setVisible(true);
                    break;
                case "chef":
                    new PedidosPendientesFrame().setVisible(true);
                    break;
                case "administrador":
                    new MenuPrincipalFrame().setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, 
                        "Rol no configurado: " + rol, 
                        "Error del Sistema", 
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al abrir la interfaz: " + e.getMessage(), 
                "Error del Sistema", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}