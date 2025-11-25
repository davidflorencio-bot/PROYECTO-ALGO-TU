package vista;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Sistema de Restaurante");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));

        JLabel lblTitulo = new JLabel("Inicio de Sesión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        txtUsuario = new JTextField();
        txtContrasena = new JPasswordField();
        btnLogin = new JButton("Iniciar Sesión");

        add(lblTitulo);
        add(new JLabel("Usuario:", SwingConstants.CENTER));
        add(txtUsuario);
        add(new JLabel("Contraseña:", SwingConstants.CENTER));
        add(txtContrasena);
        add(btnLogin);

        btnLogin.addActionListener(e -> verificarLogin());
    }

    private void verificarLogin() {
        String user = txtUsuario.getText();
        String pass = new String(txtContrasena.getPassword());

        if (user.equals("admin") && pass.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Bienvenido Administrador");
            new MenuPrincipalFrame("Administrador").setVisible(true);
            dispose();
        } else if (user.equals("mesero") && pass.equals("1111")) {
            JOptionPane.showMessageDialog(this, "Bienvenido Mesero");
            new MenuPrincipalFrame("Mesero").setVisible(true);
            dispose();
        } else if (user.equals("cocinero") && pass.equals("2222")) {
            JOptionPane.showMessageDialog(this, "Bienvenido Cocinero");
            new MenuPrincipalFrame("Cocinero").setVisible(true);
            dispose();
        } else if (user.equals("cliente") && pass.equals("3333")) {
            JOptionPane.showMessageDialog(this, "Bienvenido Cliente");
            new MenuPrincipalFrame("Cliente").setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}