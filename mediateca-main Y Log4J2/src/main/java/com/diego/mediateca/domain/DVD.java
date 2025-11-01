package com.diego.mediateca.domain;

public class DVD extends Material {
    private String director;
    private String duracion;
    private String genero;

    public DVD(String idInterno, String titulo, int unidadesDisponibles,
               String director, String duracion, String genero) {
        super(idInterno, titulo, unidadesDisponibles);
        this.director = director;
        this.duracion = duracion;
        this.genero = genero;
    }

    // Getters
    public String getDirector() { return director; }
    public String getDuracion() { return duracion; }
    public String getGenero() { return genero; }

    // Setters
    public void setDirector(String director) { this.director = director; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public void setGenero(String genero) { this.genero = genero; }

    @Override
    public String toString() {
        return String.format("DVD{id='%s', titulo='%s', director='%s', duracion='%s', genero='%s', unidades=%d}",
                idInterno, titulo, director, duracion, genero, unidadesDisponibles);
    }
}
