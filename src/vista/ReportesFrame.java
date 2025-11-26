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
    private JLabel lblTotalVentas, lblTotalPedidos, lblVentasHoy, lblPedidosPendientes;
    
    public ReportesFrame() {
        // INICIALIZAR PRIMERO
        reporteDAO = new ReporteDAO();
        initComponents();
        cargarDatosIniciales();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Reportes y Estadísticas - Administrador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        
        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Pestaña 1: Resumen General
        tabbedPane.addTab("📊 Resumen General", crearPanelResumen());
        
        // Pestaña 2: Ventas por Fecha
        tabbedPane.addTab("📅 Ventas por Fecha", crearPanelVentas());
        
        // Pestaña 3: Platillos Más Vendidos
        tabbedPane.addTab("🍽️ Platillos Populares", crearPanelPlatillos());
        
        add(tabbedPane);
    }
    
    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de estadísticas
        JPanel panelStats = new JPanel(new GridLayout(2, 2, 20, 20));
        panelStats.setBorder(BorderFactory.createTitledBorder("Estadísticas Generales"));
        
        double totalVentas = reporteDAO.obtenerTotalVentas();
        int totalPedidos = reporteDAO.obtenerTotalPedidos();
        double ventasHoy = reporteDAO.obtenerVentasHoy();
        int pedidosPendientes = reporteDAO.obtenerPedidosPendientes();
        
        lblTotalVentas = new JLabel("Total Ventas: S/" + String.format("%.2f", totalVentas));
        lblTotalPedidos = new JLabel("Total Pedidos Completados: " + totalPedidos);
        lblVentasHoy = new JLabel("Ventas Hoy: S/" + String.format("%.2f", ventasHoy));
        lblPedidosPendientes = new JLabel("Pedidos Pendientes: " + pedidosPendientes);
        
        // Estilo
        Font font = new Font("Arial", Font.BOLD, 14);
        lblTotalVentas.setFont(font);
        lblTotalPedidos.setFont(font);
        lblVentasHoy.setFont(font);
        lblPedidosPendientes.setFont(font);
        
        lblTotalVentas.setForeground(Color.BLUE);
        lblVentasHoy.setForeground(Color.GREEN);
        lblPedidosPendientes.setForeground(Color.RED);
        
        panelStats.add(lblTotalVentas);
        panelStats.add(lblTotalPedidos);
        panelStats.add(lblVentasHoy);
        panelStats.add(lblPedidosPendientes);
        
        panel.add(panelStats, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelVentas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de fechas
        JPanel panelFechas = new JPanel(new FlowLayout());
        panelFechas.setBorder(BorderFactory.createTitledBorder("Filtrar por Fecha"));
        
        // Fechas por defecto (últimos 7 días)
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        String fechaFin = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -7);
        String fechaInicio = sdf.format(cal.getTime());
        
        panelFechas.add(new JLabel("Desde:"));
        txtFechaInicio = new JTextField(fechaInicio, 10);
        panelFechas.add(txtFechaInicio);
        
        panelFechas.add(new JLabel("Hasta:"));
        txtFechaFin = new JTextField(fechaFin, 10);
        panelFechas.add(txtFechaFin);
        
        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.addActionListener(e -> generarReporteVentas());
        panelFechas.add(btnGenerar);
        
        // Tabla de ventas
        modelVentas = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Fecha", "Pedidos Completados", "Total Ventas"}
        );
        tblVentas = new JTable(modelVentas);
        JScrollPane scrollPane = new JScrollPane(tblVentas);
        
        panel.add(panelFechas, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelPlatillos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Platillos Más Vendidos"));
        
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> cargarPlatillosPopulares());
        panelSuperior.add(btnActualizar);
        
        // Tabla de platillos
        modelPlatillos = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Platillo", "Cantidad Vendida", "Ingresos Generados"}
        );
        tblPlatillos = new JTable(modelPlatillos);
        JScrollPane scrollPane = new JScrollPane(tblPlatillos);
        
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarDatosIniciales() {
        // Cargar datos iniciales
        cargarPlatillosPopulares();
    }
    
    private void generarReporteVentas() {
        if (reporteDAO == null) return;
        
        modelVentas.setRowCount(0);
        List<Object[]> ventas = reporteDAO.obtenerVentasPorFecha(
            txtFechaInicio.getText(), txtFechaFin.getText());
        
        if (ventas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay ventas en el período seleccionado");
            return;
        }
        
        double totalPeriodo = 0;
        int totalPedidosPeriodo = 0;
        
        for (Object[] venta : ventas) {
            modelVentas.addRow(new Object[]{
                venta[0], // fecha
                venta[1], // pedidos
                "S/" + String.format("%.2f", venta[2]) // total
            });
            totalPedidosPeriodo += (Integer) venta[1];
            totalPeriodo += (Double) venta[2];
        }
        
        // Agregar fila de total
        modelVentas.addRow(new Object[]{
            "TOTAL PERÍODO",
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
                platillo[0], // nombre
                platillo[1], // cantidad
                "S/" + String.format("%.2f", platillo[2]) // ingresos
            });
            totalIngresos += (Double) platillo[2];
        }
        
        // Agregar fila de total
        modelPlatillos.addRow(new Object[]{
            "TOTAL GENERAL",
            "",
            "S/" + String.format("%.2f", totalIngresos)
        });
    }
}