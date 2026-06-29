package modelo;

import modelo.estadoCajaFragil.EstadoCajaFragil;
import modelo.estadoCajaFragil.EstadoFragilSana;

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
}