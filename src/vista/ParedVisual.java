package vista;

import java.awt.Color;
import java.awt.Graphics;

public class ParedVisual implements ElementoVisual {

    private int fila;
    private int columna;

    public ParedVisual(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public void dibujar(Graphics g, int tamanio) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(
                columna * tamanio,
                fila * tamanio,
                tamanio,
                tamanio
        );
    }
}