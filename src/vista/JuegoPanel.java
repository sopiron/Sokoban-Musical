package vista;

import controller.JuegoController;
import modelo.GestorSonido;
import modelo.ResultadoNivel;
import views.ObjetoView;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JuegoPanel extends JPanel {

    private JuegoController controller;

    private BarraPuntos barraPuntos;
    private BarraUndo barraUndo;
    private BotonRedondeado btnPausa;

    private Image imagenFondo;
    private Image imagenPiso;
    private Image imagenPared;
    private Image imagenCaja;
    private Image imagenDestino;
    private Image imagenJugador;
    private Image imagenPisoResbaladizo;
    private Map<String, Image> imagenesCache;

    private Timer timerDeslizamiento;
    private boolean animandoDeslizamiento;

    private final int TAMANIO_CELDA = 55;

    public JuegoPanel(JuegoController controller) {
        this.controller = controller;
        this.imagenesCache = new HashMap<>();

        setBackground(Color.LIGHT_GRAY);
        setFocusable(true);

        cargarImagenes();
        configurarTeclas();

        setLayout(null);

        barraPuntos = new BarraPuntos();
        add(barraPuntos);

        barraUndo = new BarraUndo(() -> ejecutarUndo());
        add(barraUndo);

        btnPausa = new BotonRedondeado(
                "⏸",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );
        btnPausa.setFont(new Font("SansSerif", Font.BOLD, 22));
        btnPausa.setFocusable(false);
        btnPausa.addActionListener(e -> abrirPausa());
        add(btnPausa);

        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    private void cargarImagenes() {
        imagenFondo = cargarImagen("/images/fondo-juego.png");
        imagenPiso = cargarImagen("/images/piso.png");
        imagenPared = cargarImagen("/images/pared.png");
        imagenCaja = cargarImagen("/images/cajaGuitarra.png");
        imagenDestino = cargarImagen("/images/destino.png");
        imagenJugador = cargarImagen("/images/personaje.png");
        imagenPisoResbaladizo = cargarImagen("/images/pisoResbaladizo.png");
    }

    private Image cargarImagen(String ruta) {
        URL url = getClass().getResource(ruta);

        if (url == null) {
            throw new IllegalArgumentException("No se encontró la imagen: " + ruta);
        }

        return new ImageIcon(url).getImage();
    }

    //Cargar imagenes con url dinámicas
    private Image obtenerImagenDesdeRuta(String ruta, Image imagenPorDefecto) {
        if (ruta == null || ruta.isEmpty()) {
            return imagenPorDefecto;
        }

        if (!imagenesCache.containsKey(ruta)) {
            imagenesCache.put(ruta, cargarImagen(ruta));
        }

        return imagenesCache.get(ruta);
    }

    private void configurarTeclas() {
        registrarTecla("UP",     () -> moverYActualizar(() -> controller.moverArriba()));
        registrarTecla("DOWN",   () -> moverYActualizar(() -> controller.moverAbajo()));
        registrarTecla("LEFT",   () -> moverYActualizar(() -> controller.moverIzquierda()));
        registrarTecla("RIGHT",  () -> moverYActualizar(() -> controller.moverDerecha()));
        registrarTecla("Z",      () -> ejecutarUndo());
        registrarTecla("ESCAPE", () -> abrirPausa());
    }

    private void ejecutarUndo() {
        boolean deshecho = controller.accionUndo();
        if (deshecho) {
            repaint();
        }
        actualizarBarraUndo();
    }

    private void abrirPausa() {
        controller.pausar();

        JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(this);

        PopupPausa popup = new PopupPausa(
                ventanaPadre,
                controller,
                () -> reiniciarNivel(),
                () -> volverAlMenu()
        );

        popup.setVisible(true);
        // Al cerrar el popup con ✕, el controller ya reanudó
    }

    private void reiniciarNivel() {
        controller.reiniciarNivel();
        barraUndo.resetearUsos();
        revalidate();
        repaint();
    }

    private void volverAlMenu() {
        JFrame ventanaActual = (JFrame) SwingUtilities.getWindowAncestor(this);
        ventanaActual.dispose();

        JuegoController ctrl = JuegoController.getInstance();
        MenuPrincipalView menu = new MenuPrincipalView(ctrl);
        menu.setVisible(true);
    }

    private void actualizarBarraUndo() {
        int usosRestantes = controller.getUsosUndoRestantes();
        barraUndo.actualizarUsos(usosRestantes);
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
        if (animandoDeslizamiento) {
            return;
        }

        boolean seMovio = movimiento.ejecutar();

        if (seMovio) {
            repaint();

            // Movimiento real del jugador: se resetean los usos consecutivos de undo
            barraUndo.resetearUsos();

            if (controller.hayCajaDeslizandose()) {
                iniciarAnimacionDeslizamiento();
            } else {
                verificarFinDeNivel();
            }
        }
    }

    private void iniciarAnimacionDeslizamiento() {
        animandoDeslizamiento = true;

        timerDeslizamiento = new Timer(150, e -> {
            boolean sigueDeslizando = controller.deslizarCajaUnPaso();

            repaint();

            if (!sigueDeslizando) {
                timerDeslizamiento.stop();
                animandoDeslizamiento = false;

                verificarFinDeNivel();

                SwingUtilities.invokeLater(() -> requestFocusInWindow());
            }
        });

        timerDeslizamiento.start();
    }

    private void verificarFinDeNivel() {
        if (controller.nivelCompletado()) {

            ResultadoNivel resultado = controller.finalizarNivelActual();

            int nivelCompletado = controller.getNivelActual();
            boolean haySiguiente = controller.haySiguienteNivel();

            AccionPopupNivel accion = mostrarPopupNivelCompletado(
                    resultado,
                    nivelCompletado,
                    haySiguiente
            );

            if (accion == AccionPopupNivel.SIGUIENTE && haySiguiente) {
                controller.pasarAlSiguienteNivel();

                revalidate();
                repaint();
            }

            if (accion == AccionPopupNivel.HOME) {
                volverAlMenuPrincipal();
            }
        }
    }

    @Override
    public void doLayout() {
        super.doLayout();

        int anchoBarra = 720;
        int altoBarra = 70;

        int x = (getWidth() - anchoBarra) / 2;

        barraPuntos.setBounds(x, 35, anchoBarra, altoBarra);

        // Botón pausa: arriba a la derecha
        btnPausa.setBounds(getWidth() - 90, 35, 70, 70);

        // Barra Undo: centrada abajo, mismo ancho que la barra de puntos
        barraUndo.setBounds(x, getHeight() - 100, anchoBarra, altoBarra);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);

        List<ObjetoView> paredes = controller.getParedesView();
        List<ObjetoView> cajas = controller.getCajasView();
        List<ObjetoView> destinos = controller.getDestinosView();
        List<ObjetoView> pisosResbaladizos = controller.getPisosResbaladizosView();
        Optional<ObjetoView> jugadorOpt = controller.getJugadorView();

        List<ObjetoView> todos = new ArrayList<>();
        todos.addAll(paredes);
        todos.addAll(cajas);
        todos.addAll(destinos);
        todos.addAll(pisosResbaladizos);
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

        // 1.5. Dibujar piso resbaladizo en toda la grilla
        for (ObjetoView pisoResbaladizo : pisosResbaladizos) {
            int x = offsetX + (pisoResbaladizo.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (pisoResbaladizo.getFila() - minFila) * TAMANIO_CELDA;

            int margen = 0;

            g.drawImage(
                    imagenPisoResbaladizo,
                    x + margen,
                    y + margen,
                    TAMANIO_CELDA - margen * 2,
                    TAMANIO_CELDA - margen * 2,
                    this
            );
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

            int margenPared = -10;

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

            Image imagenCajaActual = obtenerImagenDesdeRuta(
                    caja.getRutaImagen(),
                    imagenCaja
            );

            int margenCaja = 4;

            g.drawImage(
                    imagenCajaActual,
                    x + margenCaja,
                    y + margenCaja,
                    TAMANIO_CELDA - margenCaja * 2,
                    TAMANIO_CELDA - margenCaja * 2,
                    this
            );
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

   private AccionPopupNivel mostrarPopupNivelCompletado(
        ResultadoNivel resultado,
        int nivelCompletado,
        boolean haySiguiente
    ) {
        JFrame ventana = (JFrame) SwingUtilities.getWindowAncestor(this);

        PopupResultadoNivel popup = new PopupResultadoNivel(
                ventana,
                resultado,
                nivelCompletado,
                haySiguiente
        );

        popup.setVisible(true);

        return popup.getAccionSeleccionada();
    }

    private void volverAlMenuPrincipal() {
        controller.detenerMusica();
        Window ventanaActual = SwingUtilities.getWindowAncestor(this);

        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        MenuPrincipalView menu = new MenuPrincipalView(controller);
        menu.setVisible(true);
    }

    private interface MovimientoVista {
        boolean ejecutar();
    }


    public BarraPuntos getBarraPuntos() {
        return barraPuntos;
    }
}