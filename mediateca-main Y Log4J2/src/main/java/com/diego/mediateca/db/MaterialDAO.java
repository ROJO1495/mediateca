package com.diego.mediateca.db;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.diego.mediateca.domain.CD;
import com.diego.mediateca.domain.DVD;
import com.diego.mediateca.domain.Libro;
import com.diego.mediateca.domain.Material;
import com.diego.mediateca.domain.Revista;

public class MaterialDAO {
    
    private final DatabaseConnection dbConnection;
    
    public MaterialDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    // ==================== MÉTODOS DE MODIFICACIÓN ====================
    
    public boolean modificarLibro(String idInterno, String titulo, String autor, 
                                  String editorial, String isbn, int anioPublicacion,
                                  int numeroPaginas, int unidadesDisponibles) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            String sqlMaterial = "UPDATE Material SET titulo = ?, unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, titulo);
                ps.setInt(2, unidadesDisponibles);
                ps.setString(3, idInterno);
                ps.executeUpdate();
            }
            
            String sqlEscrito = "UPDATE MaterialEscrito SET editorial = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, editorial);
                ps.setString(2, idInterno);
                ps.executeUpdate();
            }
            
            String sqlLibro = "UPDATE Libros SET autor = ?, numero_paginas = ?, anio_publicacion = ?, isbn = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlLibro)) {
                ps.setString(1, autor);
                ps.setInt(2, numeroPaginas);
                ps.setInt(3, anioPublicacion);
                ps.setString(4, isbn);
                ps.setString(5, idInterno);
                
                int filasAfectadas = ps.executeUpdate();
                conn.commit();
                return filasAfectadas > 0;
            }
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    public boolean modificarRevista(String idInterno, String titulo, String editorial,
                                    String periodicidad, LocalDate fechaPublicacion,
                                    int unidadesDisponibles) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            String sqlMaterial = "UPDATE Material SET titulo = ?, unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, titulo);
                ps.setInt(2, unidadesDisponibles);
                ps.setString(3, idInterno);
                ps.executeUpdate();
            }
            
            String sqlEscrito = "UPDATE MaterialEscrito SET editorial = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, editorial);
                ps.setString(2, idInterno);
                ps.executeUpdate();
            }
            
            String sqlRevista = "UPDATE Revistas SET periodicidad = ?, fecha_publicacion = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlRevista)) {
                ps.setString(1, periodicidad);
                ps.setDate(2, Date.valueOf(fechaPublicacion));
                ps.setString(3, idInterno);
                
                int filasAfectadas = ps.executeUpdate();
                conn.commit();
                return filasAfectadas > 0;
            }
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    public boolean modificarDVD(String idInterno, String titulo, String director,
                                String duracion, String genero, int unidadesDisponibles) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            String sqlMaterial = "UPDATE Material SET titulo = ?, unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, titulo);
                ps.setInt(2, unidadesDisponibles);
                ps.setString(3, idInterno);
                ps.executeUpdate();
            }
            
            String sqlAudio = "UPDATE MaterialAudiovisual SET duracion = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudio)) {
                ps.setInt(1, parseDuracionAMinutos(duracion));
                ps.setString(2, idInterno);
                ps.executeUpdate();
            }
            
            String sqlDVD = "UPDATE DVDs SET director = ?, genero = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDVD)) {
                ps.setString(1, director);
                ps.setString(2, genero);
                ps.setString(3, idInterno);
                
                int filasAfectadas = ps.executeUpdate();
                conn.commit();
                return filasAfectadas > 0;
            }
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    public boolean modificarCD(String idInterno, String titulo, String artista,
                               String genero, String duracion, int numeroCanciones,
                               int unidadesDisponibles) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            String sqlMaterial = "UPDATE Material SET titulo = ?, unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, titulo);
                ps.setInt(2, unidadesDisponibles);
                ps.setString(3, idInterno);
                ps.executeUpdate();
            }
            
            String sqlAudio = "UPDATE MaterialAudiovisual SET duracion = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudio)) {
                ps.setInt(1, parseDuracionAMinutos(duracion));
                ps.setString(2, idInterno);
                ps.executeUpdate();
            }
            
            String sqlCD = "UPDATE CDs SET artista = ?, numero_canciones = ?, genero = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlCD)) {
                ps.setString(1, artista);
                ps.setInt(2, numeroCanciones);
                ps.setString(3, genero);
                ps.setString(4, idInterno);
                
                int filasAfectadas = ps.executeUpdate();
                conn.commit();
                return filasAfectadas > 0;
            }
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    // ==================== MÉTODOS DE INSERCIÓN ====================
    
    public boolean insertarLibro(Libro libro) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getTitulo());
                ps.setInt(3, libro.getUnidadesDisponibles());
                ps.executeUpdate();
            }
            
            String sqlEscrito = "INSERT INTO MaterialEscrito (id_interno, editorial) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getEditorial());
                ps.executeUpdate();
            }
            
            String sqlLibro = "INSERT INTO Libros (id_interno, autor, numero_paginas, anio_publicacion, isbn) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlLibro)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getAutor());
                ps.setInt(3, libro.getNumeroPaginas());
                ps.setInt(4, libro.getAnioPublicacion());
                ps.setString(5, libro.getIsbn());
                
                int filasAfectadas = ps.executeUpdate();
                conn.commit();
                return filasAfectadas > 0;
            }
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    public boolean insertarRevista(Revista revista) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, revista.getIdInterno());
                ps.setString(2, revista.getTitulo());
                ps.setInt(3, revista.getUnidadesDisponibles());
                ps.executeUpdate();
            }
            
            String sqlEscrito = "INSERT INTO MaterialEscrito (id_interno, editorial) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, revista.getIdInterno());
                ps.setString(2, revista.getEditorial());
                ps.executeUpdate();
            }
            
            String sqlRevista = "INSERT INTO Revistas (id_interno, fecha_publicacion, periodicidad) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlRevista)) {
                ps.setString(1, revista.getIdInterno());
                ps.setDate(2, Date.valueOf(revista.getFechaPublicacion()));
                ps.setString(3, revista.getPeriodicidad());
                
                int filasAfectadas = ps.executeUpdate();
                conn.commit();
                return filasAfectadas > 0;
            }
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    public boolean insertarCD(CD cd) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, cd.getIdInterno());
                ps.setString(2, cd.getTitulo());
                ps.setInt(3, cd.getUnidadesDisponibles());
                ps.executeUpdate();
            }
            
            String sqlAudio = "INSERT INTO MaterialAudiovisual (id_interno, duracion) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudio)) {
                ps.setString(1, cd.getIdInterno());
                ps.setInt(2, parseDuracionAMinutos(cd.getDuracion()));
                ps.executeUpdate();
            }
            
            String sqlCD = "INSERT INTO CDs (id_interno, artista, numero_canciones, genero) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlCD)) {
                ps.setString(1, cd.getIdInterno());
                ps.setString(2, cd.getArtista());
                ps.setInt(3, cd.getNumeroCanciones());
                ps.setString(4, cd.getGenero());
                
                int filasAfectadas = ps.executeUpdate();
                conn.commit();
                return filasAfectadas > 0;
            }
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    public boolean insertarDVD(DVD dvd) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, dvd.getIdInterno());
                ps.setString(2, dvd.getTitulo());
                ps.setInt(3, dvd.getUnidadesDisponibles());
                ps.executeUpdate();
            }
            
            String sqlAudio = "INSERT INTO MaterialAudiovisual (id_interno, duracion) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudio)) {
                ps.setString(1, dvd.getIdInterno());
                ps.setInt(2, parseDuracionAMinutos(dvd.getDuracion()));
                ps.executeUpdate();
            }
            
            String sqlDVD = "INSERT INTO DVDs (id_interno, director, genero) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlDVD)) {
                ps.setString(1, dvd.getIdInterno());
                ps.setString(2, dvd.getDirector());
                ps.setString(3, dvd.getGenero());
                
                int filasAfectadas = ps.executeUpdate();
                conn.commit();
                return filasAfectadas > 0;
            }
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
    
    // ==================== MÉTODOS DE LISTAR ====================
    
    public List<Libro> listarLibrosDisponibles() throws SQLException {
        String sql = """
            SELECT m.id_interno, m.titulo, m.unidades_disponibles,
                   me.editorial, l.autor, l.numero_paginas, l.anio_publicacion, l.isbn
            FROM Material m
            JOIN MaterialEscrito me ON m.id_interno = me.id_interno
            JOIN Libros l ON m.id_interno = l.id_interno
            WHERE m.unidades_disponibles > 0
            """;
        
        List<Libro> libros = new ArrayList<>();
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Libro libro = new Libro(
                    rs.getString("id_interno"),
                    rs.getString("titulo"),
                    rs.getInt("unidades_disponibles"),
                    rs.getString("autor"),
                    rs.getString("editorial"),
                    rs.getString("isbn"),
                    rs.getInt("anio_publicacion"),
                    rs.getInt("numero_paginas")
                );
                libros.add(libro);
            }
        }
        return libros;
    }
    
    public List<Revista> listarRevistasDisponibles() throws SQLException {
        String sql = """
            SELECT m.id_interno, m.titulo, m.unidades_disponibles,
                   me.editorial, r.fecha_publicacion, r.periodicidad
            FROM Material m
            JOIN MaterialEscrito me ON m.id_interno = me.id_interno
            JOIN Revistas r ON m.id_interno = r.id_interno
            WHERE m.unidades_disponibles > 0
            """;
        
        List<Revista> revistas = new ArrayList<>();
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Revista revista = new Revista(
                    rs.getString("id_interno"),
                    rs.getString("titulo"),
                    rs.getInt("unidades_disponibles"),
                    rs.getString("editorial"),
                    rs.getString("periodicidad"),
                    rs.getDate("fecha_publicacion").toLocalDate()
                );
                revistas.add(revista);
            }
        }
        return revistas;
    }
    
    public List<CD> listarCDsDisponibles() throws SQLException {
        String sql = """
            SELECT m.id_interno, m.titulo, m.unidades_disponibles,
                   ma.duracion, c.artista, c.numero_canciones, c.genero
            FROM Material m
            JOIN MaterialAudiovisual ma ON m.id_interno = ma.id_interno
            JOIN CDs c ON m.id_interno = c.id_interno
            WHERE m.unidades_disponibles > 0
            """;
        
        List<CD> cds = new ArrayList<>();
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                CD cd = new CD(
                    rs.getString("id_interno"),
                    rs.getString("titulo"),
                    rs.getInt("unidades_disponibles"),
                    rs.getString("artista"),
                    rs.getString("genero"),
                    formatearDuracion(rs.getInt("duracion")),
                    rs.getInt("numero_canciones")
                );
                cds.add(cd);
            }
        }
        return cds;
    }
    
    public List<DVD> listarDVDsDisponibles() throws SQLException {
        String sql = """
            SELECT m.id_interno, m.titulo, m.unidades_disponibles,
                   ma.duracion, d.director, d.genero
            FROM Material m
            JOIN MaterialAudiovisual ma ON m.id_interno = ma.id_interno
            JOIN DVDs d ON m.id_interno = d.id_interno
            WHERE m.unidades_disponibles > 0
            """;
        
        List<DVD> dvds = new ArrayList<>();
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DVD dvd = new DVD(
                    rs.getString("id_interno"),
                    rs.getString("titulo"),
                    rs.getInt("unidades_disponibles"),
                    rs.getString("director"),
                    formatearDuracion(rs.getInt("duracion")),
                    rs.getString("genero")
                );
                dvds.add(dvd);
            }
        }
        return dvds;
    }
    
    // ==================== MÉTODOS DE BÚSQUEDA ====================
    
    public Optional<Material> buscarPorId(String idInterno) throws SQLException {
        String tipo = identificarTipo(idInterno);
        
        return switch (tipo) {
            case "LIB" -> buscarLibro(idInterno);
            case "REV" -> buscarRevista(idInterno);
            case "CDA" -> buscarCD(idInterno);
            case "DVD" -> buscarDVDPorId(idInterno);
            default -> Optional.empty();
        };
    }
    
    public boolean existeMaterial(String idInterno) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Material WHERE id_interno = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    
    private Optional<Material> buscarLibro(String idInterno) throws SQLException {
    String sql = "SELECT m.id_interno, m.titulo, m.unidades_disponibles, me.editorial, l.autor, l.numero_paginas, l.anio_publicacion, l.isbn " +
                 "FROM Material m " +
                 "JOIN MaterialEscrito me ON m.id_interno = me.id_interno " +
                 "JOIN Libros l ON m.id_interno = l.id_interno " +
                 "WHERE m.id_interno = ?";
    
    try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
        ps.setString(1, idInterno);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                System.out.println("✓ Libro encontrado");
                System.out.println("  ID: " + rs.getString("id_interno"));
                System.out.println("  Título: " + rs.getString("titulo"));
                System.out.println("  Autor: " + rs.getString("autor"));
                System.out.println("  Año: " + rs.getInt("anio_publicacion"));
                System.out.println("  Páginas: " + rs.getInt("numero_paginas"));
                
                Libro libro = new Libro(
                    rs.getString("id_interno"),
                    rs.getString("titulo"),
                    rs.getInt("unidades_disponibles"),
                    rs.getString("autor"),
                    rs.getString("editorial"),
                    rs.getString("isbn"),
                    rs.getInt("anio_publicacion"),
                    rs.getInt("numero_paginas")
                );
                return Optional.of(libro);
            }
        }
    }
    return Optional.empty();
}
    
    private Optional<Material> buscarRevista(String idInterno) throws SQLException {
        String sql = """
            SELECT m.id_interno, m.titulo, m.unidades_disponibles,
                   me.editorial, r.fecha_publicacion, r.periodicidad
            FROM Material m
            JOIN MaterialEscrito me ON m.id_interno = me.id_interno
            JOIN Revistas r ON m.id_interno = r.id_interno
            WHERE m.id_interno = ?
            """;
        
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Revista revista = new Revista(
                    rs.getString("id_interno"),
                    rs.getString("titulo"),
                    rs.getInt("unidades_disponibles"),
                    rs.getString("editorial"),
                    rs.getString("periodicidad"),
                    rs.getDate("fecha_publicacion").toLocalDate()
                );
                return Optional.of(revista);
            }
        }
        return Optional.empty();
    }
    
    private Optional<Material> buscarCD(String idInterno) throws SQLException {
        String sql = """
            SELECT m.id_interno, m.titulo, m.unidades_disponibles,
                   ma.duracion, c.artista, c.numero_canciones, c.genero
            FROM Material m
            JOIN MaterialAudiovisual ma ON m.id_interno = ma.id_interno
            JOIN CDs c ON m.id_interno = c.id_interno
            WHERE m.id_interno = ?
            """;
        
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                CD cd = new CD(
                    rs.getString("id_interno"),
                    rs.getString("titulo"),
                    rs.getInt("unidades_disponibles"),
                    rs.getString("artista"),
                    rs.getString("genero"),
                    formatearDuracion(rs.getInt("duracion")),
                    rs.getInt("numero_canciones")
                );
                return Optional.of(cd);
            }
        }
        return Optional.empty();
    }
    
    private Optional<Material> buscarDVDPorId(String idInterno) throws SQLException {
        String sql = """
            SELECT m.id_interno, m.titulo, m.unidades_disponibles,
                   ma.duracion, d.director, d.genero
            FROM Material m
            JOIN MaterialAudiovisual ma ON m.id_interno = ma.id_interno
            JOIN DVDs d ON m.id_interno = d.id_interno
            WHERE m.id_interno = ?
            """;
        
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                DVD dvd = new DVD(
                    rs.getString("id_interno"),
                    rs.getString("titulo"),
                    rs.getInt("unidades_disponibles"),
                    rs.getString("director"),
                    formatearDuracion(rs.getInt("duracion")),
                    rs.getString("genero")
                );
                return Optional.of(dvd);
            }
        }
        return Optional.empty();
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    private String identificarTipo(String idInterno) {
        if (idInterno.startsWith("LIB")) return "LIB";
        if (idInterno.startsWith("REV")) return "REV";
        if (idInterno.startsWith("CDA")) return "CDA";
        if (idInterno.startsWith("DVD")) return "DVD";
        throw new IllegalArgumentException("Código interno inválido: " + idInterno);
    }
    
    private int parseDuracionAMinutos(String duracion) {
        String[] partes = duracion.split(":");
        if (partes.length >= 2) {
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);
            return (horas * 60) + minutos;
        }
        return 0;
    }
    
    private String formatearDuracion(int minutos) {
        int horas = minutos / 60;
        int mins = minutos % 60;
        return String.format("%02d:%02d", horas, mins);
    }
}