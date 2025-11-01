package com.diego.mediateca.db;

import java.sql.*;

/**
 * Clase para gestionar la conexión a la base de datos MySQL
 */
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            // Cargar el driver de MySQL
            Class.forName(DatabaseConfig.DB_DRIVER);
            
            // Establecer conexión
            this.connection = DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
            );
            
            System.out.println("✓ Conexión a base de datos establecida exitosamente");
            
            // Crear las tablas si no existen
            crearTablasIniciales();
            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: Driver de MySQL no encontrado", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene la instancia única de DatabaseConnection (Patrón Singleton)
     */
    public static DatabaseConnection getInstance() {
        if (instance == null || !isConnectionValid()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Obtiene la conexión a la base de datos
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Reconectar si la conexión está cerrada
                connection = DriverManager.getConnection(
                    DatabaseConfig.DB_URL,
                    DatabaseConfig.DB_USER,
                    DatabaseConfig.DB_PASSWORD
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conexión: " + e.getMessage(), e);
        }
        return connection;
    }

    /**
     * Verifica si la conexión es válida
     */
    private static boolean isConnectionValid() {
        try {
            return instance != null && 
                   instance.connection != null && 
                   !instance.connection.isClosed() &&
                   instance.connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * NOTA: Las tablas ya no se crean aquí automáticamente
     * Ahora se debe ejecutar el script SQL completo manualmente
     */
    private void crearTablasIniciales() {
        System.out.println("IMPORTANTE: haber ejecutado el script SQL completo");
        System.out.println("    El nuevo esquema usa herencia de tablas");
        System.out.println("    Ejecuta: script_mediateca_herencia.sql");
    }

    /**
     * Cierra la conexión a la base de datos
     */
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

    /**
     * Ejecuta una consulta de prueba para verificar la conexión
     */
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

    /**
     * Método auxiliar para cerrar recursos JDBC
     */
    public static void cerrarRecursos(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar ResultSet: " + e.getMessage());
        }
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para cerrar Statement
     */
    public static void cerrarStatement(Statement stmt) {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar Statement: " + e.getMessage());
        }
    }
}
