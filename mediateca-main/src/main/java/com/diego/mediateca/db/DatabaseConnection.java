package com.diego.mediateca.db;

import com.diego.mediateca.domain.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mediateca_db";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "tu_contrasenia";

    private DatabaseConnection() {
        try {
            Class.forName(DB_DRIVER);
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ Conexión a base de datos establecida exitosamente");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: Driver de MySQL no encontrado", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conexión: " + e.getMessage(), e);
        }
        return connection;
    }

    // ✅ NUEVO: Método para buscar material por ID
    public Optional<Material> buscarPorId(String id) {
        String tipo = id.substring(0, 3);
        String sql = "";

        switch (tipo) {
            case "LIB": sql = "SELECT * FROM libros WHERE id_interno = ?"; break;
            case "REV": sql = "SELECT * FROM revistas WHERE id_interno = ?"; break;
            case "DVD": sql = "SELECT * FROM dvds WHERE id_interno = ?"; break;
            case "CDA": sql = "SELECT * FROM cds WHERE id_interno = ?"; break;
            default: return Optional.empty();
        }

        try (var conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                Material material = crearMaterialDesdeResultSet(rs, tipo);
                return Optional.ofNullable(material);
            }
        } catch (Exception e) {
            System.err.println("Error buscando material: " + e.getMessage());
        }

        return Optional.empty();
    }

    // ✅ NUEVO: Método para modificar unidades de libro
    public void modificarUnidadesLibro(String id, int nuevasUnidades) {
        String sql = "UPDATE libros SET unidades_disponibles = ? WHERE id_interno = ?";

        try (var conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuevasUnidades);
            stmt.setString(2, id);
            int filas = stmt.executeUpdate();
            System.out.println("✓ Unidades de libro actualizadas. Filas afectadas: " + filas);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar unidades del libro: " + e.getMessage(), e);
        }
    }

    // ✅ NUEVO: Método para modificar unidades de revista
    public void modificarUnidadesRevista(String id, int nuevasUnidades) {
        String sql = "UPDATE revistas SET unidades_disponibles = ? WHERE id_interno = ?";

        try (var conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuevasUnidades);
            stmt.setString(2, id);
            int filas = stmt.executeUpdate();
            System.out.println("✓ Unidades de revista actualizadas. Filas afectadas: " + filas);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar unidades de revista: " + e.getMessage(), e);
        }
    }

    // Método auxiliar para crear objetos Material
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
                        rs.getInt("anio_publicacion"),
                        rs.getInt("numero_paginas")
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

    // Los demás métodos de DatabaseConnection (testConnection, closeConnection, etc.)
    public boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            System.out.println("Conectado a: " + metaData.getDatabaseProductName() +
                    " versión " + metaData.getDatabaseProductVersion());
            return true;
        } catch (SQLException e) {
            System.err.println("Error en test de conexión: " + e.getMessage());
            return false;
        }
    }
}
