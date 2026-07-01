package modelo;

import java.util.ArrayDeque;
import java.util.Deque;

public class HistorialMovimientos {

    private static final int MAX_ESTADOS    = 15;
    private static final int PASOS_POR_UNDO = 5;
    private static final int MAX_USOS_UNDO  = 3;

    // Pila de snapshots: el tope es el estado más reciente
    private final Deque<TableroMemento> pila = new ArrayDeque<>();

    private int usosConsecutivos = 0;

 
    public void guardar(TableroMemento memento) {
        pila.push(memento);

        // Limitamos la pila a MAX_ESTADOS para no acumular indefinidamente
        if (pila.size() > MAX_ESTADOS) {
            ((ArrayDeque<TableroMemento>) pila).removeLast();
        }

        usosConsecutivos = 0;
    }


    public TableroMemento undo() {
        if (pila.isEmpty() || usosConsecutivos >= MAX_USOS_UNDO) {
            return null;
        }

        TableroMemento objetivo = null;
        for (int i = 0; i < PASOS_POR_UNDO; i++) {
            if (pila.isEmpty()) break;
            objetivo = pila.pop();
        }

        usosConsecutivos++;
        return objetivo;
    }

    /**
     * Indica si el botón undo debe estar habilitado en la vista.
     */
    public boolean puedeDeshacer() {
        return !pila.isEmpty() && usosConsecutivos < MAX_USOS_UNDO;
    }

    /**
     * Limpia el historial (se usa al reiniciar o cambiar de nivel).
     */
    public void limpiar() {
        pila.clear();
        usosConsecutivos = 0;
    }

    public int getUsosConsecutivos() {
        return usosConsecutivos;
    }
}