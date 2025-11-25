package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportesFrame extends JFrame {

    public ReportesFrame() {
        setTitle("Reportes de Ventas - Administrador");
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columnas = {"Fecha", "Plato", "Cantidad Vendida", "Total (S/)"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // Datos simulados
        modelo.addRow(new Object[]{"2025-11-13", "Ceviche", 12, 300.0});
        modelo.addRow(new Object[]{"2025-11-13", "Lomo Saltado", 8, 240.0});
        modelo.addRow(new Object[]{"2025-11-13", "Tiramisú", 5, 75.0});

        JTable tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnExportar = new JButton("Exportar reporte");
        add(btnExportar, BorderLayout.SOUTH);

        btnExportar.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "El reporte ha sido exportado (simulado)."));
    }
}