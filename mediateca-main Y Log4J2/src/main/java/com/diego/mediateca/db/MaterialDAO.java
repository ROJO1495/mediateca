package com.diego.mediateca.db;

import com.diego.mediateca.domain.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MaterialDAO actualizado para trabajar con la nueva estructura de BD
 * que refleja la herencia: Material -> MaterialEscrito/MaterialAudiovisual -> Libros/Revistas/CDs/DVDs
 */
public class MaterialDAO {
    
    private final DatabaseConnection dbConnection;

    public MaterialDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    // MÉTODOS DE MODIFICACIÓN ACTUALIZADOS 
    
    /**
     * Modifica un CD 
     */
    public boolean modificarCD(String idInterno, String artista, String genero, 
                               int numeroCanciones, int unidadesDisponibles) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Actualizar tabla Material (campos comunes)
            String sqlMaterial = "UPDATE Material SET unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setInt(1, unidadesDisponibles);
                ps.setString(2, idInterno);
                ps.executeUpdate();
            }
            
            // 2. Actualizar tabla cds (campos específicos)
            String sqlCD = "UPDATE cds SET artista = ?, numero_canciones = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlCD)) {
                ps.setString(1, artista);
                ps.setInt(2, numeroCanciones);
                ps.setString(3, idInterno);
                
                int filasAfectadas = ps.executeUpdate();
                
                conn.commit(); // Confirmar transacción
                return filasAfectadas > 0;
            }
            
        } catch (SQLException e) {
            conn.rollback(); // Revertir si hay error
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * Modifica un DVD
     */
    public boolean modificarDVD(String idInterno, String director, 
                                int unidadesDisponibles) throws SQLException {
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

    /**
     * Modifica un Libro 
     */
    public boolean modificarLibro(String idInterno, int unidadesDisponibles) throws SQLException {
        // Los libros solo pueden modificar unidades (campos son final)
        String sql = "UPDATE Material SET unidades_disponibles = ? WHERE id_interno = ?";
        
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, unidadesDisponibles);
            ps.setString(2, idInterno);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    /**
     * Modifica una Revista 
     */
    public boolean modificarRevista(String idInterno, int unidadesDisponibles) throws SQLException {
        String sql = "UPDATE Material SET unidades_disponibles = ? WHERE id_interno = ?";
        
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, unidadesDisponibles);
            ps.setString(2, idInterno);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // MÉTODOS DE INSERCIÓN ACTUALIZADOS 
    
    /**
     * Inserta un Libro
     */
    public boolean insertarLibro(Libro libro) throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            // 1. Insertar en Material
            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getTitulo());
                ps.setInt(3, libro.getUnidadesDisponibles());
                ps.executeUpdate();
            }
            
            // 2. Insertar en MaterialEscrito
            String sqlEscrito = "INSERT INTO MaterialEscrito (id_interno, editorial) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getEditorial());
                ps.executeUpdate();
            }
            
            // 3. Insertar en libros
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

    /**
     * Inserta una Revista
     */
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

    /**
     * Inserta un CD
     */
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

    /**
     * Inserta un DVD 
     */
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

    //  MÉTODOS DE BÚSQUEDA 
    
    /**
     * Busca cualquier material por ID - ACTUALIZADO con JOINs
     */
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
                    rs.getString("autor"),
                    rs.getInt("numero_paginas"),
                    rs.getString("editorial"),
                    "ISBN-PENDIENTE", // Nota: ISBN no está en nueva BD
                    rs.getInt("anio_publicacion"),
                    rs.getInt("unidades_disponibles")
                );
                return Optional.of(libro);
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
                    "Genero-PENDIENTE", // Nota: género no está en nueva BD
                    formatearDuracion(rs.getInt("duracion")),
                    rs.getInt("numero_canciones")
                );
                return Optional.of(cd);
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

    /**
     * Convierte duración de formato "HH:MM:SS" a minutos totales
     */
    private int parseDuracionAMinutos(String duracion) {
        String[] partes = duracion.split(":");
        if (partes.length == 3) {
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);
            return (horas * 60) + minutos;
        } else if (partes.length == 2) {
            return Integer.parseInt(partes[0]);
        }
        return 0;
    }

    /**
     * Convierte minutos a formato "HH:MM:SS"
     */
    private String formatearDuracion(int minutos) {
        int horas = minutos / 60;
        int mins = minutos % 60;
        return String.format("%02d:%02d:00", horas, mins);
    }
}
