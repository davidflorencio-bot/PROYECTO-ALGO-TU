package vista;

import controlador.InventarioDAO;
import modelo.Inventario;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventarioFrame extends JFrame {
    private InventarioDAO inventarioDAO;
    private DefaultTableModel model;
    private JTable tabla;
    private JLabel lblStats;
    
    
    private final Color COLOR_ACENTO = new Color(184, 29, 19);
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    private final Color COLOR_BAJO = new Color(255, 230, 230);  
    private final Color COLOR_OK = new Color(230, 255, 230);    
    
    public InventarioFrame() {
        inventarioDAO = new InventarioDAO();
        initComponents();
        cargarInventario();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Sistema Restaurante - Gestion de Inventario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setResizable(true);
        setMinimumSize(new Dimension(900, 650));
        
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        
        
        JPanel header = crearHeader("GESTION DE INVENTARIO");
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
        header.setPreferredSize(new Dimension(1000, 80));
        
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.CENTER);
        
        return header;
    }
    
    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        
        JPanel panelInfo = crearPanelInformacion();
        panel.add(panelInfo, BorderLayout.NORTH);
        
        
        JPanel panelTabla = crearPanelTabla();
        panel.add(panelTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "INFORMACION DEL INVENTARIO"
        ));
        
        JLabel lblInfo = new JLabel("Lista completa de insumos disponibles en el restaurante");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(COLOR_TEXTO);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnActualizar = crearBotonSecundario("ACTUALIZAR");
        btnActualizar.addActionListener(e -> cargarInventario());
        
        panelBotones.add(btnActualizar);
        
        panel.add(lblInfo, BorderLayout.WEST);
        panel.add(panelBotones, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "LISTA DE INSUMOS"
        ));
        
       
        String[] columnas = {"ID", "Insumo", "Cantidad", "Unidad", "Minimo", "Estado"};
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(model);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);  // Insumo
        tabla.getColumnModel().getColumn(2).setPreferredWidth(80);   // Cantidad
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);   // Unidad
        tabla.getColumnModel().getColumn(4).setPreferredWidth(80);   // Mínimo
        tabla.getColumnModel().getColumn(5).setPreferredWidth(100);  // Estado
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_FONDO);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
       
        JButton btnCerrar = crearBotonSecundario("CERRAR");
        btnCerrar.addActionListener(e -> dispose());
        
        
        lblStats = new JLabel("Mostrando todos los insumos");
        lblStats.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStats.setForeground(COLOR_TEXTO);
        
        
        JLabel lblInfo = new JLabel("Sistema de Gestion - 2025");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(COLOR_TEXTO);
        
        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelDerecha.setBackground(COLOR_FONDO);
        panelDerecha.add(lblStats);
        panelDerecha.add(lblInfo);
        
        footer.add(btnCerrar, BorderLayout.WEST);
        footer.add(panelDerecha, BorderLayout.EAST);
        
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
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_FONDO);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACENTO, 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.WHITE);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return boton;
    }
    
    private void cargarInventario() {
        model.setRowCount(0);
        List<Inventario> inventario = inventarioDAO.obtenerTodoElInventario();
        
        int itemsBajos = 0;
        
        for (Inventario item : inventario) {
            String estado = item.getCantidad() <= item.getMinimo() ? "BAJO" : "OK";
            
            if (estado.equals("BAJO")) {
                itemsBajos++;
            }
            
            model.addRow(new Object[]{
                item.getId(),
                item.getNombreInsumo(),
                item.getCantidad(),
                item.getUnidad(),
                item.getMinimo(),
                estado
            });
        }
        
        
        actualizarEstadisticasFooter(inventario.size(), itemsBajos);
        
        
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "No se encontraron datos de inventario.\nVerifique la conexion a la base de datos.",
                "Inventario Vacio", 
                JOptionPane.WARNING_MESSAGE);
        }
        
        
        aplicarColoresTabla();
    }
    
    private void aplicarColoresTabla() {
        
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    try {
                        
                        Object estadoObj = table.getModel().getValueAt(row, 5);
                        if (estadoObj != null && estadoObj.toString().equals("BAJO")) {
                            c.setBackground(COLOR_BAJO);
                            if (column == 5) { 
                                c.setForeground(Color.RED);
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                        } else {
                            c.setBackground(COLOR_OK);
                            if (column == 5) { 
                                c.setForeground(new Color(0, 100, 0));
                            } else {
                                c.setForeground(Color.BLACK);
                            }
                        }
                    } catch (Exception e) {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }
                
                return c;
            }
        });
        
        
        tabla.repaint();
    }
    
    private void actualizarEstadisticasFooter(int totalItems, int itemsBajos) {
        if (lblStats != null) {
            lblStats.setText("Mostrando " + totalItems + " insumos (" + itemsBajos + " con stock bajo)");
        }
    }
}