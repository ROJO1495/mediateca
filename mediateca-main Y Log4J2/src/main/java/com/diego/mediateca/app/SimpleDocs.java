package com.diego.mediateca.app;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public final class SimpleDocs {
    private SimpleDocs() {}

    public static DocumentListener onChange(Runnable r) {
        return new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { r.run(); }
            @Override public void removeUpdate(DocumentEvent e)  { r.run(); }
            @Override public void changedUpdate(DocumentEvent e) { r.run(); }
        };
    }
}
