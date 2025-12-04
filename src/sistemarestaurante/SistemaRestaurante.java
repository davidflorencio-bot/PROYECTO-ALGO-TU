package sistemarestaurante;

import vista.LoginFrame;
import javax.swing.*;

public class SistemaRestaurante {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA RESTAURANTE ===");
        
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
      
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            System.out.println("LoginFrame hecho visible");
        });
    }
}