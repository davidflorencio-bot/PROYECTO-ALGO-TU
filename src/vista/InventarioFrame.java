package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import java.awt.*;
import java.util.ArrayList;

public class InventarioFrame extends JFrame {
    public InventarioFrame(ArrayList<Inventario> inventario) {
        setTitle("Inventario del Restaurante");
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {"ID", "Insumo", "Cantidad"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);

        for (Inventario i : inventario) {
            Object[] fila = {i.getNombreInsumo(), i.getNombreInsumo(), i.getCantidad()};
            model.addRow(fila);
        }

        JTable tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}