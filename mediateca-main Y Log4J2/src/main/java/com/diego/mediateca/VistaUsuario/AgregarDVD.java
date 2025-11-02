package com.diego.mediateca.VistaUsuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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

import com.diego.mediateca.domain.DVD;

public class AgregarDVD extends JDialog {
    private JTextField txtCodigo, txtTitulo, txtDirector, txtDuracion, txtGenero, txtUnidades;
    private JButton btnAgregar, btnCancelar;
    private DVD dvdCreado;
    private boolean agregadoExitosamente = false;

    public AgregarDVD(JFrame parent) {
        super(parent, "Agregar DVD", true);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de título
        JLabel titleLabel = new JLabel("Agregar Nuevo DVD");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 150, 243));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos
        String[] labels = {"Código (DVD00000):", "Título:", "Director:", "Duración (HH:MM):", 
                          "Género:", "Unidades Disponibles:"};
        JTextField[] fields = new JTextField[6];

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.PLAIN, 11));
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            formPanel.add(label, gbc);

            fields[i] = new JTextField(20);
            fields[i].setFont(new Font("Arial", Font.PLAIN, 11));
            fields[i].setBackground(Color.WHITE);
            fields[i].setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            formPanel.add(fields[i], gbc);
        }

        txtCodigo = fields[0];
        txtTitulo = fields[1];
        txtDirector = fields[2];
        txtDuracion = fields[3];
        txtGenero = fields[4];
        txtUnidades = fields[5];

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        btnAgregar = new JButton("✓ Agregar DVD");
        btnAgregar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnAgregar.setBackground(new Color(76, 175, 80));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> agregarDVD());

        btnCancelar = new JButton("✕ Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnCancelar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setSize(500, 320);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void agregarDVD() {
        try {
            String codigo = txtCodigo.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String director = txtDirector.getText().trim();
            String duracion = txtDuracion.getText().trim();
            String genero = txtGenero.getText().trim();
            int unidades = Integer.parseInt(txtUnidades.getText().trim());

            // Validaciones
            if (codigo.isEmpty() || titulo.isEmpty() || director.isEmpty() ||
                    duracion.isEmpty() || genero.isEmpty()) {
                showError("Por favor complete todos los campos obligatorios");
                return;
            }

            if (!codigo.matches("DVD\\d{5}")) {
                showError("El código debe tener el formato DVD00000");
                return;
            }

            if (unidades < 0) {
                showError("Las unidades no pueden ser negativas");
                return;
            }

            // Crear nuevo DVD
            dvdCreado = new DVD(codigo, titulo, unidades, director, duracion, genero);
            agregadoExitosamente = true;
            
            showSuccess("DVD agregado exitosamente!\n" + dvdCreado);
            dispose();
        } catch (NumberFormatException e) {
            showError("Por favor ingrese un valor numérico válido para unidades");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public DVD getDVDCreado() {
        return dvdCreado;
    }

    public boolean isAgregadoExitosamente() {
        return agregadoExitosamente;
    }
}