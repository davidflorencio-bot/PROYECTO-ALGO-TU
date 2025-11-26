package controlador;

import modelo.Pedido;
import modelo.Cliente;
import modelo.Mesero;
import modelo.Mesa;
import modelo.Plato;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    
    public int crearPedido(int idMesa, String nombreCliente) {
        String sql = "INSERT INTO ordenes (id_mesa, estado) VALUES (?, 'pendiente')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, idMesa);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public boolean agregarPlatoAPedido(int idOrden, int idPlatillo, int cantidad) {
        String sql = "INSERT INTO detalle_orden (id_orden, id_platillo, cantidad) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idOrden);
            pstmt.setInt(2, idPlatillo);
            pstmt.setInt(3, cantidad);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Pedido> obtenerPedidosPendientes() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT o.id_orden, o.estado, m.numero_mesa " +
                    "FROM ordenes o JOIN mesas m ON o.id_mesa = m.id_mesa " +
                    "WHERE o.estado IN ('pendiente', 'en_cocina') " +
                    "ORDER BY o.fecha_creacion";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int idOrden = rs.getInt("id_orden");
                String estado = rs.getString("estado");
                String numeroMesa = rs.getString("numero_mesa");
                
                Pedido pedido = crearPedidoDesdeBD(idOrden, estado, numeroMesa);
                if (pedido != null) {
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
    
    public List<Object[]> obtenerDetallesPedido(int idOrden) {
    List<Object[]> detalles = new ArrayList<>();
    String sql = "SELECT p.nombre, d.cantidad, p.precio " +
                "FROM detalle_orden d " +
                "JOIN platillos p ON d.id_platillo = p.id_platillo " +
                "WHERE d.id_orden = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, idOrden);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Object[] detalle = {
                rs.getString("nombre"),
                rs.getInt("cantidad"),
                rs.getDouble("precio")
            };
            detalles.add(detalle);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return detalles;
}
    
    private Pedido crearPedidoDesdeBD(int idOrden, String estado, String numeroMesa) {
        try {
            int numero = Integer.parseInt(numeroMesa.replace("M", ""));
            Mesa mesa = new Mesa(0, numero, 4);
            Cliente cliente = new Cliente(0, "Cliente", "00000000", "presencial");
            Mesero mesero = new Mesero(0, "Mesero", "turno");
            
            Pedido pedido = new Pedido(cliente, mesero, mesa);
            pedido.setIdPedido(idOrden);
            pedido.setEstado(estado);
            
            cargarPlatosDelPedido(pedido, idOrden);
            return pedido;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void cargarPlatosDelPedido(Pedido pedido, int idOrden) {
        String sql = "SELECT p.id_platillo, p.nombre, p.precio, d.cantidad " +
                    "FROM detalle_orden d JOIN platillos p ON d.id_platillo = p.id_platillo " +
                    "WHERE d.id_orden = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idOrden);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Plato plato = new Plato(
                    rs.getInt("id_platillo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    "General"
                );
                int cantidad = rs.getInt("cantidad");
                for (int i = 0; i < cantidad; i++) {
                    pedido.agregarPlato(plato);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean actualizarEstadoPedido(int idOrden, String estado) {
        String sql = "UPDATE ordenes SET estado = ? WHERE id_orden = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estado);
            pstmt.setInt(2, idOrden);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}