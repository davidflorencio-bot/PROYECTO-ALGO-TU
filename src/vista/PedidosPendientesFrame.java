package vista;

import controlador.PedidoDAO;
import modelo.Pedido;
import modelo.Plato;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PedidosPendientesFrame extends javax.swing.JFrame {
    private PedidoDAO pedidoDAO;
    private DefaultTableModel modelPedidos;
    private Timer timer;
    
    // Componentes
    private JTable tblPedidos;
    private JButton btnPreparando, btnCompletado, btnActualizar, btnVerDetalles;
    private JScrollPane jScrollPane1;
    
    public PedidosPendientesFrame() {
        initComponents();
        pedidoDAO = new PedidoDAO();
        initTablaPedidos();
        cargarPedidosPendientes();
        iniciarActualizacionAutomatica();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pedidos Pendientes - Cocina");
        
        // Tabla
        modelPedidos = new DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Mesa", "Estado", "Total", "Platos"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPedidos = new JTable(modelPedidos);
        jScrollPane1 = new JScrollPane(tblPedidos);
        
        // Botones
        btnPreparando = new JButton("En Preparacion");
        btnCompletado = new JButton("Completado");
        btnActualizar = new JButton("Actualizar");
        btnVerDetalles = new JButton("Ver Detalles");
        
        // Listeners
        btnPreparando.addActionListener(evt -> btnPreparandoActionPerformed());
        btnCompletado.addActionListener(evt -> btnCompletadoActionPerformed());
        btnActualizar.addActionListener(evt -> cargarPedidosPendientes());
        btnVerDetalles.addActionListener(evt -> btnVerDetallesActionPerformed());
        
        // Layout simple
        setLayout(new java.awt.BorderLayout());
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnPreparando);
        panelBotones.add(btnCompletado);
        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnActualizar);
        add(panelBotones, java.awt.BorderLayout.SOUTH);
        
        pack();
        setSize(700, 400);
    }
    
    private void initTablaPedidos() {
        // Configurar anchos de columnas
        tblPedidos.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tblPedidos.getColumnModel().getColumn(1).setPreferredWidth(60);  // Mesa
        tblPedidos.getColumnModel().getColumn(2).setPreferredWidth(100); // Estado
        tblPedidos.getColumnModel().getColumn(3).setPreferredWidth(80);  // Total
        tblPedidos.getColumnModel().getColumn(4).setPreferredWidth(300); // Platos
    }
    
    private void cargarPedidosPendientes() {
        modelPedidos.setRowCount(0);
        List<Pedido> pedidos = pedidoDAO.obtenerPedidosPendientes();
        for (Pedido pedido : pedidos) {
            String platos = "";
            for (Plato plato : pedido.getPlatos()) {
                if (!platos.isEmpty()) platos += ", ";
                platos += plato.getNombre();
            }
            
            modelPedidos.addRow(new Object[]{
                pedido.getIdPedido(),
                "M" + pedido.getMesa().getNumero(),
                pedido.getEstado(),
                String.format("S/%.2f", pedido.calcularTotal()),
                platos
            });
        }
    }
    
    private void iniciarActualizacionAutomatica() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javax.swing.SwingUtilities.invokeLater(() -> cargarPedidosPendientes());
            }
        }, 0, 30000); // Actualizar cada 30 segundos
    }
    
    private void btnPreparandoActionPerformed() {
        actualizarEstado("en_cocina");
    }
    
    private void btnCompletadoActionPerformed() {
        actualizarEstado("listo");
    }
    
    private void btnVerDetallesActionPerformed() {
        int selectedRow = tblPedidos.getSelectedRow();
        if (selectedRow >= 0) {
            int idOrden = (int) modelPedidos.getValueAt(selectedRow, 0);
            mostrarDetallesPedido(idOrden);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para ver detalles");
        }
    }
    
    private void actualizarEstado(String estado) {
        int selectedRow = tblPedidos.getSelectedRow();
        if (selectedRow >= 0) {
            int idOrden = (int) modelPedidos.getValueAt(selectedRow, 0);
            if (pedidoDAO.actualizarEstadoPedido(idOrden, estado)) {
                cargarPedidosPendientes();
                JOptionPane.showMessageDialog(this, "Estado actualizado a: " + estado);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido");
        }
    }
    
    private void mostrarDetallesPedido(int idOrden) {
        List<Object[]> detalles = pedidoDAO.obtenerDetallesPedido(idOrden);
        StringBuilder sb = new StringBuilder();
        sb.append("Detalles del Pedido #").append(idOrden).append("\n\n");
        
        double total = 0;
        for (Object[] detalle : detalles) {
            String nombre = (String) detalle[0];
            int cantidad = (Integer) detalle[1];
            double precio = (Double) detalle[2];
            double subtotal = cantidad * precio;
            total += subtotal;
            
            sb.append("- ").append(nombre).append("\n");
            sb.append("  Cantidad: ").append(cantidad);
            sb.append(" x S/").append(precio);
            sb.append(" = S/").append(String.format("%.2f", subtotal)).append("\n\n");
        }
        
        sb.append("TOTAL: S/").append(String.format("%.2f", total));
        
        // Usar JTextArea dentro de JScrollPane sin Font específico
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Detalles del Pedido #" + idOrden, JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void dispose() {
        if (timer != null) {
            timer.cancel();
        }
        super.dispose();
    }
}