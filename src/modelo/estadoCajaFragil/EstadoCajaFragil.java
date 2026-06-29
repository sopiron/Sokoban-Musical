package modelo.estadoCajaFragil;

import modelo.CajaFragil;
import modelo.Tablero;

public interface EstadoCajaFragil {

    void actualizarEstado(CajaFragil caja, Tablero tablero);

    String getRutaImagen();
}