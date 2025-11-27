package vista;

import javax.swing.*;
import java.awt.*;

public class UIUtils {
    
    
    public static final Color COLOR_ACENTO = new Color(184, 29, 19);
    public static final Color COLOR_FONDO = new Color(245, 245, 245);
    public static final Color COLOR_TEXTO = new Color(60, 60, 60);

    
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 12);

    
    public static JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(COLOR_ACENTO);
        boton.setForeground(Color.WHITE);
        boton.setFont(FONT_BOLD);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACENTO.darker(), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_ACENTO.darker());
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_ACENTO);
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        return boton;
    }

   
     
    public static JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(Color.WHITE);
        boton.setForeground(COLOR_TEXTO);
        boton.setFont(FONT_BOLD);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
       
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_FONDO);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACENTO, 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.WHITE);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return boton;
    }
}