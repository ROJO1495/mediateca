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

/**
 * Diálogo mejorado para modificar materiales
 */
public class DialogoModificarMaterial extends JDialog {
    
    private JTextField[] campos;
    private JButton btnGuardar, btnCancelar;
    private boolean confirmado = false;
    private String[] valores;

    public DialogoModificarMaterial(JFrame parent, String titulo, String[] etiquetas, String[] valoresActuales) {
        super(parent, titulo, true);
        this.campos = new JTextField[etiquetas.length];
        inicializarComponentes(etiquetas, valoresActuales);
    }

    private void inicializarComponentes(String[] etiquetas, String[] valoresActuales) {
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
            
            // Si es el primer campo (ID), hacerlo no editable
            if (i == 0) {
                campos[i].setEditable(false);
                campos[i].setBackground(new Color(230, 230, 230));
            }
            
            // Cargar valor actual si existe
            if (valoresActuales != null && i < valoresActuales.length) {
                campos[i].setText(valoresActuales[i]);
            }
            
            gbc.gridx = 1;
            gbc.weightx = 0.75;
            formPanel.add(campos[i], gbc);
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel inferior - Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(TemaModerno.COLOR_FONDO);

        btnGuardar = new JButton("✓ Guardar Cambios");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 13));
        btnGuardar.setBackground(TemaModerno.COLOR_PRIMARIO);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardar());

        btnCancelar = new JButton("✕ Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancelar.setBackground(TemaModerno.COLOR_ERROR);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> cancelar());

        buttonPanel.add(btnGuardar);
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
        // No validar el primer campo (ID) ya que es no editable
        for (int i = 1; i < campos.length; i++) {
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
}