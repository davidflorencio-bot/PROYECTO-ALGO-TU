package controlador;

import modelo.Plato;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatoDAO {
    public List<Plato> obtenerPlatosDisponibles() {
        List<Plato> platos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre as categoria FROM platos p " +
                    "JOIN categorias_platos c ON p.id_categoria = c.id_categoria " +
                    "WHERE p.disponible = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Plato plato = new Plato(
                    rs.getInt("id_plato"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getString("categoria") 
                );
                platos.add(plato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return platos;
    }
      
    public boolean agregarPlato(Plato plato) {
        String sql = "INSERT INTO platos (nombre, precio, disponible, id_categoria) VALUES (?, ?, TRUE, 1)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plato.getNombre());
            pstmt.setDouble(2, plato.getPrecio());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean actualizarPlato(Plato plato) {
        String sql = "UPDATE platos SET nombre = ?, precio = ? WHERE id_plato = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plato.getNombre());
            pstmt.setDouble(2, plato.getPrecio());
            pstmt.setInt(3, plato.getIdPlato());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean eliminarPlato(int idPlato) {
        String sql = "UPDATE platos SET disponible = FALSE WHERE id_plato = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPlato);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Plato> obtenerTodosPlatos() {
        List<Plato> platos = new ArrayList<>();
        String sql = "SELECT * FROM platos ORDER BY nombre";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Plato plato = new Plato(
                    rs.getInt("id_plato"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    "General"
                );
                platos.add(plato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return platos;
    }
}