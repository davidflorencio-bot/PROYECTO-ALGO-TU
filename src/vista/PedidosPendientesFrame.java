package vista;

import controlador.PedidoDAO;
import modelo.Pedido;
import modelo.Plato;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PedidosPendientesFrame extends JFrame {
    private PedidoDAO pedidoDAO;
    private DefaultTableModel modelPedidos;
    private Timer timer;
    
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
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
        
        // Botones con diseño simple
        btnPreparando = new JButton("En Preparación");
        btnCompletado = new JButton("Completado");
        btnActualizar = new JButton("Actualizar");
        btnVerDetalles = new JButton("Ver Detalles");
        
        // NUEVOS BOTONES
        JButton btnVerInventario = new JButton("Ver Inventario");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        
        // Listeners
        btnPreparando.addActionListener(evt -> actualizarEstado("en_cocina"));
        btnCompletado.addActionListener(evt -> actualizarEstado("listo"));
        btnActualizar.addActionListener(evt -> cargarPedidosPendientes());
        btnVerDetalles.addActionListener(evt -> btnVerDetallesActionPerformed());
        btnVerInventario.addActionListener(evt -> {
            InventarioFrame inventarioFrame = new InventarioFrame();
            inventarioFrame.setVisible(true);
        });
        btnCerrarSesion.addActionListener(evt -> {
            int respuesta = JOptionPane.showConfirmDialog(this, 
                "¿Cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                this.dispose();
            }
        });
        
        // Layout
        setLayout(new BorderLayout());
        add(jScrollPane1, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnPreparando);
        panelBotones.add(btnCompletado);
        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnVerInventario);
        panelBotones.add(btnCerrarSesion);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        setSize(700, 400);
    }
    
    private void initTablaPedidos() {
        // Configurar anchos de columnas
        tblPedidos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblPedidos.getColumnModel().getColumn(1).setPreferredWidth(60);
        tblPedidos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblPedidos.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblPedidos.getColumnModel().getColumn(4).setPreferredWidth(300);
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
                SwingUtilities.invokeLater(() -> cargarPedidosPendientes());
            }
        }, 0, 30000);
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
    
    private void btnVerDetallesActionPerformed() {
        int selectedRow = tblPedidos.getSelectedRow();
        if (selectedRow >= 0) {
            int idOrden = (int) modelPedidos.getValueAt(selectedRow, 0);
            mostrarDetallesPedido(idOrden);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para ver detalles");
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
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
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