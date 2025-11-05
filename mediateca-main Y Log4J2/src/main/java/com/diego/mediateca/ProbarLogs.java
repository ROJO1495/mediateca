package com.diego.mediateca;

import com.diego.mediateca.db.MaterialDAO;
import com.diego.mediateca.domain.Libro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProbarLogs {
    private static final Logger log = LogManager.getLogger(ProbarLogs.class);

    public static void main(String[] args) throws Exception {
        MaterialDAO dao = new MaterialDAO();

        // ID único para esta corrida
        String idUnico = "LIBLOG-" + (System.currentTimeMillis() % 100000);

        // S1: inserción OK
        log.info("S1) Insertando libro válido con id {}", idUnico);
        dao.insertarLibro(new Libro(
            idUnico, "Libro OK", 2, "Autor A", "Editorial X", "1234567890", 2024, 120
        ));
        log.info("S1) OK listo (id={})", idUnico);

        // S2: duplicado (mismo id)
        log.info("S2) Forzando PK duplicada con el MISMO id {}", idUnico);
        try {
            dao.insertarLibro(new Libro(
                idUnico, "Libro Duplicado", 1, "", "", "", 2024, 100
            ));
        } catch (Exception e) {
            log.error("S2) Esperado: fallo por PK duplicada (id={})", idUnico, e);
        }

        // S3: Data too long (ISBN muy largo)
        log.info("S3) Forzando 'Data too long' con ISBN larguísimo");
        String isbnLargo = "X".repeat(200);
        try {
            dao.insertarLibro(new Libro(
                "LIBLONG-" + (System.currentTimeMillis() % 100000),
                "Libro con ISBN larguísimo", 1, "Autor B", "Editorial Y", isbnLargo, 2025, 90
            ));
        } catch (Exception e) {
            log.error("S3) Esperado: fallo por 'Data too long' en ISBN", e);
        }

        log.info("Pruebas de logging finalizadas");
    }
}