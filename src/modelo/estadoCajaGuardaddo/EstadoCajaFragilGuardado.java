package modelo.estadoCajaGuardaddo;

import modelo.Caja;
import modelo.CajaFragil;
import modelo.Posicion;
import modelo.estadoCajaFragil.EstadoCajaFragil;

public final class EstadoCajaFragilGuardado implements EstadoCajaGuardado {

    private final CajaFragil caja;
    private final Posicion posicion;
    private final int resistencia;
    private final EstadoCajaFragil estado;

    public EstadoCajaFragilGuardado(CajaFragil caja) {
        this.caja = caja;
        this.posicion = new Posicion(
                caja.getPosicion().getFila(),
                caja.getPosicion().getColumna()
        );
        this.resistencia = caja.getResistencia();
        this.estado = caja.getEstado();
    }

    @Override
    public void restaurar() {
        caja.restaurarEstadoFragil(
                new Posicion(
                        posicion.getFila(),
                        posicion.getColumna()
                ),
                resistencia,
                estado
        );
    }

    @Override
    public Caja getCaja() {
        return caja;
    }
}