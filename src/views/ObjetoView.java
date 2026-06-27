package views;

public class ObjetoView {

    private int fila;
    private int columna;
    private String rutaImagen;

    public ObjetoView(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public ObjetoView(int fila, int columna, String rutaImagen) {
        this.fila = fila;
        this.columna = columna;
        this.rutaImagen = rutaImagen;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}