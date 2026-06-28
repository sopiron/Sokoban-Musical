package modelo.movimientoCaja;

import modelo.Caja;
import modelo.ElementoInteractuable;
import modelo.Posicion;
import modelo.Tablero;

public class MovimientoResbaladizo implements MovimientoCaja {

    @Override
    public boolean mover(Caja caja, int difFila, int difColumna, Tablero tablero) {

        int filaActual = caja.getPosicion().getFila() + difFila;
        int columnaActual = caja.getPosicion().getColumna() + difColumna;

        // Si ni siquiera puede entrar al primer casillero, no se mueve.
        if (tablero.obtenerElemento(filaActual, columnaActual) != null) {
            return false;
        }

        /*
         * Como la caja entró en el terreno resbaladizo,
         * sigue avanzando por el piso normal hasta encontrar un obstáculo.
         */
        boolean puedeSeguir = true;

        while (puedeSeguir) {
            int siguienteFila = filaActual + difFila;
            int siguienteColumna = columnaActual + difColumna;

            ElementoInteractuable elementoSiguiente = tablero.obtenerElemento(
                    siguienteFila,
                    siguienteColumna
            );

            if (elementoSiguiente != null) {
                puedeSeguir = false;
            } else {
                filaActual = siguienteFila;
                columnaActual = siguienteColumna;
            }
        }

        caja.setPosicion(new Posicion(filaActual, columnaActual));
        tablero.registrarEmpuje();

        return true;
    }
}