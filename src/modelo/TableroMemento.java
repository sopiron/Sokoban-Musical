package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Memento: guarda una foto inmutable del estado del tablero en un momento dado.
 * Solo el Tablero puede crear y leer un TableroMemento (acceso por paquete).
 * El HistorialMovimientos los almacena sin ver su contenido.
 */
public class TableroMemento {

    // Posiciones de cada caja (en el mismo orden que la lista original)
    private final List<Posicion> posicionesCajas;

    // Posición del jugador
    private final Posicion posicionJugador;

    TableroMemento(List<Posicion> posicionesCajas, Posicion posicionJugador) {
        // Copiamos defensivamente para que nadie pueda mutar el snapshot
        this.posicionesCajas = new ArrayList<>(posicionesCajas);
        this.posicionJugador = posicionJugador;
    }

    List<Posicion> getPosicionesCajas() {
        return new ArrayList<>(posicionesCajas); // copia defensiva al leer
    }

    Posicion getPosicionJugador() {
        return posicionJugador;
    }
}
