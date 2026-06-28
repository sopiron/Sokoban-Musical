package modelo;

public class PisoResbaladizo {

    private Posicion posicion;

    public PisoResbaladizo(Posicion posicion) {
        this.posicion = posicion;
    }

    public boolean ocupa(int fila, int columna) {
        return posicion.getFila() == fila &&
               posicion.getColumna() == columna;
    }

    public Posicion getPosicion() {
        return posicion;
    }
}