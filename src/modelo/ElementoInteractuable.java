package modelo;

public interface ElementoInteractuable {
    // Devuelve true si permite que el jugador avance, o false si lo bloquea.
    boolean interactuar(int difFila, int difColumna, Tablero tablero);
    default boolean bloquea() {
        return true;
    }
}