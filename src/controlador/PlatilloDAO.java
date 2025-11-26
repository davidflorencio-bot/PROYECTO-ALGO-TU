package controlador;

import modelo.Plato;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatilloDAO {
    public List<Plato> obtenerPlatillosDisponibles() {
    List<Plato> platillos = new ArrayList<>();
    String sql = "SELECT p.*, c.nombre as categoria FROM platillos p " +
                "JOIN categorias_platillos c ON p.id_categoria = c.id_categoria " +
                "WHERE p.disponible = TRUE";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            Plato plato = new Plato(
                rs.getInt("id_platillo"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getString("categoria") // Ahora usa la categoría real
            );
            platillos.add(plato);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return platillos;
}
      
    public boolean agregarPlatillo(Plato platillo) {
        String sql = "INSERT INTO platillos (nombre, precio, disponible, id_categoria) VALUES (?, ?, TRUE, 1)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, platillo.getNombre());
            pstmt.setDouble(2, platillo.getPrecio());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean actualizarPlatillo(Plato platillo) {
        String sql = "UPDATE platillos SET nombre = ?, precio = ? WHERE id_platillo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, platillo.getNombre());
            pstmt.setDouble(2, platillo.getPrecio());
            pstmt.setInt(3, platillo.getIdPlato());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean eliminarPlatillo(int idPlatillo) {
        String sql = "UPDATE platillos SET disponible = FALSE WHERE id_platillo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPlatillo);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Plato> obtenerTodosPlatillos() {
        List<Plato> platillos = new ArrayList<>();
        String sql = "SELECT * FROM platillos ORDER BY nombre";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Plato plato = new Plato(
                    rs.getInt("id_platillo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    "General"
                );
                platillos.add(plato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return platillos;
    }
}
