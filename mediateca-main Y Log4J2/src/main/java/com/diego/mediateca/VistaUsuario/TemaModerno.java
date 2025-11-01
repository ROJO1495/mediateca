package com.diego.mediateca.VistaUsuario;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Clase para aplicar tema moderno a la aplicación
 */
public class TemaModerno {
    
    // Colores principales
    public static final Color COLOR_PRIMARIO = new Color(33, 150, 243);
    public static final Color COLOR_SECUNDARIO = new Color(76, 175, 80);
    public static final Color COLOR_ERROR = new Color(244, 67, 54);
    public static final Color COLOR_FONDO = new Color(245, 245, 245);
    public static final Color COLOR_TEXTO = new Color(66, 66, 66);
    public static final Color COLOR_BORDE = new Color(200, 200, 200);

    public static void aplicarTema() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configurar fuentes
        Font fuente = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Label.font", fuente);
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 11));
        UIManager.put("TextField.font", fuente);
        UIManager.put("Table.font", fuente);

        // Configurar colores
        UIManager.put("Button.background", COLOR_PRIMARIO);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Panel.background", COLOR_FONDO);
    }

    /**
     * Estiliza un botón con colores modernos
     */
    public static void estilizarBoton(JButton boton, Color fondo) {
        boton.setBackground(fondo);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.PLAIN, 12));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
    }

    /**
     * Estiliza un texto field
     */
    public static void estilizarTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 11));
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
    }

    /**
     * Estiliza un panel
     */
    public static void estilizarPanel(JPanel panel) {
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    /**
     * Estiliza una tabla
     */
    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("Arial", Font.PLAIN, 11));
        tabla.setRowHeight(25);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setSelectionBackground(COLOR_PRIMARIO);
        tabla.setSelectionForeground(Color.WHITE);
    }
}