package modelo.estadoCajaGuardaddo;

import modelo.Caja;
import modelo.Posicion;

public final class EstadoCajaNormalGuardado implements EstadoCajaGuardado {

    private final Caja caja;
    private final Posicion posicion;

    public EstadoCajaNormalGuardado(Caja caja) {
        this.caja = caja;
        this.posicion = new Posicion(
                caja.getPosicion().getFila(),
                caja.getPosicion().getColumna()
        );
    }

    @Override
    public void restaurar() {
        caja.setPosicion(new Posicion(
                posicion.getFila(),
                posicion.getColumna()
        ));
    }

    @Override
    public Caja getCaja() {
        return caja;
    }
}