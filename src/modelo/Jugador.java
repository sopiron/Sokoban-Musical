package modelo;

public class Jugador {

    private Posicion posicion;

    public Jugador(Posicion posicion) {
        this.posicion = posicion;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
    
    public void mover(int difFila, int difColumna) {
        int nuevaFila = this.posicion.getFila() + difFila;
        int nuevaColumna = this.posicion.getColumna() + difColumna;
        this.posicion = new Posicion(nuevaFila, nuevaColumna);
    }

}