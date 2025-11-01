package com.diego.mediateca.domain;

public abstract class MaterialEscrito extends Material {
    protected final String editorial;

    protected MaterialEscrito(String idInterno, String titulo, int unidadesDisponibles, String editorial) {
        super(idInterno, titulo, unidadesDisponibles);
        if (editorial == null || editorial.isBlank()) {
            throw new IllegalArgumentException("La editorial es obligatoria.");
        }
        this.editorial = editorial.trim();
    }

    public String getEditorial() { return editorial; }
}
