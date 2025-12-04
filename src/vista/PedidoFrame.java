package vista;

import controlador.PlatoDAO;
import controlador.PedidoDAO;
import controlador.MesaDAO;
import modelo.Plato;
import modelo.Mesa;
import modelo.Pedido;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class PedidoFrame extends JFrame {
    private PlatoDAO platoDAO;
    private PedidoDAO pedidoDAO;
    private MesaDAO mesaDAO;
    private DefaultTableModel modelPedido;
    private DefaultTableModel modelPedidosListos;
    private List<Plato> platos;
    private List<Mesa> mesas;
    private List<Pedido> pedidosListos;
    private double totalPedido;
    private int idOrdenActual;
    
    private final Color COLOR_ACENTO = new Color(184, 29, 19);
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    
    private JComboBox<String> cmbMesa;
    private JComboBox<String> cmbPlato;
    private JTextField txtCantidad;
    private JTextField txtCliente;
    private JTable tblPedido, tblPedidosListos;
    private JButton btnAgregar, btnEnviarCocina, btnCobrar, btnLimpiar, btnCerrarSesion;
    private JLabel lblTotal;
    private JTabbedPane tabbedPane;
    
    public PedidoFrame() {
        initComponents();
        platoDAO = new PlatoDAO();
        pedidoDAO = new PedidoDAO();
        mesaDAO = new MesaDAO();
        cargarDatos();
        initTablaPedido();
        cargarPedidosListos();
        setLocationRelativeTo(null);
        idOrdenActual = -1;
    }
    
    private void initComponents() {
        setTitle("Sistema Restaurante - Toma de Pedidos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setResizable(true);
        setMinimumSize(new Dimension(1000, 700));
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        
        JPanel header = crearHeader("TOMA DE PEDIDOS - MESERO");
        panelPrincipal.add(header, BorderLayout.NORTH);
        
        tabbedPane = new JTabbedPane(); 
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JPanel panelCrearPedido = crearPanelCrearPedido();
        tabbedPane.addTab("CREAR NUEVO PEDIDO", panelCrearPedido);
        
        JPanel panelPedidosListos = crearPanelPedidosListos();
        tabbedPane.addTab("PEDIDOS LISTOS PARA COBRAR", panelPedidosListos);
        
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private JPanel crearHeader(String titulo) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_ACENTO);
        header.setPreferredSize(new Dimension(1100, 80));
        
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.CENTER);
        
        return header;
    }
    
    private JPanel crearPanelCrearPedido() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JPanel panelSuperior = crearPanelDatosPedido();
        panel.add(panelSuperior, BorderLayout.NORTH);
        
        JPanel panelCentral = crearPanelTablaPedidos();
        panel.add(panelCentral, BorderLayout.CENTER);
        
        JPanel footerCrearPedido = crearFooterCrearPedido();
        panel.add(footerCrearPedido, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearFooterCrearPedido() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_FONDO);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        btnCerrarSesion = crearBotonSecundario("CERRAR SESION");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTotal.setBackground(COLOR_FONDO);
        
        lblTotal = new JLabel("TOTAL: S/0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(COLOR_ACENTO);
        panelTotal.add(lblTotal);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setBackground(COLOR_FONDO);
        
        btnLimpiar = crearBotonSecundario("LIMPIAR PEDIDO");
        btnLimpiar.addActionListener(e -> limpiarPedido());
        
        btnEnviarCocina = crearBotonEnviarCocina("ENVIAR A COCINA", 180);
        btnEnviarCocina.addActionListener(e -> enviarPedidoACocina());
        
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEnviarCocina);
        
        footer.add(btnCerrarSesion, BorderLayout.WEST);
        footer.add(panelTotal, BorderLayout.CENTER);
        footer.add(panelBotones, BorderLayout.EAST);
        
        return footer;
    }
    
    private JPanel crearPanelPedidosListos() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "PEDIDOS PREPARADOS - LISTOS PARA COBRAR"
        ));

        JLabel lblInfo = new JLabel("Estos pedidos han sido marcados como 'LISTOS' por el chef");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(COLOR_TEXTO);
        panelInfo.add(lblInfo, BorderLayout.WEST);

        JButton btnActualizar = crearBotonSecundario("ACTUALIZAR LISTA");
        btnActualizar.addActionListener(e -> cargarPedidosListos());
        panelInfo.add(btnActualizar, BorderLayout.EAST);

        modelPedidosListos = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID Pedido", "Mesa", "Total"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPedidosListos = new JTable(modelPedidosListos);
        tblPedidosListos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblPedidosListos.setRowHeight(35);
        tblPedidosListos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        tblPedidosListos.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblPedidosListos.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblPedidosListos.getColumnModel().getColumn(2).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tblPedidosListos);
        scrollPane.setPreferredSize(new Dimension(900, 300));

        JPanel footerPedidosListos = crearFooterPedidosListos();
        
        panel.add(panelInfo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(footerPedidosListos, BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel crearFooterPedidosListos() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_FONDO);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        JButton btnCerrarSesionListos = crearBotonSecundario("CERRAR SESION");
        btnCerrarSesionListos.addActionListener(e -> cerrarSesion());
        
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInfo.setBackground(COLOR_FONDO);
        
        JLabel lblInfo = new JLabel("Seleccione un pedido y haga click en COBRAR");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInfo.setForeground(COLOR_ACENTO);
        panelInfo.add(lblInfo);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setBackground(COLOR_FONDO);
        
        btnCobrar = crearBotonCobrar("COBRAR PEDIDO SELECCIONADO", 250);
        btnCobrar.addActionListener(e -> cobrarPedidoSeleccionado());
        
        panelBotones.add(btnCobrar);
        
        footer.add(btnCerrarSesionListos, BorderLayout.WEST);
        footer.add(panelInfo, BorderLayout.CENTER);
        footer.add(panelBotones, BorderLayout.EAST);
        
        return footer;
    }
    
    private void cobrarPedidoSeleccionado() {
        int selectedRow = tblPedidosListos.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < pedidosListos.size()) {
            Pedido pedido = pedidosListos.get(selectedRow);
            int idOrden = pedido.getIdPedido();
            double total = pedido.calcularTotal();
            
            int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Confirmar cobro del pedido?\n" +
                "Pedido #: " + idOrden + "\n" +
                "Mesa: M" + pedido.getMesa().getNumero() + "\n" +
                "Total a cobrar: S/" + String.format("%.2f", total),
                "Cobrar Pedido", 
                JOptionPane.YES_NO_OPTION);
            
            if (respuesta == JOptionPane.YES_OPTION) {
                if (pedidoDAO.cobrarPedido(idOrden)) {
                    JOptionPane.showMessageDialog(this, 
                        "✅ Pedido #" + idOrden + " cobrado exitosamente\n" +
                        "Total: S/" + String.format("%.2f", total) + "\n" +
                        "El pedido ahora aparecera en los reportes");
                    
                    cargarPedidosListos();
                } else {
                    JOptionPane.showMessageDialog(this, " Error al cobrar el pedido");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un pedido de la tabla para cobrar", 
                "Pedido no seleccionado", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private JPanel crearPanelDatosPedido() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "DATOS DEL PEDIDO"
        ));
        
        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        fila1.setBackground(Color.WHITE);
        
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtCliente = new JTextField(20);
        txtCliente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel lblMesa = new JLabel("Mesa:");
        lblMesa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cmbMesa = new JComboBox<>();
        cmbMesa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbMesa.setPreferredSize(new Dimension(120, 30));
        
        fila1.add(lblCliente);
        fila1.add(txtCliente);
        fila1.add(Box.createHorizontalStrut(20));
        fila1.add(lblMesa);
        fila1.add(cmbMesa);
        
        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        fila2.setBackground(Color.WHITE);
        
        JLabel lblPlato = new JLabel("Plato:");
        lblPlato.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cmbPlato = new JComboBox<>();
        cmbPlato.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbPlato.setPreferredSize(new Dimension(250, 30));
        
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtCantidad = new JTextField("1", 3);
        txtCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCantidad.setHorizontalAlignment(JTextField.CENTER);
        
        btnAgregar = crearBotonAgregar("AGREGAR AL PEDIDO", 180);
        btnAgregar.addActionListener(e -> agregarPlatoAlPedido());
        
        fila2.add(lblPlato);
        fila2.add(cmbPlato);
        fila2.add(Box.createHorizontalStrut(20));
        fila2.add(lblCantidad);
        fila2.add(txtCantidad);
        fila2.add(Box.createHorizontalStrut(20));
        fila2.add(btnAgregar);
        
        panel.add(fila1);
        panel.add(fila2);
        
        return panel;
    }
    
    private JPanel crearPanelTablaPedidos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "DETALLES DEL PEDIDO ACTUAL"
        ));
        
        modelPedido = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Plato", "Cantidad", "Precio Unitario", "Subtotal"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblPedido = new JTable(modelPedido);
        tblPedido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblPedido.setRowHeight(25);
        tblPedido.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        tblPedido.getColumnModel().getColumn(0).setPreferredWidth(250);
        tblPedido.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblPedido.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblPedido.getColumnModel().getColumn(3).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(tblPedido);
        scrollPane.setPreferredSize(new Dimension(800, 250));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton crearBotonAgregar(String texto, int ancho) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(34, 139, 34));
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 100, 0), 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        boton.setPreferredSize(new Dimension(ancho, 45));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(50, 205, 50));
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 100, 0), 3),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(34, 139, 34));
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 100, 0), 2),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return boton;
    }
    
    private JButton crearBotonEnviarCocina(String texto, int ancho) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(30, 144, 255));
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 139), 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        boton.setPreferredSize(new Dimension(ancho, 45));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(70, 130, 180));
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 139), 3),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(30, 144, 255));
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 139), 2),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return boton;
    }
    
    private JButton crearBotonCobrar(String texto, int ancho) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(46, 204, 113));
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 174, 96), 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        boton.setPreferredSize(new Dimension(ancho, 45));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(88, 214, 141));
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(39, 174, 96), 3),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(46, 204, 113));
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(39, 174, 96), 2),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return boton;
    }
    
    private JButton crearBotonPrimario(String texto, int ancho) {
        JButton boton = new JButton(texto);
        boton.setBackground(COLOR_ACENTO);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACENTO.darker(), 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        boton.setPreferredSize(new Dimension(ancho, 45));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_ACENTO.brighter());
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACENTO.darker(), 3),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_ACENTO);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACENTO.darker(), 2),
                    BorderFactory.createEmptyBorder(12, 20, 12, 20)
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
    
    private void cargarDatos() {
        mesas = mesaDAO.obtenerMesasDisponibles();
        cmbMesa.removeAllItems();
        
        for (Mesa mesa : mesas) {
            cmbMesa.addItem("M" + mesa.getNumero());
        }
        
        platos = platoDAO.obtenerPlatosDisponibles();
        cmbPlato.removeAllItems();
        
        if (platos != null) {
            for (Plato plato : platos) {
                cmbPlato.addItem(plato.getNombre() + " - S/" + plato.getPrecio());
            }
        } else {
            cmbPlato.addItem("No hay platos disponibles");
        }
        
        if (cmbMesa.getItemCount() > 0) cmbMesa.setSelectedIndex(0);
        if (cmbPlato.getItemCount() > 0) cmbPlato.setSelectedIndex(0);
    }
    
    private void initTablaPedido() {
        totalPedido = 0.0;
        actualizarTotal();
    }
    
    private void cargarPedidosListos() {
        if (modelPedidosListos != null) {
            modelPedidosListos.setRowCount(0);
            pedidosListos = pedidoDAO.obtenerPedidosListosParaCobrar();
            
            if (pedidosListos != null) {
                for (Pedido pedido : pedidosListos) {
                    modelPedidosListos.addRow(new Object[]{
                        "PED-" + pedido.getIdPedido(),
                        "M" + pedido.getMesa().getNumero(),
                        String.format("S/%.2f", pedido.calcularTotal())
                    });
                }
            }
            
            if (pedidosListos != null && pedidosListos.size() > 0) {
                tabbedPane.setTitleAt(1, "PEDIDOS LISTOS PARA COBRAR (" + pedidosListos.size() + ")");
            } else {
                tabbedPane.setTitleAt(1, "PEDIDOS LISTOS PARA COBRAR");
            }
        }
    }
    
    private void agregarPlatoAlPedido() {
        if (idOrdenActual == -1) {
            if (!crearNuevoPedido()) return;
        }
        
        int selectedIndex = cmbPlato.getSelectedIndex();
        if (selectedIndex >= 0 && platos != null && selectedIndex < platos.size()) {
            try {
                Plato platoSeleccionado = platos.get(selectedIndex);
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0");
                    return;
                }
                
                boolean exito = pedidoDAO.agregarPlatoAPedido(idOrdenActual, platoSeleccionado.getIdPlato(), cantidad);
                
                if (exito) {
                    double subtotal = platoSeleccionado.getPrecio() * cantidad;
                    totalPedido += subtotal;
                    
                    modelPedido.addRow(new Object[]{
                        platoSeleccionado.getNombre(),
                        cantidad,
                        String.format("S/%.2f", platoSeleccionado.getPrecio()),
                        String.format("S/%.2f", subtotal)
                    });
                    
                    actualizarTotal();
                    txtCantidad.setText("1");
                    
                    JOptionPane.showMessageDialog(this, 
                        "Plato agregado al pedido #" + idOrdenActual);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar plato al pedido");
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un plato válido");
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
            
           String nombreCliente = txtCliente.getText().trim();
           idOrdenActual = pedidoDAO.crearPedido(idMesa, nombreCliente);
            
            if (idOrdenActual != -1) {
                mesaDAO.actualizarEstadoMesa(idMesa, "ocupada");
                
                JOptionPane.showMessageDialog(this, 
                    "Pedido #" + idOrdenActual + " creado exitosamente\n" +
                    "Cliente: " + txtCliente.getText() + "\n" +
                    "Mesa: " + mesaSeleccionada);
                
                cmbMesa.setEnabled(false);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear el pedido");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }
    
    private void enviarPedidoACocina() {
        if (idOrdenActual == -1) {
            JOptionPane.showMessageDialog(this, "No hay un pedido activo para enviar a cocina");
            return;
        }
        
        if (modelPedido.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue platos al pedido antes de enviar a cocina");
            return;
        }
        
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Enviar pedido a cocina?\n" +
            "Pedido #: " + idOrdenActual + "\n" +
            "Cliente: " + txtCliente.getText() + "\n" +
            "Total: S/" + String.format("%.2f", totalPedido) + "\n\n" +
            "⚠️ Una vez enviado, no podrá modificar el pedido",
            "Enviar a Cocina", 
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            if (pedidoDAO.enviarACocina(idOrdenActual)) {
                JOptionPane.showMessageDialog(this, 
                    "Pedido #" + idOrdenActual + " enviado a cocina exitosamente\n" +
                    "El chef recibirá el pedido y lo preparará");
                
                limpiarPedido();
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al enviar pedido a cocina");
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
        cargarDatos();
    }
    
    private void actualizarTotal() {
        lblTotal.setText("TOTAL: S/" + String.format("%.2f", totalPedido));
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