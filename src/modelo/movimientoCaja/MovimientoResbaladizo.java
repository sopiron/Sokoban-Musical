package modelo.movimientoCaja;

import modelo.Caja;
import modelo.ElementoInteractuable;
import modelo.Posicion;
import modelo.Tablero;

public class MovimientoResbaladizo implements MovimientoCaja {

    @Override
    public boolean mover(Caja caja, int difFila, int difColumna, Tablero tablero) {

        int destinoFila = caja.getPosicion().getFila() + difFila;
        int destinoColumna = caja.getPosicion().getColumna() + difColumna;

        ElementoInteractuable elementoDestino = tablero.obtenerElemento(
                destinoFila,
                destinoColumna
        );

        if (elementoDestino != null) {
            return false;
        }

        // La caja entra solamente al primer casillero resbaladizo
        caja.setPosicion(new Posicion(destinoFila, destinoColumna));

        tablero.registrarEmpuje();

        // Acá le avisamos al tablero que desde ahora debe seguir deslizándose
        tablero.iniciarDeslizamiento(caja, difFila, difColumna);

        return true;
    }
}