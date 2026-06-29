package modelo;

import modelo.estadoCajaFragil.EstadoCajaFragil;
import modelo.estadoCajaFragil.EstadoFragilSana;
import modelo.estadoCajaGuardaddo.EstadoCajaFragilGuardado;
import modelo.estadoCajaGuardaddo.EstadoCajaGuardado;

public class CajaFragil extends Caja {

    private int resistencia;
    private EstadoCajaFragil estado;

    public CajaFragil(Posicion posicion) {
        super(posicion, "/images/cajaFragil1.png");

        this.resistencia = 7;
        this.estado = new EstadoFragilSana();
    }

    @Override
    public boolean interactuar(int difFila, int difColumna, Tablero tablero) {
        boolean seMovio = super.interactuar(difFila, difColumna, tablero);

        if (seMovio) {
            reducirResistencia();
            estado.actualizarEstado(this, tablero);
        }

        return seMovio;
    }

    private void reducirResistencia() {
        resistencia--;
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setEstado(EstadoCajaFragil estado) {
        this.estado = estado;
    }

    @Override
    public String getRutaImagen() {
        return estado.getRutaImagen();
    }

    public EstadoCajaFragil getEstado() {
        return estado;
    }

    public void restaurarEstadoFragil(
            Posicion posicion,
            int resistencia,
            EstadoCajaFragil estado
    ) {
        setPosicion(posicion);
        this.resistencia = resistencia;
        this.estado = estado;
    }

    //Este método es el que permite que una caja frágil guarde su estado, su posición, mi resistencia y mi estado visual.
    @Override
    public EstadoCajaGuardado guardarEstado() {
        return new EstadoCajaFragilGuardado(this);
    }
}