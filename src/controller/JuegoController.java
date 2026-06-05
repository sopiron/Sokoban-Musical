package controller;

import modelo.Tablero;
import vista.JuegoPanel;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JuegoController implements KeyListener {

    private Tablero tablero;
    private JuegoPanel panel;

    public JuegoController(Tablero tablero, JuegoPanel panel) {
        this.tablero = tablero;
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean seMovio = false;

        // Mandamos la diferencia matemática: (Fila, Columna)
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    seMovio = tablero.moverJugador(-1, 0); break;
            case KeyEvent.VK_DOWN:  seMovio = tablero.moverJugador(1, 0); break;
            case KeyEvent.VK_LEFT:  seMovio = tablero.moverJugador(0, -1); break;
            case KeyEvent.VK_RIGHT: seMovio = tablero.moverJugador(0, 1); break;
        }

        // Si el tablero confirmó que se pudo mover, actualizamos la pantalla
        if (seMovio) {
            panel.repaint();

            if (tablero.verificarVictoria()) {
                JOptionPane.showMessageDialog(panel, "¡Nivel Completado!");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}


}