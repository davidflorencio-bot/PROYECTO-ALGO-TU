package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PedidosMeseroFrame extends JFrame {
    private DefaultTableModel modeloPedidos;

    public PedidosMeseroFrame() {
        setTitle("Gestión de Pedidos - Mesero");
        setSize(550, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columnas = {"#", "Plato", "Precio", "Estado"};
        modeloPedidos = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloPedidos);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEntregado = new JButton("Marcar como Entregado");
        JButton btnCobrar = new JButton("Cobrar Pedido");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEntregado);
        panelBotones.add(btnCobrar);
        add(panelBotones, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> cargarPedidos());
        btnEntregado.addActionListener(e -> cambiarEstado(tabla, "Entregado"));
        btnCobrar.addActionListener(e -> cambiarEstado(tabla, "Cobrado"));

        cargarPedidos();
    }

    private void cargarPedidos() {
        modeloPedidos.setRowCount(0);
        ArrayList<String[]> pedidos = DatosCompartidos.obtenerPedidos();
        int i = 1;
        for (String[] p : pedidos) {
            modeloPedidos.addRow(new Object[]{i++, p[0], p[1], p[2]});
        }
    }

    private void cambiarEstado(JTable tabla, String nuevoEstado) {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            DatosCompartidos.obtenerPedidos().get(fila)[2] = nuevoEstado;
            cargarPedidos();
            JOptionPane.showMessageDialog(this, "Pedido marcado como " + nuevoEstado + ".");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido para cambiar su estado.");
        }
    }
}