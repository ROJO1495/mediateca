package com.diego.mediateca.VistaUsuario;

import com.diego.mediateca.domain.CD;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgregarCD extends JDialog {
    private JTextField txtCodigo, txtTitulo, txtArtista, txtGenero, txtDuracion, txtCanciones, txtUnidades;
    private JButton btnAgregar, btnCancelar;
    private CD cdCreado;
    private boolean agregadoExitosamente = false;

    public AgregarCD(JFrame parent) {
        super(parent, "Agregar CD de Audio", true);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new GridLayout(8, 2, 10, 10));
        setSize(400, 350);
        setLocationRelativeTo(getParent());

        // Componentes de la interfaz
        add(new JLabel("Código (CDA00000):"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Artista:"));
        txtArtista = new JTextField();
        add(txtArtista);

        add(new JLabel("Género:"));
        txtGenero = new JTextField();
        add(txtGenero);

        add(new JLabel("Duración:"));
        txtDuracion = new JTextField();
        add(txtDuracion);

        add(new JLabel("Número de Canciones:"));
        txtCanciones = new JTextField();
        add(txtCanciones);

        add(new JLabel("Unidades Disponibles:"));
        txtUnidades = new JTextField();
        add(txtUnidades);

        btnAgregar = new JButton("Agregar CD");
        btnCancelar = new JButton("Cancelar");

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarCD();
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

    private void agregarCD() {
        try {
            String codigo = txtCodigo.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String artista = txtArtista.getText().trim();
            String genero = txtGenero.getText().trim();
            String duracion = txtDuracion.getText().trim();
            int numeroCanciones = Integer.parseInt(txtCanciones.getText().trim());
            int unidades = Integer.parseInt(txtUnidades.getText().trim());

            // Validaciones
            if (codigo.isEmpty() || titulo.isEmpty() || artista.isEmpty() ||
                    genero.isEmpty() || duracion.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor complete todos los campos obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!codigo.matches("CDA\\d{5}")) {
                JOptionPane.showMessageDialog(this,
                        "El código debe tener el formato CDA00000",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear nuevo CD
            cdCreado = new CD(codigo, titulo, unidades, artista, genero, duracion, numeroCanciones);
            agregadoExitosamente = true;

            JOptionPane.showMessageDialog(this,
                    "CD agregado exitosamente!\n" + cdCreado.toString(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese valores numéricos válidos para canciones y unidades",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public CD getCDCreado() {
        return cdCreado;
    }

    public boolean isAgregadoExitosamente() {
        return agregadoExitosamente;
    }
}
