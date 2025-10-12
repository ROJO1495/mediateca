package com.diego.mediateca.app;

import com.diego.mediateca.domain.*;
import com.diego.mediateca.db.DatabaseConnection;
import com.diego.mediateca.db.MaterialDAO;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class AppPrincipal extends JFrame {

    private final JPanel content = new JPanel(new CardLayout());
    private final DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    private final MaterialDAO materialDAO = new MaterialDAO();

    // Paneles para mostrar listas
    private final JPanel librosPanel = new JPanel(new BorderLayout());
    private final JPanel revistasPanel = new JPanel(new BorderLayout());
    private final JPanel dvdsPanel = new JPanel(new BorderLayout());
    private final JPanel cdsPanel = new JPanel(new BorderLayout());

    public AppPrincipal() {
        verificarConexionBD();
        initComponents();
    }

    /**
     * Verificar conexión a base de datos al iniciar
     */
    private void verificarConexionBD() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            boolean conexionExitosa = db.testConnection();

            if (!conexionExitosa) {
                JOptionPane.showMessageDialog(this,
                        "Error al conectar con la base de datos.\n" +
                                "La aplicación puede no funcionar correctamente.",
                        "Error de Conexión",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                System.out.println("✓ Aplicación conectada a base de datos correctamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error crítico de conexión: " + e.getMessage() + "\n" +
                            "Verifique que MySQL esté ejecutándose.",
                    "Error de Configuración",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        setTitle("Mediateca - Principal");
        setSize(980, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar paneles de listas
        inicializarPanelLibros();
        inicializarPanelRevistas();
        inicializarPanelDVDs();
        inicializarPanelCDs();

        // Agregar paneles al CardLayout
        content.add(librosPanel, "LIBROS");
        content.add(revistasPanel, "REVISTAS");
        content.add(dvdsPanel, "DVDS");
        content.add(cdsPanel, "CDS");

        add(content, BorderLayout.CENTER);

        // Menú de navegación requerido
        crearMenu();

        // Mostrar por defecto LIBROS
        showCard("LIBROS");
    }

    private void inicializarPanelLibros() {
        librosPanel.removeAll();
        JLabel titulo = new JLabel("LIBROS DISPONIBLES", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        librosPanel.add(titulo, BorderLayout.NORTH);

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        librosPanel.add(scrollPane, BorderLayout.CENTER);

        // Cargar datos
        cargarLibrosEnArea(areaTexto);
    }

    private void inicializarPanelRevistas() {
        revistasPanel.removeAll();
        JLabel titulo = new JLabel("REVISTAS DISPONIBLES", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        revistasPanel.add(titulo, BorderLayout.NORTH);

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        revistasPanel.add(scrollPane, BorderLayout.CENTER);

        // Cargar datos
        cargarRevistasEnArea(areaTexto);
    }

    private void inicializarPanelDVDs() {
        dvdsPanel.removeAll();
        JLabel titulo = new JLabel("DVDs DISPONIBLES", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        dvdsPanel.add(titulo, BorderLayout.NORTH);

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        dvdsPanel.add(scrollPane, BorderLayout.CENTER);

        // Cargar datos
        cargarDVDsEnArea(areaTexto);
    }

    private void inicializarPanelCDs() {
        cdsPanel.removeAll();
        JLabel titulo = new JLabel("CDs DISPONIBLES", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        cdsPanel.add(titulo, BorderLayout.NORTH);

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        cdsPanel.add(scrollPane, BorderLayout.CENTER);

        // Cargar datos
        cargarCDsEnArea(areaTexto);
    }

    private void cargarLibrosEnArea(JTextArea areaTexto) {
        try {
            List<Libro> libros = materialDAO.listarLibrosDisponibles();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-10s %-30s %-20s %-15s %-8s\n",
                    "ID", "TÍTULO", "AUTOR", "EDITORIAL", "UNIDADES"));
            sb.append("--------------------------------------------------------------------------------\n");

            for (Libro libro : libros) {
                sb.append(String.format("%-10s %-30s %-20s %-15s %-8d\n",
                        libro.getIdInterno(),
                        limitarTexto(libro.getTitulo(), 28),
                        limitarTexto(libro.getAutor(), 18),
                        limitarTexto(libro.getEditorial(), 13),
                        libro.getUnidadesDisponibles()));
            }

            areaTexto.setText(sb.toString());
        } catch (Exception e) {
            areaTexto.setText("Error al cargar libros: " + e.getMessage());
        }
    }

    private void cargarRevistasEnArea(JTextArea areaTexto) {
        try {
            List<Revista> revistas = materialDAO.listarRevistasDisponibles();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-10s %-30s %-15s %-12s %-15s %-8s\n",
                    "ID", "TÍTULO", "EDITORIAL", "PERIODICIDAD", "FECHA", "UNIDADES"));
            sb.append("------------------------------------------------------------------------------------------\n");

            for (Revista revista : revistas) {
                sb.append(String.format("%-10s %-30s %-15s %-12s %-15s %-8d\n",
                        revista.getIdInterno(),
                        limitarTexto(revista.getTitulo(), 28),
                        limitarTexto(revista.getEditorial(), 13),
                        limitarTexto(revista.getPeriodicidad(), 10),
                        revista.getFechaPublicacion().toString(),
                        revista.getUnidadesDisponibles()));
            }

            areaTexto.setText(sb.toString());
        } catch (Exception e) {
            areaTexto.setText("Error al cargar revistas: " + e.getMessage());
        }
    }

    private void cargarDVDsEnArea(JTextArea areaTexto) {
        try {
            List<DVD> dvds = materialDAO.listarDVDsDisponibles();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-10s %-30s %-20s %-10s %-15s %-8s\n",
                    "ID", "TÍTULO", "DIRECTOR", "DURACIÓN", "GÉNERO", "UNIDADES"));
            sb.append("------------------------------------------------------------------------------------------\n");

            for (DVD dvd : dvds) {
                sb.append(String.format("%-10s %-30s %-20s %-10s %-15s %-8d\n",
                        dvd.getIdInterno(),
                        limitarTexto(dvd.getTitulo(), 28),
                        limitarTexto(dvd.getDirector(), 18),
                        limitarTexto(dvd.getDuracion(), 8),
                        limitarTexto(dvd.getGenero(), 13),
                        dvd.getUnidadesDisponibles()));
            }

            areaTexto.setText(sb.toString());
        } catch (Exception e) {
            areaTexto.setText("Error al cargar DVDs: " + e.getMessage());
        }
    }

    private void cargarCDsEnArea(JTextArea areaTexto) {
        try {
            List<CD> cds = materialDAO.listarCDsDisponibles();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-10s %-30s %-20s %-15s %-10s %-6s %-8s\n",
                    "ID", "TÍTULO", "ARTISTA", "GÉNERO", "DURACIÓN", "CANC.", "UNIDADES"));
            sb.append("----------------------------------------------------------------------------------------------------\n");

            for (CD cd : cds) {
                sb.append(String.format("%-10s %-30s %-20s %-15s %-10s %-6d %-8d\n",
                        cd.getIdInterno(),
                        limitarTexto(cd.getTitulo(), 28),
                        limitarTexto(cd.getArtista(), 18),
                        limitarTexto(cd.getGenero(), 13),
                        limitarTexto(cd.getDuracion(), 8),
                        cd.getNumeroCanciones(),
                        cd.getUnidadesDisponibles()));
            }

            areaTexto.setText(sb.toString());
        } catch (Exception e) {
            areaTexto.setText("Error al cargar CDs: " + e.getMessage());
        }
    }

    private String limitarTexto(String texto, int longitud) {
        if (texto == null) return "";
        if (texto.length() <= longitud) return texto;
        return texto.substring(0, longitud - 3) + "...";
    }

    // MENU 6 OPCIONES
    private void crearMenu() {
        JMenuBar mb = new JMenuBar();

        // MENU Disponibles (ver las listas por tipo)
        JMenu mDisponibles = new JMenu("Disponibles");
        JMenuItem itLibros   = new JMenuItem("Libros");
        JMenuItem itRevistas = new JMenuItem("Revistas");
        JMenuItem itDVDs     = new JMenuItem("DVDs");
        JMenuItem itCDs      = new JMenuItem("CDs");

        itLibros.addActionListener(e -> showCard("LIBROS"));
        itRevistas.addActionListener(e -> showCard("REVISTAS"));
        itDVDs.addActionListener(e -> showCard("DVDS"));
        itCDs.addActionListener(e -> showCard("CDS"));

        mDisponibles.add(itLibros);
        mDisponibles.add(itRevistas);
        mDisponibles.add(itDVDs);
        mDisponibles.add(itCDs);

        // Base de Datos
        JMenu mBaseDatos = new JMenu("Base de Datos");
        JMenuItem miTestConexion = new JMenuItem("Probar Conexión");
        JMenuItem miInfoBD = new JMenuItem("Información BD");

        miTestConexion.addActionListener(e -> probarConexionBD());
        miInfoBD.addActionListener(e -> mostrarInfoBD());

        mBaseDatos.add(miTestConexion);
        mBaseDatos.add(miInfoBD);

        // MENU OPERACIONES
        JMenu mOps = new JMenu("Operaciones");
        JMenuItem miAgregar   = new JMenuItem("Agregar material…");
        JMenuItem miModificar = new JMenuItem("Modificar material…");
        JMenuItem miBorrar    = new JMenuItem("Borrar material…");
        JMenuItem miBuscar    = new JMenuItem("Buscar material…");
        JMenuItem miSalir     = new JMenuItem("Salir");

        miAgregar.addActionListener(e -> onAgregarMaterial());
        miModificar.addActionListener(e -> onModificarMaterial());
        miBorrar.addActionListener(e -> onBorrarMaterial());
        miBuscar.addActionListener(e -> onBuscarMaterial());
        miSalir.addActionListener(e -> salirAplicacion());

        mOps.add(miAgregar);
        mOps.add(miModificar);
        mOps.add(miBorrar);
        mOps.add(miBuscar);
        mOps.addSeparator();
        mOps.add(miSalir);

        mb.add(mDisponibles);
        mb.add(mOps);
        mb.add(mBaseDatos);

        setJMenuBar(mb);
    }

    /**
     * Probar conexión a base de datos
     */
    private void probarConexionBD() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            boolean conexionOk = db.testConnection();

            if (conexionOk) {
                JOptionPane.showMessageDialog(this,
                        "✓ Conexión a la base de datos exitosa",
                        "Conexión Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "✗ No se pudo establecer conexión con la base de datos",
                        "Error de Conexión",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            showError("Error al probar conexión: " + e.getMessage());
        }
    }

    /**
     * Mostrar información de la base de datos
     */
    private void mostrarInfoBD() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            java.sql.Connection conn = db.getConnection();
            java.sql.DatabaseMetaData metaData = conn.getMetaData();

            String info = String.format(
                    "Información de Base de Datos:\n\n" +
                            "Producto: %s\n" +
                            "Versión: %s\n" +
                            "Driver: %s\n" +
                            "URL: %s\n" +
                            "Usuario: %s\n" +
                            "Conexión válida: %s",
                    metaData.getDatabaseProductName(),
                    metaData.getDatabaseProductVersion(),
                    metaData.getDriverName(),
                    metaData.getURL(),
                    metaData.getUserName(),
                    db.isConnectionValid() ? "Sí" : "No"
            );

            JOptionPane.showMessageDialog(this, info, "Información BD", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            showError("Error al obtener información: " + e.getMessage());
        }
    }

    /**
     * Salir de la aplicación cerrando conexiones
     */
    private void salirAplicacion() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            db.closeConnection();
        } catch (Exception e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        } finally {
            dispose();
        }
    }

    // ACCIONES PRINCIPALES - SIMPLIFICADAS USANDO MaterialDAO
    private void onAgregarMaterial() {
        String[] tipos = {"LIBRO", "REVISTA", "DVD", "CD"};
        String tipo = (String) JOptionPane.showInputDialog(
                this, "Tipo de material:", "Agregar",
                JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);

        if (tipo == null) return;

        try {
            switch (tipo) {
                case "LIBRO"   -> agregarLibro();
                case "REVISTA" -> agregarRevista();
                case "DVD"     -> agregarDVD();
                case "CD"      -> agregarCD();
            }
            JOptionPane.showMessageDialog(this, "Material agregado con éxito a la base de datos.");
            refrescarVistaActual();
        } catch (Exception ex) {
            showError("No se pudo agregar: " + ex.getMessage());
        }
    }

    private void onModificarMaterial() {
        String id = input("ID interno del material a modificar:");
        if (id == null || id.isBlank()) return;

        try {
            Optional<Material> op = materialDAO.buscarPorId(id);
            if (op.isEmpty()) {
                showWarn("No existe material con ID: " + id);
                return;
            }
            Material m = op.get();

            if (m instanceof Libro) {
                int u = inputInt("Unidades nuevas para LIBRO " + m.getIdInterno() + ":");
                materialDAO.modificarLibro(id, u);
            } else if (m instanceof Revista) {
                int u = inputInt("Unidades nuevas para REVISTA " + m.getIdInterno() + ":");
                materialDAO.modificarRevista(id, u);
            } else if (m instanceof DVD) {
                String director = input("Director:");
                String dur = input("Duración (hh:mm):");
                String genero = input("Género:");
                int u = inputInt("Unidades disponibles:");
                materialDAO.modificarDVD(id, director, dur, genero, u);
            } else if (m instanceof CD) {
                String artista = input("Artista:");
                String genero = input("Género:");
                String dur = input("Duración (hh:mm):");
                int num = inputInt("Número de canciones:");
                int u = inputInt("Unidades disponibles:");
                materialDAO.modificarCD(id, artista, genero, dur, num, u);
            }
            JOptionPane.showMessageDialog(this, "Material modificado.");
            refrescarVistaActual();
        } catch (Exception ex) {
            showError("No se pudo modificar: " + ex.getMessage());
        }
    }

    private void onBorrarMaterial() {
        String id = input("ID interno del material a borrar:");
        if (id == null || id.isBlank()) return;

        try {
            if (!materialDAO.existeMaterial(id)) {
                showWarn("No existe material con ID: " + id);
                return;
            }

            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Borrar definitivamente el material " + id + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION) return;

            borrarMaterial(id);
            JOptionPane.showMessageDialog(this, "Material borrado.");
            refrescarVistaActual();
        } catch (Exception ex) {
            showError("No se pudo borrar: " + ex.getMessage());
        }
    }

    private void onBuscarMaterial() {
        String id = input("ID interno a buscar:");
        if (id == null || id.isBlank()) return;

        try {
            Optional<Material> op = materialDAO.buscarPorId(id);
            if (op.isEmpty()) {
                showWarn("No existe material con ID: " + id);
                return;
            }
            Material m = op.get();
            JOptionPane.showMessageDialog(this, detalleMaterial(m), "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error al buscar material: " + ex.getMessage());
        }
    }

    // MÉTODOS SIMPLIFICADOS USANDO MaterialDAO
    private void agregarLibro() {
        String titulo = input("Título:");
        String autor = input("Autor:");
        int paginas = inputInt("Número de páginas:");
        String editorial = input("Editorial:");
        String isbn = input("ISBN:");
        int anio = inputInt("Año de publicación:");
        int unidades = inputInt("Unidades disponibles:");

        String nuevoId = generarNuevoId("LIB");
        // ORDEN CORREGIDO: id, titulo, unidades, autor, editorial, isbn, paginas, anio
        Libro libro = new Libro(nuevoId, titulo, unidades, autor, editorial, isbn, paginas, anio);

        try {
            materialDAO.insertarLibro(libro);
            System.out.println("✓ Libro agregado a BD. ID: " + nuevoId);
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar libro: " + e.getMessage(), e);
        }
    }

    private void agregarRevista() {
        String titulo = input("Título:");
        String editorial = input("Editorial:");
        String periodicidad = input("Periodicidad (Mensual/Semanal/etc):");
        String fechaStr = input("Fecha publicación (YYYY-MM-DD):");
        int unidades = inputInt("Unidades disponibles:");

        String nuevoId = generarNuevoId("REV");
        Revista revista = new Revista(nuevoId, titulo, unidades, editorial, periodicidad,
                LocalDate.parse(fechaStr));

        try {
            materialDAO.insertarRevista(revista);
            System.out.println("✓ Revista agregada a BD. ID: " + nuevoId);
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar revista: " + e.getMessage(), e);
        }
    }

    private void agregarDVD() {
        String titulo = input("Título:");
        String director = input("Director:");
        String duracion = input("Duración (hh:mm):");
        String genero = input("Género:");
        int unidades = inputInt("Unidades disponibles:");

        String nuevoId = generarNuevoId("DVD");
        DVD dvd = new DVD(nuevoId, titulo, unidades, director, duracion, genero);

        try {
            materialDAO.insertarDVD(dvd);
            System.out.println("✓ DVD agregado a BD. ID: " + nuevoId);
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar DVD: " + e.getMessage(), e);
        }
    }

    private void agregarCD() {
        String titulo = input("Título:");
        String artista = input("Artista:");
        String genero = input("Género:");
        String duracion = input("Duración (hh:mm):");
        int num = inputInt("Número de canciones:");
        int unidades = inputInt("Unidades disponibles:");

        String nuevoId = generarNuevoId("CDA");
        CD cd = new CD(nuevoId, titulo, unidades, artista, genero, duracion, num);

        try {
            materialDAO.insertarCD(cd);
            System.out.println("✓ CD agregado a BD. ID: " + nuevoId);
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar CD: " + e.getMessage(), e);
        }
    }

    // MÉTODO PARA BORRAR
    private void borrarMaterial(String id) {
        String tipo = id.substring(0, 3);
        String sql = "";

        switch (tipo) {
            case "LIB": sql = "DELETE FROM libros WHERE id_interno = ?"; break;
            case "REV": sql = "DELETE FROM revistas WHERE id_interno = ?"; break;
            case "CDA": sql = "DELETE FROM cds WHERE id_interno = ?"; break;
            case "DVD": sql = "DELETE FROM dvds WHERE id_interno = ?"; break;
            default: throw new IllegalArgumentException("ID inválido: " + id);
        }

        try (var conn = dbConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int filas = stmt.executeUpdate();
            System.out.println("✓ Material borrado. ID: " + id + ", Filas afectadas: " + filas);

        } catch (Exception e) {
            throw new RuntimeException("Error al borrar material: " + e.getMessage(), e);
        }
    }

    // MÉTODO PARA GENERAR IDs
    private String generarNuevoId(String tipo) {
        String sql = "";

        switch (tipo) {
            case "LIB": sql = "SELECT MAX(id_interno) FROM libros WHERE id_interno LIKE 'LIB%'"; break;
            case "REV": sql = "SELECT MAX(id_interno) FROM revistas WHERE id_interno LIKE 'REV%'"; break;
            case "DVD": sql = "SELECT MAX(id_interno) FROM dvds WHERE id_interno LIKE 'DVD%'"; break;
            case "CDA": sql = "SELECT MAX(id_interno) FROM cds WHERE id_interno LIKE 'CDA%'"; break;
            default: return tipo + "00001";
        }

        try (var conn = dbConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String ultimoId = rs.getString(1);
                if (ultimoId != null && !ultimoId.isEmpty()) {
                    int ultimoNumero = Integer.parseInt(ultimoId.substring(3));
                    return String.format("%s%05d", tipo, ultimoNumero + 1);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al generar ID, usando valor por defecto: " + e.getMessage());
        }

        return tipo + "00001";
    }

    // HELPERS UI
    private void showCard(String name) {
        ((CardLayout) content.getLayout()).show(content, name);
        refrescarVistaActual();
    }

    private void refrescarVistaActual() {
        // Reconstruir el panel actual con datos frescos de la base de datos
        String currentCard = getCurrentCardName();
        switch (currentCard) {
            case "LIBROS":
                inicializarPanelLibros();
                break;
            case "REVISTAS":
                inicializarPanelRevistas();
                break;
            case "DVDS":
                inicializarPanelDVDs();
                break;
            case "CDS":
                inicializarPanelCDs();
                break;
        }
        content.revalidate();
        content.repaint();
    }

    private String getCurrentCardName() {
        CardLayout layout = (CardLayout) content.getLayout();
        for (Component comp : content.getComponents()) {
            if (comp.isVisible()) {
                // Buscar el nombre del panel visible
                if (comp == librosPanel) return "LIBROS";
                if (comp == revistasPanel) return "REVISTAS";
                if (comp == dvdsPanel) return "DVDS";
                if (comp == cdsPanel) return "CDS";
            }
        }
        return "LIBROS"; // default
    }

    private static String input(String msg) {
        return JOptionPane.showInputDialog(null, msg);
    }

    private static int inputInt(String msg) {
        String s = JOptionPane.showInputDialog(null, msg);
        if (s == null) throw new IllegalArgumentException("Acción cancelada");
        return Integer.parseInt(s.trim());
    }

    private static void showWarn(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private static void showError(String msg){
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static String detalleMaterial(Material m) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(m.getIdInterno()).append('\n')
                .append("Título: ").append(m.getTitulo()).append('\n')
                .append("Unidades: ").append(m.getUnidadesDisponibles()).append('\n');

        if (m instanceof Libro l) {
            sb.append("Autor: ").append(l.getAutor()).append('\n')
                    .append("Editorial: ").append(l.getEditorial()).append('\n')
                    .append("ISBN: ").append(l.getIsbn()).append('\n')
                    .append("Año: ").append(l.getAnioPublicacion());
        } else if (m instanceof Revista r) {
            sb.append("Editorial: ").append(r.getEditorial()).append('\n')
                    .append("Periodicidad: ").append(r.getPeriodicidad()).append('\n')
                    .append("Fecha publicación: ").append(r.getFechaPublicacion());
        } else if (m instanceof DVD d) {
            sb.append("Director: ").append(d.getDirector()).append('\n')
                    .append("Duración: ").append(d.getDuracion()).append('\n')
                    .append("Género: ").append(d.getGenero());
        } else if (m instanceof CD c) {
            sb.append("Artista: ").append(c.getArtista()).append('\n')
                    .append("Género: ").append(c.getGenero()).append('\n')
                    .append("Duración: ").append(c.getDuracion()).append('\n')
                    .append("# Canciones: ").append(c.getNumeroCanciones());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppPrincipal().setVisible(true));
    }
}