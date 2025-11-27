package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuPlatosFrame extends JFrame {

    
    public MenuPlatosFrame() {
        this(new ArrayList<>()); 
    }


    public MenuPlatosFrame(ArrayList<Plato> menu) {
        setTitle("Menu de Platos");
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {"ID", "Plato", "Precio", "Categoria"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);

        for (Plato p : menu) {
            Object[] fila = {p.getIdPlato(), p.getNombre(), p.getPrecio(), p.getCategoria()};
            model.addRow(fila);
        }

        JTable tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}