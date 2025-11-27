package vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {
    private JButton btnUsuarios, btnReportes, btnInventario, btnSalir;
    
    public MenuPrincipalFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Menú Principal - Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        
        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titulo = new JLabel("Sistema de Gestión - Administrador", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titulo, BorderLayout.NORTH);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Botones sin colores complejos
        JButton btnPlatillos = new JButton("🍽️ Gestión de Platillos");
        btnUsuarios = new JButton("👥 Gestión de Usuarios");
        btnReportes = new JButton("📊 Reportes y Estadísticas");
        btnInventario = new JButton("📦 Control de Inventario");
        JButton btnCerrarSesion = new JButton("🚪 Cerrar Sesión");
        
        // Fuente simple
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        btnPlatillos.setFont(buttonFont);
        btnUsuarios.setFont(buttonFont);
        btnReportes.setFont(buttonFont);
        btnInventario.setFont(buttonFont);
        btnCerrarSesion.setFont(buttonFont);
        
        // Agregar botones al panel
        panelBotones.add(btnPlatillos);
        panelBotones.add(btnUsuarios);
        panelBotones.add(btnReportes);
        panelBotones.add(btnInventario);
        panelBotones.add(btnCerrarSesion);
        
        panel.add(panelBotones, BorderLayout.CENTER);
        
        // EVENT LISTENERS
        btnPlatillos.addActionListener(e -> {
            GestionPlatillosFrame gestionFrame = new GestionPlatillosFrame();
            gestionFrame.setVisible(true);
        });
        
        btnUsuarios.addActionListener(e -> {
            new GestionUsuariosFrame().setVisible(true);
        });

        btnReportes.addActionListener(e -> {
            new ReportesFrame().setVisible(true);
        });
        
        btnInventario.addActionListener(e -> {
            InventarioFrame inventarioFrame = new InventarioFrame();
            inventarioFrame.setVisible(true);
        });
        
        btnCerrarSesion.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea cerrar sesión?", "Confirmar salida", 
                JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                this.dispose();
            }
        });
        
        add(panel);
    }
}