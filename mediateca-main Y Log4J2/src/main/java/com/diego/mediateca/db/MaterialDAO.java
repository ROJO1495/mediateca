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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MaterialDAO {

    private static final Logger log = LogManager.getLogger(MaterialDAO.class);

    private final DatabaseConnection dbConnection;

    public MaterialDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    // ==================== MÉTODOS DE MODIFICACIÓN ====================

    public boolean modificarLibro(String idInterno, String titulo, String autor,
                                  String editorial, String isbn, int anioPublicacion,
                                  int numeroPaginas, int unidadesDisponibles) throws SQLException {
        log.debug("modificarLibro() id={}, titulo={}, autor={}, anio={}, paginas={}, unidades={}",
                idInterno, titulo, autor, anioPublicacion, numeroPaginas, unidadesDisponibles);

        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            String sqlMaterial = "UPDATE Material SET titulo = ?, unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, titulo);
                ps.setInt(2, unidadesDisponibles);
                ps.setString(3, idInterno);
                int n = ps.executeUpdate();
                log.debug("UPDATE Material filas={}", n);
            }

            String sqlEscrito = "UPDATE MaterialEscrito SET editorial = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, editorial);
                ps.setString(2, idInterno);
                int n = ps.executeUpdate();
                log.debug("UPDATE MaterialEscrito filas={}", n);
            }

            String sqlLibro = "UPDATE Libros SET autor = ?, numero_paginas = ?, anio_publicacion = ?, isbn = ? WHERE id_interno = ?";
            int filasAfectadas;
            try (PreparedStatement ps = conn.prepareStatement(sqlLibro)) {
                ps.setString(1, autor);
                ps.setInt(2, numeroPaginas);
                ps.setInt(3, anioPublicacion);
                ps.setString(4, isbn);
                ps.setString(5, idInterno);
                filasAfectadas = ps.executeUpdate();
                log.debug("UPDATE Libros filas={}", filasAfectadas);
            }

            conn.commit();
            if (filasAfectadas == 0) {
                log.warn("modificarLibro() no actualizó ninguna fila para id={}", idInterno);
            } else {
                log.info("Libro modificado correctamente id={}", idInterno);
            }
            return filasAfectadas > 0;

        } catch (SQLException e) {
            log.error("Error modificarLibro() id={}: {}", idInterno, e.getMessage(), e);
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public boolean modificarRevista(String idInterno, String titulo, String editorial,
                                    String periodicidad, LocalDate fechaPublicacion,
                                    int unidadesDisponibles) throws SQLException {
        log.debug("modificarRevista() id={}, titulo={}, periodicidad={}, fecha={}, unidades={}",
                idInterno, titulo, periodicidad, fechaPublicacion, unidadesDisponibles);

        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            String sqlMaterial = "UPDATE Material SET titulo = ?, unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, titulo);
                ps.setInt(2, unidadesDisponibles);
                ps.setString(3, idInterno);
                int n = ps.executeUpdate();
                log.debug("UPDATE Material filas={}", n);
            }

            String sqlEscrito = "UPDATE MaterialEscrito SET editorial = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, editorial);
                ps.setString(2, idInterno);
                int n = ps.executeUpdate();
                log.debug("UPDATE MaterialEscrito filas={}", n);
            }

            String sqlRevista = "UPDATE Revistas SET periodicidad = ?, fecha_publicacion = ? WHERE id_interno = ?";
            int filasAfectadas;
            try (PreparedStatement ps = conn.prepareStatement(sqlRevista)) {
                ps.setString(1, periodicidad);
                ps.setDate(2, Date.valueOf(fechaPublicacion));
                ps.setString(3, idInterno);
                filasAfectadas = ps.executeUpdate();
                log.debug("UPDATE Revistas filas={}", filasAfectadas);
            }

            conn.commit();
            if (filasAfectadas == 0) {
                log.warn("modificarRevista() no actualizó ninguna fila para id={}", idInterno);
            } else {
                log.info("Revista modificada correctamente id={}", idInterno);
            }
            return filasAfectadas > 0;

        } catch (SQLException e) {
            log.error("Error modificarRevista() id={}: {}", idInterno, e.getMessage(), e);
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public boolean modificarDVD(String idInterno, String titulo, String director,
                                String duracion, String genero, int unidadesDisponibles) throws SQLException {
        log.debug("modificarDVD() id={}, titulo={}, director={}, duracion={}, genero={}, unidades={}",
                idInterno, titulo, director, duracion, genero, unidadesDisponibles);

        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            String sqlMaterial = "UPDATE Material SET titulo = ?, unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, titulo);
                ps.setInt(2, unidadesDisponibles);
                ps.setString(3, idInterno);
                int n = ps.executeUpdate();
                log.debug("UPDATE Material filas={}", n);
            }

            String sqlAudio = "UPDATE MaterialAudiovisual SET duracion = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudio)) {
                ps.setInt(1, parseDuracionAMinutos(duracion));
                ps.setString(2, idInterno);
                int n = ps.executeUpdate();
                log.debug("UPDATE MaterialAudiovisual filas={}", n);
            }

            String sqlDVD = "UPDATE DVDs SET director = ?, genero = ? WHERE id_interno = ?";
            int filasAfectadas;
            try (PreparedStatement ps = conn.prepareStatement(sqlDVD)) {
                ps.setString(1, director);
                ps.setString(2, genero);
                ps.setString(3, idInterno);
                filasAfectadas = ps.executeUpdate();
                log.debug("UPDATE DVDs filas={}", filasAfectadas);
            }

            conn.commit();
            if (filasAfectadas == 0) {
                log.warn("modificarDVD() no actualizó ninguna fila para id={}", idInterno);
            } else {
                log.info("DVD modificado correctamente id={}", idInterno);
            }
            return filasAfectadas > 0;

        } catch (SQLException e) {
            log.error("Error modificarDVD() id={}: {}", idInterno, e.getMessage(), e);
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public boolean modificarCD(String idInterno, String titulo, String artista,
                               String genero, String duracion, int numeroCanciones,
                               int unidadesDisponibles) throws SQLException {
        log.debug("modificarCD() id={}, titulo={}, artista={}, genero={}, duracion={}, canciones={}, unidades={}",
                idInterno, titulo, artista, genero, duracion, numeroCanciones, unidadesDisponibles);

        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            String sqlMaterial = "UPDATE Material SET titulo = ?, unidades_disponibles = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, titulo);
                ps.setInt(2, unidadesDisponibles);
                ps.setString(3, idInterno);
                int n = ps.executeUpdate();
                log.debug("UPDATE Material filas={}", n);
            }

            String sqlAudio = "UPDATE MaterialAudiovisual SET duracion = ? WHERE id_interno = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudio)) {
                ps.setInt(1, parseDuracionAMinutos(duracion));
                ps.setString(2, idInterno);
                int n = ps.executeUpdate();
                log.debug("UPDATE MaterialAudiovisual filas={}", n);
            }

            String sqlCD = "UPDATE CDs SET artista = ?, numero_canciones = ?, genero = ? WHERE id_interno = ?";
            int filasAfectadas;
            try (PreparedStatement ps = conn.prepareStatement(sqlCD)) {
                ps.setString(1, artista);
                ps.setInt(2, numeroCanciones);
                ps.setString(3, genero);
                ps.setString(4, idInterno);
                filasAfectadas = ps.executeUpdate();
                log.debug("UPDATE CDs filas={}", filasAfectadas);
            }

            conn.commit();
            if (filasAfectadas == 0) {
                log.warn("modificarCD() no actualizó ninguna fila para id={}", idInterno);
            } else {
                log.info("CD modificado correctamente id={}", idInterno);
            }
            return filasAfectadas > 0;

        } catch (SQLException e) {
            log.error("Error modificarCD() id={}: {}", idInterno, e.getMessage(), e);
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // ==================== MÉTODOS DE INSERCIÓN ====================

    public boolean insertarLibro(Libro libro) throws SQLException {
        log.debug("insertarLibro() id={}, titulo={}, autor={}, anio={}, paginas={}, unidades={}",
                libro.getIdInterno(), libro.getTitulo(), libro.getAutor(),
                libro.getAnioPublicacion(), libro.getNumeroPaginas(), libro.getUnidadesDisponibles());

        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getTitulo());
                ps.setInt(3, libro.getUnidadesDisponibles());
                int n = ps.executeUpdate();
                log.debug("INSERT Material filas={}", n);
            }

            String sqlEscrito = "INSERT INTO MaterialEscrito (id_interno, editorial) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getEditorial());
                int n = ps.executeUpdate();
                log.debug("INSERT MaterialEscrito filas={}", n);
            }

            String sqlLibro = "INSERT INTO Libros (id_interno, autor, numero_paginas, anio_publicacion, isbn) VALUES (?, ?, ?, ?, ?)";
            int filasAfectadas;
            try (PreparedStatement ps = conn.prepareStatement(sqlLibro)) {
                ps.setString(1, libro.getIdInterno());
                ps.setString(2, libro.getAutor());
                ps.setInt(3, libro.getNumeroPaginas());
                ps.setInt(4, libro.getAnioPublicacion());
                ps.setString(5, libro.getIsbn());

                filasAfectadas = ps.executeUpdate();
                log.debug("INSERT Libros filas={}", filasAfectadas);
            }

            conn.commit();
            if (filasAfectadas == 0) {
                log.warn("insertarLibro() no insertó filas para id={}", libro.getIdInterno());
            } else {
                log.info("Libro insertado correctamente id={}", libro.getIdInterno());
            }
            return filasAfectadas > 0;

        } catch (SQLException e) {
            log.error("Error insertarLibro() id={}: {}", libro.getIdInterno(), e.getMessage(), e);
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public boolean insertarRevista(Revista revista) throws SQLException {
        log.debug("insertarRevista() id={}, titulo={}, periodicidad={}, fecha={}, unidades={}",
                revista.getIdInterno(), revista.getTitulo(), revista.getPeriodicidad(),
                revista.getFechaPublicacion(), revista.getUnidadesDisponibles());

        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, revista.getIdInterno());
                ps.setString(2, revista.getTitulo());
                ps.setInt(3, revista.getUnidadesDisponibles());
                int n = ps.executeUpdate();
                log.debug("INSERT Material filas={}", n);
            }

            String sqlEscrito = "INSERT INTO MaterialEscrito (id_interno, editorial) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEscrito)) {
                ps.setString(1, revista.getIdInterno());
                ps.setString(2, revista.getEditorial());
                int n = ps.executeUpdate();
                log.debug("INSERT MaterialEscrito filas={}", n);
            }

            String sqlRevista = "INSERT INTO Revistas (id_interno, fecha_publicacion, periodicidad) VALUES (?, ?, ?)";
            int filasAfectadas;
            try (PreparedStatement ps = conn.prepareStatement(sqlRevista)) {
                ps.setString(1, revista.getIdInterno());
                ps.setDate(2, Date.valueOf(revista.getFechaPublicacion()));
                ps.setString(3, revista.getPeriodicidad());

                filasAfectadas = ps.executeUpdate();
                log.debug("INSERT Revistas filas={}", filasAfectadas);
            }

            conn.commit();
            if (filasAfectadas == 0) {
                log.warn("insertarRevista() no insertó filas para id={}", revista.getIdInterno());
            } else {
                log.info("Revista insertada correctamente id={}", revista.getIdInterno());
            }
            return filasAfectadas > 0;

        } catch (SQLException e) {
            log.error("Error insertarRevista() id={}: {}", revista.getIdInterno(), e.getMessage(), e);
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public boolean insertarCD(CD cd) throws SQLException {
        log.debug("insertarCD() id={}, titulo={}, artista={}, genero={}, duracion={}, canciones={}, unidades={}",
                cd.getIdInterno(), cd.getTitulo(), cd.getArtista(), cd.getGenero(),
                cd.getDuracion(), cd.getNumeroCanciones(), cd.getUnidadesDisponibles());

        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, cd.getIdInterno());
                ps.setString(2, cd.getTitulo());
                ps.setInt(3, cd.getUnidadesDisponibles());
                int n = ps.executeUpdate();
                log.debug("INSERT Material filas={}", n);
            }

            String sqlAudio = "INSERT INTO MaterialAudiovisual (id_interno, duracion) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudio)) {
                ps.setString(1, cd.getIdInterno());
                ps.setInt(2, parseDuracionAMinutos(cd.getDuracion()));
                int n = ps.executeUpdate();
                log.debug("INSERT MaterialAudiovisual filas={}", n);
            }

            String sqlCD = "INSERT INTO CDs (id_interno, artista, numero_canciones, genero) VALUES (?, ?, ?, ?)";
            int filasAfectadas;
            try (PreparedStatement ps = conn.prepareStatement(sqlCD)) {
                ps.setString(1, cd.getIdInterno());
                ps.setString(2, cd.getArtista());
                ps.setInt(3, cd.getNumeroCanciones());
                ps.setString(4, cd.getGenero());

                filasAfectadas = ps.executeUpdate();
                log.debug("INSERT CDs filas={}", filasAfectadas);
            }

            conn.commit();
            if (filasAfectadas == 0) {
                log.warn("insertarCD() no insertó filas para id={}", cd.getIdInterno());
            } else {
                log.info("CD insertado correctamente id={}", cd.getIdInterno());
            }
            return filasAfectadas > 0;

        } catch (SQLException e) {
            log.error("Error insertarCD() id={}: {}", cd.getIdInterno(), e.getMessage(), e);
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public boolean insertarDVD(DVD dvd) throws SQLException {
        log.debug("insertarDVD() id={}, titulo={}, director={}, genero={}, duracion={}, unidades={}",
                dvd.getIdInterno(), dvd.getTitulo(), dvd.getDirector(), dvd.getGenero(),
                dvd.getDuracion(), dvd.getUnidadesDisponibles());

        Connection conn = dbConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            String sqlMaterial = "INSERT INTO Material (id_interno, titulo, unidades_disponibles) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                ps.setString(1, dvd.getIdInterno());
                ps.setString(2, dvd.getTitulo());
                ps.setInt(3, dvd.getUnidadesDisponibles());
                int n = ps.executeUpdate();
                log.debug("INSERT Material filas={}", n);
            }

            String sqlAudio = "INSERT INTO MaterialAudiovisual (id_interno, duracion) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAudio)) {
                ps.setString(1, dvd.getIdInterno());
                ps.setInt(2, parseDuracionAMinutos(dvd.getDuracion()));
                int n = ps.executeUpdate();
                log.debug("INSERT MaterialAudiovisual filas={}", n);
            }

            String sqlDVD = "INSERT INTO DVDs (id_interno, director, genero) VALUES (?, ?, ?)";
            int filasAfectadas;
            try (PreparedStatement ps = conn.prepareStatement(sqlDVD)) {
                ps.setString(1, dvd.getIdInterno());
                ps.setString(2, dvd.getDirector());
                ps.setString(3, dvd.getGenero());

                filasAfectadas = ps.executeUpdate();
                log.debug("INSERT DVDs filas={}", filasAfectadas);
            }

            conn.commit();
            if (filasAfectadas == 0) {
                log.warn("insertarDVD() no insertó filas para id={}", dvd.getIdInterno());
            } else {
                log.info("DVD insertado correctamente id={}", dvd.getIdInterno());
            }
            return filasAfectadas > 0;

        } catch (SQLException e) {
            log.error("Error insertarDVD() id={}: {}", dvd.getIdInterno(), e.getMessage(), e);
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
        log.debug("listarLibrosDisponibles() ejecutando consulta");

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
            log.info("listarLibrosDisponibles() resultados={}", libros.size());
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
        log.debug("listarRevistasDisponibles() ejecutando consulta");

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
            log.info("listarRevistasDisponibles() resultados={}", revistas.size());
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
        log.debug("listarCDsDisponibles() ejecutando consulta");

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
            log.info("listarCDsDisponibles() resultados={}", cds.size());
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
        log.debug("listarDVDsDisponibles() ejecutando consulta");

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
            log.info("listarDVDsDisponibles() resultados={}", dvds.size());
        }
        return dvds;
    }

    // ==================== MÉTODOS DE BÚSQUEDA ====================

    public Optional<Material> buscarPorId(String idInterno) throws SQLException {
        String tipo = identificarTipo(idInterno);
        log.debug("buscarPorId() id={}, tipo={}", idInterno, tipo);

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
            boolean existe = rs.next() && rs.getInt(1) > 0;
            log.debug("existeMaterial() id={} -> {}", idInterno, existe);
            return existe;
        }
    }

    private Optional<Material> buscarLibro(String idInterno) throws SQLException {
        String sql =
            "SELECT m.id_interno, m.titulo, m.unidades_disponibles, me.editorial, " +
            "       l.autor, l.numero_paginas, l.anio_publicacion, l.isbn " +
            "FROM Material m " +
            "JOIN MaterialEscrito me ON m.id_interno = me.id_interno " +
            "JOIN Libros l ON m.id_interno = l.id_interno " +
            "WHERE m.id_interno = ?";
        log.trace("buscarLibro() SQL: {}", sql);

        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
                    log.debug("buscarLibro() encontrado id={}", idInterno);
                    return Optional.of(libro);
                }
            }
        }
        log.debug("buscarLibro() no encontrado id={}", idInterno);
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
        log.trace("buscarRevista() SQL: {}", sql);

        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Revista revista = new Revista(
                        rs.getString("id_interno"),
                        rs.getString("titulo"),
                        rs.getInt("unidades_disponibles"),
                        rs.getString("editorial"),
                        rs.getString("periodicidad"),
                        rs.getDate("fecha_publicacion").toLocalDate()
                    );
                    log.debug("buscarRevista() encontrada id={}", idInterno);
                    return Optional.of(revista);
                }
            }
        }
        log.debug("buscarRevista() no encontrada id={}", idInterno);
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
        log.trace("buscarCD() SQL: {}", sql);

        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            try (ResultSet rs = ps.executeQuery()) {
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
                    log.debug("buscarCD() encontrado id={}", idInterno);
                    return Optional.of(cd);
                }
            }
        }
        log.debug("buscarCD() no encontrado id={}", idInterno);
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
        log.trace("buscarDVDPorId() SQL: {}", sql);

        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idInterno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DVD dvd = new DVD(
                        rs.getString("id_interno"),
                        rs.getString("titulo"),
                        rs.getInt("unidades_disponibles"),
                        rs.getString("director"),
                        formatearDuracion(rs.getInt("duracion")),
                        rs.getString("genero")
                    );
                    log.debug("buscarDVDPorId() encontrado id={}", idInterno);
                    return Optional.of(dvd);
                }
            }
        }
        log.debug("buscarDVDPorId() no encontrado id={}", idInterno);
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
