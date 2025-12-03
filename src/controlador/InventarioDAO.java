package controlador;

import modelo.Inventario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO {
    
    public List<Inventario> obtenerTodoElInventario() {
        List<Inventario> inventario = new ArrayList<>();
        String sql = "SELECT * FROM inventario ORDER BY nombre_insumo";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Inventario item = new Inventario();
                item.setId(rs.getInt("id_insumo"));
                item.setNombreInsumo(rs.getString("nombre_insumo"));
                item.setCantidad(rs.getDouble("cantidad"));
                item.setUnidad(rs.getString("unidad"));
                item.setMinimo(rs.getDouble("minimo"));
                inventario.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            return obtenerInventarioEjemplo();
        }
        return inventario;
    }
    
    
    private List<Inventario> obtenerInventarioEjemplo() {
        List<Inventario> inventario = new ArrayList<>();
        
        
        String[][] datos = {
            {"1", "Papa Amarilla", "50", "kg", "10"},
            {"2", "AjI Amarillo", "5", "kg", "2"},
            {"3", "Pollo", "30", "kg", "15"},
            {"4", "Pescado", "25", "kg", "10"},
            {"5", "Arroz", "100", "kg", "20"},
            {"6", "Cebolla", "40", "kg", "8"},
            {"7", "Ajo", "3", "kg", "1"},
            {"8", "Limon", "60", "unidades", "20"},
            {"9", "Maiz Morado", "15", "kg", "5"},
            {"10", "Leche", "30", "litros", "10"}
        };
        
        for (String[] dato : datos) {
            Inventario item = new Inventario();
            item.setId(Integer.parseInt(dato[0]));
            item.setNombreInsumo(dato[1]);
            item.setCantidad(Double.parseDouble(dato[2]));
            item.setUnidad(dato[3]);
            item.setMinimo(Double.parseDouble(dato[4]));
            inventario.add(item);
        }
        
        return inventario;
    }
}