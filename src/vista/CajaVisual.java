package vista;

import java.awt.Color;
import java.awt.Graphics;

public class CajaVisual implements ElementoVisual {

    private int fila;
    private int columna;

    public CajaVisual(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public void dibujar(Graphics g, int tamanio) {
        g.setColor(new Color(139, 69, 19));
        g.fillRect(
                columna * tamanio + 5,
                fila * tamanio + 5,
                40,
                40
        );
    }
}