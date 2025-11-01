package com.diego.mediateca.VistaUsuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.diego.mediateca.db.MaterialDAO;
import com.diego.mediateca.domain.CD;
import com.diego.mediateca.domain.DVD;
import com.diego.mediateca.domain.Libro;
import com.diego.mediateca.domain.Material;
import com.diego.mediateca.domain.Revista;
import com.diego.mediateca.utils.ValidacionesUtil;

/**
 * Diálogo para modificar materiales de la mediateca
 */
public class ModificarMaterialDialog extends JDialog {
    
    private JTextField txtIdInterno;
    private JButton btnBuscar;
    private JPanel panelFormulario;
    private JButton btnGuardar, btnCancelar;
    
    private MaterialDAO materialDAO;
    private Material materialActual;

    public ModificarMaterialDialog(JFrame parent) {
        super(parent, "Modificar Material", true);
        this.materialDAO = new MaterialDAO();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 400);
        setLocationRelativeTo(getParent());

        // Panel superior - Búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();
        add(panelBusqueda, BorderLayout.NORTH);

        // Panel central - Formulario (vacío inicialmente)
        panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(panelFormulario), BorderLayout.CENTER);

        // Panel inferior - Botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Material"));

        panel.add(new JLabel("ID Interno:"));
        txtIdInterno = new JTextField(15);
        panel.add(txtIdInterno);

        btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarMaterial());
        panel.add(btnBuscar);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnGuardar = new JButton("💾 Guardar Cambios");
        btnGuardar.setEnabled(false);
        btnGuardar.addActionListener(e -> guardarCambios());
        panel.add(btnGuardar);

        btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panel.add(btnCancelar);

        return panel;
    }

    private void buscarMaterial() {
        try {
            String idInterno = txtIdInterno.getText().trim().toUpperCase();
            
            if (idInterno.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un ID interno",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar formato según tipo
            ValidacionesUtil.validarTipoCodigo(idInterno);

            // Buscar en base de datos
            var materialOpt = materialDAO.buscarPorId(idInterno);
            
            if (materialOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontró ningún material con ID: " + idInterno,
                    "No encontrado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            materialActual = materialOpt.get();
            mostrarFormulario(materialActual);
            btnGuardar.setEnabled(true);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al buscar en base de datos: " + e.getMessage(),
                "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarFormulario(Material material) {
        panelFormulario.removeAll();

        if (material instanceof CD) {
            mostrarFormularioCD((CD) material);
        } else if (material instanceof DVD) {
            mostrarFormularioDVD((DVD) material);
        } else if (material instanceof Libro) {
            mostrarFormularioLibro((Libro) material);
        } else if (material instanceof Revista) {
            mostrarFormularioRevista((Revista) material);
        }

        panelFormulario.revalidate();
        panelFormulario.repaint();
    }

    private void mostrarFormularioCD(CD cd) {
        panelFormulario.add(crearLabel("Modificar CD de Audio"));
        panelFormulario.add(Box.createVerticalStrut(10));

        panelFormulario.add(crearCampoTexto("ID:", cd.getIdInterno(), false));
        panelFormulario.add(crearCampoTexto("Título:", cd.getTitulo(), false));
        panelFormulario.add(crearCampoTexto("Artista:", cd.getArtista(), true, "artista"));
        panelFormulario.add(crearCampoTexto("Género:", cd.getGenero(), true, "genero"));
        panelFormulario.add(crearCampoTexto("Duración:", cd.getDuracion(), true, "duracion"));
        panelFormulario.add(crearCampoTexto("Nº Canciones:", String.valueOf(cd.getNumeroCanciones()), true, "canciones"));
        panelFormulario.add(crearCampoTexto("Unidades:", String.valueOf(cd.getUnidadesDisponibles()), true, "unidades"));
    }

    private void mostrarFormularioDVD(DVD dvd) {
        panelFormulario.add(crearLabel("Modificar DVD"));
        panelFormulario.add(Box.createVerticalStrut(10));

        panelFormulario.add(crearCampoTexto("ID:", dvd.getIdInterno(), false));
        panelFormulario.add(crearCampoTexto("Título:", dvd.getTitulo(), false));
        panelFormulario.add(crearCampoTexto("Director:", dvd.getDirector(), true, "director"));
        panelFormulario.add(crearCampoTexto("Duración:", dvd.getDuracion(), true, "duracion"));
        panelFormulario.add(crearCampoTexto("Género:", dvd.getGenero(), true, "genero"));
        panelFormulario.add(crearCampoTexto("Unidades:", String.valueOf(dvd.getUnidadesDisponibles()), true, "unidades"));
    }

    private void mostrarFormularioLibro(Libro libro) {
        panelFormulario.add(crearLabel("Modificar Libro"));
        panelFormulario.add(Box.createVerticalStrut(10));

        panelFormulario.add(crearCampoTexto("ID:", libro.getIdInterno(), false));
        panelFormulario.add(crearCampoTexto("Título:", libro.getTitulo(), false));
        panelFormulario.add(crearCampoTexto("Autor:", libro.getAutor(), false));
        panelFormulario.add(crearCampoTexto("Editorial:", libro.getEditorial(), false));
        panelFormulario.add(crearCampoTexto("ISBN:", libro.getIsbn(), false));
        panelFormulario.add(crearCampoTexto("Unidades:", String.valueOf(libro.getUnidadesDisponibles()), true, "unidades"));
        
        JLabel lblNota = new JLabel("⚠️ Solo se pueden modificar las unidades disponibles");
        lblNota.setForeground(Color.BLUE);
        panelFormulario.add(lblNota);
    }

    private void mostrarFormularioRevista(Revista revista) {
        panelFormulario.add(crearLabel("Modificar Revista"));
        panelFormulario.add(Box.createVerticalStrut(10));

        panelFormulario.add(crearCampoTexto("ID:", revista.getIdInterno(), false));
        panelFormulario.add(crearCampoTexto("Título:", revista.getTitulo(), false));
        panelFormulario.add(crearCampoTexto("Editorial:", revista.getEditorial(), false));
        panelFormulario.add(crearCampoTexto("Periodicidad:", revista.getPeriodicidad(), false));
        panelFormulario.add(crearCampoTexto("Unidades:", String.valueOf(revista.getUnidadesDisponibles()), true, "unidades"));
        
        JLabel lblNota = new JLabel("⚠️ Solo se pueden modificar las unidades disponibles");
        lblNota.setForeground(Color.BLUE);
        panelFormulario.add(lblNota);
    }

    private JPanel crearCampoTexto(String label, String valor, boolean editable, String nombre) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(120, 25));
        panel.add(lbl, BorderLayout.WEST);
        
        JTextField txt = new JTextField(valor);
        txt.setEditable(editable);
        txt.setName(nombre);
        if (!editable) {
            txt.setBackground(Color.LIGHT_GRAY);
        }
        panel.add(txt, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel crearCampoTexto(String label, String valor, boolean editable) {
        return crearCampoTexto(label, valor, editable, "");
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void guardarCambios() {
        try {
            if (materialActual instanceof CD) {
                guardarCD();
            } else if (materialActual instanceof DVD) {
                guardarDVD();
            } else if (materialActual instanceof Libro) {
                guardarLibro();
            } else if (materialActual instanceof Revista) {
                guardarRevista();
            }

            JOptionPane.showMessageDialog(this,
                "Material modificado exitosamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarCD() throws SQLException {
        String artista = obtenerValorCampo("artista");
        String genero = obtenerValorCampo("genero");
        String duracion = obtenerValorCampo("duracion");
        int canciones = ValidacionesUtil.parseEnteroPositivo(obtenerValorCampo("canciones"), "Número de canciones");
        int unidades = ValidacionesUtil.parseEnteroNoNegativo(obtenerValorCampo("unidades"), "Unidades");

        ValidacionesUtil.validarDuracion(duracion);
        
        materialDAO.modificarCD(materialActual.getIdInterno(), artista, genero, duracion, canciones, unidades);
    }

    private void guardarDVD() throws SQLException {
        String director = obtenerValorCampo("director");
        String duracion = obtenerValorCampo("duracion");
        String genero = obtenerValorCampo("genero");
        int unidades = ValidacionesUtil.parseEnteroNoNegativo(obtenerValorCampo("unidades"), "Unidades");

        ValidacionesUtil.validarDuracion(duracion);
        
        materialDAO.modificarDVD(materialActual.getIdInterno(), director, duracion, genero, unidades);
    }

    private void guardarLibro() throws SQLException {
        int unidades = ValidacionesUtil.parseEnteroNoNegativo(obtenerValorCampo("unidades"), "Unidades");
        materialDAO.modificarLibro(materialActual.getIdInterno(), unidades);
    }

    private void guardarRevista() throws SQLException {
        int unidades = ValidacionesUtil.parseEnteroNoNegativo(obtenerValorCampo("unidades"), "Unidades");
        materialDAO.modificarRevista(materialActual.getIdInterno(), unidades);
    }

    private String obtenerValorCampo(String nombre) {
        for (Component comp : panelFormulario.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component subComp : ((JPanel) comp).getComponents()) {
                    if (subComp instanceof JTextField && nombre.equals(subComp.getName())) {
                        return ((JTextField) subComp).getText().trim();
                    }
                }
            }
        }
        return "";
    }
}
