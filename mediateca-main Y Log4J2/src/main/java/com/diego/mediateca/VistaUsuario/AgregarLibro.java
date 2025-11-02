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

import com.diego.mediateca.domain.Libro;

public class AgregarLibro extends JDialog {
    private JTextField txtCodigo, txtTitulo, txtAutor, txtEditorial, txtISBN, txtPaginas, txtAnio, txtUnidades;
    private JButton btnAgregar, btnCancelar;
    private Libro libroCreado;
    private boolean agregadoExitosamente = false;

    public AgregarLibro(JFrame parent) {
        super(parent, "Agregar Libro", true);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de título
        JLabel titleLabel = new JLabel("Agregar Nuevo Libro");
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
        String[] labels = {"Código (LIB00000):", "Título:", "Autor:", "Editorial:", 
                          "ISBN:", "Número de Páginas:", "Año de Publicación:", "Unidades Disponibles:"};
        JTextField[] fields = new JTextField[8];

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
        txtAutor = fields[2];
        txtEditorial = fields[3];
        txtISBN = fields[4];
        txtPaginas = fields[5];
        txtAnio = fields[6];
        txtUnidades = fields[7];

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        btnAgregar = new JButton("✓ Agregar Libro");
        btnAgregar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnAgregar.setBackground(new Color(76, 175, 80));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> agregarLibro());

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
        setSize(520, 380);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void agregarLibro() {
        try {
            String codigo = txtCodigo.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String editorial = txtEditorial.getText().trim();
            String isbn = txtISBN.getText().trim();
            int paginas = Integer.parseInt(txtPaginas.getText().trim());
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int unidades = Integer.parseInt(txtUnidades.getText().trim());

            // Validaciones
            if (codigo.isEmpty() || titulo.isEmpty() || autor.isEmpty() || 
                    editorial.isEmpty() || isbn.isEmpty()) {
                showError("Por favor complete todos los campos obligatorios");
                return;
            }

            if (!codigo.matches("LIB\\d{5}")) {
                showError("El código debe tener el formato LIB00000");
                return;
            }

            if (paginas <= 0) {
                showError("El número de páginas debe ser mayor a 0");
                return;
            }

            if (anio < 1000 || anio > 2100) {
                showError("El año debe estar entre 1000 y 2100");
                return;
            }

            if (unidades < 0) {
                showError("Las unidades no pueden ser negativas");
                return;
            }

            // Crear nuevo libro
            libroCreado = new Libro(codigo, titulo, unidades, autor, editorial, isbn, anio, paginas);
            agregadoExitosamente = true;
            
            showSuccess("Libro agregado exitosamente!\n" + libroCreado);
            dispose();
        } catch (NumberFormatException e) {
            showError("Por favor ingrese valores numéricos válidos");
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

    public Libro getLibroCreado() {
        return libroCreado;
    }

    public boolean isAgregadoExitosamente() {
        return agregadoExitosamente;
    }
}