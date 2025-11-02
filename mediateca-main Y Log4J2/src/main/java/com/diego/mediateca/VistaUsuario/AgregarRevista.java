package com.diego.mediateca.VistaUsuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.diego.mediateca.domain.Revista;

public class AgregarRevista extends JDialog {
    private JTextField txtCodigo, txtTitulo, txtEditorial, txtPeriodicidad, txtUnidades;
    private JTextField txtFecha; // Para la fecha
    private JButton btnAgregar, btnCancelar;
    private Revista revistaCreada;
    private boolean agregadoExitosamente = false;

    public AgregarRevista(JFrame parent) {
        super(parent, "Agregar Revista", true);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de título
        JLabel titleLabel = new JLabel("Agregar Nueva Revista");
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
        String[] labels = {"Código (REV00000):", "Título:", "Editorial:", "Periodicidad:", 
                          "Fecha de Publicación (YYYY-MM-DD):", "Unidades Disponibles:"};
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
        txtEditorial = fields[2];
        txtPeriodicidad = fields[3];
        txtFecha = fields[4];
        txtUnidades = fields[5];

        // Poner fecha actual como ejemplo
        txtFecha.setText(LocalDate.now().toString());

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        btnAgregar = new JButton("✓ Agregar Revista");
        btnAgregar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnAgregar.setBackground(new Color(76, 175, 80));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> agregarRevista());

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
        setSize(520, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void agregarRevista() {
        try {
            String codigo = txtCodigo.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String editorial = txtEditorial.getText().trim();
            String periodicidad = txtPeriodicidad.getText().trim();
            String fechaStr = txtFecha.getText().trim();
            int unidades = Integer.parseInt(txtUnidades.getText().trim());

            // Validaciones
            if (codigo.isEmpty() || titulo.isEmpty() || editorial.isEmpty() || 
                    periodicidad.isEmpty() || fechaStr.isEmpty()) {
                showError("Por favor complete todos los campos obligatorios");
                return;
            }

            if (!codigo.matches("REV\\d{5}")) {
                showError("El código debe tener el formato REV00000");
                return;
            }

            if (unidades < 0) {
                showError("Las unidades no pueden ser negativas");
                return;
            }

            // Parsear fecha
            LocalDate fecha = LocalDate.parse(fechaStr);

            // Crear nueva revista
            revistaCreada = new Revista(codigo, titulo, unidades, editorial, periodicidad, fecha);
            agregadoExitosamente = true;
            
            showSuccess("Revista agregada exitosamente!\n" + revistaCreada);
            dispose();
        } catch (java.time.format.DateTimeParseException e) {
            showError("Formato de fecha inválido. Use YYYY-MM-DD (ej: 2024-01-15)");
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

    public Revista getRevistaCreada() {
        return revistaCreada;
    }

    public boolean isAgregadoExitosamente() {
        return agregadoExitosamente;
    }
}