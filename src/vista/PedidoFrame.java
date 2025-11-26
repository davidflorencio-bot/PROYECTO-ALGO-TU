package vista;

import controlador.PlatilloDAO;
import controlador.PedidoDAO;
import controlador.MesaDAO;
import modelo.Plato;
import modelo.Mesa;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PedidoFrame extends JFrame {
    private PlatilloDAO platilloDAO;
    private PedidoDAO pedidoDAO;
    private MesaDAO mesaDAO;
    private DefaultTableModel modelPedido;
    private List<Plato> platillos;
    private List<Mesa> mesas;
    private double totalPedido;
    
    // Componentes
    private JComboBox<String> cmbMesa;
    private JComboBox<String> cmbPlatillo;
    private JTextField txtCantidad;
    private JTextField txtCliente;
    private JTable tblPedido;
    private JButton btnAgregar, btnFinalizar, btnLimpiar;
    private JLabel lblTotal;
    
    public PedidoFrame() {
        initComponents();
        platilloDAO = new PlatilloDAO();
        pedidoDAO = new PedidoDAO();
        mesaDAO = new MesaDAO();
        cargarDatos();
        initTablaPedido();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Toma de Pedidos - Mesero");
        
        // Crear componentes CON TAMAÑOS DEFINIDOS
        txtCliente = new JTextField(15);
        cmbMesa = new JComboBox<>();
        cmbMesa.setPreferredSize(new Dimension(120, 25));
        cmbPlatillo = new JComboBox<>();
        cmbPlatillo.setPreferredSize(new Dimension(200, 25));
        txtCantidad = new JTextField("1", 3);
        btnAgregar = new JButton("Agregar al Pedido");
        btnFinalizar = new JButton("Finalizar Pedido");
        btnLimpiar = new JButton("Limpiar");
        lblTotal = new JLabel("Total: S/0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Configurar tabla
        modelPedido = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Platillo", "Cantidad", "Precio", "Subtotal"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPedido = new JTable(modelPedido);
        // Hacer la tabla más ancha
        tblPedido.getColumnModel().getColumn(0).setPreferredWidth(200); // Platillo
        tblPedido.getColumnModel().getColumn(1).setPreferredWidth(80);  // Cantidad
        tblPedido.getColumnModel().getColumn(2).setPreferredWidth(80);  // Precio
        tblPedido.getColumnModel().getColumn(3).setPreferredWidth(100); // Subtotal
        
        JScrollPane scrollPane = new JScrollPane(tblPedido);
        scrollPane.setPreferredSize(new Dimension(700, 250));
        
        // LAYOUT MEJORADO - Usar GridBagLayout para mejor control
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // PANEL SUPERIOR - Datos del pedido
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Datos del Pedido"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        panelSuperior.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        panelSuperior.add(txtCliente, gbc);
        
        gbc.gridx = 2;
        panelSuperior.add(new JLabel("Mesa:"), gbc);
        gbc.gridx = 3;
        panelSuperior.add(cmbMesa, gbc);
        
        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        panelSuperior.add(new JLabel("Platillo:"), gbc);
        gbc.gridx = 1;
        panelSuperior.add(cmbPlatillo, gbc);
        
        gbc.gridx = 2;
        panelSuperior.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 3;
        panelSuperior.add(txtCantidad, gbc);
        
        gbc.gridx = 4;
        panelSuperior.add(btnAgregar, gbc);
        
        // PANEL CENTRAL - Tabla de pedidos
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Detalles del Pedido"));
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        
        // PANEL INFERIOR - Botones y total
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnLimpiar);
        panelInferior.add(btnFinalizar);
        panelInferior.add(lblTotal);
        
        // Ensamblar la interfaz
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // Event listeners
        btnAgregar.addActionListener(e -> agregarPlatoAlPedido());
        btnFinalizar.addActionListener(e -> finalizarPedido());
        btnLimpiar.addActionListener(e -> limpiarPedido());
        
        // Tamaño de la ventana
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private void cargarDatos() {
        // Cargar mesas
        mesas = mesaDAO.obtenerMesasDisponibles();
        cmbMesa.removeAllItems();
        
        System.out.println("Cargando mesas... Total: " + mesas.size());
        
        for (Mesa mesa : mesas) {
            String item = "M" + mesa.getNumero();
            cmbMesa.addItem(item);
            System.out.println("Mesa agregada: " + item);
        }
        
        // Cargar platillos
        platillos = platilloDAO.obtenerPlatillosDisponibles();
        cmbPlatillo.removeAllItems();
        for (Plato plato : platillos) {
            cmbPlatillo.addItem(plato.getNombre() + " - S/" + plato.getPrecio());
        }
        
        // Si hay mesas, seleccionar la primera automáticamente
        if (cmbMesa.getItemCount() > 0) {
            cmbMesa.setSelectedIndex(0);
            System.out.println("Mesa seleccionada: " + cmbMesa.getSelectedItem());
        }
        
        // Si hay platillos, seleccionar el primero automáticamente
        if (cmbPlatillo.getItemCount() > 0) {
            cmbPlatillo.setSelectedIndex(0);
        }
    }
    
    private void initTablaPedido() {
        totalPedido = 0.0;
        actualizarTotal();
    }
    
    private void agregarPlatoAlPedido() {
        int selectedIndex = cmbPlatillo.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < platillos.size()) {
            try {
                Plato plato = platillos.get(selectedIndex);
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0");
                    return;
                }
                
                double subtotal = plato.getPrecio() * cantidad;
                totalPedido += subtotal;
                
                modelPedido.addRow(new Object[]{
                    plato.getNombre(),
                    cantidad,
                    String.format("S/%.2f", plato.getPrecio()),
                    String.format("S/%.2f", subtotal)
                });
                
                actualizarTotal();
                txtCantidad.setText("1");
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un platillo válido");
        }
    }
    
    private void finalizarPedido() {
        if (modelPedido.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue platillos al pedido");
            return;
        }
        
        if (txtCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del cliente");
            return;
        }
        
        if (cmbMesa.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una mesa");
            return;
        }
        
        try {
            String mesaSeleccionada = cmbMesa.getSelectedItem().toString();
            int idMesa = Integer.parseInt(mesaSeleccionada.replace("M", ""));
            
            // Crear pedido en BD
            int idOrden = pedidoDAO.crearPedido(idMesa, txtCliente.getText().trim());
            
            if (idOrden != -1) {
                // Agregar platillos al pedido
                for (int i = 0; i < modelPedido.getRowCount(); i++) {
                    String platilloNombre = modelPedido.getValueAt(i, 0).toString();
                    int cantidad = Integer.parseInt(modelPedido.getValueAt(i, 1).toString());
                    
                    for (Plato plato : platillos) {
                        if (plato.getNombre().equals(platilloNombre)) {
                            pedidoDAO.agregarPlatoAPedido(idOrden, plato.getIdPlato(), cantidad);
                            break;
                        }
                    }
                }
                
                // Actualizar estado de la mesa
                mesaDAO.actualizarEstadoMesa(idMesa, "ocupada");
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Pedido #" + idOrden + " registrado exitosamente\n" +
                    "👤 Cliente: " + txtCliente.getText() + "\n" +
                    "🪑 Mesa: " + mesaSeleccionada + "\n" +
                    "💰 Total: S/" + String.format("%.2f", totalPedido));
                
                limpiarPedido();
                cargarDatos(); // Recargar mesas disponibles
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al registrar el pedido");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void limpiarPedido() {
        modelPedido.setRowCount(0);
        totalPedido = 0.0;
        actualizarTotal();
        txtCliente.setText("");
        txtCantidad.setText("1");
    }
    
    private void actualizarTotal() {
        lblTotal.setText("Total: S/" + String.format("%.2f", totalPedido));
    }
}