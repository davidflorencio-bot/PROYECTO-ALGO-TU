package vista;

import controlador.InventarioDAO;
import modelo.Inventario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventarioFrame extends JFrame {
    private InventarioDAO inventarioDAO;
    private DefaultTableModel model;
    private JTable tabla;
    
    public InventarioFrame() {
        inventarioDAO = new InventarioDAO();
        initComponents();
        cargarInventario();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Inventario del Restaurante");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        
        // Panel principal
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titulo = new JLabel("GESTIÓN DE INVENTARIO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titulo, BorderLayout.NORTH);
        
        // Tabla
        String[] columnas = {"ID", "Insumo", "Cantidad", "Unidad", "Mínimo", "Estado"};
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(model);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Insumos"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnCerrar = new JButton("Cerrar");
        
        btnActualizar.addActionListener(e -> cargarInventario());
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);
        
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        add(panel);
    }
    
    private void cargarInventario() {
        model.setRowCount(0);
        List<Inventario> inventario = inventarioDAO.obtenerTodoElInventario();
        
        for (Inventario item : inventario) {
            String estado = item.getCantidad() <= item.getMinimo() ? "BAJO" : "OK";
            
            model.addRow(new Object[]{
                item.getId(),
                item.getNombreInsumo(),
                item.getCantidad(),
                item.getUnidad(),
                item.getMinimo(),
                estado
            });
        }
        
        // Mostrar mensaje si no hay datos
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "No se encontraron datos de inventario.\nVerifique la conexión a la base de datos.",
                "Inventario Vacío", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
}