package com.diego.mediateca.domain;

public class Libro extends Material {
    private String autor;
    private String editorial;
    private String isbn;
    private int anioPublicacion;
    private int numeroPaginas;

    public Libro(String idInterno, String titulo, int unidadesDisponibles,
                 String autor, String editorial, String isbn,
                 int anioPublicacion, int numeroPaginas) {
        super(idInterno, titulo, unidadesDisponibles);
        this.autor = autor;
        this.editorial = editorial;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.numeroPaginas = numeroPaginas;
    }

    // Getters
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public String getIsbn() { return isbn; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public int getNumeroPaginas() { return numeroPaginas; }

    // Setters
    public void setAutor(String autor) { this.autor = autor; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }
    public void setNumeroPaginas(int numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    @Override
    public String toString() {
        return String.format("Libro{id='%s', titulo='%s', autor='%s', editorial='%s', isbn='%s', año=%d, paginas=%d, unidades=%d}",
                idInterno, titulo, autor, editorial, isbn, anioPublicacion, numeroPaginas, unidadesDisponibles);
    }
}