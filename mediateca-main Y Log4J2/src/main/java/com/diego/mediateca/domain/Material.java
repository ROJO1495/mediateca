package com.diego.mediateca.domain;

public class Material {
    protected String idInterno;
    protected String titulo;
    protected int unidadesDisponibles;

    public Material(String idInterno, String titulo, int unidadesDisponibles) {
        this.idInterno = idInterno;
        this.titulo = titulo;
        this.unidadesDisponibles = unidadesDisponibles;
    }

    // Getters
    public String getIdInterno() {
        return idInterno;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getUnidadesDisponibles() {
        return unidadesDisponibles;
    }

    // Setters
    public void setIdInterno(String idInterno) {
        this.idInterno = idInterno;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setUnidadesDisponibles(int unidadesDisponibles) {
        this.unidadesDisponibles = unidadesDisponibles;
    }

    @Override
    public String toString() {
        return String.format("Material{id='%s', titulo='%s', unidades=%d}",
                idInterno, titulo, unidadesDisponibles);
    }
}