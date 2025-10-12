package com.diego.mediateca.domain;

import java.time.LocalDate;

public class Revista extends Material {
    private String editorial;
    private String periodicidad;
    private LocalDate fechaPublicacion;

    public Revista(String idInterno, String titulo, int unidadesDisponibles,
                   String editorial, String periodicidad, LocalDate fechaPublicacion) {
        super(idInterno, titulo, unidadesDisponibles);
        this.editorial = editorial;
        this.periodicidad = periodicidad;
        this.fechaPublicacion = fechaPublicacion;
    }

    // Getters
    public String getEditorial() { return editorial; }
    public String getPeriodicidad() { return periodicidad; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }

    // Setters
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public void setPeriodicidad(String periodicidad) { this.periodicidad = periodicidad; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    @Override
    public String toString() {
        return String.format("Revista{id='%s', titulo='%s', editorial='%s', periodicidad='%s', fecha=%s, unidades=%d}",
                idInterno, titulo, editorial, periodicidad, fechaPublicacion, unidadesDisponibles);
    }
}
