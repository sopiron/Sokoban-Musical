package modelo;

import java.util.ArrayList;
import java.util.List;

import modelo.estadoCajaGuardaddo.EstadoCajaGuardado;

public class TableroMemento {

    private final List<EstadoCajaGuardado> estadosCajas;

    private final Posicion posicionJugador;

    private final List<Boolean> estadosCerrojos;
    private final List<Boolean> estadosMuros;

    TableroMemento(List<Caja> cajas, Posicion posicionJugador, List<Cerrojo> cerrojos, List<MuroCerrado> murosCerrados) {

        this.estadosCajas = new ArrayList<>();

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
