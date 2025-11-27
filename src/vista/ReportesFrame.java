package vista;

import controlador.ReporteDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ReportesFrame extends JFrame {
    private ReporteDAO reporteDAO;
    private DefaultTableModel modelVentas, modelPlatillos;
    private JTable tblVentas, tblPlatillos;
    private JTextField txtFechaInicio, txtFechaFin;
    private JLabel lblTotalVentas, lblTotalPedidos, lblVentasHoy, lblPedidosPendientes, lblGananciasHoy;
    

    private final Color COLOR_ACENTO = new Color(184, 29, 19);
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    
    public ReportesFrame() {
        reporteDAO = new ReporteDAO();
        initComponents();
        cargarDatosIniciales();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Sistema Restaurante - Reportes y Estadisticas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setResizable(true);
        setMinimumSize(new Dimension(900, 650));
        
      
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
 
        JPanel header = crearHeader("REPORTES Y ESTADISTICAS");
        panelPrincipal.add(header, BorderLayout.NORTH);
        
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
       
        tabbedPane.addTab("RESUMEN GENERAL", crearPanelResumen());
        
     
        tabbedPane.addTab("VENTAS POR FECHA", crearPanelVentas());
        
        
        tabbedPane.addTab("PLATILLOS POPULARES", crearPanelPlatillos());
        
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);
        
        
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
    
    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
      
        JLabel titulo = new JLabel("RESUMEN GENERAL DE VENTAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panel.add(titulo, BorderLayout.NORTH);
        
        
        JPanel panelStats = new JPanel(new GridLayout(2, 3, 20, 20));
        panelStats.setBackground(Color.WHITE);
        panelStats.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        double totalVentas = reporteDAO.obtenerTotalVentas();
        int totalPedidos = reporteDAO.obtenerTotalPedidos();
        double ventasHoy = reporteDAO.obtenerVentasHoy();
        int pedidosPendientes = reporteDAO.obtenerPedidosPendientes();
        double gananciasHoy = reporteDAO.obtenerGananciasHoy();
        
     
        lblTotalVentas = crearTarjetaEstadistica("VENTAS TOTALES", "S/" + String.format("%.2f", totalVentas), COLOR_ACENTO);
        lblTotalPedidos = crearTarjetaEstadistica("PEDIDOS COMPLETADOS", String.valueOf(totalPedidos), new Color(52, 152, 219));
        lblVentasHoy = crearTarjetaEstadistica("VENTAS HOY", "S/" + String.format("%.2f", ventasHoy), new Color(46, 204, 113));
        lblPedidosPendientes = crearTarjetaEstadistica("PEDIDOS PENDIENTES", String.valueOf(pedidosPendientes), new Color(155, 89, 182));
        lblGananciasHoy = crearTarjetaEstadistica("GANANCIAS HOY", "S/" + String.format("%.2f", gananciasHoy), new Color(241, 196, 15));
        
        panelStats.add(lblTotalVentas);
        panelStats.add(lblTotalPedidos);
        panelStats.add(lblVentasHoy);
        panelStats.add(lblPedidosPendientes);
        panelStats.add(lblGananciasHoy);
        
        panel.add(panelStats, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel crearTarjetaEstadistica(String titulo, String valor, Color color) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(COLOR_TEXTO);
        
        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValor.setForeground(color);
        
        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        
        JLabel contenedor = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(color);
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        contenedor.setLayout(new BorderLayout());
        contenedor.add(tarjeta);
        
        return contenedor;
    }
    
    private JPanel crearPanelVentas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelFechas.setBackground(Color.WHITE);
        panelFechas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "FILTRAR POR FECHA"
        ));
        
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        String fechaFin = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -7);
        String fechaInicio = sdf.format(cal.getTime());
        
        panelFechas.add(new JLabel("Desde:"));
        txtFechaInicio = new JTextField(fechaInicio, 12);
        txtFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        panelFechas.add(new JLabel("Hasta:"));
        txtFechaFin = new JTextField(fechaFin, 12);
        txtFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JButton btnGenerar = crearBotonSecundario("GENERAR REPORTE");
        btnGenerar.addActionListener(e -> generarReporteVentas());
        panelFechas.add(btnGenerar);
        
        
        modelVentas = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Fecha", "Pedidos Completados", "Total Ventas"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblVentas = new JTable(modelVentas);
        tblVentas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblVentas.setRowHeight(25);
        tblVentas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(tblVentas);
        
        panel.add(panelFechas, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelPlatillos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
      
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "PLATILLOS MÁS VENDIDOS"
        ));
        
        JButton btnActualizar = crearBotonSecundario("ACTUALIZAR LISTA");
        btnActualizar.addActionListener(e -> cargarPlatillosPopulares());
        panelSuperior.add(btnActualizar);
        
    
        modelPlatillos = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Platillo", "Cantidad Vendida", "Ingresos Generados"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblPlatillos = new JTable(modelPlatillos);
        tblPlatillos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblPlatillos.setRowHeight(25);
        tblPlatillos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(tblPlatillos);
        
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_FONDO);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        
        JButton btnCerrar = crearBotonSecundario("CERRAR");
        btnCerrar.addActionListener(e -> dispose());
        
        
        JLabel lblInfo = new JLabel("Sistema de Gestion - 2025");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(COLOR_TEXTO);
        
        footer.add(btnCerrar, BorderLayout.WEST);
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
    
    private void cargarDatosIniciales() {
        cargarPlatillosPopulares();
    }
    
    private void generarReporteVentas() {
        if (reporteDAO == null) return;
        
        modelVentas.setRowCount(0);
        List<Object[]> ventas = reporteDAO.obtenerVentasPorFecha(
            txtFechaInicio.getText(), txtFechaFin.getText());
        
        if (ventas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay ventas en el periodo seleccionado");
            return;
        }
        
        double totalPeriodo = 0;
        int totalPedidosPeriodo = 0;
        
        for (Object[] venta : ventas) {
            modelVentas.addRow(new Object[]{
                venta[0], 
                venta[1], 
                "S/" + String.format("%.2f", venta[2]) 
            });
            totalPedidosPeriodo += (Integer) venta[1];
            totalPeriodo += (Double) venta[2];
        }
        
        
        modelVentas.addRow(new Object[]{
            "TOTAL PERIODO",
            totalPedidosPeriodo,
            "S/" + String.format("%.2f", totalPeriodo)
        });
    }
    
    private void cargarPlatillosPopulares() {
        if (reporteDAO == null) return;
        
        modelPlatillos.setRowCount(0);
        List<Object[]> platillos = reporteDAO.obtenerPlatillosMasVendidos();
        
        if (platillos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay datos de platillos vendidos");
            return;
        }
        
        double totalIngresos = 0;
        
        for (Object[] platillo : platillos) {
            modelPlatillos.addRow(new Object[]{
                platillo[0], 
                platillo[1], 
                "S/" + String.format("%.2f", platillo[2]) 
            });
            totalIngresos += (Double) platillo[2];
        }
        
        
        modelPlatillos.addRow(new Object[]{
            "TOTAL GENERAL",
            "",
            "S/" + String.format("%.2f", totalIngresos)
        });
    }
}