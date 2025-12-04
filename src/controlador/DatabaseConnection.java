package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Statement;

public class DatabaseConnection {
    
    public static Connection getConnection() {
        
        try {
            System.out.println("🔌 Intentando conectar sin contraseña...");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/",  
                "root", 
                "");  
            
            
            System.out.println(" Conexión a MySQL exitosa (sin contraseña)");
            
            
            crearBaseDeDatosYTablas(conn);
            
            return conn;
            
        } catch (SQLException e) {
            System.out.println("Sin contraseña no funcionó: " + e.getMessage());
            
            
            return conectarConContrasena();
        }
    }
    
    private static Connection conectarConContrasena() {
        while (true) {
            // Pedir contraseña AMIGABLEMENTE
            String contrasena = JOptionPane.showInputDialog(
                null,
                "? CONTRASEÑA DE MySQL\n\n" +
                "Tu MySQL pide contraseña para el usuario 'root'.\n\n" +
                "Ingresa la contraseña que pusiste cuando\n" +
                "INSTALASTE MySQL en esta computadora:\n\n" +
                "(Si no recuerdas, prueba dejando vacío)",
                "Contraseña de MySQL",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (contrasena == null) {
                // El usuario canceló
                JOptionPane.showMessageDialog(null,
                    "No se puede usar el sistema sin MySQL.\n" +
                    "Instala MySQL primero o contacta al administrador.",
                    "Conexión Cancelada",
                    JOptionPane.WARNING_MESSAGE);
                return null;
            }
            
   
            try {
                System.out.println(" Intentando con contraseña...");
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/",
                    "root",
                    contrasena);
                
                System.out.println("Conexión exitosa con contraseña proporcionada");
                
                
                crearBaseDeDatosYTablas(conn);
                
                JOptionPane.showMessageDialog(null,
                    "¡TODO LISTO!\n\n" +
                    "Base de datos 'restaurante' creada.\n" +
                    "Usuarios disponibles:\n" +
                    "• admin / admin\n" +
                    "• mesero / mesero\n" +
                    "• cocinero / cocinero",
                    "Configuración Completa",
                    JOptionPane.INFORMATION_MESSAGE);
                
                return conn;
                
            } catch (SQLException e) {
                
                String mensajeError = "";
                
                if (e.getMessage().contains("Access denied")) {
                    mensajeError = " CONTRASEÑA INCORRECTA\n\n" +
                        "La contraseña ingresada no es la correcta.\n" +
                        "¿La instalaste tú? Pregunta a quien instaló MySQL.";
                        
                } else if (e.getMessage().contains("Communications link failure")) {
                    mensajeError = " MYSQL NO ESTÁ EJECUTÁNDOSE\n\n" +
                        "1. Busca 'Services' en el menú inicio\n" +
                        "2. Busca 'MySQL' en la lista\n" +
                        "3. Haz click en 'Iniciar'\n" +
                        "4. Vuelve a ejecutar el programa";
                        
                } else {
                    mensajeError = " ERROR: " + e.getMessage();
                }
                
                int opcion = JOptionPane.showConfirmDialog(
                    null,
                    mensajeError + "\n\n" +
                    "¿Quieres intentar con OTRA contraseña?",
                    "Error de Conexión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
                );
                
                if (opcion != JOptionPane.YES_OPTION) {
                    return null;
                }
               
            }
        }
    }
    
    private static void crearBaseDeDatosYTablas(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            
            
            stmt.execute("CREATE DATABASE IF NOT EXISTS restaurante");
            stmt.execute("USE restaurante");
            
            // 2. Crear tabla de usuarios
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                "id_usuario INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(100) NOT NULL," +
                "rol VARCHAR(20) NOT NULL," +
                "estado BOOLEAN DEFAULT TRUE)");
            
            
            stmt.execute("CREATE TABLE IF NOT EXISTS platos (" +
                "id_plato INT AUTO_INCREMENT PRIMARY KEY," +
                "nombre VARCHAR(100) NOT NULL," +
                "precio DECIMAL(10,2) NOT NULL," +
                "disponible BOOLEAN DEFAULT TRUE)");
            
            
            stmt.execute("INSERT IGNORE INTO usuarios (username, password, rol) VALUES " +
                "('admin', 'admin', 'admin'), " +
                "('mesero', 'mesero', 'mesero'), " +
                "('cocinero', 'cocinero', 'cocinero')");
            
            System.out.println("✅ Base de datos y tablas creadas/verificadas");
            
        } catch (SQLException e) {
            System.out.println("⚠️ Error creando tablas: " + e.getMessage());
            
        }
    }
}