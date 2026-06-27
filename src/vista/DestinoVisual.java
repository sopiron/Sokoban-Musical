package vista;

import java.awt.Color;
import java.awt.Graphics;

public class DestinoVisual implements ElementoVisual {

    private int fila;
    private int columna;

    public DestinoVisual(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public void dibujar(Graphics g, int tamanio) {
        g.setColor(Color.GREEN);
        g.fillOval(
                columna * tamanio + 15,
                fila * tamanio + 15,
                20,
                20
        );
    }
}