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
 * AppPrincipal - Sistema de Gestión de Mediateca
 * Interfaz profesional con diálogos mejorados
 */
public class AppPrincipal extends JFrame {
    
    private final JPanel content = new JPanel(new CardLayout());
    private DatabaseConnection dbConnection;
    private final MaterialDAO materialDAO = new MaterialDAO();
    
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
        
        inicializarPanelLibros();
        inicializarPanelRevistas();
        inicializarPanelDVDs();
        inicializarPanelCDs();
        
        content.add(librosPanel,   "LIBROS");
        content.add(revistasPanel, "REVISTAS");
        content.add(dvdsPanel,     "DVDS");
        content.add(cdsPanel,      "CDS");
        add(content, BorderLayout.CENTER);
        
        crearMenu();
        showCard("LIBROS");
    }
    
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
    
    private void probarConexionBD() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            boolean conexionOk = db.testConnection();
            if (conexionOk) {
                JOptionPane.showMessageDialog(this,
                        "✓ Conexión exitosa",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "✗ Conexión fallida",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
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
                            "Driver: %s",
                    metaData.getDatabaseProductName(),
                    metaData.getDatabaseProductVersion(),
                    metaData.getDriverName());
            JOptionPane.showMessageDialog(this, info, "Información BD", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }
    
    private void salirAplicacion() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dispose();
        }
    }
    
    // ==================== ACCIONES PRINCIPALES ====================
    
    private void onAgregarMaterial() {
        String[] tipos = {"LIBRO", "REVISTA", "DVD", "CD"};
        String tipo = (String) JOptionPane.showInputDialog(
                this, "Selecciona tipo de material:", "Agregar Material",
                JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (tipo == null) return;
        
        try {
            switch (tipo) {
                case "LIBRO":
                    DialogoAgregarMaterial dialogo = new DialogoAgregarMaterial(this, "Agregar Libro", 
                        new String[]{"Título:", "Autor:", "Editorial:", "ISBN:", "Año:", "Páginas:", "Unidades:"});
                    dialogo.setVisible(true);
                    if (dialogo.isConfirmado()) {
                        String id = generarId("LIB");
                        Libro libro = new Libro(id, dialogo.getValor(0), 
                            Integer.parseInt(dialogo.getValor(6)), dialogo.getValor(1),
                            dialogo.getValor(2), dialogo.getValor(3),
                            Integer.parseInt(dialogo.getValor(4)),
                            Integer.parseInt(dialogo.getValor(5)));
                        materialDAO.insertarLibro(libro);
                        JOptionPane.showMessageDialog(this, "Libro agregado: " + id);
                    }
                    break;
                    
                case "REVISTA":
                    dialogo = new DialogoAgregarMaterial(this, "Agregar Revista",
                        new String[]{"Título:", "Editorial:", "Periodicidad:", "Fecha (YYYY-MM-DD):", "Unidades:"});
                    dialogo.setVisible(true);
                    if (dialogo.isConfirmado()) {
                        String id = generarId("REV");
                        Revista revista = new Revista(id, dialogo.getValor(0),
                            Integer.parseInt(dialogo.getValor(4)), dialogo.getValor(1),
                            dialogo.getValor(2), LocalDate.parse(dialogo.getValor(3)));
                        materialDAO.insertarRevista(revista);
                        JOptionPane.showMessageDialog(this, "Revista agregada: " + id);
                    }
                    break;
                    
                case "DVD":
                    dialogo = new DialogoAgregarMaterial(this, "Agregar DVD",
                        new String[]{"Título:", "Director:", "Duración (HH:MM):", "Género:", "Unidades:"});
                    dialogo.setVisible(true);
                    if (dialogo.isConfirmado()) {
                        String id = generarId("DVD");
                        DVD dvd = new DVD(id, dialogo.getValor(0),
                            Integer.parseInt(dialogo.getValor(4)), dialogo.getValor(1),
                            dialogo.getValor(2), dialogo.getValor(3));
                        materialDAO.insertarDVD(dvd);
                        JOptionPane.showMessageDialog(this, "DVD agregado: " + id);
                    }
                    break;
                    
                case "CD":
                    dialogo = new DialogoAgregarMaterial(this, "Agregar CD",
                        new String[]{"Título:", "Artista:", "Género:", "Duración (HH:MM):", "Canciones:", "Unidades:"});
                    dialogo.setVisible(true);
                    if (dialogo.isConfirmado()) {
                        String id = generarId("CDA");
                        CD cd = new CD(id, dialogo.getValor(0),
                            Integer.parseInt(dialogo.getValor(5)), dialogo.getValor(1),
                            dialogo.getValor(2), dialogo.getValor(3),
                            Integer.parseInt(dialogo.getValor(4)));
                        materialDAO.insertarCD(cd);
                        JOptionPane.showMessageDialog(this, "CD agregado: " + id);
                    }
                    break;
            }
            refrescarVistaActual();
        } catch (Exception ex) {
            showError("Error al agregar: " + ex.getMessage());
        }
    }
    
    private void onModificarMaterial() {
        DialogoModificarMaterial dialogo = new DialogoModificarMaterial(this);
        dialogo.setVisible(true);
        refrescarVistaActual();
    }
    
    private void onBorrarMaterial() {
    String id = JOptionPane.showInputDialog(this, "ID a borrar:");
    if (id == null || id.isBlank()) return;

    id = id.trim().toUpperCase();
    int confirm = JOptionPane.showConfirmDialog(this, "¿Borrar " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    try {
        // Verifica que exista
        Optional<Material> op = materialDAO.buscarPorId(id);
        if (op.isEmpty()) {
            showWarn("No encontrado");
            return;
        }

        // ¡Borrar desde la tabla base!
        final String sql = "DELETE FROM Material WHERE id_interno = ?";
        try (var conn = dbConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                showWarn("No se pudo borrar (0 filas afectadas).");
            } else {
                JOptionPane.showMessageDialog(this, "Borrado exitosamente");
            }
        }
        refrescarVistaActual();
    } catch (Exception ex) {
        showError("Error: " + ex.getMessage());
    }
}

    
    private void onBuscarMaterial() {
        String id = JOptionPane.showInputDialog(this, "ID a buscar:");
        if (id == null || id.isBlank()) return;
        
        try {
            Optional<Material> op = materialDAO.buscarPorId(id.trim().toUpperCase());
            if (op.isEmpty()) {
                showWarn("No encontrado");
                return;
            }
            JOptionPane.showMessageDialog(this, detalleMaterial(op.get()), "Detalles", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }
    
   /*  private String getTablaPorId(String id) {
        if (id.startsWith("LIB")) return "libros";
        if (id.startsWith("REV")) return "revistas";
        if (id.startsWith("DVD")) return "dvds";
        if (id.startsWith("CDA")) return "cds";
        return "material";
    }*/
    
    private String generarId(String tipo) {
    // tipo = "LIB" | "REV" | "DVD" | "CDA"
    final String sql = "SELECT MAX(id_interno) FROM Material WHERE id_interno LIKE ?";
    try (var conn = dbConnection.getConnection();
         var ps = conn.prepareStatement(sql)) {
        ps.setString(1, tipo + "%");
        try (var rs = ps.executeQuery()) {
            if (rs.next()) {
                String ultimo = rs.getString(1); // p.ej. LIB00012
                if (ultimo != null && !ultimo.isEmpty()) {
                    int num = Integer.parseInt(ultimo.substring(tipo.length())); // desde después del prefijo
                    return String.format("%s%05d", tipo, num + 1);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return tipo + "00001";
}

    
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
    
    private static void showWarn(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
    
    private static void showError(String msg) {
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
              .append("Fecha: ").append(r.getFechaPublicacion());
        } else if (m instanceof DVD d) {
            sb.append("Director: ").append(d.getDirector()).append('\n')
              .append("Duración: ").append(d.getDuracion()).append('\n')
              .append("Género: ").append(d.getGenero());
        } else if (m instanceof CD c) {
            sb.append("Artista: ").append(c.getArtista()).append('\n')
              .append("Género: ").append(c.getGenero()).append('\n')
              .append("Duración: ").append(c.getDuracion()).append('\n')
              .append("Canciones: ").append(c.getNumeroCanciones());
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppPrincipal().setVisible(true));
    }
}