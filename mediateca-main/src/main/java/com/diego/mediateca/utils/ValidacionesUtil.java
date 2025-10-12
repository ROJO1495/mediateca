package com.diego.mediateca.utils;

import java.time.LocalDate;
import java.time.Year;

/**
 * Clase utilitaria para validaciones de datos de la mediateca
 */
public class ValidacionesUtil {

    private ValidacionesUtil() {
        // Constructor privado para evitar instanciación
    }

    // ============ VALIDACIONES GENERALES ============
    
    public static void validarCampoNoVacio(String campo, String nombreCampo) {
        if (campo == null || campo.trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " no puede estar vacío");
        }
    }

    public static void validarNumeroPositivo(int numero, String nombreCampo) {
        if (numero <= 0) {
            throw new IllegalArgumentException(nombreCampo + " debe ser mayor a 0");
        }
    }

    public static void validarNumeroNoNegativo(int numero, String nombreCampo) {
        if (numero < 0) {
            throw new IllegalArgumentException(nombreCampo + " no puede ser negativo");
        }
    }

    // ============ VALIDACIONES DE CÓDIGOS INTERNOS ============
    
    public static void validarCodigoLibro(String codigo) {
        if (!codigo.matches("LIB\\d{5}")) {
            throw new IllegalArgumentException(
                "Código de libro inválido. Formato esperado: LIB00000");
        }
    }

    public static void validarCodigoRevista(String codigo) {
        if (!codigo.matches("REV\\d{5}")) {
            throw new IllegalArgumentException(
                "Código de revista inválido. Formato esperado: REV00000");
        }
    }

    public static void validarCodigoCD(String codigo) {
        if (!codigo.matches("CDA\\d{5}")) {
            throw new IllegalArgumentException(
                "Código de CD inválido. Formato esperado: CDA00000");
        }
    }

    public static void validarCodigoDVD(String codigo) {
        if (!codigo.matches("DVD\\d{5}")) {
            throw new IllegalArgumentException(
                "Código de DVD inválido. Formato esperado: DVD00000");
        }
    }

    public static String validarTipoCodigo(String codigo) {
        if (codigo.startsWith("LIB")) return "Libro";
        if (codigo.startsWith("REV")) return "Revista";
        if (codigo.startsWith("CDA")) return "CD";
        if (codigo.startsWith("DVD")) return "DVD";
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }

    // ============ VALIDACIONES ESPECÍFICAS PARA LIBROS ============
    
    public static void validarISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN no puede estar vacío");
        }
        // Eliminar guiones y espacios
        String isbnLimpio = isbn.replaceAll("[\\s-]", "");
        
        // Validar ISBN-10 o ISBN-13
        if (!isbnLimpio.matches("\\d{10}") && !isbnLimpio.matches("\\d{13}")) {
            throw new IllegalArgumentException(
                "ISBN inválido. Debe tener 10 o 13 dígitos");
        }
    }

    public static void validarAnioPublicacion(int anio) {
        int anioActual = Year.now().getValue();
        if (anio < 1450 || anio > anioActual) {
            throw new IllegalArgumentException(
                "Año de publicación inválido. Debe estar entre 1450 y " + anioActual);
        }
    }

    // ============ VALIDACIONES PARA REVISTAS ============
    
    public static void validarPeriodicidad(String periodicidad) {
        if (periodicidad == null || periodicidad.trim().isEmpty()) {
            throw new IllegalArgumentException("Periodicidad no puede estar vacía");
        }
        String periodLower = periodicidad.toLowerCase().trim();
        if (!periodLower.matches("diario|semanal|quincenal|mensual|bimestral|trimestral|semestral|anual")) {
            throw new IllegalArgumentException(
                "Periodicidad inválida. Valores permitidos: diario, semanal, quincenal, " +
                "mensual, bimestral, trimestral, semestral, anual");
        }
    }

    public static void validarFechaPublicacion(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("Fecha de publicación no puede ser nula");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha de publicación no puede ser futura");
        }
    }

    // ============ VALIDACIONES PARA CDs Y DVDs ============
    
    public static void validarDuracion(String duracion) {
        if (duracion == null || duracion.trim().isEmpty()) {
            throw new IllegalArgumentException("Duración no puede estar vacía");
        }
        // Formato aceptado: HH:MM:SS o MM:SS
        if (!duracion.matches("\\d{1,2}:\\d{2}(:\\d{2})?")) {
            throw new IllegalArgumentException(
                "Formato de duración inválido. Use HH:MM:SS o MM:SS");
        }
    }

    public static void validarGenero(String genero) {
        validarCampoNoVacio(genero, "Género");
    }

    // ============ MÉTODOS DE UTILIDAD ============
    
    /**
     * Limpia y valida un string, eliminando espacios extras
     */
    public static String limpiarTexto(String texto) {
        if (texto == null) return null;
        return texto.trim().replaceAll("\\s+", " ");
    }

    /**
     * Valida que un valor esté dentro de un rango
     */
    public static void validarRango(int valor, int min, int max, String nombreCampo) {
        if (valor < min || valor > max) {
            throw new IllegalArgumentException(
                nombreCampo + " debe estar entre " + min + " y " + max);
        }
    }

    /**
     * Valida formato de entrada numérica desde TextField
     */
    public static int parseEnteroPositivo(String texto, String nombreCampo) {
        try {
            int valor = Integer.parseInt(texto.trim());
            validarNumeroPositivo(valor, nombreCampo);
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                nombreCampo + " debe ser un número entero válido");
        }
    }

    public static int parseEnteroNoNegativo(String texto, String nombreCampo) {
        try {
            int valor = Integer.parseInt(texto.trim());
            validarNumeroNoNegativo(valor, nombreCampo);
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                nombreCampo + " debe ser un número entero válido");
        }
    }
}