package com.diego.mediateca.domain;

public class CD extends Material {
    private String artista;
    private String genero;
    private String duracion;
    private int numeroCanciones;

    public CD(String idInterno, String titulo, int unidadesDisponibles,
              String artista, String genero, String duracion, int numeroCanciones) {
        super(idInterno, titulo, unidadesDisponibles);
        this.artista = artista;
        this.genero = genero;
        this.duracion = duracion;
        this.numeroCanciones = numeroCanciones;
    }

    // Getters
    public String getArtista() { return artista; }
    public String getGenero() { return genero; }
    public String getDuracion() { return duracion; }
    public int getNumeroCanciones() { return numeroCanciones; }

    // Setters
    public void setArtista(String artista) { this.artista = artista; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public void setNumeroCanciones(int numeroCanciones) { this.numeroCanciones = numeroCanciones; }

    @Override
    public String toString() {
        return String.format("CD{id='%s', titulo='%s', artista='%s', genero='%s', duracion='%s', canciones=%d, unidades=%d}",
                idInterno, titulo, artista, genero, duracion, numeroCanciones, unidadesDisponibles);
    }
}