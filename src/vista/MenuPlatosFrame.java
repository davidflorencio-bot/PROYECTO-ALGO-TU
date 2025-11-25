package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuPlatosFrame extends JFrame {

    // ✅ Constructor sin parámetros (evita el error "Change constructor")
    public MenuPlatosFrame() {
        this(new ArrayList<>()); // llama al otro constructor con lista vacía
    }

    // ✅ Constructor principal (recibe el menú)
    public MenuPlatosFrame(ArrayList<Plato> menu) {
        setTitle("Menú de Platos");
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {"ID", "Plato", "Precio", "Categoría"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);

        for (Plato p : menu) {
            Object[] fila = {p.getIdPlato(), p.getNombre(), p.getPrecio(), p.getCategoria()};
            model.addRow(fila);
        }

        JTable tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}