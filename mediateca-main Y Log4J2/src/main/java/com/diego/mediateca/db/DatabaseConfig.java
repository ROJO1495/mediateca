package com.diego.mediateca.db;

/**
 * Configuración centralizada de la base de datos
 */
public class DatabaseConfig {
    
    // Configuración de conexión
    public static final String DB_URL = "jdbc:mysql://localhost:3306/mediateca_db";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = ""; // Cambiar según tu configuración
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Configuración de pool de conexiones
    public static final int MAX_CONNECTIONS = 10;
    public static final int CONNECTION_TIMEOUT = 30000; // 30 segundos
    
    // NOTA: Ahora usamos el script SQL completo de Persona 1
    // Ya no necesitamos estos CREATE TABLE aquí
    // El script completo está en el archivo SQL separado
    
    // Mensaje informativo
    public static final String INFO_SCRIPT = """
        Las tablas ahora siguen la jerarquía de herencia:
        - Material (tabla base)
        - MaterialEscrito y MaterialAudiovisual (intermedias)
        - libros, revistas, cds, dvds (finales)
        
        Ejecutar el script SQL completo antes de usar la aplicación.
        """;
    
    private DatabaseConfig() {
        // Constructor privado para evitar instanciación
    }
}
