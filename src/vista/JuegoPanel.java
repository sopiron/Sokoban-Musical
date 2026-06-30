package vista;

import controller.JuegoController;
import modelo.ResultadoNivel;
import views.ObjetoView;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JuegoPanel extends JPanel {

    private JuegoController controller;

    private BarraPuntos barraPuntos;
    private BarraUndo barraUndo;
    private BotonRedondeado btnPausa;
    private BotonRedondeado btnMusica;
    private BotonRedondeado btnEfectos;

    private Image imagenFondo;
    private Image imagenPiso;
    private Image imagenPared;
    private Image imagenCaja;
    private Image imagenDestino;
    private Image imagenJugadorFrente;
    private Image imagenJugadorAtras;
    private Image imagenJugadorDerecha;
    private Image imagenJugadorIzquierda;
    private Image imagenJugadorActual;
    private Image imagenPisoResbaladizo;
    private Image imagenCerrojo;
    private Map<String, Image> imagenesCache;

    private Timer timerDeslizamiento;
    private boolean animandoDeslizamiento;

    private final int TAMANIO_CELDA = 50;

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
                "II",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );
        btnPausa.setFont(new Font("SansSerif", Font.BOLD, 22));
        btnPausa.setFocusable(false);
        btnPausa.addActionListener(e -> abrirPausa());
        add(btnPausa);

        btnMusica = new BotonRedondeado(
                "♪ ON",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );

        btnMusica.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnMusica.setFocusable(false);
        btnMusica.setMargin(new Insets(0, 0, 0, 0));
        btnMusica.addActionListener(e -> toggleMusica());
        add(btnMusica);

        btnEfectos = new BotonRedondeado(
                "",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );

        btnEfectos.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEfectos.setFocusable(false);
        btnEfectos.setMargin(new Insets(0, 0, 0, 0));
        btnEfectos.addActionListener(e -> toggleEfectos());
        add(btnEfectos);

        actualizarTextoBotonMusica();

        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    private void cargarImagenes() {
        imagenFondo = cargarImagen("/images/fondo-juego.png");
        imagenPiso = cargarImagen("/images/piso.png");
        imagenPared = cargarImagen("/images/pared.png");
        imagenCaja = cargarImagen("/images/cajaGuitarra.png");
        imagenDestino = cargarImagen("/images/destino.png");
        imagenPisoResbaladizo = cargarImagen("/images/pisoResbaladizo.png");
        imagenCerrojo = cargarImagen("/images/cerrojo.png");

        imagenJugadorFrente = cargarImagen("/images/personajeFrente.png");
        imagenJugadorAtras = cargarImagen("/images/personajeAtras.png");
        imagenJugadorDerecha = cargarImagen("/images/personajeDerecha.png");
        imagenJugadorIzquierda = cargarImagen("/images/personajeIzquierda.png");

        imagenJugadorActual = imagenJugadorFrente;
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
        registrarTecla("UP", () -> {
            imagenJugadorActual = imagenJugadorAtras;
            moverYActualizar(() -> controller.moverArriba());
        });

        registrarTecla("DOWN", () -> {
            imagenJugadorActual = imagenJugadorFrente;
            moverYActualizar(() -> controller.moverAbajo());
        });

        registrarTecla("LEFT", () -> {
            imagenJugadorActual = imagenJugadorIzquierda;
            moverYActualizar(() -> controller.moverIzquierda());
        });

        registrarTecla("RIGHT", () -> {
            imagenJugadorActual = imagenJugadorDerecha;
            moverYActualizar(() -> controller.moverDerecha());
        });

        registrarTecla("Z", () -> ejecutarUndo());
        registrarTecla("ESCAPE", () -> abrirPausa());
    }

    private void toggleMusica() {
        controller.toggleMusica();
        actualizarTextoBotonMusica();
    }

    private void toggleEfectos() {
        controller.toggleEfectos();
        actualizarTextoBotonMusica();
    }

    private void actualizarTextoBotonMusica() {
        if (controller.estaMusicaMuteada()) {
            btnMusica.setText("♪ OFF");
        } else {
            btnMusica.setText("♪ ON");
        }

        if (controller.estanEfectosMuteados()) {
            btnEfectos.setText("FX OFF");
        } else {
            btnEfectos.setText("FX ON");
        }
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

            controller.reproducirSonidoNivelCompletado();

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
                barraUndo.resetearUsos();
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

        int anchoPanel = getWidth();

        int ySuperior = 35;
        int altoSuperior = 70;

        int margenDerecho = 60;
        int espacio = 18;

        int anchoPausa = 70;
        int anchoSonido = 120;

        int xPausa = anchoPanel - margenDerecho - anchoPausa;
        int xEfectos = xPausa - espacio - anchoSonido;
        int xMusica = xEfectos - espacio - anchoSonido;

        btnMusica.setBounds(
                xMusica,
                ySuperior,
                anchoSonido,
                altoSuperior
        );

        btnEfectos.setBounds(
                xEfectos,
                ySuperior,
                anchoSonido,
                altoSuperior
        );

        btnPausa.setBounds(
                xPausa,
                ySuperior,
                anchoPausa,
                altoSuperior
        );

        int xBarra = 120;
        int anchoBarra = xMusica - xBarra - espacio;

        barraPuntos.setBounds(
                xBarra,
                ySuperior,
                anchoBarra,
                altoSuperior
        );

        barraUndo.setBounds(
                220,
                getHeight() - 110,
                getWidth() - 440,
                70
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);

        List<ObjetoView> paredes = controller.getParedesView();
        List<ObjetoView> cajas = controller.getCajasView();
        List<ObjetoView> destinos = controller.getDestinosView();
        List<ObjetoView> pisosResbaladizos = controller.getPisosResbaladizosView();
        List<ObjetoView> cerrojos = controller.getCerrojosView();
        Optional<ObjetoView> jugadorOpt = controller.getJugadorView();

        List<ObjetoView> todos = new ArrayList<>();
        todos.addAll(paredes);
        todos.addAll(cajas);
        todos.addAll(destinos);
        todos.addAll(pisosResbaladizos);
        todos.addAll(cerrojos);
        jugadorOpt.ifPresent(todos::add);

        if (todos.isEmpty()) return;

        // Calcular límites del tablero
        int minFila = Integer.MAX_VALUE;
        int maxFila = Integer.MIN_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxCol = Integer.MIN_VALUE;

        for (ObjetoView obj : todos) {
            if (obj.getFila()    < minFila) minFila = obj.getFila();
            if (obj.getFila()    > maxFila) maxFila = obj.getFila();
            if (obj.getColumna() < minCol)  minCol  = obj.getColumna();
            if (obj.getColumna() > maxCol)  maxCol  = obj.getColumna();
        }

        int filas    = maxFila - minFila + 1;
        int columnas = maxCol  - minCol  + 1;

        int anchoTablero = columnas * TAMANIO_CELDA;
        int altoTablero  = filas    * TAMANIO_CELDA;

        int offsetX = (getWidth()  - anchoTablero) / 2;
        int offsetY = (getHeight() - altoTablero)  / 2;

       // ── Detectar paredes ─────────────────────────────────────────────
        Set<String> celdasPared = new HashSet<>();

        for (ObjetoView pared : paredes) {
            if (pared.getRutaImagen() == null || pared.getRutaImagen().isEmpty()) {
                continue;
            }

            celdasPared.add(pared.getFila() + "," + pared.getColumna());
        }

        // ── Flood fill desde afuera del tablero ──────────────────────────
        // La idea es marcar todo lo que está afuera de las paredes.
        // Lo que NO sea pared y NO sea alcanzable desde afuera se considera piso interior.

        int filaInicio = minFila - 1;
        int filaFin = maxFila + 1;
        int colInicio = minCol - 1;
        int colFin = maxCol + 1;

        Set<String> visitadosAfuera = new HashSet<>();
        Queue<int[]> cola = new ArrayDeque<>();

        cola.add(new int[] { filaInicio, colInicio });
        visitadosAfuera.add(filaInicio + "," + colInicio);

        int[][] direcciones = {
                { -1, 0 },
                { 1, 0 },
                { 0, -1 },
                { 0, 1 }
        };

        while (!cola.isEmpty()) {
            int[] actual = cola.poll();

            int filaActual = actual[0];
            int colActual = actual[1];

            for (int[] direccion : direcciones) {
                int nuevaFila = filaActual + direccion[0];
                int nuevaCol = colActual + direccion[1];

                if (nuevaFila < filaInicio || nuevaFila > filaFin ||
                    nuevaCol < colInicio || nuevaCol > colFin) {
                    continue;
                }

                String clave = nuevaFila + "," + nuevaCol;

                if (visitadosAfuera.contains(clave)) {
                    continue;
                }

                if (celdasPared.contains(clave)) {
                    continue;
                }

                visitadosAfuera.add(clave);
                cola.add(new int[] { nuevaFila, nuevaCol });
            }
        }

        // ── Armar celdas reales del nivel ────────────────────────────────
        Set<String> celdasNivel = new HashSet<>();

        for (int fila = minFila; fila <= maxFila; fila++) {
            for (int col = minCol; col <= maxCol; col++) {
                String clave = fila + "," + col;

                if (celdasPared.contains(clave)) {
                    celdasNivel.add(clave);
                } else if (!visitadosAfuera.contains(clave)) {
                    celdasNivel.add(clave);
                }
            }
        }

        // ── Sombra siguiendo la forma real del nivel ─────────────────────
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 60));

        for (String clave : celdasNivel) {
            String[] partes = clave.split(",");

            int fila = Integer.parseInt(partes[0]);
            int col = Integer.parseInt(partes[1]);

            int x = offsetX + (col - minCol) * TAMANIO_CELDA - 8;
            int y = offsetY + (fila - minFila) * TAMANIO_CELDA - 8;

            g2.fillRoundRect(
                    x,
                    y,
                    TAMANIO_CELDA + 16,
                    TAMANIO_CELDA + 16,
                    16,
                    16
            );
        }

        g2.dispose();

        // ── Dibujar piso solo dentro del nivel ───────────────────────────
        for (int fila = minFila; fila <= maxFila; fila++) {
            for (int col = minCol; col <= maxCol; col++) {
                String clave = fila + "," + col;

                if (celdasPared.contains(clave)) {
                    continue;
                }

                if (visitadosAfuera.contains(clave)) {
                    continue;
                }

                int x = offsetX + (col - minCol) * TAMANIO_CELDA;
                int y = offsetY + (fila - minFila) * TAMANIO_CELDA;

                g.drawImage(
                        imagenPiso,
                        x,
                        y,
                        TAMANIO_CELDA,
                        TAMANIO_CELDA,
                        this
                );
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
            int y = offsetY + (destino.getFila()    - minFila) * TAMANIO_CELDA;
            
            g.drawImage(imagenDestino, x, y, TAMANIO_CELDA, TAMANIO_CELDA, this);
        }

        // 3. Dibujar paredes
        for (ObjetoView pared : paredes) {
            if (pared.getRutaImagen() == null || pared.getRutaImagen().isEmpty()) {
                continue;
            }

            int x = offsetX + (pared.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (pared.getFila() - minFila) * TAMANIO_CELDA;

            Image imagenParedActual = obtenerImagenDesdeRuta(
                    pared.getRutaImagen(),
                    imagenPared
            );

            int margenPared = -10;

            g.drawImage(
                    imagenParedActual,
                    x + margenPared,
                    y + margenPared,
                    TAMANIO_CELDA - margenPared * 2,
                    TAMANIO_CELDA - margenPared * 2,
                    this
            );
        }

        // Dibujar cerrojos
        for (ObjetoView cerrojo : cerrojos) {
            int x = offsetX + (cerrojo.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (cerrojo.getFila() - minFila) * TAMANIO_CELDA;

            g.drawImage(
                    imagenCerrojo,
                    x,
                    y,
                    TAMANIO_CELDA,
                    TAMANIO_CELDA,
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

            int margenCaja = -3;

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
                    imagenJugadorActual,
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