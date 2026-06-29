package modelo.estadoCajaFragil;

import modelo.CajaFragil;
import modelo.Tablero;

public class EstadoFragilMuyAgrietada implements EstadoCajaFragil {

    @Override
    public void actualizarEstado(CajaFragil caja, Tablero tablero) {
        if (caja.getResistencia() <= 1) {
            caja.setEstado(new EstadoFragilCasiRota());
        }
    }

    @Override
    public String getRutaImagen() {
        return "/images/cajaFragil/cajaFragil3.png";
    }
}