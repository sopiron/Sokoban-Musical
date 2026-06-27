package vista;

import controller.JuegoController;
import views.ObjetoView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JuegoPanel extends JPanel {

    private JuegoController controller;

    private Image imagenFondo;
    private Image imagenPiso;
    private Image imagenPared;
    private Image imagenCaja;
    private Image imagenDestino;
    private Image imagenJugador;

    private final int TAMANIO_CELDA = 100;

    public JuegoPanel(JuegoController controller) {
        this.controller = controller;

        setBackground(Color.LIGHT_GRAY);
        setFocusable(true);

        cargarImagenes();
        configurarTeclas();

        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    private void cargarImagenes() {
        imagenFondo = cargarImagen("/images/fondo-juego.png");
        imagenPiso = cargarImagen("/images/piso.png");
        imagenPared = cargarImagen("/images/pared.png");
        imagenCaja = cargarImagen("/images/caja.png");
        imagenDestino = cargarImagen("/images/destino.png");
        imagenJugador = cargarImagen("/images/personaje.png");
    }

    private Image cargarImagen(String ruta) {
        URL url = getClass().getResource(ruta);

        if (url == null) {
            throw new IllegalArgumentException("No se encontró la imagen: " + ruta);
        }

        return new ImageIcon(url).getImage();
    }

    private void configurarTeclas() {
        registrarTecla("UP", () -> moverYActualizar(() -> controller.moverArriba()));
        registrarTecla("DOWN", () -> moverYActualizar(() -> controller.moverAbajo()));
        registrarTecla("LEFT", () -> moverYActualizar(() -> controller.moverIzquierda()));
        registrarTecla("RIGHT", () -> moverYActualizar(() -> controller.moverDerecha()));
    }

    private void registrarTecla(String tecla, Runnable accion) {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(tecla), tecla);

        getActionMap().put(tecla, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accion.run();
            }
        });
    }

    private void moverYActualizar(MovimientoVista movimiento) {
        boolean seMovio = movimiento.ejecutar();

        if (seMovio) {
            repaint();

            if (controller.nivelCompletado()) {
                boolean haySiguiente = controller.pasarAlSiguienteNivel();

                if (haySiguiente) {
                    JOptionPane.showMessageDialog(
                            this,
                            "¡Nivel completado! Pasás al nivel " + controller.getNivelActual()
                    );

                    repaint();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "¡Felicitaciones! Completaste todos los niveles."
                    );
                }
            }
        }
    }

     @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);

        List<ObjetoView> paredes = controller.getParedesView();
        List<ObjetoView> cajas = controller.getCajasView();
        List<ObjetoView> destinos = controller.getDestinosView();
        Optional<ObjetoView> jugadorOpt = controller.getJugadorView();

        List<ObjetoView> todos = new ArrayList<>();
        todos.addAll(paredes);
        todos.addAll(cajas);
        todos.addAll(destinos);
        jugadorOpt.ifPresent(todos::add);

        if (todos.isEmpty()) {
            return;
        }

        // Calcular límites del tablero
        int minFila = Integer.MAX_VALUE;
        int maxFila = Integer.MIN_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxCol = Integer.MIN_VALUE;

        for (ObjetoView obj : todos) {
            if (obj.getFila() < minFila) minFila = obj.getFila();
            if (obj.getFila() > maxFila) maxFila = obj.getFila();
            if (obj.getColumna() < minCol) minCol = obj.getColumna();
            if (obj.getColumna() > maxCol) maxCol = obj.getColumna();
        }

        int filas = maxFila - minFila + 1;
        int columnas = maxCol - minCol + 1;

        int anchoTablero = columnas * TAMANIO_CELDA;
        int altoTablero = filas * TAMANIO_CELDA;

        int offsetX = (getWidth() - anchoTablero) / 2;
        int offsetY = (getHeight() - altoTablero) / 2;

        g.setColor(new Color(0, 0, 0, 90));
        g.fillRoundRect(
                offsetX - 20,
                offsetY - 20,
                anchoTablero + 40,
                altoTablero + 40,
                30,
                30
        );

        // 1. Dibujar piso en toda la grilla
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                int x = offsetX + col * TAMANIO_CELDA;
                int y = offsetY + fila * TAMANIO_CELDA;

                g.drawImage(imagenPiso, x, y, TAMANIO_CELDA, TAMANIO_CELDA, this);
            }
        }

        // 2. Dibujar destinos
        for (ObjetoView destino : destinos) {
            int x = offsetX + (destino.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (destino.getFila() - minFila) * TAMANIO_CELDA;

            g.drawImage(imagenDestino, x, y, TAMANIO_CELDA, TAMANIO_CELDA, this);
        }

        // 3. Dibujar paredes
        for (ObjetoView pared : paredes) {
            int x = offsetX + (pared.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (pared.getFila() - minFila) * TAMANIO_CELDA;

            int margenPared = -15;

            g.drawImage(
                    imagenPared,
                    x + margenPared,
                    y + margenPared,
                    TAMANIO_CELDA - margenPared * 2,
                    TAMANIO_CELDA - margenPared * 2,
                    this
            );
        }

        // 4. Dibujar cajas
        for (ObjetoView caja : cajas) {
            int x = offsetX + (caja.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (caja.getFila() - minFila) * TAMANIO_CELDA;

            g.drawImage(imagenCaja, x, y, TAMANIO_CELDA, TAMANIO_CELDA, this);
        }

        // 5. Dibujar jugador
        if (jugadorOpt.isPresent()) {
            ObjetoView jugador = jugadorOpt.get();

            int x = offsetX + (jugador.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (jugador.getFila() - minFila) * TAMANIO_CELDA;
            int margenJugador = -3;

            g.drawImage(
                    imagenJugador,
                    x + margenJugador,
                    y + margenJugador,
                    TAMANIO_CELDA - margenJugador * 2,
                    TAMANIO_CELDA - margenJugador * 2,
                    this
            );
        }
    }

    private interface MovimientoVista {
        boolean ejecutar();
    }
}