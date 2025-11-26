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
        
        // Panel de botones - AHORA CON GESTIÓN DE PLATILLOS
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // CREAR BOTÓN DE GESTIÓN DE PLATILLOS
        JButton btnPlatillos = new JButton("🍽️ Gestión de Platillos");
        btnUsuarios = new JButton("👥 Gestión de Usuarios");
        btnReportes = new JButton("📊 Reportes y Estadísticas");
        btnInventario = new JButton("📦 Control de Inventario");
        btnSalir = new JButton("🚪 Salir");
        
        // Estilo de botones
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        btnPlatillos.setFont(buttonFont);
        btnUsuarios.setFont(buttonFont);
        btnReportes.setFont(buttonFont);
        btnInventario.setFont(buttonFont);
        btnSalir.setFont(buttonFont);
        
        // Agregar botones al panel
        panelBotones.add(btnPlatillos);
        panelBotones.add(btnUsuarios);
        panelBotones.add(btnReportes);
        panelBotones.add(btnInventario);
        panelBotones.add(btnSalir);
        
        panel.add(panelBotones, BorderLayout.CENTER);
        
        // EVENT LISTENER PARA GESTIÓN DE PLATILLOS
        btnPlatillos.addActionListener(e -> {
            GestionPlatillosFrame gestionFrame = new GestionPlatillosFrame();
            gestionFrame.setVisible(true);
        });
        
        // Event listeners para los otros botones (placeholder)
        btnUsuarios.addActionListener(e -> {
            new GestionUsuariosFrame().setVisible(true);
        });

        btnReportes.addActionListener(e -> {
            new ReportesFrame().setVisible(true);
        });
        
        btnInventario.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Módulo de Inventario\n(En desarrollo)");
        });
        
        btnSalir.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea salir?", "Confirmar salida", 
                JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        add(panel);
    }
}