package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import modelo.Plato;

public class PedidoFrame extends JFrame {

    private DefaultTableModel modeloPedido;
    private JTable tablaPedido;
    private JLabel lblTotal;
    private double total;
    private java.util.ArrayList<Plato> menu;

    public PedidoFrame(java.util.ArrayList<Plato> menu) {
        this.menu = menu;

        setTitle("Hacer Pedido - Cliente");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columnas = {"Plato", "Precio"};
        modeloPedido = new DefaultTableModel(columnas, 0);
        tablaPedido = new JTable(modeloPedido);
        add(new JScrollPane(tablaPedido), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout());
        lblTotal = new JLabel("Total: S/ 0.00");
        JButton btnAgregar = new JButton("Agregar Plato");
        JButton btnFactura = new JButton("Generar Factura");

        panelInferior.add(btnAgregar);
        panelInferior.add(btnFactura);
        panelInferior.add(lblTotal);
        add(panelInferior, BorderLayout.SOUTH);

        // Evento: agregar plato
        btnAgregar.addActionListener(e -> agregarPlato());

        // Evento: generar factura
        btnFactura.addActionListener(e -> {
            if (modeloPedido.getRowCount() > 0) {
                String cliente = JOptionPane.showInputDialog(this, "Ingrese su nombre:");
                if (cliente == null || cliente.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar su nombre para registrar el pedido.");
                    return;
                }

                // Guardar los platos en DatosCompartidos
                for (int i = 0; i < modeloPedido.getRowCount(); i++) {
                    String nombre = modeloPedido.getValueAt(i, 0).toString();
                    double precio = Double.parseDouble(modeloPedido.getValueAt(i, 1).toString());
                    DatosCompartidos.agregarPedido(nombre, precio, cliente);
                }

                JOptionPane.showMessageDialog(this, "Pedido realizado correctamente.\n" + lblTotal.getText());
                modeloPedido.setRowCount(0);
                actualizarTotal();
            } else {
                JOptionPane.showMessageDialog(this, "No hay platos en el pedido.");
            }
        });
    }

    private void agregarPlato() {
        String[] nombres = menu.stream().map(Plato::getNombre).toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(this, "Seleccione un plato:", "Menú",
                JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);

        if (seleccion != null) {
            for (Plato p : menu) {
                if (p.getNombre().equals(seleccion)) {
                    modeloPedido.addRow(new Object[]{p.getNombre(), p.getPrecio()});
                    actualizarTotal();
                    break;
                }
            }
        }
    }

    private void actualizarTotal() {
        total = 0;
        for (int i = 0; i < modeloPedido.getRowCount(); i++) {
            total += Double.parseDouble(modeloPedido.getValueAt(i, 1).toString());
        }
        lblTotal.setText(String.format("Total: S/ %.2f", total));
    }
}
