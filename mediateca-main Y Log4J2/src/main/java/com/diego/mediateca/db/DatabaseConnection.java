package com.diego.mediateca.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * DatabaseConnection
 * - Integra Log4j2 (remueve System.out/err)
 * - isConnectionValid() es PUBLIC
 */
public class DatabaseConnection {

    private static final Logger log = LogManager.getLogger(DatabaseConnection.class);

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            log.debug("Cargando driver JDBC: {}", DatabaseConfig.DB_DRIVER);
            Class.forName(DatabaseConfig.DB_DRIVER);

            log.info("Intentando conectar a BD (url={}, user={})", DatabaseConfig.DB_URL, DatabaseConfig.DB_USER);
            this.connection = DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
            );

            log.info("✓ Conexión a base de datos establecida exitosamente");
            crearTablasIniciales();

        } catch (ClassNotFoundException e) {
            log.fatal("Driver JDBC no encontrado: {}", DatabaseConfig.DB_DRIVER, e);
            throw new RuntimeException("Error: Driver de MySQL no encontrado", e);
        } catch (SQLException e) {
            // Nunca loguear la contraseña
            log.error("Error al conectar con la base de datos (url={}, user={}): {}",
                    DatabaseConfig.DB_URL, DatabaseConfig.DB_USER, e.getMessage(), e);
            throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null || !isInstanceValid()) {
            log.debug("Creando nueva instancia de DatabaseConnection (o la previa no es válida)");
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                log.warn("Conexión nula o cerrada; reabriendo conexión (url={}, user={})",
                        DatabaseConfig.DB_URL, DatabaseConfig.DB_USER);
                connection = DriverManager.getConnection(
                    DatabaseConfig.DB_URL,
                    DatabaseConfig.DB_USER,
                    DatabaseConfig.DB_PASSWORD
                );
                log.info("Conexión reabierta correctamente");
            }
        } catch (SQLException e) {
            log.error("Error al obtener conexión: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener conexión: " + e.getMessage(), e);
        }
        return connection;
    }

    // PUBLIC para que AppPrincipal pueda llamarlo
    public boolean isConnectionValid() {
        try {
            boolean ok = connection != null && !connection.isClosed() && connection.isValid(2);
            log.debug("isConnectionValid(): {}", ok);
            return ok;
        } catch (SQLException e) {
            log.warn("isConnectionValid() lanzó excepción: {}", e.getMessage(), e);
            return false;
        }
    }

    private static boolean isInstanceValid() {
        try {
            boolean ok = instance != null &&
                         instance.connection != null &&
                         !instance.connection.isClosed() &&
                         instance.connection.isValid(2);
            return ok;
        } catch (SQLException e) {
            return false;
        }
    }

    private void crearTablasIniciales() {
        // Mensaje informativo (no ejecuta SQL aquí por diseño actual)
        log.warn("IMPORTANTE: asegúrate de haber ejecutado el script SQL del esquema actual (herencia de tablas).");
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                log.info("✓ Conexión a base de datos cerrada correctamente");
            }
        } catch (SQLException e) {
            log.warn("Error al cerrar conexión: {}", e.getMessage(), e);
        }
    }

    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            log.info("Conectado a: {} versión {} | Driver: {}",
                    metaData.getDatabaseProductName(),
                    metaData.getDatabaseProductVersion(),
                    metaData.getDriverName());
            return true;
        } catch (SQLException e) {
            log.error("Error en test de conexión: {}", e.getMessage(), e);
            return false;
        }
    }

    public static void cerrarRecursos(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            log.warn("Error al cerrar ResultSet: {}", e.getMessage(), e);
        }
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            log.warn("Error al cerrar PreparedStatement: {}", e.getMessage(), e);
        }
    }

    public static void cerrarStatement(Statement stmt) {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            log.warn("Error al cerrar Statement: {}", e.getMessage(), e);
        }
    }
}
