package modelo;

public class Caja implements ElementoInteractuable {
    private Posicion posicion;

    public Caja(Posicion posicion) {
        this.posicion = posicion;
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

        // La caja le pregunta al tablero qué hay en la celda a la que quiere ir
        ElementoInteractuable elementoDetras = tablero.obtenerElemento(destinoFila, destinoCol);

        // En el Sokoban clásico, la caja solo se mueve si la celda de atrás está totalmente vacía
        if (elementoDetras == null) {
            this.mover(difFila, difColumna);
            return true; // Se movió, deja que el jugador ocupe su lugar original
        }

        return false; // Atrás hay una pared u otra caja, bloquea el movimiento
    }
}