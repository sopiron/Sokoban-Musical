package views;

public class ObjetoView {

    private int fila;
    private int columna;

    public ObjetoView(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}