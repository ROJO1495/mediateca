package com.diego.mediateca.db;

import com.diego.mediateca.domain.*;

import java.sql.*;
import java.util.Optional;

public class DatabaseConnection {

    private static final org.apache.logging.log4j.Logger log =
            org.apache.logging.log4j.LogManager.getLogger(DatabaseConnection.class);

    private static DatabaseConnection instance;
    private Connection connection;

    private static final String DB_DRIVER   = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/mediateca_db";
    private static final String DB_USER     = "user";
    private static final String DB_PASSWORD = "tu_contrasenia";

    private DatabaseConnection() {
        try {
            log.debug("Cargando driver: {}", DB_DRIVER);
            Class.forName(DB_DRIVER);
            long t0 = System.nanoTime();
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            long ms = (System.nanoTime() - t0) / 1_000_000;
            log.info("Conexión a BD abierta en {} ms | url={} | user={}", ms, DB_URL, DB_USER);
        } catch (ClassNotFoundException e) {
            log.error("Driver JDBC no encontrado: {}", DB_DRIVER, e);
            throw new RuntimeException("Error: Driver de MySQL no encontrado", e);
        } catch (SQLException e) {
            log.error("Error al conectar con la base de datos (url={}, user={})", DB_URL, DB_USER, e);
            throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                log.warn("Conexión inválida o cerrada. Reabriendo conexión...");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            log.error("Error al obtener conexión (reconectar)", e);
            throw new RuntimeException("Error al obtener conexión: " + e.getMessage(), e);
        }
        return connection;
    }

    // ===== Métodos auxiliares que usa AppPrincipal =====

    public Optional<Material> buscarPorId(String id) {
        String tipo = id.substring(0, 3);
        String sql;
        switch (tipo) {
            case "LIB": sql = "SELECT * FROM libros WHERE id_interno = ?";   break;
            case "REV": sql = "SELECT * FROM revistas WHERE id_interno = ?"; break;
            case "DVD": sql = "SELECT * FROM dvds WHERE id_interno = ?";     break;
            case "CDA": sql = "SELECT * FROM cds WHERE id_interno = ?";      break;
            default:    return Optional.empty();
        }

        log.debug("buscarPorId SQL: {} | id={}", sql, id);
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                Material m = crearMaterialDesdeResultSet(rs, tipo);
                log.info("buscarPorId OK (id={}, tipo={})", id, tipo);
                return Optional.ofNullable(m);
            }
        } catch (Exception e) {
            log.error("Error buscando material (id={})", id, e);
        }
        return Optional.empty();
    }

    public void modificarUnidadesLibro(String id, int nuevasUnidades) {
        final String sql = "UPDATE libros SET unidades_disponibles = ? WHERE id_interno = ?";
        log.debug("SQL: {} | id={} | unidades={}", sql, id, nuevasUnidades);
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nuevasUnidades);
            stmt.setString(2, id);
            int filas = stmt.executeUpdate();
            log.info("Unidades de libro actualizadas (id={}, filas={})", id, filas);
        } catch (Exception e) {
            log.error("Error al actualizar unidades del libro (id={})", id, e);
            throw new RuntimeException("Error al actualizar unidades del libro: " + e.getMessage(), e);
        }
    }

    public void modificarUnidadesRevista(String id, int nuevasUnidades) {
        final String sql = "UPDATE revistas SET unidades_disponibles = ? WHERE id_interno = ?";
        log.debug("SQL: {} | id={} | unidades={}", sql, id, nuevasUnidades);
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nuevasUnidades);
            stmt.setString(2, id);
            int filas = stmt.executeUpdate();
            log.info("Unidades de revista actualizadas (id={}, filas={})", id, filas);
        } catch (Exception e) {
            log.error("Error al actualizar unidades de revista (id={})", id, e);
            throw new RuntimeException("Error al actualizar unidades de revista: " + e.getMessage(), e);
        }
    }

    private Material crearMaterialDesdeResultSet(ResultSet rs, String tipo) throws Exception {
        switch (tipo) {
            case "LIB":
                return new Libro(
                        rs.getString("id_interno"),
                        rs.getString("titulo"),
                        rs.getInt("unidades_disponibles"),
                        rs.getString("autor"),
                        rs.getString("editorial"),
                        rs.getString("isbn"),
                        rs.getInt("numero_paginas"),
                        rs.getInt("anio_publicacion")
                );
            case "REV":
                return new Revista(
                        rs.getString("id_interno"),
                        rs.getString("titulo"),
                        rs.getInt("unidades_disponibles"),
                        rs.getString("editorial"),
                        rs.getString("periodicidad"),
                        rs.getDate("fecha_publicacion").toLocalDate()
                );
            case "DVD":
                return new DVD(
                        rs.getString("id_interno"),
                        rs.getString("titulo"),
                        rs.getInt("unidades_disponibles"),
                        rs.getString("director"),
                        rs.getString("duracion"),
                        rs.getString("genero")
                );
            case "CDA":
                return new CD(
                        rs.getString("id_interno"),
                        rs.getString("titulo"),
                        rs.getInt("unidades_disponibles"),
                        rs.getString("artista"),
                        rs.getString("genero"),
                        rs.getString("duracion"),
                        rs.getInt("numero_canciones")
                );
            default:
                return null;
        }
    }

    // ===== Utilidades de conexión =====

    public boolean isConnectionValid() {
        try {
            boolean ok = connection != null && !connection.isClosed() && connection.isValid(2);
            log.debug("isConnectionValid -> {}", ok);
            return ok;
        } catch (SQLException e) {
            log.error("Error en isConnectionValid()", e);
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                log.info("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            log.error("Error al cerrar conexión", e);
        }
    }

    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            DatabaseMetaData meta = conn.getMetaData();
            log.info("Conectado a {} v{}", meta.getDatabaseProductName(), meta.getDatabaseProductVersion());
            return true;
        } catch (SQLException e) {
            log.error("Error en test de conexión", e);
            return false;
        }
    }
}
