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
    
    // SQL Scripts para crear tablas
    public static final String CREATE_TABLE_LIBROS = """
        CREATE TABLE IF NOT EXISTS libros (
            id_interno VARCHAR(10) PRIMARY KEY,
            titulo VARCHAR(200) NOT NULL,
            autor VARCHAR(100) NOT NULL,
            numero_paginas INT NOT NULL,
            editorial VARCHAR(100) NOT NULL,
            isbn VARCHAR(20) NOT NULL UNIQUE,
            anio_publicacion INT NOT NULL,
            unidades_disponibles INT NOT NULL DEFAULT 0,
            fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        )
        """;
    
    public static final String CREATE_TABLE_REVISTAS = """
        CREATE TABLE IF NOT EXISTS revistas (
            id_interno VARCHAR(10) PRIMARY KEY,
            titulo VARCHAR(200) NOT NULL,
            editorial VARCHAR(100) NOT NULL,
            periodicidad VARCHAR(20) NOT NULL,
            fecha_publicacion DATE NOT NULL,
            unidades_disponibles INT NOT NULL DEFAULT 0,
            fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        )
        """;
    
    public static final String CREATE_TABLE_CDS = """
        CREATE TABLE IF NOT EXISTS cds (
            id_interno VARCHAR(10) PRIMARY KEY,
            titulo VARCHAR(200) NOT NULL,
            artista VARCHAR(100) NOT NULL,
            genero VARCHAR(50) NOT NULL,
            duracion VARCHAR(10) NOT NULL,
            numero_canciones INT NOT NULL,
            unidades_disponibles INT NOT NULL DEFAULT 0,
            fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        )
        """;
    
    public static final String CREATE_TABLE_DVDS = """
        CREATE TABLE IF NOT EXISTS dvds (
            id_interno VARCHAR(10) PRIMARY KEY,
            titulo VARCHAR(200) NOT NULL,
            director VARCHAR(100) NOT NULL,
            duracion VARCHAR(10) NOT NULL,
            genero VARCHAR(50) NOT NULL,
            unidades_disponibles INT NOT NULL DEFAULT 0,
            fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        )
        """;
    
    private DatabaseConfig() {
        // Constructor privado para evitar instanciación
    }
}