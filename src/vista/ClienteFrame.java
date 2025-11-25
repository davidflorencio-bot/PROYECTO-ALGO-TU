package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ClienteFrame extends JFrame {

    private DefaultTableModel modeloPedidos;

    public ClienteFrame() {
        setTitle("Mis Pedidos - Cliente");
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columnas = {"#", "Plato", "Precio", "Estado"};
        modeloPedidos = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloPedidos);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar pedidos");
        JButton btnCuenta = new JButton("Solicitar cuenta");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCuenta);
        add(panelBotones, BorderLayout.SOUTH);

        // Cargar los pedidos iniciales
        cargarPedidos();

        // Eventos
        btnActualizar.addActionListener(e -> cargarPedidos());
        btnCuenta.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Tu cuenta será llevada a la mesa por un mesero."));
    }

    private void cargarPedidos() {
        modeloPedidos.setRowCount(0);
        ArrayList<String[]> pedidos = DatosCompartidos.obtenerPedidos();
        int i = 1;
        for (String[] p : pedidos) {
            modeloPedidos.addRow(new Object[]{i++, p[0], p[1], p[2]});
        }
    }
}