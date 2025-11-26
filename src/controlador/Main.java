package controlador;

import vista.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA RESTAURANTE ===");
        
        // Configurar look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Ejecutar en el hilo de EDT
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            System.out.println("LoginFrame hecho visible");
        });
    }
}