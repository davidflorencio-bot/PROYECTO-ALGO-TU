package controlador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {
    
    public List<Object[]> obtenerVentasPorFecha(String fechaInicio, String fechaFin) {
        List<Object[]> ventas = new ArrayList<>();
        // CONTAR TODOS los pedidos creados, no solo los "listos"
        String sql = "SELECT DATE(fecha_creacion) as fecha, COUNT(*) as pedidos, SUM(total) as total " +
                    "FROM ordenes " +
                    "WHERE fecha_creacion BETWEEN ? AND ? " +  // QUITAR: AND estado = 'listo'
                    "GROUP BY DATE(fecha_creacion) " +
                    "ORDER BY fecha";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fechaInicio + " 00:00:00");
            pstmt.setString(2, fechaFin + " 23:59:59");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] venta = {
                    rs.getString("fecha"),
                    rs.getInt("pedidos"),
                    rs.getDouble("total")
                };
                ventas.add(venta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventas;
    }
    
    public List<Object[]> obtenerPlatillosMasVendidos() {
        List<Object[]> platillos = new ArrayList<>();
        String sql = "SELECT p.nombre, SUM(d.cantidad) as total_vendido, " +
                    "SUM(d.cantidad * p.precio) as ingresos " +
                    "FROM detalle_orden d " +
                    "JOIN platillos p ON d.id_platillo = p.id_platillo " +
                    "JOIN ordenes o ON d.id_orden = o.id_orden " +
                    // QUITAR: WHERE o.estado = 'listo' 
                    "GROUP BY p.id_platillo, p.nombre " +
                    "ORDER BY total_vendido DESC " +
                    "LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] platillo = {
                    rs.getString("nombre"),
                    rs.getInt("total_vendido"),
                    rs.getDouble("ingresos")
                };
                platillos.add(platillo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return platillos;
    }
    
    public double obtenerTotalVentas() {
        // CONTAR TODOS los pedidos, no solo los "listos"
        String sql = "SELECT COALESCE(SUM(total), 0) as total FROM ordenes"; // QUITAR: WHERE estado = 'listo'
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int obtenerTotalPedidos() {
        // CONTAR TODOS los pedidos
        String sql = "SELECT COUNT(*) as total FROM ordenes"; // QUITAR: WHERE estado = 'listo'
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public double obtenerVentasHoy() {
        String sql = "SELECT COALESCE(SUM(total), 0) as total FROM ordenes " +
                    "WHERE DATE(fecha_creacion) = CURDATE()"; // QUITAR: AND estado = 'listo'
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // MANTENER este método para seguimiento de cocina
    public int obtenerPedidosPendientes() {
        String sql = "SELECT COUNT(*) as total FROM ordenes WHERE estado IN ('pendiente', 'en_cocina')";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // NUEVO MÉTODO: Obtener pedidos completados (para chef)
    public int obtenerPedidosCompletados() {
        String sql = "SELECT COUNT(*) as total FROM ordenes WHERE estado = 'listo'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}