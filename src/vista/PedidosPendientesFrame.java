package vista;

import controlador.PedidoDAO;
import modelo.Pedido;
import modelo.Plato;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PedidosPendientesFrame extends JFrame {
    private PedidoDAO pedidoDAO;
    private DefaultTableModel modelPedidos;
    private Timer timer;
    
    
    private final Color COLOR_ACENTO = new Color(184, 29, 19);
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    private final Color COLOR_PENDIENTE = new Color(255, 243, 224); 
    private final Color COLOR_PREPARACION = new Color(255, 236, 229); 
    private final Color COLOR_LISTO = new Color(232, 245, 233); 

    private JTable tblPedidos;
    private JButton btnPreparando, btnCompletado, btnActualizar, btnVerDetalles;
    private JButton btnVerInventario, btnCerrarSesion;
    private JScrollPane scrollPane;
    private JLabel lblContador;
    
    public PedidosPendientesFrame() {
        initComponents();
        pedidoDAO = new PedidoDAO();
        initTablaPedidos();
        cargarPedidosPendientes();
        iniciarActualizacionAutomatica();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Sistema Restaurante - Gestion de Cocina");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setResizable(true);
        setMinimumSize(new Dimension(900, 650));
        
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        
        
        JPanel header = crearHeader("GESTION DE COCINA - CHEF");
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
        
        
        JPanel panelSuperior = crearPanelSuperior();
        panel.add(panelSuperior, BorderLayout.NORTH);
        
        
        JPanel panelCentral = crearPanelTablaPedidos();
        panel.add(panelCentral, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "ESTADO ACTUAL - PEDIDOS PARA PREPARAR"
        ));
        
        
        JPanel panelContador = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelContador.setBackground(Color.WHITE);
        
        lblContador = new JLabel("Pedidos pendientes: 0");
        lblContador.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblContador.setForeground(COLOR_ACENTO);
        panelContador.add(lblContador);
        

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        
        btnActualizar = crearBotonSecundario("ACTUALIZAR");
        btnActualizar.addActionListener(e -> cargarPedidosPendientes());
        
        btnVerInventario = crearBotonSecundario("VER INVENTARIO");
        btnVerInventario.addActionListener(e -> {
            InventarioFrame inventarioFrame = new InventarioFrame();
            inventarioFrame.setVisible(true);
        });
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnVerInventario);
        
        panel.add(panelContador, BorderLayout.WEST);
        panel.add(panelBotones, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel crearPanelTablaPedidos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "PEDIDOS PENDIENTES DE PREPARACIÓN"
        ));
        
        
        modelPedidos = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID Pedido", "Mesa", "Estado", "Total", "Platos", "Tiempo"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblPedidos = new JTable(modelPedidos);
        tblPedidos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblPedidos.setRowHeight(30);
        tblPedidos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
        tblPedidos.getColumnModel().getColumn(0).setPreferredWidth(80);  
        tblPedidos.getColumnModel().getColumn(1).setPreferredWidth(60);  
        tblPedidos.getColumnModel().getColumn(2).setPreferredWidth(100); 
        tblPedidos.getColumnModel().getColumn(3).setPreferredWidth(90);  
        tblPedidos.getColumnModel().getColumn(4).setPreferredWidth(250); 
        tblPedidos.getColumnModel().getColumn(5).setPreferredWidth(80);  
        
       
        tblPedidos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    
                    String estadoFila = "";
                    if (table.getModel().getRowCount() > row) {
                        Object estadoObj = table.getModel().getValueAt(row, 2);
                        if (estadoObj != null) {
                            estadoFila = estadoObj.toString().toLowerCase();
                        }
                    }
                    
                   
                    Color colorFondo = Color.WHITE;
                    if (estadoFila.contains("pendiente")) {
                        colorFondo = COLOR_PENDIENTE;
                    } else if (estadoFila.contains("cocina") || estadoFila.contains("preparación")) {
                        colorFondo = COLOR_PREPARACION;
                    } else if (estadoFila.contains("listo")) {
                        colorFondo = COLOR_LISTO;
                    }
                    
                    c.setBackground(colorFondo);
                }
                
                return c;
            }
        });
        
        scrollPane = new JScrollPane(tblPedidos);
        scrollPane.setPreferredSize(new Dimension(900, 350));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_FONDO);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
      
        btnCerrarSesion = crearBotonSecundario("CERRAR SESION");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
      
        JPanel panelEstados = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelEstados.setBackground(COLOR_FONDO);
        
        btnPreparando = crearBotonEstado("MARCAR EN PREPARACION", new Color(255, 152, 0));
        btnPreparando.addActionListener(e -> actualizarEstado("en_cocina"));
        
        btnCompletado = crearBotonEstado("MARCAR COMO LISTO", new Color(76, 175, 80));
        btnCompletado.addActionListener(e -> actualizarEstado("listo"));
        
        btnVerDetalles = crearBotonSecundario("VER DETALLES");
        btnVerDetalles.addActionListener(e -> btnVerDetallesActionPerformed());
        
        panelEstados.add(btnPreparando);
        panelEstados.add(btnCompletado);
        panelEstados.add(btnVerDetalles);
        
        
        JLabel lblInfo = new JLabel("Sistema de Gestion - 2025");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(COLOR_TEXTO);
        
        footer.add(btnCerrarSesion, BorderLayout.WEST);
        footer.add(panelEstados, BorderLayout.CENTER);
        footer.add(lblInfo, BorderLayout.EAST);
        
        return footer;
    }
    
    private JButton crearBotonEstado(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setBackground(colorFondo);
        boton.setForeground(Color.BLACK); 
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorFondo.darker(), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo.brighter());
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorFondo.darker(), 3),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorFondo.darker(), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return boton;
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
    
    private void initTablaPedidos() {
        tblPedidos.setAutoCreateRowSorter(true);
    }
    
    
    private void cargarPedidosPendientes() {
        modelPedidos.setRowCount(0);
        List<Pedido> pedidos = pedidoDAO.obtenerPedidosParaCocina();
        
        for (Pedido pedido : pedidos) {
            String platos = "";
            for (Plato plato : pedido.getPlatos()) {
                if (!platos.isEmpty()) platos += ", ";
                platos += plato.getNombre();
            }
            
           
            String estado = pedido.getEstado();
            String estadoMostrar = estado;
            if ("en_cocina".equals(estado)) {
                estadoMostrar = "EN PREPARACION";
            } else if ("pendiente".equals(estado)) {
                estadoMostrar = "PENDIENTE";
            } else if ("listo".equals(estado)) {
                estadoMostrar = "LISTO";
            }
            
            modelPedidos.addRow(new Object[]{
                "PED-" + pedido.getIdPedido(),
                "M" + pedido.getNumeroMesa(),
                estadoMostrar,
                String.format("S/%.2f", pedido.calcularTotal()),
                platos,
                "5 min"
            });
        }
        
       
        lblContador.setText("Pedidos para preparar: " + pedidos.size());
        tblPedidos.repaint();
    }
    
    private void iniciarActualizacionAutomatica() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> cargarPedidosPendientes());
            }
        }, 0, 30000);
    }
    
    
    private void actualizarEstado(String estado) {
        int selectedRow = tblPedidos.getSelectedRow();
        if (selectedRow >= 0) {
            String idPedidoStr = (String) modelPedidos.getValueAt(selectedRow, 0);
            int idOrden = Integer.parseInt(idPedidoStr.replace("PED-", ""));
            
          
            String estadoActual = (String) modelPedidos.getValueAt(selectedRow, 2);
            
            
            if ("listo".equals(estado) && !"EN PREPARACION".equals(estadoActual)) {
                JOptionPane.showMessageDialog(this, 
                    "Solo puede marcar como 'LISTO' un pedido que esté 'EN PREPARACION'",
                    "Error de Estado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
           
            boolean exito = pedidoDAO.actualizarEstadoPedido(idOrden, estado);
            
            if (exito) {
                cargarPedidosPendientes();
                
                String mensaje = "";
                if ("en_cocina".equals(estado)) {
                    mensaje = "Pedido marcado como EN PREPARACION";
                } else if ("listo".equals(estado)) {
                    mensaje = " Pedido marcado como LISTO\nEl mesero podra cobrarlo";
                }
                
                JOptionPane.showMessageDialog(this, mensaje);
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el estado");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido de la tabla");
        }
    }
    
    private void btnVerDetallesActionPerformed() {
        int selectedRow = tblPedidos.getSelectedRow();
        if (selectedRow >= 0) {
            String idPedidoStr = (String) modelPedidos.getValueAt(selectedRow, 0);
            int idOrden = Integer.parseInt(idPedidoStr.replace("PED-", ""));
            mostrarDetallesPedido(idOrden);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para ver detalles");
        }
    }
    
    private void mostrarDetallesPedido(int idOrden) {
        List<Object[]> detalles = pedidoDAO.obtenerDetallesPedido(idOrden);
        StringBuilder sb = new StringBuilder();
        sb.append("DETALLES DEL PEDIDO #").append(idOrden).append("\n\n");
        
        double total = 0;
        for (Object[] detalle : detalles) {
            String nombre = (String) detalle[0];
            int cantidad = (Integer) detalle[1];
            double precio = (Double) detalle[2];
            double subtotal = cantidad * precio;
            total += subtotal;
            
            sb.append("- ").append(nombre).append("\n");
            sb.append("  Cantidad: ").append(cantidad);
            sb.append(" x S/").append(String.format("%.2f", precio));
            sb.append(" = S/").append(String.format("%.2f", subtotal)).append("\n\n");
        }
        
        sb.append("TOTAL DEL PEDIDO: S/").append(String.format("%.2f", total));
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Detalles del Pedido #" + idOrden, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de cerrar sesion?",
            "Cerrar Sesion",
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            if (timer != null) {
                timer.cancel();
            }
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }
    
    @Override
    public void dispose() {
        if (timer != null) {
            timer.cancel();
        }
        super.dispose();
    }
}