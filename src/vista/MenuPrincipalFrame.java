package vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {
    
    
    private final Color COLOR_ACENTO = new Color(184, 29, 19);
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    
    public MenuPrincipalFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Sistema Restaurante - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(true);
        setMinimumSize(new Dimension(900, 650));
        
       
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        
       
        JPanel header = crearHeader("MENU PRINCIPAL");
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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        
        JPanel panelBienvenida = crearPanelBienvenida();
        panel.add(panelBienvenida, BorderLayout.NORTH);
       
        
        JPanel panelOpciones = crearPanelOpciones();
        panel.add(panelOpciones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JLabel lblBienvenida = new JLabel("Sistema de Gestion - Administrador", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBienvenida.setForeground(COLOR_TEXTO);
        
        panel.add(lblBienvenida, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 25, 25));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        
        JButton btnPlatillos = crearBotonPrimario("GESTION DE PLATILLOS");
        JButton btnUsuarios = crearBotonPrimario("GESTION DE USUARIOS");
        JButton btnReportes = crearBotonPrimario("REPORTES Y ESTADISTICAS");
        JButton btnInventario = crearBotonPrimario("CONTROL DE INVENTARIO");
        
        
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
        
        panel.add(btnPlatillos);
        panel.add(btnUsuarios);
        panel.add(btnReportes);
        panel.add(btnInventario);
        
        return panel;
    }
    
    private JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton("<html><center>" + texto + "</center></html>");
        boton.setBackground(COLOR_ACENTO);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACENTO.darker(), 2),
            BorderFactory.createEmptyBorder(30, 20, 30, 20) 
        ));
        
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_ACENTO.brighter());
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACENTO.darker(), 3),
                    BorderFactory.createEmptyBorder(30, 20, 30, 20)
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_ACENTO);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACENTO.darker(), 2),
                    BorderFactory.createEmptyBorder(30, 20, 30, 20) 
                ));
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return boton;
    }
    
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_FONDO);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        
        JButton btnCerrarSesion = crearBotonSecundario("CERRAR SESION");
        btnCerrarSesion.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this, 
                "¿Esta seguro de que desea cerrar sesion?", 
                "Confirmar salida", 
                JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                this.dispose();
            }
        });
        
        
        JLabel lblInfo = new JLabel("Sistema de Gestion de Restaurante - 2025");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(COLOR_TEXTO);
        
        footer.add(btnCerrarSesion, BorderLayout.WEST);
        footer.add(lblInfo, BorderLayout.EAST);
        
        return footer;
    }
    
    private JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(Color.WHITE);
        boton.setForeground(COLOR_TEXTO);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20) 
        ));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_FONDO);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACENTO, 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20) 
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.WHITE);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20) 
                ));
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return boton;
    }
}