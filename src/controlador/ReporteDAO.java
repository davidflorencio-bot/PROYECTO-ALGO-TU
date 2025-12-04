package controlador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {
    
    
    public List<Object[]> obtenerVentasPorFecha(String fechaInicio, String fechaFin) {
        List<Object[]> ventas = new ArrayList<>();
        String sql = "SELECT DATE(fecha_creacion) as fecha, COUNT(*) as pedidos, SUM(total) as total " +
                    "FROM ordenes " +
                    "WHERE estado = 'cobrado' AND fecha_creacion BETWEEN ? AND ? " +
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
    
    
    public double obtenerGananciasHoy() {
        String sql = "SELECT COALESCE(SUM(total), 0) as ganancias FROM ordenes " +
                    "WHERE DATE(fecha_creacion) = CURDATE() AND estado = 'cobrado'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("ganancias");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public double obtenerTotalVentas() {
        String sql = "SELECT COALESCE(SUM(total), 0) as total FROM ordenes WHERE estado = 'cobrado'";
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
        String sql = "SELECT COUNT(*) as total FROM ordenes WHERE estado = 'cobrado'";
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
                    "WHERE DATE(fecha_creacion) = CURDATE() AND estado = 'cobrado'";
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
    
    
    public int obtenerPedidosPendientes() {
        String sql = "SELECT COUNT(*) as total FROM ordenes WHERE estado IN ('pendiente', 'en_cocina', 'listo')";
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
    
    
    public Object[] obtenerEstadisticasEstados() {
        String sql = "SELECT " +
                    "SUM(CASE WHEN estado = 'pendiente' THEN 1 ELSE 0 END) as pendientes, " +
                    "SUM(CASE WHEN estado = 'en_cocina' THEN 1 ELSE 0 END) as en_cocina, " +
                    "SUM(CASE WHEN estado = 'listo' THEN 1 ELSE 0 END) as listos, " +
                    "SUM(CASE WHEN estado = 'cobrado' THEN 1 ELSE 0 END) as cobrados " +
                    "FROM ordenes WHERE DATE(fecha_creacion) = CURDATE()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return new Object[]{
                    rs.getInt("pendientes"),
                    rs.getInt("en_cocina"),
                    rs.getInt("listos"),
                    rs.getInt("cobrados")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[]{0, 0, 0, 0};
    }
    
    
    public List<Object[]> obtenerPlatosMasVendidos() {
    List<Object[]> platillos = new ArrayList<>();
    String sql = "SELECT p.nombre, SUM(d.cantidad) as total_vendido, " +
                "SUM(d.cantidad * d.precio_unitario) as ingresos " +
                "FROM detalle_orden d " +
                "JOIN platos p ON d.id_plato = p.id_plato " +  
                "JOIN ordenes o ON d.id_orden = o.id_orden " +
                "WHERE o.estado = 'cobrado' " +
                "GROUP BY p.id_plato, p.nombre " +  
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
    
    
    public List<Object[]> obtenerVentasPorMes(int año) {
        List<Object[]> ventas = new ArrayList<>();
        String sql = "SELECT MONTH(fecha_creacion) as mes, COUNT(*) as pedidos, SUM(total) as total " +
                    "FROM ordenes " +
                    "WHERE estado = 'cobrado' AND YEAR(fecha_creacion) = ? " +
                    "GROUP BY MONTH(fecha_creacion) " +
                    "ORDER BY mes";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, año);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] venta = {
                    rs.getInt("mes"),
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
}