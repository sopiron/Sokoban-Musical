package modelo.estadoCajaFragil;

import modelo.CajaFragil;
import modelo.Tablero;

public class EstadoFragilCasiRota implements EstadoCajaFragil {

    @Override
    public void actualizarEstado(CajaFragil caja, Tablero tablero) {
        if (caja.getResistencia() <= 0) {
            tablero.romperCaja(caja);
        }
    }

    @Override
    public String getRutaImagen() {
        return "/images/cajaFragil/cajaFragil4.png";
    }
}