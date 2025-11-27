package controlador;

import modelo.Mesa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO {
    public List<Mesa> obtenerMesasDisponibles() {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT * FROM mesas WHERE estado = 'libre'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int idMesa = rs.getInt("id_mesa");
                String numeroMesa = rs.getString("numero_mesa");
                int capacidad = rs.getInt("capacidad");
                
                int numero = Integer.parseInt(numeroMesa.replace("M", ""));
                Mesa mesa = new Mesa(idMesa, numero, capacidad);
                mesas.add(mesa);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mesas;
    }
    
    public boolean actualizarEstadoMesa(int idMesa, String estado) {
        String sql = "UPDATE mesas SET estado = ? WHERE id_mesa = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estado); 
            pstmt.setInt(2, idMesa);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}