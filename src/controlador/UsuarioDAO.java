package controlador;

import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    
    public Usuario validarUsuario(String username, String password) {
        
        String sql = "SELECT id_usuario, username, rol FROM usuarios WHERE username = ? AND password = ? AND estado = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setRol(rs.getString("rol"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL en validarUsuario: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public List<Usuario> obtenerTodosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        
        String sql = "SELECT id_usuario, username, password, rol FROM usuarios WHERE estado = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password")); 
                usuario.setRol(rs.getString("rol"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL en obtenerTodosUsuarios: " + e.getMessage());
            e.printStackTrace();
        }
        return usuarios;
    }

    
    public boolean agregarUsuario(Usuario usuario) {
        
        String sql = "INSERT INTO usuarios (username, password, rol, estado) VALUES (?, ?, ?, true)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getPassword());
            pstmt.setString(3, usuario.getRol());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            
            System.out.println("❌ ERROR SQL en agregarUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET username = ?, password = ?, rol = ? WHERE id_usuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getPassword());
            pstmt.setString(3, usuario.getRol());
            pstmt.setInt(4, usuario.getIdUsuario());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL en actualizarUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean eliminarUsuario(int idUsuario) {
        
        String sql = "UPDATE usuarios SET estado = false WHERE id_usuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL en eliminarUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}