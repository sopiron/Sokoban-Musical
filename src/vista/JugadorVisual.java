package vista;

import java.awt.Color;
import java.awt.Graphics;

public class JugadorVisual implements ElementoVisual {

    private int fila;
    private int columna;

    public JugadorVisual(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public void dibujar(Graphics g, int tamanio) {
        g.setColor(Color.BLUE);
        g.fillOval(
                columna * tamanio + 5,
                fila * tamanio + 5,
                180,
                180
        );
    }
}