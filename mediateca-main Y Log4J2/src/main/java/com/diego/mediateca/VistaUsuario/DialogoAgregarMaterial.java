package com.diego.mediateca.VistaUsuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Diálogo mejorado y moderno para agregar materiales
 */
public class DialogoAgregarMaterial extends JDialog {
    
    private JTextField[] campos;
    private JButton btnAgregar, btnCancelar;
    private boolean confirmado = false;
    private String[] valores;

    public DialogoAgregarMaterial(JFrame parent, String titulo, String[] etiquetas) {
        super(parent, titulo, true);
        this.campos = new JTextField[etiquetas.length];
        inicializarComponentes(etiquetas);
    }

    private void inicializarComponentes(String[] etiquetas) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(TemaModerno.COLOR_FONDO);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior - Título
        JLabel titleLabel = new JLabel(getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(TemaModerno.COLOR_PRIMARIO);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel central - Formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(TemaModerno.COLOR_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < etiquetas.length; i++) {
            // Etiqueta
            JLabel label = new JLabel(etiquetas[i]);
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            label.setForeground(TemaModerno.COLOR_TEXTO);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.25;
            formPanel.add(label, gbc);

            // Campo de texto
            campos[i] = new JTextField(25);
            campos[i].setFont(new Font("Arial", Font.PLAIN, 12));
            campos[i].setBackground(Color.WHITE);
            campos[i].setBorder(BorderFactory.createLineBorder(TemaModerno.COLOR_BORDE, 1));
            campos[i].setPreferredSize(new Dimension(300, 35));
            gbc.gridx = 1;
            gbc.weightx = 0.75;
            formPanel.add(campos[i], gbc);
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel inferior - Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(TemaModerno.COLOR_FONDO);

        btnAgregar = new JButton("✓ Guardar");
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 13));
        btnAgregar.setBackground(TemaModerno.COLOR_SECUNDARIO);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.setFocusPainted(false);
        btnAgregar.addActionListener(e -> guardar());

        btnCancelar = new JButton("✕ Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancelar.setBackground(TemaModerno.COLOR_ERROR);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> cancelar());

        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnCancelar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setSize(550, 100 + (campos.length * 50));
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void guardar() {
        if (validarCampos()) {
            valores = new String[campos.length];
            for (int i = 0; i < campos.length; i++) {
                valores[i] = campos[i].getText().trim();
            }
            confirmado = true;
            dispose();
        }
    }

    private void cancelar() {
        confirmado = false;
        dispose();
    }

    private boolean validarCampos() {
        for (int i = 0; i < campos.length; i++) {
            if (campos[i].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor complete todos los campos", 
                    "Campos vacíos", 
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getValor(int indice) {
        return confirmado && indice < valores.length ? valores[indice] : null;
    }

    public String[] getValores() {
        return valores;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] etiquetas = {"Código:", "Título:", "Autor:", "Editorial:"};
            DialogoAgregarMaterial dialogo = new DialogoAgregarMaterial(null, "Agregar Libro", etiquetas);
            dialogo.setVisible(true);
            
            if (dialogo.isConfirmado()) {
                System.out.println("Valores ingresados:");
                for (String valor : dialogo.getValores()) {
                    System.out.println("- " + valor);
                }
            }
        });
    }
}