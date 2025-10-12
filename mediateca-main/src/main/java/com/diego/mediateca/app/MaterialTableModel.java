package com.diego.mediateca.app;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MaterialTableModel extends AbstractTableModel {

    private final String tipo;
    private final List<?> rows;
    private final String[] cols;

    private MaterialTableModel(String tipo, List<?> rows, String[] cols) {
        this.tipo = tipo;
        this.rows = rows;
        this.cols = cols;
    }

    public static MaterialTableModel of(String tipo, List<?> rows) {
        return switch (tipo) {
            case "LIBRO" -> new MaterialTableModel(tipo, rows,
                new String[]{"Código","Título","Autor","Editorial","ISBN","Año","Páginas","Unidades"});
            case "REVISTA" -> new MaterialTableModel(tipo, rows,
                new String[]{"Código","Título","Editorial","Periodicidad","Fecha publicación","Unidades"});
            case "DVD" -> new MaterialTableModel(tipo, rows,
                new String[]{"Código","Título","Autor/Director","Duración","Género","Unidades"}); 
            case "CD" -> new MaterialTableModel(tipo, rows,
                new String[]{"Código","Título","Artista","Pistas","Unidades"});
            default -> throw new IllegalArgumentException("Tipo no soportado: " + tipo);
        };
    }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }

    @Override
    public Object getValueAt(int r, int c) {
        Object o = rows.get(r);
        switch (tipo) {
            case "LIBRO" -> {
                var x = (com.diego.mediateca.domain.Libro) o;
                return switch (c) {
                    case 0 -> x.getIdInterno();
                    case 1 -> x.getTitulo();
                    case 2 -> x.getAutor();
                    case 3 -> x.getEditorial();
                    case 4 -> x.getIsbn();
                    case 5 -> x.getAnioPublicacion();
                    case 6 -> x.getNumeroPaginas();         
                    case 7 -> x.getUnidadesDisponibles();
                    default -> "";
                };
            }
            case "REVISTA" -> {
                var x = (com.diego.mediateca.domain.Revista) o;
                return switch (c) {
                    case 0 -> x.getIdInterno();
                    case 1 -> x.getTitulo();
                    case 2 -> x.getEditorial();
                    case 3 -> x.getPeriodicidad();
                    case 4 -> x.getFechaPublicacion();
                    case 5 -> x.getUnidadesDisponibles();
                    default -> "";
                };
            }
            case "DVD" -> {
                var x = (com.diego.mediateca.domain.DVD) o;
                return switch (c) {
                    case 0 -> x.getIdInterno();
                    case 1 -> x.getTitulo();
                    case 2 -> x.getDirector();
                    case 3 -> x.getDuracion();
                    case 4 -> x.getGenero();
                    case 5 -> x.getUnidadesDisponibles();
                    default -> "";
                };
            }
            case "CD" -> {
                var x = (com.diego.mediateca.domain.CD) o;
                return switch (c) {
                    case 0 -> x.getIdInterno();
                    case 1 -> x.getTitulo();
                    case 2 -> x.getArtista();
                    case 3 -> x.getNumeroCanciones();
                    case 4 -> x.getUnidadesDisponibles();
                    default -> "";
                };
            }
            default -> { return ""; }
        }
    }
}
