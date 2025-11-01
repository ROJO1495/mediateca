package com.diego.mediateca.VistaUsuario;

import com.diego.mediateca.domain.DVD;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        setLayout(new GridLayout(7, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(getParent());

        // Componentes de la interfaz
        add(new JLabel("Código (DVD00000):"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Director:"));
        txtDirector = new JTextField();
        add(txtDirector);

        add(new JLabel("Duración:"));
        txtDuracion = new JTextField();
        add(txtDuracion);

        add(new JLabel("Género:"));
        txtGenero = new JTextField();
        add(txtGenero);

        add(new JLabel("Unidades Disponibles:"));
        txtUnidades = new JTextField();
        add(txtUnidades);

        btnAgregar = new JButton("Agregar DVD");
        btnCancelar = new JButton("Cancelar");

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarDVD();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        add(btnAgregar);
        add(btnCancelar);
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
                JOptionPane.showMessageDialog(this,
                        "Por favor complete todos los campos obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!codigo.matches("DVD\\d{5}")) {
                JOptionPane.showMessageDialog(this,
                        "El código debe tener el formato DVD00000",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear nuevo DVD
            dvdCreado = new DVD(codigo, titulo, unidades, director, duracion, genero);
            agregadoExitosamente = true;

            JOptionPane.showMessageDialog(this,
                    "DVD agregado exitosamente!\n" + dvdCreado.toString(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un valor numérico válido para unidades",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public DVD getDVDCreado() {
        return dvdCreado;
    }

    public boolean isAgregadoExitosamente() {
        return agregadoExitosamente;
    }
}