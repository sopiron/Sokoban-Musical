package vista;

import controller.JuegoController;
import modelo.GestorSonido;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PopupPausa extends JDialog {

    private boolean musicaActiva = true;
    private BotonRedondeado btnMusica;

    public PopupPausa(JFrame padre, JuegoController controller, Runnable alReiniciar, Runnable alVolverAlMenu) {
        super(padre, "Pausa", true); // modal

        setUndecorated(true); // sin barra de título nativa
        setSize(420, 380);
        setLocationRelativeTo(padre);
        setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo oscuro semitransparente
                g2.setColor(new Color(15, 8, 3, 245));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Borde dorado
                g2.setColor(new Color(224, 171, 74));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 30, 30);

                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(null);

        // ── Botón cerrar (✕) arriba a la derecha ──
        JLabel btnCerrar = new JLabel("✕", SwingConstants.CENTER);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 22));
        btnCerrar.setForeground(new Color(224, 171, 74));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setBounds(370, 15, 35, 35);

        btnCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cerrar(controller);
            }
        });

        panel.add(btnCerrar);

        // ── Título ──
        JLabel titulo = new JLabel("⏸ PAUSADO", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(224, 171, 74));
        titulo.setBounds(0, 20, 420, 50);
        panel.add(titulo);

        // ── Notas actuales ──
        String notas = controller.getNotasActuales();
        JLabel lblNotas = new JLabel("Notas: " + notas, SwingConstants.CENTER);
        lblNotas.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblNotas.setForeground(Color.WHITE);
        lblNotas.setBounds(0, 85, 420, 40);
        panel.add(lblNotas);

        // ── Botón Reiniciar nivel ──
        BotonRedondeado btnReiniciar = new BotonRedondeado(
                "↻  Reiniciar nivel",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );
        btnReiniciar.setBounds(60, 150, 300, 50);
        btnReiniciar.setFocusable(false);
        btnReiniciar.addActionListener(e -> {
            dispose();
            alReiniciar.run();
        });
        panel.add(btnReiniciar);

        // ── Botón Silenciar música ──
        btnMusica = new BotonRedondeado(
                "♪  Silenciar música",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );
        btnMusica.setBounds(60, 215, 300, 50);
        btnMusica.setFocusable(false);
        btnMusica.addActionListener(e -> toggleMusica(controller));
        panel.add(btnMusica);

        // ── Botón Menú principal ──
        BotonRedondeado btnMenu = new BotonRedondeado(
                "⌂  Menú principal",
                new Color(91, 53, 29),
                new Color(166, 103, 45)
        );
        btnMenu.setBounds(60, 280, 300, 50);
        btnMenu.setFocusable(false);
        btnMenu.addActionListener(e -> {
            controller.detenerMusica();;
            dispose();
            alVolverAlMenu.run();
        });
        panel.add(btnMenu);

        setContentPane(panel);

        // Al cerrar la ventana con Alt+F4 también reanudamos
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrar(controller);
            }
        });
    }

    private void cerrar(JuegoController controller) {
        controller.reanudar();
        dispose();
    }

    private void toggleMusica(JuegoController controller) {
        musicaActiva = !musicaActiva;
        if (musicaActiva) {
            btnMusica.setText("♪  Silenciar música");
            // Reanudar música — el controller/gestor sabe qué pista estaba sonando
            // Como GestorSonido no tiene "reanudar", lo más simple es no hacer nada
            // (la música nunca se detuvo, solo la silenciamos con el volumen)
            controller.setMutearMusica(false);
        } else {
            btnMusica.setText("♫  Activar música");
            controller.setMutearMusica(true);
        }
    }
}