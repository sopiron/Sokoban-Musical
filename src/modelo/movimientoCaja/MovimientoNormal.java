package modelo.movimientoCaja;

import modelo.Caja;
import modelo.ElementoInteractuable;
import modelo.Posicion;
import modelo.Tablero;

public class MovimientoNormal implements MovimientoCaja {

    @Override
    public boolean mover(Caja caja, int difFila, int difColumna, Tablero tablero) {
        int nuevaFila = caja.getPosicion().getFila() + difFila;
        int nuevaColumna = caja.getPosicion().getColumna() + difColumna;

        ElementoInteractuable elemento = tablero.obtenerElemento(
                nuevaFila,
                nuevaColumna
        );

        if (elemento != null) {
            return false;
        }

        caja.setPosicion(new Posicion(nuevaFila, nuevaColumna));
        tablero.registrarEmpuje();

        return true;
    }
}