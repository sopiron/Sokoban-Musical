package modelo;

import modelo.movimientoCaja.MovimientoCaja;

public class Caja implements ElementoInteractuable {
    private Posicion posicion;
    private String rutaImagen;

    public Caja(Posicion posicion, String rutaImagen) {
        this.posicion = posicion;
        this.rutaImagen = rutaImagen;
    }

    public Posicion getPosicion() { return posicion; }
    public void setPosicion(Posicion posicion) { this.posicion = posicion; }

    public void mover(int difFila, int difColumna) {
        int nuevaFila = this.posicion.getFila() + difFila;
        int nuevaColumna = this.posicion.getColumna() + difColumna;
        this.posicion = new Posicion(nuevaFila, nuevaColumna);
    }

    @Override
    public boolean interactuar(int difFila, int difColumna, Tablero tablero) {
        int destinoFila = this.posicion.getFila() + difFila;
        int destinoCol = this.posicion.getColumna() + difColumna;

        MovimientoCaja estrategia = tablero.obtenerMovimientoCaja(
            destinoFila,
            destinoCol
        );

        return estrategia.mover(this, difFila, difColumna, tablero);
    }

    public String getRutaImagen() {
        return rutaImagen;
    }
}