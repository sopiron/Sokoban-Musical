package vista;
import javax.swing.*;

import controller.JuegoController;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class MenuPrincipalView extends JFrame {

    private JButton btnJugar;
    private JButton btnSalir;

    public MenuPrincipalView(JuegoController controller) {
        setTitle("Sokoban Musical");

        // Tamaño inicial proporcionado al fondo horizontal
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

        btnSalir = new BotonRedondeado(
                "♪  SALIR",
                new Color(91, 53, 29),
                new Color(138, 72, 42)
        );

        fondo.configurarBotones(btnJugar, btnSalir);

        setContentPane(fondo);

        configurarEventos(controller);
    }

    public JButton getBtnJugar() {
        return btnJugar;
    }

    public JButton getBtnSalir() {
        return btnSalir;
    }

    private static class FondoMenuPanel extends JPanel {

        private final Image imagenFondo;

        private final JLabel titulo;
        private final JLabel subtitulo;
        private final JLabel personaje;

        private JButton btnJugar;
        private JButton btnSalir;

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
                JButton btnSalir
        ) {
            this.btnJugar = btnJugar;
            this.btnSalir = btnSalir;

            add(btnJugar);
            add(btnSalir);
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

            btnSalir.setBounds(
                    xBoton,
                    (int) (alto * 0.56),
                    anchoBoton,
                    altoBoton
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

    private void configurarEventos(JuegoController controller) {
    btnJugar.addActionListener(e -> {
        controller.iniciarJuego();

        JuegoPanel panelJuego = new JuegoPanel(controller);

        JFrame ventanaJuego = new JFrame("Sokoban Musical - Jugando");
        ventanaJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaJuego.setMinimumSize(new Dimension(1000, 700));
        ventanaJuego.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ventanaJuego.setLocationRelativeTo(null);
        ventanaJuego.add(panelJuego);

        dispose();

        ventanaJuego.setVisible(true);
        panelJuego.requestFocusInWindow();
    });

    btnSalir.addActionListener(e -> System.exit(0));
}


    private static class BotonRedondeado extends JButton {

        private final Color colorNormal;
        private final Color colorHover;

        public BotonRedondeado(
                String texto,
                Color colorNormal,
                Color colorHover
        ) {
            super(texto);

            this.colorNormal = colorNormal;
            this.colorHover = colorHover;

            setBackground(colorNormal);
            setForeground(new Color(255, 245, 220));

            setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            25
                    )
            );

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(colorHover);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(colorNormal);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // Fondo redondeado
            g2.setColor(getBackground());

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    38,
                    38
            );

            // Borde dorado
            g2.setColor(new Color(224, 171, 74));
            g2.setStroke(new BasicStroke(3));

            g2.drawRoundRect(
                    1,
                    1,
                    getWidth() - 3,
                    getHeight() - 3,
                    38,
                    38
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}