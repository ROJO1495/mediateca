package com.diego.mediateca.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import com.diego.mediateca.domain.CD;
import com.diego.mediateca.domain.DVD;
import com.diego.mediateca.domain.Libro;
import com.diego.mediateca.domain.Material;
import com.diego.mediateca.domain.Revista;

/**
 * DAO para operaciones CRUD de materiales
 */
public class MaterialDAO {

    private final DatabaseConnection dbConnection;

    public MaterialDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    // MODIFICACIÓN
    public boolean modificarLibro(String idInterno, int unidadesDisponibles) throws SQLException {
        String sql = "UPDATE libros SET unidades_disponibles = ? WHERE id_interno = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, unidadesDisponibles);
            ps.setString(2, idInterno);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean modificarRevista(String idInterno, int unidadesDisponibles) throws SQLException {
        String sql = "UPDATE revistas SET unidades_disponibles = ? WHERE id_interno = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, unidadesDisponibles);
            ps.setString(2, idInterno);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean modificarDVD(String idInterno, String director, String duracion,
                                String genero, int unidadesDisponibles) throws SQLException {
        String sql = "UPDATE dvds SET " +
                "director = ?, duracion = ?, genero = ?, unidades_disponibles = ? " +
                "WHERE id_interno = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, director);
            ps.setString(2, duracion);
            ps.setString(3, genero);
            ps.setInt(4, unidadesDisponibles);
            ps.setString(5, idInterno);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean modificarCD(String idInterno, String artista, String genero,
                               String duracion, int numeroCanciones, int unidadesDisponibles)
            throws SQLException {
        String sql = "UPDATE cds SET " +
                "artista = ?, genero = ?, duracion = ?, numero_canciones = ?, unidades_disponibles = ? " +
                "WHERE id_interno = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, artista);
            ps.setString(2, genero);
            ps.setString(3, duracion);
            ps.setInt(4, numeroCanciones);
            ps.setInt(5, unidadesDisponibles);
            ps.setString(6, idInterno);
            return ps.executeUpdate() > 0;
        }
    }

    // INSERCIÓN
    public boolean insertarLibro(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros " +
                "(id_interno, titulo, unidades_disponibles, autor, editorial, isbn, numero_paginas, anio_publicacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, libro.getIdInterno());
            ps.setString(2, libro.getTitulo());
            ps.setInt(3, libro.getUnidadesDisponibles());
            ps.setString(4, libro.getAutor());
            ps.setString(5, libro.getEditorial());
            ps.setString(6, libro.getIsbn());
            ps.setInt(7, libro.getNumeroPaginas());
            ps.setInt(8, libro.getAnioPublicacion());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean insertarRevista(Revista revista) throws SQLException {
        String sql = "INSERT INTO revistas " +
                "(id_interno, titulo, unidades_disponibles, editorial, periodicidad, fecha_publicacion) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, revista.getIdInterno());
            ps.setString(2, revista.getTitulo());
            ps.setInt(3, revista.getUnidadesDisponibles());
            ps.setString(4, revista.getEditorial());
            ps.setString(5, revista.getPeriodicidad());
            ps.setDate(6, Date.valueOf(revista.getFechaPublicacion()));
            return ps.executeUpdate() > 0;
        }
    }

    public boolean insertarCD(CD cd) throws SQLException {
        String sql = "INSERT INTO cds " +
                "(id_interno, titulo, unidades_disponibles, artista, genero, duracion, numero_canciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, cd.getIdInterno());
            ps.setString(2, cd.getTitulo());
            ps.setInt(3, cd.getUnidadesDisponibles());
            ps.setString(4, cd.getArtista());
            ps.setString(5, cd.getGenero());
            ps.setString(6, cd.getDuracion());
            ps.setInt(7, cd.getNumeroCanciones());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean insertarDVD(DVD dvd) throws SQLException {
        String sql = "INSERT INTO dvds " +
                "(id_interno, titulo, unidades_disponibles, director, duracion, genero) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, dvd.getIdInterno());
            ps.setString(2, dvd.getTitulo());
            ps.setInt(3, dvd.getUnidadesDisponibles());
            ps.setString(4, dvd.getDirector());
            ps.setString(5, dvd.getDuracion());
            ps.setString(6, dvd.getGenero());
            return ps.executeUpdate() > 0;
        }
    }

    // LISTAR DISPONIBLES
    public List<Libro> listarLibrosDisponibles() throws SQLException {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE unidades_disponibles > 0";
        try (var c = dbConnection.getConnection();
             var ps = c.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Libro(
                        rs.getString("id_interno"),
                        rs.getString("titulo"),
                        rs.getInt("unidades_disponibles"),
                        rs.getString("autor"),
                        rs.getString("editorial"),
                        rs.getString("isbn"),
                        rs.getInt("numero_paginas"),
                        rs.getInt("anio_publicacion")
                ));
            }
        }
        return lista;
    }

    public List<Revista> listarRevistasDisponibles() throws SQLException {
        List<Revista> lista = new ArrayList<>();
        String sql = "SELECT * FROM revistas WHERE unidades_disponibles > 0";
        try (var c = dbConnection.getConnection();
             var ps = c.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Revista(
                        rs.getString("id_interno"),
                        rs.getString("titulo"),
                        rs.getInt("unidades_disponibles"),
                        rs.getString("editorial"),
                        rs.getString("periodicidad"),
                        rs.getDate("fecha_publicacion").toLocalDate()
                ));
            }
        }
        return lista;
    }

    public List<DVD> listarDVDsDisponibles() throws SQLException {
        List<DVD> lista = new ArrayList<>();
        String sql = "SELECT * FROM dvds WHERE unidades_disponibles > 0";
        try (var c = dbConnection.getConnection();
             var ps = c.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new DVD(
                        rs.getString("id_interno"),
                        rs.getString("titulo"),
                        rs.getInt("unidades_disponibles"),
                        rs.getString("director"),
                        rs.getString("duracion"),
                        rs.getString("genero")
                ));
            }
        }
        return lista;
    }

    public List<CD> listarCDsDisponibles() throws SQLException {
        List<CD> lista = new ArrayList<>();
        String sql = "SELECT * FROM cds WHERE unidades_disponibles > 0";
        try (var c = dbConnection.getConnection();
             var ps = c.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new CD(
                        rs.getString("id_interno"),
                        rs.getString("titulo"),
                        rs.getInt("unidades_disponibles"),
                        rs.getString("artista"),
                        rs.getString("genero"),
                        rs.getString("duracion"),
                        rs.getInt("numero_canciones")
                ));
            }
        }
        return lista;
    }

    // BÚSQUEDA POR ID
    public Optional<Material> buscarPorId(String idInterno) throws SQLException {
        String tipo = identificarTipo(idInterno);
        switch (tipo) {
            case "LIB": return buscarLibro(idInterno);
            case "REV": return buscarRevista(idInterno);
            case "CDA": return buscarCD(idInterno);
            case "DVD": return buscarDVDPorId(idInterno);
            default:     return Optional.empty();
        }
    }

    private Optional<Material> buscarLibro(String idInterno) throws SQLException {
        String sql = "SELECT * FROM libros WHERE id_interno = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Libro(
                            rs.getString("id_interno"),
                            rs.getString("titulo"),
                            rs.getInt("unidades_disponibles"),
                            rs.getString("autor"),
                            rs.getString("editorial"),
                            rs.getString("isbn"),
                            rs.getInt("numero_paginas"),
                            rs.getInt("anio_publicacion")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Material> buscarRevista(String idInterno) throws SQLException {
        String sql = "SELECT * FROM revistas WHERE id_interno = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Revista(
                            rs.getString("id_interno"),
                            rs.getString("titulo"),
                            rs.getInt("unidades_disponibles"),
                            rs.getString("editorial"),
                            rs.getString("periodicidad"),
                            rs.getDate("fecha_publicacion").toLocalDate()
                    ));
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Material> buscarCD(String idInterno) throws SQLException {
        String sql = "SELECT * FROM cds WHERE id_interno = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new CD(
                            rs.getString("id_interno"),
                            rs.getString("titulo"),
                            rs.getInt("unidades_disponibles"),
                            rs.getString("artista"),
                            rs.getString("genero"),
                            rs.getString("duracion"),
                            rs.getInt("numero_canciones")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Material> buscarDVDPorId(String idInterno) throws SQLException {
        String sql = "SELECT * FROM dvds WHERE id_interno = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new DVD(
                            rs.getString("id_interno"),
                            rs.getString("titulo"),
                            rs.getInt("unidades_disponibles"),
                            rs.getString("director"),
                            rs.getString("duracion"),
                            rs.getString("genero")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    // AUXILIARES
    private String identificarTipo(String idInterno) {
        if (idInterno.startsWith("LIB")) return "LIB";
        if (idInterno.startsWith("REV")) return "REV";
        if (idInterno.startsWith("CDA")) return "CDA";
        if (idInterno.startsWith("DVD")) return "DVD";
        throw new IllegalArgumentException("Código interno inválido: " + idInterno);
    }

    public boolean existeMaterial(String idInterno) throws SQLException {
        String tipo = identificarTipo(idInterno);
        String tabla;
        switch (tipo) {
            case "LIB": tabla = "libros"; break;
            case "REV": tabla = "revistas"; break;
            case "CDA": tabla = "cds"; break;
            case "DVD": tabla = "dvds"; break;
            default: throw new IllegalArgumentException("Tipo desconocido");
        }
        String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE id_interno = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}