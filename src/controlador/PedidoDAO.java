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
        String sql = "INSERT INTO ordenes (id_mesa, nombre_cliente, estado, total) VALUES (?, ?, 'pendiente', 0)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, idMesa);
            pstmt.setString(2, nombreCliente);
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
    
    public boolean agregarPlatoAPedido(int idOrden, int idPlato, int cantidad) {
        
        double precio = obtenerPrecioPlato(idPlato);
        if (precio == -1) return false;
        
        double subtotal = precio * cantidad;
        
        String sql = "INSERT INTO detalle_orden (id_orden, id_plato, cantidad, precio_unitario, subtotal) " +
                    "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idOrden);
            pstmt.setInt(2, idPlato);
            pstmt.setInt(3, cantidad);
            pstmt.setDouble(4, precio);
            pstmt.setDouble(5, subtotal);
            
            boolean resultado = pstmt.executeUpdate() > 0;
            
            if (resultado) {
                actualizarTotalPedido(idOrden);
            }
            
            return resultado;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private double obtenerPrecioPlato(int idPlato) {
        String sql = "SELECT precio FROM platos WHERE id_plato = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPlato);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("precio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    private void actualizarTotalPedido(int idOrden) {
        String sql = "UPDATE ordenes SET total = (" +
                    "SELECT COALESCE(SUM(subtotal), 0) FROM detalle_orden WHERE id_orden = ?" +
                    ") WHERE id_orden = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idOrden);
            pstmt.setInt(2, idOrden);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean enviarACocina(int idOrden) {
        String sql = "UPDATE ordenes SET estado = 'en_cocina' WHERE id_orden = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idOrden);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean cobrarPedido(int idOrden) {
        String sql = "UPDATE ordenes SET estado = 'cobrado' WHERE id_orden = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idOrden);
            boolean resultado = pstmt.executeUpdate() > 0;
            
            if (resultado) {
                liberarMesa(idOrden);
            }
            
            return resultado;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Pedido> obtenerPedidosParaCocina() {
    List<Pedido> pedidos = new ArrayList<>();
    String sql = "SELECT o.id_orden, o.estado, o.total, m.numero_mesa, o.nombre_cliente " +  // ✅ CORREGIDO
                "FROM ordenes o JOIN mesas m ON o.id_mesa = m.id_mesa " +
                "WHERE o.estado IN ('pendiente', 'en_cocina') " +
                "ORDER BY o.fecha_creacion";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            int idOrden = rs.getInt("id_orden");
            String estado = rs.getString("estado");
            double total = rs.getDouble("total");
            String numeroMesa = rs.getString("numero_mesa");  // ✅ numero_mesa
            String nombreCliente = rs.getString("nombre_cliente");
            
            Pedido pedido = crearPedidoDesdeBD(idOrden, estado, numeroMesa, nombreCliente);
            if (pedido != null) {
                pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
        e.printStackTrace();
        }
    return pedidos;
    }
    
    public List<Pedido> obtenerPedidosListosParaCobrar() {
    List<Pedido> pedidos = new ArrayList<>();
    String sql = "SELECT o.id_orden, o.estado, o.total, m.numero_mesa, o.nombre_cliente " +  
                "FROM ordenes o JOIN mesas m ON o.id_mesa = m.id_mesa " +
                "WHERE o.estado = 'listo' " +
                "ORDER BY o.fecha_creacion";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            int idOrden = rs.getInt("id_orden");
            String estado = rs.getString("estado");
            double total = rs.getDouble("total");
            String numeroMesa = rs.getString("numero_mesa");  
            String nombreCliente = rs.getString("nombre_cliente");
            
            Pedido pedido = crearPedidoDesdeBD(idOrden, estado, numeroMesa, nombreCliente);
            if (pedido != null) {
                pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
        e.printStackTrace();
        }
        return pedidos;
    }
    
    private void liberarMesa(int idOrden) {
        String sql = "UPDATE mesas SET estado = 'libre' WHERE id_mesa = (" +
                    "SELECT id_mesa FROM ordenes WHERE id_orden = ?" +
                    ")";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idOrden);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Object[]> obtenerDetallesPedido(int idOrden) {
        List<Object[]> detalles = new ArrayList<>();
        String sql = "SELECT p.nombre, d.cantidad, d.precio_unitario as precio " +
                    "FROM detalle_orden d " +
                    "JOIN platos p ON d.id_plato = p.id_plato " +
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
    
    public boolean finalizarPedido(int idOrden) {
        System.out.println("⚠️ ADVERTENCIA: finalizarPedido() está deprecado. Usar enviarACocina() o cobrarPedido()");
        return false;
    }
    
    private Pedido crearPedidoDesdeBD(int idOrden, String estado, String numeroMesa, String nombreCliente) {
        try {
            int numero = Integer.parseInt(numeroMesa.replace("M", ""));
            Mesa mesa = new Mesa(0, numero, 4);
            Cliente cliente = new Cliente(0, nombreCliente != null ? nombreCliente : "Cliente", "00000000", "presencial");
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
        String sql = "SELECT p.id_plato, p.nombre, p.precio, d.cantidad " +
                    "FROM detalle_orden d JOIN platos p ON d.id_plato = p.id_plato " +
                    "WHERE d.id_orden = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idOrden);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Plato plato = new Plato(
                    rs.getInt("id_plato"),
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
}