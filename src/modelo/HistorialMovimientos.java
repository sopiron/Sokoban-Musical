package modelo;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Caretaker del patrón Memento.
 * Gestiona la pila de estados guardados y las reglas del TP:
 *   - Hasta 15 mementos almacenados
 *   - Cada uso de undo restaura 5 estados hacia atrás
 *   - Máximo 3 usos consecutivos de undo (se resetea al mover)
 */
public class HistorialMovimientos {

    private static final int MAX_ESTADOS    = 15;
    private static final int PASOS_POR_UNDO = 5;
    private static final int MAX_USOS_UNDO  = 3;

    // Pila de snapshots: el tope es el estado más reciente
    private final Deque<TableroMemento> pila = new ArrayDeque<>();

    // Cuántas veces seguidas se usó undo sin hacer un movimiento real en el medio
    private int usosConsecutivos = 0;

    /**
     * Guarda el estado actual antes de ejecutar un movimiento.
     * También resetea el contador de usos consecutivos de undo.
     */
    public void guardar(TableroMemento memento) {
        pila.push(memento);

        // Limitamos la pila a MAX_ESTADOS para no acumular indefinidamente
        if (pila.size() > MAX_ESTADOS) {
            // removeLast() saca el estado más antiguo (el fondo de la pila)
            ((ArrayDeque<TableroMemento>) pila).removeLast();
        }

        // Mover resetea los usos consecutivos
        usosConsecutivos = 0;
    }

    /**
     * Devuelve el memento al que hay que restaurar (5 pasos atrás),
     * o null si no se puede deshacer (pila vacía o límite de usos alcanzado).
     */
    public TableroMemento undo() {
        if (pila.isEmpty() || usosConsecutivos >= MAX_USOS_UNDO) {
            return null;
        }

        // Descartamos hasta PASOS_POR_UNDO estados, quedándonos con el último descartado
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