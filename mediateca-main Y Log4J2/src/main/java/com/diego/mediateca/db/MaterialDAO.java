package com.diego.mediateca.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.diego.mediateca.domain.CD;
import com.diego.mediateca.domain.DVD;
import com.diego.mediateca.domain.Libro;
import com.diego.mediateca.domain.Material;
import com.diego.mediateca.domain.Revista;

/**
 * MaterialDAO COMPLETO Y CORREGIDO
 * Incluye TODOS los métodos necesarios para AppPrincipal
 */
public class MaterialDAO {
    
    private final DatabaseConnection dbConnection;

    public MaterialDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    // ==================== MÉTODOS DE MODIFICACIÓN ====================
    
    public boolean modificarCD(String idInterno, String artista, String genero, 
                               String duracion, int numeroCanciones, int unidadesDisponibles) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            // 1. Actualizar Material
            String sqlMaterial = "UPDATE Material SET unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setInt(1, unidadesDisponibles);
                ps.setString(2, idInterno);
                ps.executeUpdate();
            }
            
            // 2. Actualizar cds
            String sqlCD = "UPDATE cds SET artista = ?, numero_canciones = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlCD)) {
                ps.setString(1, artista);
                ps.setInt(2, numeroCanciones);
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

    public boolean modificarDVD(String idInterno, String director, String duracion,
                                String genero, int unidadesDisponibles) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            // 1. Actualizar Material
            String sqlMaterial = "UPDATE Material SET unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setInt(1, unidadesDisponibles);
                ps.setString(2, idInterno);
                ps.executeUpdate();
            }
            
            // 2. Actualizar dvds
            String sqlDVD = "UPDATE dvds SET director = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDVD)) {
                ps.setString(1, director);
                ps.setString(2, idInterno);
                
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

    public boolean modificarLibro(String idInterno, int unidadesDisponibles) throws SQLException {
        String sql = "UPDATE Material SET unidades_disponibles = ? WHERE id_interno = ?";
        
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, unidadesDisponibles);
            ps.setString(2, idInterno);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    public boolean modificarRevista(String idInterno, int unidadesDisponibles) throws SQLException {
        String sql = "UPDATE Material SET unidades_disponibles = ? WHERE id_interno = ?";
        
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, unidadesDisponibles);
            ps.setString(2, idInterno);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // ==================== MÉTODOS DE INSERCIÓN ====================
    
    public boolean insertarLibro(Libro libro) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            // 1. Material
            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getTitulo());
                ps.setInt(3, libro.getUnidadesDisponibles());
                ps.executeUpdate();
            }
            
            // 2. MaterialEscrito
            String sqlEscrito = "INSERT INTO MaterialEscrito (id_interno, editorial) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getEditorial());
                ps.executeUpdate();
            }
            
            // 3. libros
            String sqlLibro = "INSERT INTO libros (id_interno, autor, numero_paginas, anio_publicacion) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlLibro)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getAutor());
                ps.setInt(3, libro.getNumeroPaginas());
                ps.setInt(4, libro.getAnioPublicacion());
                
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
            
            // 1. Material
            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, revista.getIdInterno());
                ps.setString(2, revista.getTitulo());
                ps.setInt(3, revista.getUnidadesDisponibles());
                ps.executeUpdate();
            }
            
            // 2. MaterialEscrito
            String sqlEscrito = "INSERT INTO MaterialEscrito (id_interno, editorial) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, revista.getIdInterno());
                ps.setString(2, revista.getEditorial());
                ps.executeUpdate();
            }
            
            // 3. revistas
            String sqlRevista = "INSERT INTO revistas (id_interno, fecha_publicacion, periodicidad) VALUES (?, ?, ?)";
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
            
            // 1. Material
            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, cd.getIdInterno());
                ps.setString(2, cd.getTitulo());
                ps.setInt(3, cd.getUnidadesDisponibles());
                ps.executeUpdate();
            }
            
            // 2. MaterialAudiovisual
            String sqlAudio = "INSERT INTO MaterialAudiovisual (id_interno, duracion) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudio)) {
                ps.setString(1, cd.getIdInterno());
                ps.setInt(2, parseDuracionAMinutos(cd.getDuracion()));
                ps.executeUpdate();
            }
            
            // 3. cds
            String sqlCD = "INSERT INTO cds (id_interno, artista, numero_canciones) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlCD)) {
                ps.setString(1, cd.getIdInterno());
                ps.setString(2, cd.getArtista());
                ps.setInt(3, cd.getNumeroCanciones());
                
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
            
            // 1. Material
            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, dvd.getIdInterno());
                ps.setString(2, dvd.getTitulo());
                ps.setInt(3, dvd.getUnidadesDisponibles());
                ps.executeUpdate();
            }
            
            // 2. MaterialAudiovisual
            String sqlAudio = "INSERT INTO MaterialAudiovisual (id_interno, duracion) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudio)) {
                ps.setString(1, dvd.getIdInterno());
                ps.setInt(2, parseDuracionAMinutos(dvd.getDuracion()));
                ps.executeUpdate();
            }
            
            // 3. dvds
            String sqlDVD = "INSERT INTO dvds (id_interno, director) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlDVD)) {
                ps.setString(1, dvd.getIdInterno());
                ps.setString(2, dvd.getDirector());
                
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

    // ==================== MÉTODOS DE LISTAR (NUEVOS) ====================
    
    public List<Libro> listarLibrosDisponibles() throws SQLException {
        String sql = """
            SELECT m.id_interno, m.titulo, m.unidades_disponibles,
                   me.editorial, l.autor, l.numero_paginas, l.anio_publicacion
            FROM Material m
            JOIN MaterialEscrito me ON m.id_interno = me.id_interno
            JOIN libros l ON me.id_interno = l.id_interno
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
                    "ISBN-AUTO",
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
            JOIN revistas r ON me.id_interno = r.id_interno
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
                   ma.duracion, c.artista, c.numero_canciones
            FROM Material m
            JOIN MaterialAudiovisual ma ON m.id_interno = ma.id_interno
            JOIN cds c ON ma.id_interno = c.id_interno
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
                    "Rock", // Género por defecto
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
                   ma.duracion, d.director
            FROM Material m
            JOIN MaterialAudiovisual ma ON m.id_interno = ma.id_interno
            JOIN dvds d ON ma.id_interno = d.id_interno
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
                    "Drama" // Género por defecto
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
        String sql = """
            SELECT m.id_interno, m.titulo, m.unidades_disponibles,
                   me.editorial, l.autor, l.numero_paginas, l.anio_publicacion
            FROM Material m
            JOIN MaterialEscrito me ON m.id_interno = me.id_interno
            JOIN libros l ON me.id_interno = l.id_interno
            WHERE m.id_interno = ?
            """;
        
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Libro libro = new Libro(
                    rs.getString("id_interno"),
                    rs.getString("titulo"),
                    rs.getInt("unidades_disponibles"),
                    rs.getString("autor"),
                    rs.getString("editorial"),
                    "ISBN-AUTO",
                    rs.getInt("anio_publicacion"),
                    rs.getInt("numero_paginas")
                );
                return Optional.of(libro);
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
            JOIN revistas r ON me.id_interno = r.id_interno
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
                   ma.duracion, c.artista, c.numero_canciones
            FROM Material m
            JOIN MaterialAudiovisual ma ON m.id_interno = ma.id_interno
            JOIN cds c ON ma.id_interno = c.id_interno
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
                    "Rock",
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
                   ma.duracion, d.director
            FROM Material m
            JOIN MaterialAudiovisual ma ON m.id_interno = ma.id_interno
            JOIN dvds d ON ma.id_interno = d.id_interno
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
                    "Drama"
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