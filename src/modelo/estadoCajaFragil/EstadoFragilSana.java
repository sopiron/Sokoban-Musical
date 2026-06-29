package modelo.estadoCajaFragil;

import modelo.CajaFragil;
import modelo.Tablero;

public class EstadoFragilSana implements EstadoCajaFragil {

    @Override
    public void actualizarEstado(CajaFragil caja, Tablero tablero) {
        if (caja.getResistencia() <= 5) {
            caja.setEstado(new EstadoFragilAgrietada());
        }
    }

    @Override
    public String getRutaImagen() {
        return "/images/cajaFragil/cajaFragil1.png";
    }
}