package com.diego.mediateca.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.time.LocalDate;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.diego.mediateca.VistaUsuario.DialogoAgregarMaterial;
import com.diego.mediateca.VistaUsuario.DialogoModificarMaterial;
import com.diego.mediateca.VistaUsuario.VentanaMateriales;
import com.diego.mediateca.db.DatabaseConnection;
import com.diego.mediateca.db.MaterialDAO;
import com.diego.mediateca.domain.CD;
import com.diego.mediateca.domain.DVD;
import com.diego.mediateca.domain.Libro;
import com.diego.mediateca.domain.Material;
import com.diego.mediateca.domain.Revista;

/**
 * AppPrincipal CON DIÁLOGOS MEJORADOS
 * - Usa DialogoAgregarMaterial para diálogos profesionales
 * - Usa VentanaMateriales para tablas modernas
 * - Interfaz completa mejorada
 */
public class AppPrincipal extends JFrame {
    
    private final JPanel content = new JPanel(new CardLayout());
    private DatabaseConnection dbConnection;
    private final MaterialDAO materialDAO = new MaterialDAO();
    
    // Paneles para mostrar listas
    private final JPanel librosPanel   = new JPanel(new BorderLayout());
    private final JPanel revistasPanel = new JPanel(new BorderLayout());
    private final JPanel dvdsPanel     = new JPanel(new BorderLayout());
    private final JPanel cdsPanel      = new JPanel(new BorderLayout());

    public AppPrincipal() {
        System.out.println("Inicializando AppPrincipal");
        instalarManejadorExcepcionesGlobal();
        verificarConexionBD();
        initComponents();
    }

    private void instalarManejadorExcepcionesGlobal() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println("Excepción no manejada en hilo: " + t.getName());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Ha ocurrido un error inesperado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        });
    }

    private void verificarConexionBD() {
        try {
            System.out.println("Verificando conexión a BD...");
            dbConnection = DatabaseConnection.getInstance();
            boolean conexionExitosa = dbConnection.testConnection();
            if (!conexionExitosa) {
                System.err.println("Fallo de conexión inicial");
                JOptionPane.showMessageDialog(this,
                        "Error al conectar con la base de datos.\nLa aplicación puede no funcionar correctamente.",
                        "Error de Conexión", JOptionPane.WARNING_MESSAGE);
            } else {
                System.out.println("✓ Aplicación conectada a base de datos correctamente");
            }
        } catch (Exception e) {
            System.err.println("Error crítico de conexión al iniciar la app");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error crítico de conexión: " + e.getMessage() + "\nVerifique que MySQL esté ejecutándose.",
                    "Error de Configuración",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        setTitle("Mediateca - Sistema de Gestión");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar paneles de listas con VentanaMateriales
        inicializarPanelLibros();
        inicializarPanelRevistas();
        inicializarPanelDVDs();
        inicializarPanelCDs();

        // Agregar paneles al CardLayout
        content.add(librosPanel,   "LIBROS");
        content.add(revistasPanel, "REVISTAS");
        content.add(dvdsPanel,     "DVDS");
        content.add(cdsPanel,      "CDS");

        add(content, BorderLayout.CENTER);

        // Menú de navegación
        crearMenu();

        // Mostrar por defecto LIBROS
        showCard("LIBROS");
    }

    // ==================== INICIALIZAR PANELES ====================
    
    private void inicializarPanelLibros() {
        librosPanel.removeAll();
        VentanaMateriales ventana = new VentanaMateriales();
        librosPanel.add(ventana, BorderLayout.CENTER);
    }

    private void inicializarPanelRevistas() {
        revistasPanel.removeAll();
        VentanaMateriales ventana = new VentanaMateriales();
        revistasPanel.add(ventana, BorderLayout.CENTER);
    }

    private void inicializarPanelDVDs() {
        dvdsPanel.removeAll();
        VentanaMateriales ventana = new VentanaMateriales();
        dvdsPanel.add(ventana, BorderLayout.CENTER);
    }

    private void inicializarPanelCDs() {
        cdsPanel.removeAll();
        VentanaMateriales ventana = new VentanaMateriales();
        cdsPanel.add(ventana, BorderLayout.CENTER);
    }

    // ==================== MENÚ ====================
    
    private void crearMenu() {
        JMenuBar mb = new JMenuBar();

        // MENU Disponibles
        JMenu mDisponibles = new JMenu("Disponibles");
        JMenuItem itLibros   = new JMenuItem("📚 Libros");
        JMenuItem itRevistas = new JMenuItem("📰 Revistas");
        JMenuItem itDVDs     = new JMenuItem("🎬 DVDs");
        JMenuItem itCDs      = new JMenuItem("🎵 CDs");

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
        JMenuItem miInfoBD       = new JMenuItem("Información BD");

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

    // ==================== ACCIONES DEL MENÚ BD ====================
    
    private void probarConexionBD() {
        try {
            System.out.println("Probando conexión a BD desde menú");
            DatabaseConnection db = DatabaseConnection.getInstance();
            boolean conexionOk = db.testConnection();
            if (conexionOk) {
                System.out.println("Probar Conexión: OK");
                JOptionPane.showMessageDialog(this,
                        "✓ Conexión a la base de datos exitosa",
                        "Conexión Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.err.println("Probar Conexión: FAIL");
                JOptionPane.showMessageDialog(this,
                        "✗ No se pudo establecer conexión con la base de datos",
                        "Error de Conexión",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Error al probar conexión: " + e.getMessage());
            e.printStackTrace();
            showError("Error al probar conexión: " + e.getMessage());
        }
    }

    private void mostrarInfoBD() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            java.sql.Connection conn = db.getConnection();
            java.sql.DatabaseMetaData metaData = conn.getMetaData();
            String info = String.format(
                    "Información de Base de Datos:%n%n" +
                            "Producto: %s%n" +
                            "Versión: %s%n" +
                            "Driver: %s%n" +
                            "URL: %s%n" +
                            "Usuario: %s%n" +
                            "Conexión válida: %s",
                    metaData.getDatabaseProductName(),
                    metaData.getDatabaseProductVersion(),
                    metaData.getDriverName(),
                    metaData.getURL(),
                    metaData.getUserName(),
                    db.isConnectionValid() ? "Sí" : "No"
            );
            System.out.println("Mostrando información de BD");
            JOptionPane.showMessageDialog(this, info, "Información BD", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error al obtener información de BD: " + e.getMessage());
            e.printStackTrace();
            showError("Error al obtener información: " + e.getMessage());
        }
    }

    private void salirAplicacion() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            db.closeConnection();
            System.out.println("Aplicación cerrada, conexión de BD cerrada");
        } catch (Exception e) {
            System.err.println("Error al cerrar conexión al salir: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dispose();
        }
    }

    // ==================== ACCIONES PRINCIPALES ====================
    
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
                default        -> { }
            }
            System.out.println("Material agregado correctamente (tipo=" + tipo + ")");
            refrescarVistaActual();
        } catch (Exception ex) {
            System.err.println("No se pudo agregar material (tipo=" + tipo + "): " + ex.getMessage());
            ex.printStackTrace();
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
        
        if (m instanceof Libro l) {
            String[] etiquetas = {"ID:", "Autor:", "Editorial:", "ISBN:", "Año:", "Páginas:", "Unidades:"};
            String[] valores = {
                l.getIdInterno(),
                l.getAutor(),
                l.getEditorial(),
                l.getIsbn(),
                String.valueOf(l.getAnioPublicacion()),
                String.valueOf(l.getNumeroPaginas()),
                String.valueOf(l.getUnidadesDisponibles())
            };
            
            DialogoModificarMaterial dialogo = new DialogoModificarMaterial(this, "Modificar Libro", etiquetas, valores);
            dialogo.setVisible(true);
            
            if (dialogo.isConfirmado()) {
                int unidades = Integer.parseInt(dialogo.getValor(6));
                materialDAO.modificarLibro(id, unidades);
                JOptionPane.showMessageDialog(this, "Libro modificado exitosamente!");
                refrescarVistaActual();
            }
        } 
        else if (m instanceof Revista r) {
            String[] etiquetas = {"ID:", "Editorial:", "Periodicidad:", "Fecha:", "Unidades:"};
            String[] valores = {
                r.getIdInterno(),
                r.getEditorial(),
                r.getPeriodicidad(),
                r.getFechaPublicacion().toString(),
                String.valueOf(r.getUnidadesDisponibles())
            };
            
            DialogoModificarMaterial dialogo = new DialogoModificarMaterial(this, "Modificar Revista", etiquetas, valores);
            dialogo.setVisible(true);
            
            if (dialogo.isConfirmado()) {
                int unidades = Integer.parseInt(dialogo.getValor(4));
                materialDAO.modificarRevista(id, unidades);
                JOptionPane.showMessageDialog(this, "Revista modificada exitosamente!");
                refrescarVistaActual();
            }
        } 
        else if (m instanceof DVD d) {
            String[] etiquetas = {"ID:", "Director:", "Duración:", "Género:", "Unidades:"};
            String[] valores = {
                d.getIdInterno(),
                d.getDirector(),
                d.getDuracion(),
                d.getGenero(),
                String.valueOf(d.getUnidadesDisponibles())
            };
            
            DialogoModificarMaterial dialogo = new DialogoModificarMaterial(this, "Modificar DVD", etiquetas, valores);
            dialogo.setVisible(true);
            
            if (dialogo.isConfirmado()) {
                String director = dialogo.getValor(1);
                String duracion = dialogo.getValor(2);
                String genero = dialogo.getValor(3);
                int unidades = Integer.parseInt(dialogo.getValor(4));
                materialDAO.modificarDVD(id, director, duracion, genero, unidades);
                JOptionPane.showMessageDialog(this, "DVD modificado exitosamente!");
                refrescarVistaActual();
            }
        } 
        else if (m instanceof CD c) {
            String[] etiquetas = {"ID:", "Artista:", "Género:", "Duración:", "Canciones:", "Unidades:"};
            String[] valores = {
                c.getIdInterno(),
                c.getArtista(),
                c.getGenero(),
                c.getDuracion(),
                String.valueOf(c.getNumeroCanciones()),
                String.valueOf(c.getUnidadesDisponibles())
            };
            
            DialogoModificarMaterial dialogo = new DialogoModificarMaterial(this, "Modificar CD", etiquetas, valores);
            dialogo.setVisible(true);
            
            if (dialogo.isConfirmado()) {
                String artista = dialogo.getValor(1);
                String genero = dialogo.getValor(2);
                String duracion = dialogo.getValor(3);
                int num = Integer.parseInt(dialogo.getValor(4));
                int unidades = Integer.parseInt(dialogo.getValor(5));
                materialDAO.modificarCD(id, artista, genero, duracion, num, unidades);
                JOptionPane.showMessageDialog(this, "CD modificado exitosamente!");
                refrescarVistaActual();
            }
        }
    } catch (NumberFormatException e) {
        showError("Por favor ingrese valores numéricos válidos");
    } catch (Exception ex) {
        System.err.println("No se pudo modificar material (id=" + id + "): " + ex.getMessage());
        ex.printStackTrace();
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
            System.out.println("Material borrado (id=" + id + ")");
            JOptionPane.showMessageDialog(this, "Material borrado.");
            refrescarVistaActual();
        } catch (Exception ex) {
            System.err.println("No se pudo borrar material (id=" + id + "): " + ex.getMessage());
            ex.printStackTrace();
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
            System.err.println("Error al buscar material (id=" + id + "): " + ex.getMessage());
            ex.printStackTrace();
            showError("Error al buscar material: " + ex.getMessage());
        }
    }

    // ==================== MÉTODOS DE AGREGAR CON DIÁLOGOS MEJORADOS ====================
    
    private void agregarLibro() {
        String[] etiquetas = {
            "Título:",
            "Autor:",
            "Editorial:",
            "ISBN:",
            "Año de publicación:",
            "Número de páginas:",
            "Unidades disponibles:"
        };
        
        DialogoAgregarMaterial dialogo = new DialogoAgregarMaterial(this, "Agregar Libro", etiquetas);
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            try {
                String titulo = dialogo.getValor(0);
                String autor = dialogo.getValor(1);
                String editorial = dialogo.getValor(2);
                String isbn = dialogo.getValor(3);
                int anio = Integer.parseInt(dialogo.getValor(4));
                int paginas = Integer.parseInt(dialogo.getValor(5));
                int unidades = Integer.parseInt(dialogo.getValor(6));

                String nuevoId = generarNuevoId("LIB");
                Libro libro = new Libro(nuevoId, titulo, unidades, autor, editorial, isbn, anio, paginas);
                materialDAO.insertarLibro(libro);
                
                System.out.println("Libro agregado a BD (id=" + nuevoId + ")");
                JOptionPane.showMessageDialog(this, "Libro agregado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                showError("Por favor ingrese valores numéricos válidos en los campos correspondientes");
            } catch (Exception e) {
                showError("Error al agregar libro: " + e.getMessage());
            }
        }
    }

    private void agregarRevista() {
        String[] etiquetas = {
            "Título:",
            "Editorial:",
            "Periodicidad:",
            "Fecha publicación (YYYY-MM-DD):",
            "Unidades disponibles:"
        };
        
        DialogoAgregarMaterial dialogo = new DialogoAgregarMaterial(this, "Agregar Revista", etiquetas);
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            try {
                String titulo = dialogo.getValor(0);
                String editorial = dialogo.getValor(1);
                String periodicidad = dialogo.getValor(2);
                String fechaStr = dialogo.getValor(3);
                int unidades = Integer.parseInt(dialogo.getValor(4));

                String nuevoId = generarNuevoId("REV");
                Revista revista = new Revista(nuevoId, titulo, unidades, editorial, periodicidad, LocalDate.parse(fechaStr));
                materialDAO.insertarRevista(revista);
                
                System.out.println("Revista agregada a BD (id=" + nuevoId + ")");
                JOptionPane.showMessageDialog(this, "Revista agregada exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                showError("Error al agregar revista: " + e.getMessage());
            }
        }
    }

    private void agregarDVD() {
        String[] etiquetas = {
            "Título:",
            "Director:",
            "Duración (HH:MM):",
            "Género:",
            "Unidades disponibles:"
        };
        
        DialogoAgregarMaterial dialogo = new DialogoAgregarMaterial(this, "Agregar DVD", etiquetas);
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            try {
                String titulo = dialogo.getValor(0);
                String director = dialogo.getValor(1);
                String duracion = dialogo.getValor(2);
                String genero = dialogo.getValor(3);
                int unidades = Integer.parseInt(dialogo.getValor(4));

                String nuevoId = generarNuevoId("DVD");
                DVD dvd = new DVD(nuevoId, titulo, unidades, director, duracion, genero);
                materialDAO.insertarDVD(dvd);
                
                System.out.println("DVD agregado a BD (id=" + nuevoId + ")");
                JOptionPane.showMessageDialog(this, "DVD agregado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                showError("Error al agregar DVD: " + e.getMessage());
            }
        }
    }

    private void agregarCD() {
        String[] etiquetas = {
            "Título:",
            "Artista:",
            "Género:",
            "Duración (HH:MM):",
            "Número de canciones:",
            "Unidades disponibles:"
        };
        
        DialogoAgregarMaterial dialogo = new DialogoAgregarMaterial(this, "Agregar CD", etiquetas);
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            try {
                String titulo = dialogo.getValor(0);
                String artista = dialogo.getValor(1);
                String genero = dialogo.getValor(2);
                String duracion = dialogo.getValor(3);
                int num = Integer.parseInt(dialogo.getValor(4));
                int unidades = Integer.parseInt(dialogo.getValor(5));

                String nuevoId = generarNuevoId("CDA");
                CD cd = new CD(nuevoId, titulo, unidades, artista, genero, duracion, num);
                materialDAO.insertarCD(cd);
                
                System.out.println("CD agregado a BD (id=" + nuevoId + ")");
                JOptionPane.showMessageDialog(this, "CD agregado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                showError("Error al agregar CD: " + e.getMessage());
            }
        }
    }

private void borrarMaterial(String id) {
    final String sql = "DELETE FROM Material WHERE id_interno = ?";
    try (var conn = dbConnection.getConnection();
         var ps = conn.prepareStatement(sql)) {

        ps.setString(1, id);
        int filas = ps.executeUpdate();
        System.out.println("Borrado en Material id=" + id + " filas=" + filas);

        if (filas == 0) {
            throw new RuntimeException("No existe material con ID: " + id);
        }
    } catch (Exception e) {
        System.err.println("Error al borrar material (id=" + id + "): " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Error al borrar material: " + e.getMessage(), e);
    }
}

    private String generarNuevoId(String tipo) {
        String sql;
        switch (tipo) {
            case "LIB": sql = "SELECT MAX(id_interno) FROM libros WHERE id_interno LIKE 'LIB%'"; break;
            case "REV": sql = "SELECT MAX(id_interno) FROM revistas WHERE id_interno LIKE 'REV%'"; break;
            case "DVD": sql = "SELECT MAX(id_interno) FROM dvds WHERE id_interno LIKE 'DVD%'";   break;
            case "CDA": sql = "SELECT MAX(id_interno) FROM cds WHERE id_interno LIKE 'CDA%'";    break;
            default: return tipo + "00001";
        }

        try (var conn = dbConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String ultimoId = rs.getString(1);
                if (ultimoId != null && !ultimoId.isEmpty()) {
                    int ultimoNumero = Integer.parseInt(ultimoId.substring(3));
                    String nuevo = String.format("%s%05d", tipo, ultimoNumero + 1);
                    System.out.println("Generado nuevo ID: " + nuevo);
                    return nuevo;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al generar ID: " + e.getMessage());
        }

        String def = tipo + "00001";
        System.out.println("Generado ID por defecto: " + def);
        return def;
    }

    // ==================== HELPERS UI ====================
    
    private void showCard(String name) {
        ((CardLayout) content.getLayout()).show(content, name);
        refrescarVistaActual();
    }

    private void refrescarVistaActual() {
        String currentCard = getCurrentCardName();
        switch (currentCard) {
            case "LIBROS":   inicializarPanelLibros();   break;
            case "REVISTAS": inicializarPanelRevistas(); break;
            case "DVDS":     inicializarPanelDVDs();     break;
            case "CDS":      inicializarPanelCDs();      break;
            default: break;
        }
        content.revalidate();
        content.repaint();
    }

    private String getCurrentCardName() {
        for (Component comp : content.getComponents()) {
            if (comp.isVisible()) {
                if (comp == librosPanel)   return "LIBROS";
                if (comp == revistasPanel) return "REVISTAS";
                if (comp == dvdsPanel)     return "DVDS";
                if (comp == cdsPanel)      return "CDS";
            }
        }
        return "LIBROS";
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