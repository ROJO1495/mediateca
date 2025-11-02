package com.diego.mediateca.VistaUsuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.diego.mediateca.db.MaterialDAO;
import com.diego.mediateca.domain.CD;
import com.diego.mediateca.domain.DVD;
import com.diego.mediateca.domain.Libro;
import com.diego.mediateca.domain.Revista;

/**
 * Panel mejorado para mostrar todos los materiales disponibles
 */
public class VentanaMateriales extends JPanel {
    
    private MaterialDAO materialDAO;
    private JTabbedPane tabbedPane;
    private JTable tablaLibros, tablaRevistas, tablaCDs, tablaDVDs;
    private DefaultTableModel modeloLibros, modeloRevistas, modeloCDs, modeloDVDs;

    public VentanaMateriales() {
        this.materialDAO = new MaterialDAO();
        inicializarComponentes();
        cargarDatos();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBackground(TemaModerno.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel superior con título
        JPanel panelTitulo = crearPanelTitulo();
        add(panelTitulo, BorderLayout.NORTH);

        // Tabbed Pane para cada tipo de material
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 12));
        tabbedPane.setBackground(Color.WHITE);

        // Pestaña Libros
        tabbedPane.addTab("📚 Libros", crearPanelLibros());
        
        // Pestaña Revistas
        tabbedPane.addTab("📰 Revistas", crearPanelRevistas());
        
        // Pestaña CDs
        tabbedPane.addTab("🎵 CDs", crearPanelCDs());
        
        // Pestaña DVDs
        tabbedPane.addTab("🎬 DVDs", crearPanelDVDs());

        add(tabbedPane, BorderLayout.CENTER);

        // Panel inferior con opciones
        JPanel panelInferior = crearPanelOpciones();
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(TemaModerno.COLOR_FONDO);

        JLabel titulo = new JLabel("MATERIALES DISPONIBLES");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(TemaModerno.COLOR_PRIMARIO);
        panel.add(titulo, BorderLayout.WEST);

        JButton btnActualizar = new JButton("🔄 Actualizar");
        TemaModerno.estilizarBoton(btnActualizar, TemaModerno.COLOR_PRIMARIO);
        btnActualizar.addActionListener(e -> cargarDatos());
        panel.add(btnActualizar, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelLibros() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID", "Título", "Autor", "Editorial", "Año", "Unidades"};
        modeloLibros = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloLibros);
        TemaModerno.estilizarTabla(tablaLibros);
        tablaLibros.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        scrollPane.setBorder(BorderFactory.createLineBorder(TemaModerno.COLOR_BORDE));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelRevistas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID", "Título", "Editorial", "Periodicidad", "Fecha Publicación", "Unidades"};
        modeloRevistas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRevistas = new JTable(modeloRevistas);
        TemaModerno.estilizarTabla(tablaRevistas);
        tablaRevistas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tablaRevistas);
        scrollPane.setBorder(BorderFactory.createLineBorder(TemaModerno.COLOR_BORDE));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelCDs() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID", "Título", "Artista", "Género", "Duración", "Canciones", "Unidades"};
        modeloCDs = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCDs = new JTable(modeloCDs);
        TemaModerno.estilizarTabla(tablaCDs);
        tablaCDs.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tablaCDs);
        scrollPane.setBorder(BorderFactory.createLineBorder(TemaModerno.COLOR_BORDE));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelDVDs() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID", "Título", "Director", "Duración", "Género", "Unidades"};
        modeloDVDs = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDVDs = new JTable(modeloDVDs);
        TemaModerno.estilizarTabla(tablaDVDs);
        tablaDVDs.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tablaDVDs);
        scrollPane.setBorder(BorderFactory.createLineBorder(TemaModerno.COLOR_BORDE));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(TemaModerno.COLOR_FONDO);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, TemaModerno.COLOR_BORDE));

        JLabel labelInfo = new JLabel("Selecciona una pestaña para ver el inventario");
        labelInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        labelInfo.setForeground(Color.GRAY);
        panel.add(labelInfo);

        return panel;
    }

    private void cargarDatos() {
        try {
            // Limpiar tablas
            modeloLibros.setRowCount(0);
            modeloRevistas.setRowCount(0);
            modeloCDs.setRowCount(0);
            modeloDVDs.setRowCount(0);

            // Cargar libros
            List<Libro> libros = materialDAO.listarLibrosDisponibles();
            for (Libro libro : libros) {
                modeloLibros.addRow(new Object[]{
                libro.getIdInterno(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getEditorial(),
                libro.getAnioPublicacion(),  
                libro.getUnidadesDisponibles()
            });
            }

            // Cargar revistas
            List<Revista> revistas = materialDAO.listarRevistasDisponibles();
            for (Revista revista : revistas) {
                modeloRevistas.addRow(new Object[]{
                    revista.getIdInterno(),
                    revista.getTitulo(),
                    revista.getEditorial(),
                    revista.getPeriodicidad(),
                    revista.getFechaPublicacion(),
                    revista.getUnidadesDisponibles()
                });
            }

            // Cargar CDs
            List<CD> cds = materialDAO.listarCDsDisponibles();
            for (CD cd : cds) {
                modeloCDs.addRow(new Object[]{
                    cd.getIdInterno(),
                    cd.getTitulo(),
                    cd.getArtista(),
                    cd.getGenero(),
                    cd.getDuracion(),
                    cd.getNumeroCanciones(),
                    cd.getUnidadesDisponibles()
                });
            }

            // Cargar DVDs
            List<DVD> dvds = materialDAO.listarDVDsDisponibles();
            for (DVD dvd : dvds) {
                modeloDVDs.addRow(new Object[]{
                    dvd.getIdInterno(),
                    dvd.getTitulo(),
                    dvd.getDirector(),
                    dvd.getDuracion(),
                    dvd.getGenero(),
                    dvd.getUnidadesDisponibles()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}