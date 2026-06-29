package modelo;

import java.util.ArrayList;
import java.util.List;

import modelo.estadoCajaGuardaddo.EstadoCajaGuardado;

/**
 * Memento: guarda una foto inmutable del estado del tablero en un momento dado.
 * Solo el Tablero puede crear y leer un TableroMemento (acceso por paquete).
 * El HistorialMovimientos los almacena sin ver su contenido.
 */
public class TableroMemento {

    // Estado de cada caja (en el mismo orden que la lista original)
    private final List<EstadoCajaGuardado> estadosCajas;

    // Posición del jugador
    private final Posicion posicionJugador;

    private final List<Boolean> estadosCerrojos;
    private final List<Boolean> estadosMuros;

    TableroMemento(List<Caja> cajas, Posicion posicionJugador, List<Cerrojo> cerrojos, List<MuroCerrado> murosCerrados) {

        this.estadosCajas = new ArrayList<>();

        // Copiamos defensivamente para que nadie pueda mutar el snapshot
        for (Caja caja : cajas) {
            this.estadosCajas.add(caja.guardarEstado());
        }

        this.estadosCerrojos = new ArrayList<>();

        for (Cerrojo cerrojo : cerrojos) {
            this.estadosCerrojos.add(cerrojo.estaActivado());
        }

        this.estadosMuros = new ArrayList<>();

        for (MuroCerrado muro : murosCerrados) {
            this.estadosMuros.add(muro.estaAbierto());
        }

        this.posicionJugador = new Posicion(
                posicionJugador.getFila(),
                posicionJugador.getColumna()
        );
    }

    List<EstadoCajaGuardado> getEstadosCajas() {
        return new ArrayList<>(estadosCajas);
    }

    Posicion getPosicionJugador() {
        return posicionJugador;
    }

    List<Boolean> getEstadosCerrojos() {
        return new ArrayList<>(estadosCerrojos);
    }

    List<Boolean> getEstadosMuros() {
        return new ArrayList<>(estadosMuros);
    }
}
