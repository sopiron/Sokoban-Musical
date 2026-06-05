package vista; // AGRGUE TODA ESTA CLASE. MEL

import modelo.*;
import javax.swing.*;
import java.awt.*;

// JPanel es simplemente un lienzo en blanco donde podemos dibujar cosas.
public class JuegoPanel extends JPanel {

    private Tablero tablero;

    public JuegoPanel(Tablero tablero) {
        this.tablero = tablero;
        setBackground(Color.LIGHT_GRAY); // Color de fondo para el piso
    }

    // Este método es el que Java usa para dibujar en la pantalla.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (tablero == null) return;

        int tamaño = 50; // Cada celda del tablero va a medir 50x50 píxeles

        // 1. Dibujamos las Paredes (Gris oscuro)
        g.setColor(Color.DARK_GRAY);
        for (Pared p : tablero.getParedes()) {
            g.fillRect(p.getPosicion().getColumna() * tamaño, p.getPosicion().getFila() * tamaño, tamaño, tamaño);
        }

        // 2. Dibujamos los Destinos (Círculos verdes pequeños)
        g.setColor(Color.GREEN);
        for (Destino d : tablero.getDestinos()) {
            g.fillOval(d.getPosicion().getColumna() * tamaño + 15, d.getPosicion().getFila() * tamaño + 15, 20, 20);
        }

        // 3. Dibujamos las Cajas (Cuadrados marrones)
        g.setColor(new Color(139, 69, 19));
        for (Caja c : tablero.getCajas()) {
            g.fillRect(c.getPosicion().getColumna() * tamaño + 5, c.getPosicion().getFila() * tamaño + 5, 40, 40);
        }

        // 4. Dibujamos al Jugador (Círculo azul)
        if (tablero.getJugador() != null) {
            g.setColor(Color.BLUE);
            Posicion pj = tablero.getJugador().getPosicion();
            g.fillOval(pj.getColumna() * tamaño + 5, pj.getFila() * tamaño + 5, 40, 40);
        }
    }
}
