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
    private int idOrdenActual; // NUEVO: Para guardar el ID del pedido actual
    
    // Componentes
    private JComboBox<String> cmbMesa;
    private JComboBox<String> cmbPlatillo;
    private JTextField txtCantidad;
    private JTextField txtCliente;
    private JTable tblPedido;
    private JButton btnAgregar, btnFinalizar, btnLimpiar, btnCerrarSesion;
    private JLabel lblTotal;
    
    public PedidoFrame() {
        initComponents();
        platilloDAO = new PlatilloDAO();
        pedidoDAO = new PedidoDAO();
        mesaDAO = new MesaDAO();
        cargarDatos();
        initTablaPedido();
        setLocationRelativeTo(null);
        idOrdenActual = -1; // Inicializar sin pedido activo
    }
    
    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("🍽️ Toma de Pedidos - Mesero");
        
        // Crear componentes
        txtCliente = new JTextField(15);
        cmbMesa = new JComboBox<>();
        cmbMesa.setPreferredSize(new Dimension(120, 25));
        cmbPlatillo = new JComboBox<>();
        cmbPlatillo.setPreferredSize(new Dimension(200, 25));
        txtCantidad = new JTextField("1", 3);
        
        btnAgregar = new JButton("➕ Agregar al Pedido");
        btnFinalizar = new JButton("💳 Finalizar Pedido");
        btnLimpiar = new JButton("🗑️ Limpiar");
        btnCerrarSesion = new JButton("🚪 Cerrar Sesión");
        
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
        tblPedido.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblPedido.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblPedido.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblPedido.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tblPedido);
        scrollPane.setPreferredSize(new Dimension(700, 250));
        
        // LAYOUT MEJORADO
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // PANEL SUPERIOR - Datos del pedido
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("📋 Datos del Pedido"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        panelSuperior.add(new JLabel("👤 Cliente:"), gbc);
        gbc.gridx = 1;
        panelSuperior.add(txtCliente, gbc);
        
        gbc.gridx = 2;
        panelSuperior.add(new JLabel("🪑 Mesa:"), gbc);
        gbc.gridx = 3;
        panelSuperior.add(cmbMesa, gbc);
        
        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        panelSuperior.add(new JLabel("🍽️ Platillo:"), gbc);
        gbc.gridx = 1;
        panelSuperior.add(cmbPlatillo, gbc);
        
        gbc.gridx = 2;
        panelSuperior.add(new JLabel("🔢 Cantidad:"), gbc);
        gbc.gridx = 3;
        panelSuperior.add(txtCantidad, gbc);
        
        gbc.gridx = 4;
        panelSuperior.add(btnAgregar, gbc);
        
        // PANEL CENTRAL - Tabla de pedidos
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("📦 Detalles del Pedido"));
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        
        // PANEL INFERIOR - Botones y total
        JPanel panelInferior = new JPanel(new BorderLayout());
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnFinalizar);
        panelBotones.add(lblTotal);
        
        JPanel panelCerrarSesion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCerrarSesion.add(btnCerrarSesion);
        
        panelInferior.add(panelCerrarSesion, BorderLayout.WEST);
        panelInferior.add(panelBotones, BorderLayout.EAST);
        
        // Ensamblar la interfaz
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // EVENT LISTENERS CORREGIDOS
        btnAgregar.addActionListener(e -> agregarPlatoAlPedido());
        btnFinalizar.addActionListener(e -> finalizarPedidoCompleto());
        btnLimpiar.addActionListener(e -> limpiarPedido());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        // Tamaño de la ventana
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private void cargarDatos() {
        // Cargar mesas disponibles
        mesas = mesaDAO.obtenerMesasDisponibles();
        cmbMesa.removeAllItems();
        
        System.out.println("Cargando mesas... Total: " + mesas.size());
        
        for (Mesa mesa : mesas) {
            String item = "M" + mesa.getNumero();
            cmbMesa.addItem(item);
            System.out.println("Mesa agregada: " + item);
        }
        
        // Cargar platillos disponibles
        platillos = platilloDAO.obtenerPlatillosDisponibles();
        cmbPlatillo.removeAllItems();
        for (Plato plato : platillos) {
            cmbPlatillo.addItem(plato.getNombre() + " - S/" + plato.getPrecio());
        }
        
        // Seleccionar primera mesa y platillo si existen
        if (cmbMesa.getItemCount() > 0) {
            cmbMesa.setSelectedIndex(0);
        }
        if (cmbPlatillo.getItemCount() > 0) {
            cmbPlatillo.setSelectedIndex(0);
        }
    }
    
    private void initTablaPedido() {
        totalPedido = 0.0;
        actualizarTotal();
    }
    
    private void agregarPlatoAlPedido() {
        // VERIFICAR SI HAY UN PEDIDO ACTIVO, SINO CREAR UNO NUEVO
        if (idOrdenActual == -1) {
            if (!crearNuevoPedido()) {
                return; // Si no se pudo crear el pedido, salir
            }
        }
        
        int selectedIndex = cmbPlatillo.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < platillos.size()) {
            try {
                Plato plato = platillos.get(selectedIndex);
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0");
                    return;
                }
                
                // AGREGAR A LA BASE DE DATOS
                boolean exito = pedidoDAO.agregarPlatoAPedido(idOrdenActual, plato.getIdPlato(), cantidad);
                
                if (exito) {
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
                    
                    JOptionPane.showMessageDialog(this, 
                        "✅ Platillo agregado al pedido #" + idOrdenActual);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al agregar platillo");
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un platillo válido");
        }
    }
    
    private boolean crearNuevoPedido() {
        if (txtCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del cliente");
            return false;
        }
        
        if (cmbMesa.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una mesa");
            return false;
        }
        
        try {
            String mesaSeleccionada = cmbMesa.getSelectedItem().toString();
            int idMesa = Integer.parseInt(mesaSeleccionada.replace("M", ""));
            
            // CREAR PEDIDO EN BD (método corregido)
            idOrdenActual = pedidoDAO.crearPedido(idMesa);
            
            if (idOrdenActual != -1) {
                // Actualizar estado de la mesa a OCUPADA
                mesaDAO.actualizarEstadoMesa(idMesa, "ocupada");
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Pedido #" + idOrdenActual + " creado exitosamente\n" +
                    "👤 Cliente: " + txtCliente.getText() + "\n" +
                    "🪑 Mesa: " + mesaSeleccionada);
                
                // Deshabilitar cambio de mesa
                cmbMesa.setEnabled(false);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al crear el pedido");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private void finalizarPedidoCompleto() {
        if (idOrdenActual == -1) {
            JOptionPane.showMessageDialog(this, "No hay un pedido activo para finalizar");
            return;
        }
        
        if (modelPedido.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue platillos al pedido antes de finalizar");
            return;
        }
        
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Confirmar pago y finalizar pedido?\n" +
            "Pedido #: " + idOrdenActual + "\n" +
            "Cliente: " + txtCliente.getText() + "\n" +
            "Total: S/" + String.format("%.2f", totalPedido),
            "💳 Finalizar Pedido", 
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            // USAR EL NUEVO MÉTODO finalizarPedido() que cambia estado a 'entregado'
            if (pedidoDAO.finalizarPedido(idOrdenActual)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Pedido #" + idOrdenActual + " finalizado exitosamente\n" +
                    "💰 Total cobrado: S/" + String.format("%.2f", totalPedido) + "\n" +
                    "📊 Este pedido ahora aparecerá en los reportes del administrador");
                
                limpiarPedido();
                cargarDatos(); // Recargar mesas disponibles
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al finalizar pedido");
            }
        }
    }
    
    private void limpiarPedido() {
        modelPedido.setRowCount(0);
        totalPedido = 0.0;
        actualizarTotal();
        txtCliente.setText("");
        txtCantidad.setText("1");
        idOrdenActual = -1;
        cmbMesa.setEnabled(true);
        
        // Recargar mesas por si alguna se liberó
        cargarDatos();
    }
    
    private void actualizarTotal() {
        lblTotal.setText("Total: S/" + String.format("%.2f", totalPedido));
    }
    
    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }
}