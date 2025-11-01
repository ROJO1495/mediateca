package com.diego.mediateca.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseConnection 
 * Ahora isConnectionValid() es PUBLIC
 */
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName(DatabaseConfig.DB_DRIVER);
            
            this.connection = DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
            );
            
            System.out.println("✓ Conexión a base de datos establecida exitosamente");
            crearTablasIniciales();
            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: Driver de MySQL no encontrado", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null || !isInstanceValid()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
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

    //  PUBLIC PARA QUE AppPrincipal PUEDA LLAMARLO
    public boolean isConnectionValid() {
        try {
            return connection != null && 
                   !connection.isClosed() &&
                   connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    private static boolean isInstanceValid() {
        try {
            return instance != null && 
                   instance.connection != null && 
                   !instance.connection.isClosed() &&
                   instance.connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    private void crearTablasIniciales() {
        System.out.println("⚠️  IMPORTANTE: Asegúrate de haber ejecutado el script SQL completo");
        System.out.println("    El nuevo esquema usa herencia de tablas");
        System.out.println("    Ejecuta: script_mediateca.sql");
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

    public static void cerrarStatement(Statement stmt) {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar Statement: " + e.getMessage());
        }
    }
}