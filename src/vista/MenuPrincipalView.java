package vista;
import javax.swing.*;

import controller.JuegoController;

import java.awt.*;
import java.net.URL;

public class MenuPrincipalView extends JFrame {

    private BotonRedondeado btnJugar;
    private BotonRedondeado btnNiveles;
    private BotonRedondeado btnMusica;
    private BotonRedondeado btnEfectos;
    private BotonRedondeado btnInstrucciones;

    public MenuPrincipalView(JuegoController controller) {
        setTitle("Sokoban Musical");

        setSize(1200, 800);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        FondoMenuPanel fondo = new FondoMenuPanel("/images/fondo-menu.png");

        btnJugar = new BotonRedondeado(
                "♫  JUGAR",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );

        btnNiveles = new BotonRedondeado(
                "♪  NIVELES",
                new Color(91, 53, 29),
                new Color(138, 72, 42)
        );

        btnInstrucciones = new BotonRedondeado(
                "CÓMO JUGAR ?",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );

        btnMusica = new BotonRedondeado(
                "",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );

        btnEfectos = new BotonRedondeado(
                "",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );
        btnMusica.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEfectos.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnInstrucciones.setFont(new Font("SansSerif", Font.BOLD, 18));

        fondo.configurarBotones(btnJugar, btnNiveles, btnMusica, btnEfectos, btnInstrucciones);

        setContentPane(fondo);

        configurarEventos(controller);

        actualizarTextoBotonMusica(controller);
    }

    public JButton getBtnJugar() {
        return btnJugar;
    }

    public JButton getbtnNiveles() {
        return btnNiveles;
    }

    private static class FondoMenuPanel extends JPanel {

        private final Image imagenFondo;

        private final JLabel titulo;
        private final JLabel subtitulo;
        private final JLabel personaje;

        private JButton btnJugar;
        private JButton btnNiveles;
        private JButton btnMusica;
        private JButton btnEfectos;
        private JButton btnInstrucciones;

        public FondoMenuPanel(String rutaFondo) {
            setLayout(null);

            URL fondoUrl = getClass().getResource(rutaFondo);

            if (fondoUrl == null) {
                throw new IllegalArgumentException(
                        "No se encontró el fondo: " + rutaFondo
                );
            }

            imagenFondo = new ImageIcon(fondoUrl).getImage();

            titulo = new JLabel(
                    "SOKOBAN MUSICAL",
                    SwingConstants.CENTER
            );

            titulo.setForeground(new Color(245, 196, 92));

            subtitulo = new JLabel(
                    "¡Resolvé los desafíos musicales!",
                    SwingConstants.CENTER
            );

            subtitulo.setForeground(new Color(235, 218, 185));

            URL gifUrl = getClass().getResource(
                    "/images/nene-menu.gif"
            );

            if (gifUrl == null) {
                throw new IllegalArgumentException(
                        "No se encontró el GIF del personaje"
                );
            }

            personaje = new JLabel(new ImageIcon(gifUrl));

            add(titulo);
            add(subtitulo);
            add(personaje);
        }

        public void configurarBotones(
                JButton btnJugar,
                JButton btnNiveles,
                JButton btnMusica,
                JButton btnEfectos,
                JButton btnInstrucciones
        ) {
            this.btnJugar = btnJugar;
            this.btnNiveles = btnNiveles;
            this.btnMusica = btnMusica;
            this.btnEfectos = btnEfectos;
            this.btnInstrucciones = btnInstrucciones;

            add(btnJugar);
            add(btnNiveles);
            add(btnMusica);
            add(btnEfectos);
            add(btnInstrucciones);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawImage(
                    imagenFondo,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this
            );
        }

        @Override
        public void doLayout() {
            super.doLayout();

            int ancho = getWidth();
            int alto = getHeight();

            // Fuente adaptable al tamaño de la ventana
            int tamanioTitulo = Math.max(38, ancho / 18);
            int tamanioSubtitulo = Math.max(18, ancho / 48);

            titulo.setFont(
                    new Font("Serif", Font.BOLD, tamanioTitulo)
            );

            subtitulo.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            tamanioSubtitulo
                    )
            );

            /*
             * El espacio central útil del fondo empieza
             * aproximadamente después de la zona de instrumentos
             * de la izquierda y termina antes del mueble derecho.
             */
            int anchoTitulo = (int) (ancho * 0.66);
            int xTitulo = (int) (ancho * 0.18);

            titulo.setBounds(
                    xTitulo,
                    (int) (alto * 0.11),
                    anchoTitulo,
                    (int) (alto * 0.12)
            );

            subtitulo.setBounds(
                    xTitulo,
                    (int) (alto * 0.23),
                    anchoTitulo,
                    (int) (alto * 0.06)
            );

            int anchoBoton = (int) (ancho * 0.31);
            int altoBoton = (int) (alto * 0.105);
            int xBoton = (ancho - anchoBoton) / 2;

            btnJugar.setBounds(
                    xBoton,
                    (int) (alto * 0.41),
                    anchoBoton,
                    altoBoton
            );

            btnNiveles.setBounds(
                    xBoton,
                    (int) (alto * 0.56),
                    anchoBoton,
                    altoBoton
            );

            btnInstrucciones.setBounds(
                        xBoton,
                        (int) (alto * 0.71),
                        anchoBoton,
                        altoBoton
                );

            // Botón música: arriba a la derecha
            btnMusica.setBounds(
                    ancho - 210,
                    20,
                    190,
                    50
            );

            btnEfectos.setBounds(
                    ancho - 210,
                    80,
                    190,
                    50
            );
            

            int anchoPersonaje = 320;
            int altoPersonaje = 390;

            int xPersonaje = (int) (ancho * 0.13);
            int yPersonaje = alto - altoPersonaje - 20;

            personaje.setBounds(
                    xPersonaje,
                    yPersonaje,
                    anchoPersonaje,
                    altoPersonaje
            );
        }
    }

    private void abrirVentanaJuego(JuegoController controller) {
        JuegoPanel panelJuego = new JuegoPanel(controller);

        controller.agregarObservadorBarra(panelJuego.getBarraPuntos());

        JFrame ventanaJuego = new JFrame("Sokoban Musical - Jugando");
        ventanaJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaJuego.setMinimumSize(new Dimension(1000, 700));
        ventanaJuego.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ventanaJuego.setLocationRelativeTo(null);
        ventanaJuego.add(panelJuego);

        dispose();

        ventanaJuego.setVisible(true);

        SwingUtilities.invokeLater(() -> panelJuego.requestFocusInWindow());
    }

    private void configurarEventos(JuegoController controller) {
        btnJugar.addActionListener(e -> {
            controller.iniciarJuego();
            abrirVentanaJuego(controller);
        });

        btnNiveles.addActionListener(e -> {
            mostrarSelectorNiveles(controller);
        });

        btnInstrucciones.addActionListener(e -> {
                PopupInstrucciones popup = new PopupInstrucciones(this);
                popup.setVisible(true);
        });

        btnMusica.addActionListener(e -> {
                controller.toggleMusica();
                actualizarTextoBotonMusica(controller);
        });

        btnEfectos.addActionListener(e -> {
                controller.toggleEfectos();
                actualizarTextoBotonMusica(controller);
        });
    }

    private void actualizarTextoBotonMusica(JuegoController controller) {
        if (controller.estaMusicaMuteada()) {
                btnMusica.setText("♪ Música: OFF");
        } else {
                btnMusica.setText("♪ Música: ON");
        }

        if (controller.estanEfectosMuteados()) {
                btnEfectos.setText("FX OFF");
        } else {
                btnEfectos.setText("FX ON");
        }
        }


    private void mostrarSelectorNiveles(JuegoController controller) {
        JDialog dialog = new JDialog(this, "Seleccionar nivel", true);
        dialog.setSize(420, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Elegí un nivel", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(91, 53, 29));

        JPanel panelNiveles = new JPanel();
        panelNiveles.setLayout(new GridLayout(0, 3, 15, 15));
        panelNiveles.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panelNiveles.setBackground(new Color(235, 218, 185));

        int cantidadNiveles = controller.getCantidadNiveles();

        for (int i = 1; i <= cantidadNiveles; i++) {
            int nivel = i;

            JButton botonNivel = new BotonRedondeado(
                    "Nivel " + nivel,
                    new Color(91, 53, 29),
                    new Color(166, 103, 45)
            );

            botonNivel.setFont(new Font("SansSerif", Font.BOLD, 18));

            botonNivel.addActionListener(e -> {
                dialog.dispose();

                controller.iniciarJuegoEnNivel(nivel);
                abrirVentanaJuego(controller);
            });

            panelNiveles.add(botonNivel);
        }

        dialog.add(titulo, BorderLayout.NORTH);
        dialog.add(panelNiveles, BorderLayout.CENTER);

        dialog.setVisible(true);
    }
}