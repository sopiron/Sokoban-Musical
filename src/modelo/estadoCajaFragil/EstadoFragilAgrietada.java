package modelo.estadoCajaFragil;

import modelo.CajaFragil;
import modelo.Tablero;

public class EstadoFragilAgrietada implements EstadoCajaFragil {

    @Override
    public void actualizarEstado(CajaFragil caja, Tablero tablero) {
        if (caja.getResistencia() <= 3) {
            caja.setEstado(new EstadoFragilMuyAgrietada());
        }
    }

    @Override
    public String getRutaImagen() {
        return "/images/cajaFragil/cajaFragil2.png";
    }
}