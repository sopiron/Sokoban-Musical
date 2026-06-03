package modelo;

public class Jugador {

    private Posicion posicion;

    public Jugador(Posicion posicion) {
        this.posicion = posicion;
    }

    public Posicion getPosicion() {
        return posicion;
    }
}