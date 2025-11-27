package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/restaurante";
    private static final String USER = "root";
    private static final String PASSWORD = "Passthecoursewith11"; 
    
    public static Connection getConnection() {
        try {
            System.out.println("=== INTENTANDO CONEXIÓN BD ===");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" CONEXIÓN BD EXITOSA");
            return conn;
        } catch (SQLException e) {
            System.out.println(" ERROR CONEXIÓN BD: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}