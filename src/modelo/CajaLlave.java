package modelo;

public class CajaLlave extends Caja {

    public CajaLlave(Posicion posicion) {
        super(posicion, "/images/cajaLlave.png");
    }

    @Override
    public void alTerminarMovimiento(Tablero tablero) {
        tablero.activarCerrojoEn(getPosicion());
    }

    @Override
    public boolean cuentaParaDestino() {
        return false;
    }
}