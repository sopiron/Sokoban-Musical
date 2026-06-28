package modelo.movimientoCaja;

import modelo.Caja;
import modelo.Tablero;

public interface MovimientoCaja {
    boolean mover(Caja caja, int difFila, int difColumna, Tablero tablero);
}
