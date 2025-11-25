package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PedidosPendientesFrame extends JFrame {

    private DefaultTableModel modeloPedidos;

    public PedidosPendientesFrame() {
        setTitle("Pedidos Pendientes - Cocina");
        setSize(600, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columnas = {"#", "Plato", "Precio", "Estado", "Cliente"};
        modeloPedidos = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloPedidos);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar");
        JButton btnListo = new JButton("Marcar como Listo");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnActualizar);
        panelBotones.add(btnListo);
        add(panelBotones, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> cargarPedidos());
        btnListo.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                DatosCompartidos.cambiarEstado(fila, "Listo");
                cargarPedidos();
                JOptionPane.showMessageDialog(this, "Pedido marcado como listo.");
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un pedido primero.");
            }
        });

        cargarPedidos();
    }

    private void cargarPedidos() {
        modeloPedidos.setRowCount(0);
        ArrayList<String[]> lista = DatosCompartidos.obtenerPedidos();
        int i = 1;
        for (String[] p : lista) {
            modeloPedidos.addRow(new Object[]{i++, p[0], p[1], p[2], p[3]});
        }
    }
}