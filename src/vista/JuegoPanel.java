package vista;

import controller.JuegoController;
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
    private BotonRedondeado btnMusica;
    private boolean musicaActiva = true;

    private Image imagenFondo;
    private Image imagenPiso;
    private Image imagenPared;
    private Image imagenCaja;
    private Image imagenDestino;
    private Image imagenJugador;
    private Map<String, Image> imagenesCache;

    private final int TAMANIO_CELDA = 60;

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
        btnMusica.addActionListener(e -> toggleMusica());
        add(btnMusica);

        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    private void cargarImagenes() {
        imagenFondo = cargarImagen("/images/fondo-juego.png");
        imagenPiso = cargarImagen("/images/piso.png");
        imagenPared = cargarImagen("/images/pared.png");
        imagenCaja = cargarImagen("/images/cajaGuitarra.png");
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

    private void toggleMusica() {
        musicaActiva = !musicaActiva;
        controller.setMutearMusica(!musicaActiva);
        btnMusica.setText(musicaActiva ? "♪ ON" : "♪ OFF");
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
        boolean seMovio = movimiento.ejecutar();

        if (seMovio) {
            repaint();
            barraUndo.resetearUsos(); // movimiento real → resetea usos consecutivos

            if (controller.nivelCompletado()) {

                ResultadoNivel resultado = controller.finalizarNivelActual();

                int nivelCompletado = controller.getNivelActual();
                boolean haySiguiente = controller.haySiguienteNivel();

                boolean continuar = mostrarPopupNivelCompletado(
                        resultado,
                        nivelCompletado,
                        haySiguiente
                );

                if (haySiguiente && continuar) {
                    controller.pasarAlSiguienteNivel();
                    barraUndo.resetearUsos();
                    revalidate();
                    repaint();
                }
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

        // Botón música: al lado del pausa
        btnMusica.setBounds(getWidth() - 175, 35, 80, 70);

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
        Optional<ObjetoView> jugadorOpt = controller.getJugadorView();

        List<ObjetoView> todos = new ArrayList<>();
        todos.addAll(paredes);
        todos.addAll(cajas);
        todos.addAll(destinos);
        jugadorOpt.ifPresent(todos::add);

        if (todos.isEmpty()) return;

        int minFila = Integer.MAX_VALUE, maxFila = Integer.MIN_VALUE;
        int minCol  = Integer.MAX_VALUE, maxCol  = Integer.MIN_VALUE;

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

        g.setColor(new Color(0, 0, 0, 90));
        g.fillRoundRect(offsetX - 20, offsetY - 20, anchoTablero + 40, altoTablero + 40, 30, 30);

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                g.drawImage(imagenPiso,
                        offsetX + col  * TAMANIO_CELDA,
                        offsetY + fila * TAMANIO_CELDA,
                        TAMANIO_CELDA, TAMANIO_CELDA, this);
            }
        }

        for (ObjetoView destino : destinos) {
            int x = offsetX + (destino.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (destino.getFila()    - minFila) * TAMANIO_CELDA;
            g.drawImage(imagenDestino, x, y, TAMANIO_CELDA, TAMANIO_CELDA, this);
        }

        for (ObjetoView pared : paredes) {
            int x = offsetX + (pared.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (pared.getFila()    - minFila) * TAMANIO_CELDA;
            int m = -10;
            g.drawImage(imagenPared, x + m, y + m, TAMANIO_CELDA - m * 2, TAMANIO_CELDA - m * 2, this);
        }

        for (ObjetoView caja : cajas) {
            int x = offsetX + (caja.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (caja.getFila()    - minFila) * TAMANIO_CELDA;
            Image img = obtenerImagenDesdeRuta(caja.getRutaImagen(), imagenCaja);
            int m = 4;
            g.drawImage(img, x + m, y + m, TAMANIO_CELDA - m * 2, TAMANIO_CELDA - m * 2, this);
        }

        if (jugadorOpt.isPresent()) {
            ObjetoView jugador = jugadorOpt.get();
            int x = offsetX + (jugador.getColumna() - minCol) * TAMANIO_CELDA;
            int y = offsetY + (jugador.getFila()    - minFila) * TAMANIO_CELDA;
            int m = -3;
            g.drawImage(imagenJugador, x + m, y + m, TAMANIO_CELDA - m * 2, TAMANIO_CELDA - m * 2, this);
        }
    }

    private boolean mostrarPopupNivelCompletado(ResultadoNivel resultado, int nivelCompletado, boolean haySiguiente) {
        String textoBoton = haySiguiente ? "Pasar al próximo nivel" : "Finalizar juego";

        String mensaje =
                "<html><div style='text-align:center; width:330px;'>" +
                        "<h2>♪ Nivel " + nivelCompletado + " completado</h2>" +
                        "<p><b>Tiempo:</b> "       + resultado.getTiempoFormateado() + "</p>" +
                        "<p><b>Movimientos:</b> "  + resultado.getMovimientos()      + "</p>" +
                        "<p><b>Empujes:</b> "      + resultado.getEmpujes()          + "</p>" +
                        "<p><b>Uso de undo:</b> "  + resultado.getUsosUndo()         + "</p>" +
                        "<p><b>Notas:</b></p>" +
                        "<p style='font-size:28px; color:#E0AB4A;'>" + resultado.getNotas() + "</p>" +
                        "<h2>Puntaje final: " + resultado.getPuntajeFinal() + "</h2>" +
                        "</div></html>";

        int opcion = JOptionPane.showOptionDialog(this, mensaje, "Resultado del nivel",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, new Object[]{textoBoton}, textoBoton);

        return opcion == 0;
    }

    private interface MovimientoVista {
        boolean ejecutar();
    }

    public BarraPuntos getBarraPuntos() {
        return barraPuntos;
    }
}